package ru.viksimurg.viknote.view.folders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.ItemListFolderBinding
import ru.viksimurg.viknote.repository.room.Folder
import java.util.*

class FoldersAdapter(private var clickListener: (Int) -> Unit): RecyclerView.Adapter<FoldersAdapter.RecyclerItemViewHolder>(), Filterable {

    private var dataFirst: MutableList<Folder> = mutableListOf()
    private var values: MutableList<Folder> = mutableListOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<Folder>) {
        dataFirst = data
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

    override fun getItemCount(): Int = values.size

    inner class RecyclerItemViewHolder(private val binding: ItemListFolderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(folder: Folder){
            if (layoutPosition != RecyclerView.NO_POSITION){
                binding.title.text = folder.name
                binding.title.setOnClickListener { clickListener.invoke(folder.id) }
                binding.countNotes.text = folder.countNotes.toString()
                when(folder.priority){
                    0 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_bookmark_grey_24)
                    1 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_bookmark_green_24)
                    2 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_bookmark_red_24)
                }
            }
        }
    }

    override fun getFilter(): Filter {
        return object: Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    values = dataFirst
                }else{
                    val resultList: MutableList<Folder> = mutableListOf()
                    for (el in dataFirst){
                        if (el.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))){
                            resultList.add(el)
                        }
                    }
                    values = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = values
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                values = results?.values as MutableList<Folder>
                notifyDataSetChanged()
            }
        }
    }

    fun getCurrentFolderId(position: Int): Int = values[position].id

}