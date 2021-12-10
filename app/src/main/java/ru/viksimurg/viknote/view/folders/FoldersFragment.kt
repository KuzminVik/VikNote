package ru.viksimurg.viknote.view.folders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.android.ext.android.inject
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentFoldersBinding
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.view.OnListItemClickListener

class FoldersFragment : Fragment() {

    private var _binding: FragmentFoldersBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FoldersViewModel by inject()

    private val onListItemClickListener: OnListItemClickListener<Folder> =
        object : OnListItemClickListener<Folder>{
            override fun onItemClick(data: Folder) {
                Toast.makeText(requireContext(), "onItemClick", Toast.LENGTH_SHORT).show()
            }

            override fun onPriorityClick() {
                Toast.makeText(requireContext(), "onPriorityClick", Toast.LENGTH_SHORT).show()
            }

            override fun onChoosePriorityClick(priority: Int) {
                TODO("Not yet implemented")
            }
        }

    private val foldersAdapter = FoldersAdapter(onListItemClickListener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFoldersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribe().observe(viewLifecycleOwner, {renderData(it)})
        viewModel.getData()
        binding.foldersRv.adapter = foldersAdapter
        binding.foldersRv.layoutManager = LinearLayoutManager(requireView().context)
//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    private fun renderData(appState: AppState){
        when(appState){
            is AppState.SuccessListFolders ->{
                val data = appState.data
                Log.d("TAG FoldersFragment", "!!!!!! fun renderData ${data.toString()} ??????")
                if(data == null || data.isEmpty()){
                    showErrorScreen(getString(R.string.empty_data))
                }else{
                    binding.errorFoldersLayout.visibility = View.GONE
                    binding.laodingFoldersLayout.visibility = View.GONE
                    binding.successFoldersLayout.visibility = View.VISIBLE
                    foldersAdapter.setData(data)
                }
            }
            is AppState.Loading ->{
                binding.successFoldersLayout.visibility = View.GONE
                binding.errorFoldersLayout.visibility = View.GONE
                binding.laodingFoldersLayout.visibility = View.VISIBLE
            }
            is AppState.Error ->{
                showErrorScreen(appState.error.message)

            }
            else -> {}
        }
    }

    private fun showErrorScreen(error: String?) {
        binding.errorTextview.text = error ?: getString(R.string.undefined_error)
        binding.reloadButton.setOnClickListener {
            //TODO
        }
        binding.successFoldersLayout.visibility = View.GONE
        binding.errorFoldersLayout.visibility = View.VISIBLE
        binding.laodingFoldersLayout.visibility = View.GONE
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