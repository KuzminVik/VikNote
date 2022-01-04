package ru.viksimurg.viknote.view.folders

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentFoldersBinding
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.utils.swipe.SwipeHelper
import ru.viksimurg.viknote.view.OnListItemClickListener
import ru.viksimurg.viknote.view.edit.EditFragment
import ru.viksimurg.viknote.view.main.MainViewModel
import ru.viksimurg.viknote.view.notes.NotesFragment

class FoldersFragment : Fragment() {

    private var _binding: FragmentFoldersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoldersViewModel by inject()
    private val sharedViewModelFab by sharedViewModel<MainViewModel>()

    private val onListItemClickListener: OnListItemClickListener<Folder> =
        object : OnListItemClickListener<Folder> {
            override fun onItemClick(data: Folder) {
                viewModel.saveFolderId(data.id)
                parentFragmentManager.apply {
                    beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.container, NotesFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
            }
        }

    private val onClickFabListElements = object : OnClickFabListElements {
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

    private val foldersAdapter = FoldersAdapter(onListItemClickListener)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoldersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModelFab.setDataFab(LIST_ELEMENTS_FRAGMENT, onClickFabListElements)
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getData()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.foldersRv.layoutManager = LinearLayoutManager(requireView().context)
        binding.foldersRv.adapter = foldersAdapter
        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(requireContext()) {
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.RIGHT){
                    viewModel.getResultCheckCountNotes().observe(viewLifecycleOwner, {
                        if(it){
                            //Если папка не пустая
                            showDialogWithInformation(
                                "Вначале удалите или переместите все заметки, содержащиеся в этой папке.",
                                "Удаление невозможно!"
                            )
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.container, newInstance())
                                .addToBackStack(" ")
                                .commitAllowingStateLoss()
                        }else{
                            //Если папка пустая
                            showDialogWithConfirmation(
                                "Вы действительно хотите удалить эту папку?",
                                "Внимание!"
                            ) { _, _ ->
                                viewModel.deleteFolder(foldersAdapter.getCurrentFolderId(position))
                                parentFragmentManager.beginTransaction()
                                    .replace(R.id.container, newInstance())
                                    .addToBackStack(" ")
                                    .commitAllowingStateLoss()
                            }
                        }
                        foldersAdapter.notifyDataSetChanged()
                    })
                    viewModel.checkCountNotesInFolder(foldersAdapter.getCurrentFolderId(position))
                }else{
                    sharedViewModelFab.saveIntPrefs(EDITING_STATE, STATE_FOLDER_EDIT)
                    viewModel.saveFolderIdForEdit(foldersAdapter.getCurrentFolderId(position))
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
        itemTouchHelper.attachToRecyclerView(binding.foldersRv)
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.SuccessListFolders -> {
                val data = appState.data?.toMutableList()
                if (data == null || data.isEmpty()) {
                    showDialogWithInformation("У вас нет ни одной папки для заметок!", "Внимание!")
                    sharedViewModelFab.saveIntPrefs(EDITING_STATE, STATE_FOLDER_EMPTY)
                    parentFragmentManager.apply {
                        beginTransaction()
                            .setReorderingAllowed(true)
                            .replace(R.id.container, EditFragment.newInstance())
                            .addToBackStack("")
                            .commitAllowingStateLoss()
                    }
                } else {
                    binding.errorFoldersLayout.visibility = View.GONE
                    binding.loadingFoldersLayout.visibility = View.GONE
                    binding.successFoldersLayout.visibility = View.VISIBLE
                    foldersAdapter.setData(data)
                    binding.foldersSearch.setOnQueryTextListener(
                        object : SearchView.OnQueryTextListener {
                            override fun onQueryTextSubmit(query: String?): Boolean {
                                return false
                            }

                            override fun onQueryTextChange(newText: String?): Boolean {
                                foldersAdapter.filter.filter(newText)
                                return false
                            }
                        })
                }
            }
            is AppState.Loading -> {
                binding.successFoldersLayout.visibility = View.GONE
                binding.errorFoldersLayout.visibility = View.GONE
                binding.loadingFoldersLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                showErrorScreen(appState.error.message)
            }
            else -> {}
        }
    }

    private fun showErrorScreen(error: String?) {
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            viewModel.getData()
        }
        binding.successFoldersLayout.visibility = View.GONE
        binding.errorFoldersLayout.visibility = View.VISIBLE
        binding.loadingFoldersLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() =
            FoldersFragment()
    }
}