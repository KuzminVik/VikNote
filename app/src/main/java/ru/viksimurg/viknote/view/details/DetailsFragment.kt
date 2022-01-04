package ru.viksimurg.viknote.view.details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentDetailsBinding
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.edit.EditFragment
import ru.viksimurg.viknote.view.folders.FoldersFragment
import ru.viksimurg.viknote.view.main.MainViewModel
import ru.viksimurg.viknote.view.notes.NotesFragment

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by inject()
    private val sharedViewModelFab by sharedViewModel<MainViewModel>()

    private val onClickFabEditElement = object: OnClickFabEditElement {
        override fun onClickEditNote() {
            sharedViewModelFab.saveIntPrefs(EDITING_STATE, STATE_NOTE_EDIT)
            viewModel.setNoteId()
            parentFragmentManager.apply {
                beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.container, EditFragment.newInstance())
                    .addToBackStack("")
                    .commitAllowingStateLoss()
            }
        }

        override fun onClickCloseNote() {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, NotesFragment.newInstance())
                .addToBackStack(" ")
                .commitAllowingStateLoss()
        }

        override fun onClickDeleteNote() {
            showDialogWithConfirmation("Вы действительно хотите удалить эту заметку?", requireContext().resources.getString(R.string.attention)) { _, _ ->
                viewModel.deleteNote()
                viewModel.upCountNotesByFolderId()
                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, NotesFragment.newInstance())
                    .addToBackStack(" ")
                    .commitAllowingStateLoss()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModelFab.setDataFab(DETAILS_FRAGMENT, onClickFabEditElement)
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData()
    }

    private fun renderData(appState: AppState){
        when(appState){
            is AppState.SuccessNote -> {
                val note = appState.note
                if (note == null){
                    showDialogWithInformation("Такой заметки не существует", requireContext().resources.getString(R.string.attention))
                    parentFragmentManager.popBackStack()
                }else{
                    binding.errorDetailsLayout.visibility = View.GONE
                    binding.loadingDetailsLayout.visibility = View.GONE
                    binding.successDetailsLayout.visibility = View.VISIBLE
                    binding.detailsTitle.text = note.name
                    binding.detailsText.text = note.text
                    binding.detailsDate.text = note.date
                    when(note.priority){
                        PRIORITY_GREY -> binding.ivIconBookmark.setImageResource(R.drawable.ic_baseline_bookmark_grey_24)
                        PRIORITY_RED -> binding.ivIconBookmark.setImageResource(R.drawable.ic_baseline_bookmark_red_24)
                        PRIORITY_GREEN -> binding.ivIconBookmark.setImageResource(R.drawable.ic_baseline_bookmark_green_24)
                    }
                    binding.ivIconUpLevel.setOnClickListener {
                        viewModel.saveFolderId(note.folderId)
                        parentFragmentManager.apply {
                            beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.container, NotesFragment.newInstance())
                                .addToBackStack("")
                                .commitAllowingStateLoss()
                        }
                    }
                    binding.ivIconHome.setOnClickListener {
                        parentFragmentManager.apply {
                            beginTransaction()
                                .setReorderingAllowed(true)
                                .replace(R.id.container, FoldersFragment.newInstance())
                                .addToBackStack("")
                                .commitAllowingStateLoss()
                        }
                    }
                }
            }
            is AppState.Loading -> {
                binding.errorDetailsLayout.visibility = View.GONE
                binding.loadingDetailsLayout.visibility = View.VISIBLE
                binding.successDetailsLayout.visibility = View.GONE
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
            else -> {}
        }
    }

    private fun showErrorScreen(error: String?){
        binding.errorDetailsLayout.visibility = View.VISIBLE
        binding.loadingDetailsLayout.visibility = View.GONE
        binding.successDetailsLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() =
            DetailsFragment()
    }
}