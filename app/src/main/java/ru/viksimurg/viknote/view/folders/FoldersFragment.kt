package ru.viksimurg.viknote.view.folders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.FragmentFoldersBinding
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.view.OnListItemClickListener

class FoldersFragment : Fragment() {

    private var _binding: FragmentFoldersBinding? = null
    private val binding get() = _binding!!

    private val onListItemClickListener: OnListItemClickListener<Folder> =
        object : OnListItemClickListener<Folder>{
            override fun onItemClick(data: Folder) {
                TODO("Not yet implemented")
            }

            override fun onPriorityClick() {
                TODO("Not yet implemented")
            }

            override fun onChoosePriorityClick(priority: Int) {
                TODO("Not yet implemented")
            }

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

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}