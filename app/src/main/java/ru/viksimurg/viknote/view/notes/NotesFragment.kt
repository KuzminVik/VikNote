package ru.viksimurg.viknote.view.notes

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentNotesBinding
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.utils.swipe.SwipeHelper
import ru.viksimurg.viknote.view.folders.OnClickFabListElements
import ru.viksimurg.viknote.view.details.DetailsFragment
import ru.viksimurg.viknote.view.edit.EditFragment
import ru.viksimurg.viknote.view.folders.FoldersFragment
import ru.viksimurg.viknote.view.main.MainViewModel

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotesViewModel by inject()
    private val sharedViewModelFab by sharedViewModel<MainViewModel>()

    private val onClickFabListElements =
        object : OnClickFabListElements {
            override fun onClickAddFolder() {
                sharedViewModelFab.saveIntPrefs(EDITING_STATE, STATE_FOLDER_EMPTY)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, EditFragment.newInstance())
                    .addToBackStack(" ")
                    .commitAllowingStateLoss()
            }

            override fun onClickAddNote() {
                sharedViewModelFab.saveIntPrefs(EDITING_STATE, STATE_NOTE_EMPTY)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, EditFragment.newInstance())
                    .addToBackStack(" ")
                    .commitAllowingStateLoss()
            }
        }

    private val notesAdapter = NotesAdapter {
        viewModel.saveOpenNoteId(it)
        parentFragmentManager.apply {
            beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.container, DetailsFragment.newInstance(), DETAILS_FRAGMENT)
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModelFab.setDataFab(LIST_ELEMENTS_FRAGMENT, onClickFabListElements)
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData()
        setRecyclerView()
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessListNotes -> {
                val data = appState.data
                val name = appState.nameFolder
                binding.errorNotesLayout.visibility = View.GONE
                binding.loadingNotesLayout.visibility = View.GONE
                binding.successNotesLayout.visibility = View.VISIBLE
                binding.ivIconUpLevel.setOnClickListener {
                    parentFragmentManager.apply {
                        beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.container, FoldersFragment.newInstance())
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
                if (data == null || data.isEmpty()) {
                    binding.tvNoData.text = "В этой папке нет заметок"
                } else {
                    notesAdapter.setData(data)
                    binding.path.text = "$name >"
                    binding.notesSearch.setOnQueryTextListener(
                        object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                notesAdapter.filter.filter(newText)
                                return false
                            }
                        })
                }
            }
            is AppState.Loading -> {
                binding.errorNotesLayout.visibility = View.GONE
                binding.successNotesLayout.visibility = View.GONE
                binding.loadingNotesLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
            else -> {}
        }
    }

    private fun setRecyclerView() {
        binding.notesRv.layoutManager = LinearLayoutManager(requireView().context)
        binding.notesRv.adapter = notesAdapter
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(requireContext()) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.RIGHT) {
                    showDialogWithConfirmation(
                        "Вы действительно хотите удалить эту заметку?",
                        "Внимание!"
                    ) { _, _ ->
                        viewModel.deleteNote(notesAdapter.getCurrentNoteId(position))
                        viewModel.upCountNotesByFolderId()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.container, newInstance())
                            .addToBackStack(" ")
                            .commitAllowingStateLoss()
                    }
                    notesAdapter.notifyDataSetChanged()
                } else {
                    sharedViewModelFab.saveIntPrefs(EDITING_STATE, STATE_NOTE_EDIT)
                    viewModel.saveOpenNoteId(notesAdapter.getCurrentNoteId(position))
                    viewModel.setNoteIdForEdit()
                    parentFragmentManager.apply {
                        beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.container, EditFragment.newInstance())
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.notesRv)
    }

    private fun showErrorScreen(error: String?) {
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            viewModel.getData()
        }
        binding.successNotesLayout.visibility = View.GONE
        binding.errorNotesLayout.visibility = View.VISIBLE
        binding.loadingNotesLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NotesFragment()
    }

}