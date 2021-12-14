package ru.viksimurg.viknote.view.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentEditBinding
import ru.viksimurg.viknote.model.EditingModeState
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.Note
import ru.viksimurg.viknote.utils.PRIORITY_GREEN
import ru.viksimurg.viknote.utils.PRIORITY_GREY
import ru.viksimurg.viknote.utils.PRIORITY_RED
import ru.viksimurg.viknote.view.folders.FoldersFragment
import kotlin.properties.Delegates

class EditFragment: Fragment() {

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditingViewModel by inject()

    private var priorityFolder = PRIORITY_GREY
    private var priorityNote by Delegates.notNull<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCheckNameFolder().observe(viewLifecycleOwner, { renderCheckNameFolder(it) })
        viewModel.subscribe().observe(viewLifecycleOwner, {renderData(it)})
        viewModel.getData()
    }

    private fun renderData(editingModeState: EditingModeState){
        when(editingModeState){
            is EditingModeState.FolderState -> {
                showFolderLayout(editingModeState.folder)
            }
            is EditingModeState.NoteState -> {
                showNoteLayout(editingModeState.data)
            }
            is EditingModeState.Error -> {
                showError()
            }
        }
    }

    private fun showFolderLayout(folder: Folder?){
        binding.editNoteLayout.visibility = View.GONE
        binding.editFolderLayout.visibility = View.VISIBLE
        if (folder != null){
            binding.editTextHead.setText(folder.name)
            binding.editTextDescription.setText(folder.desc)
        }
        binding.chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {
                when(it.text){
                    "серый" -> priorityFolder = PRIORITY_GREY
                    "зеленый" -> priorityFolder = PRIORITY_GREEN
                    "красный" -> priorityFolder = PRIORITY_RED
                }
            }
        }
        binding.buttonSaveFolder.setOnClickListener {
            val temp = binding.editTextHead.text.toString()
            if(temp=="" || temp==" " || temp.isEmpty()){
                EditDialogFragment().show(parentFragmentManager, "Name_Dialog")
            }else{
                viewModel.checkNameFolder(temp)
            }
        }
    }

    private fun renderCheckNameFolder(result: Boolean){
        if (result){
            val builder = AlertDialog.Builder(requireContext())
            with(builder){
                setTitle("Папка с таким именем уже существует")
                setMessage("We have a message")
                setPositiveButton("OK", null)
                show()
            }
        } else{
            runSaveFolder()
        }
    }

    private fun runSaveFolder(){
        viewModel.saveFolder(
            name = binding.editTextHead.text.toString(),
            desc = binding.editTextDescription.text.toString(),
            priority = priorityFolder
        )
        parentFragmentManager.apply {
            beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, FoldersFragment.newInstance())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    private fun showNoteLayout(data: Pair<List<Folder>?, Note?>?){
        binding.editNoteLayout.visibility = View.VISIBLE
        binding.editFolderLayout.visibility = View.GONE
        val note = data?.second
        val listFolders = data?.first
        if(listFolders == null) {
            showError()
        }else{
            if(note != null){
                binding.editTextHeadNote.setText(note.name)
                binding.editTextDescriptionNote.setText(note.desc)
                binding.editTextTextNote.setText(note.text)
                setSpinner(listFolders)
            }
            setSpinner(listFolders)
        }
    }

    private fun setSpinner(listFolders: List<Folder>){
        val newList: MutableList<Folder> = mutableListOf()
        newList.add(Folder(id = -1, name = "Выбрать папку...", desc = "", priority = -1))
        newList.addAll(listFolders)
        SpinnerAdapter(
            requireContext(),
            newList
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerFolders.adapter = adapter
        }
    }

    private fun showError(){
        //TODO
    }

    companion object{
        fun newInstance() = EditFragment()
    }
}