package ru.viksimurg.viknote.view.folders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.ItemListFolderBinding
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.view.OnListItemClickListener

class FoldersAdapter(
    private var onListItemClickListener: OnListItemClickListener<Folder>
    ): RecyclerView.Adapter<FoldersAdapter.RecyclerItemViewHolder>() {

    private var values: List<Folder> = listOf()
    fun setData(data: List<Folder>) {
        values = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerItemViewHolder {
        val binding = ItemListFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class RecyclerItemViewHolder(private val binding: ItemListFolderBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(folder: Folder){
            if (layoutPosition != RecyclerView.NO_POSITION){
                binding.title.text = folder.name
                binding.title.setOnClickListener { onListItemClickListener.onItemClick(folder) }
                binding.folderIcon.setOnClickListener { onListItemClickListener.onPriorityClick() }
                when(folder.priority){
                    0 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_bookmark_grey_24)
                    1 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_bookmark_green_24)
                    2 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_bookmark_red_24)
                }

            }
        }
    }
}