package ru.viksimurg.viknote.view.edit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.chip.Chip
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentEditBinding
import ru.viksimurg.viknote.model.EditingModeState
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.room.Note
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.folders.FoldersFragment
import ru.viksimurg.viknote.view.main.MainViewModel
import ru.viksimurg.viknote.view.notes.NotesFragment

class EditFragment : Fragment(){

    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditingViewModel by inject()
    private val sharedViewModelFab by sharedViewModel<MainViewModel>()

    private val onClickFabSaveElement = object : OnClickFabSaveElement {
        override fun onClickSave() {
            onFabSaveElementClick()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChoiceChipId().observe(viewLifecycleOwner, {
            binding.chipGroupPriorityNote.check(it)
            binding.chipGroupPriority.check(it)
            Log.d("observe !!!", "check(it) it = $it")
        })
        sharedViewModelFab.setDataFab(EDIT_FRAGMENT, onClickFabSaveElement)
        viewModel.getCheckNameFolder().observe(viewLifecycleOwner, { renderCheckNameFolder(it) })
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData()
    }

    private fun renderData(editingModeState: EditingModeState) {
        when (editingModeState) {
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

    fun onFabSaveElementClick() {
        if(binding.editNoteLayout.isVisible){
            if (viewModel.checkNameNote(binding.editTextHeadNote.text.toString())){
                runSaveNote()
            }else{
                showDialogWithInformation("Отсутствует заголовок!", "Внимание!")
            }
        }
        if (binding.editFolderLayout.isVisible){
            val temp = binding.editTextHead.text.toString()
            if (temp == "" || temp == " " || temp.isEmpty()) {
                showDialogWithInformation("Вы не добавили заголовок!", "Внимание!")
            } else {
                viewModel.checkNameFolder(temp)
            }
        }
    }

    private fun showFolderLayout(folder: Folder?) {
        binding.editNoteLayout.visibility = View.GONE
        binding.editFolderLayout.visibility = View.VISIBLE
        binding.scrollViewNote.visibility = View.GONE
        if (folder != null) {
            binding.editTextHead.setText(folder.name)
            binding.labelAddFolder.text = "Отредактируйте папку"
        }
        binding.chipGroupPriority.setOnCheckedChangeListener { group, checkedId ->
            group.findViewById<Chip>(checkedId)?.let {
                viewModel.setChipToChoice(checkedId)
            }
        }
    }

    private fun renderCheckNameFolder(result: Boolean) {
        if (result) {
            showDialogWithInformation("Папка с таким именем уже существует", "Внимание!")
        } else {
            runSaveFolder()
        }
    }

    private fun runSaveFolder() {
        viewModel.saveFolder(
            name = binding.editTextHead.text.toString(),
        )
        parentFragmentManager.apply {
            beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, FoldersFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showNoteLayout(data: Pair<List<Folder>?, Note?>?) {
        binding.editNoteLayout.visibility = View.VISIBLE
        binding.editFolderLayout.visibility = View.GONE
        binding.scrollViewNote.visibility = View.VISIBLE
        val note = data?.second
        val listFolders = data?.first
        if (listFolders == null || listFolders.isEmpty()) {
            showError()
        } else {
            if (note != null) {
                binding.editTextHeadNote.setText(note.name)
                binding.editTextTextNote.setText(note.text)
                binding.labelAddNote.text = "Отредактируйте заметку"
                setSpinner(listFolders, note.folderId)
            } else {
                setSpinner(listFolders, null)
            }
            binding.chipGroupPriorityNote.setOnCheckedChangeListener { group, checkedId ->
                group.findViewById<Chip>(checkedId)?.let {
                    viewModel.setChipToChoice(checkedId)
                }
            }
            binding.editTextTextNote.setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
        }
    }

    private fun setSpinner(listFolders: List<Folder>, folderId: Int?) {
        val newList: MutableList<Folder> = mutableListOf()
        newList.add(Folder(id = -1, name = "Выбрать папку...", priority = -1))
        newList.addAll(listFolders)
        var selectFolderPosition = 0
        if (folderId != null) {
            newList.forEach { it ->
                if (folderId == it.id){
                    selectFolderPosition = newList.indexOf(it)
                    return@forEach
                }
            }
        }
        SpinnerAdapter(requireContext(), newList).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            binding.spinnerFolders.adapter = adapter
            binding.spinnerFolders.setSelection(selectFolderPosition)
        }
    }

    private fun runSaveNote(){
        val selectedFolder = binding.spinnerFolders.selectedItem as Folder
        viewModel.saveNote(
            name = binding.editTextHeadNote.text.toString(),
            text = binding.editTextTextNote.text.toString(),
            folderId = selectedFolder.id
        )
        viewModel.saveFolderId(selectedFolder.id)
        parentFragmentManager.apply {
            beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, NotesFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    private fun showError() {
        //TODO
    }

    companion object {
        fun newInstance() = EditFragment()
    }
}