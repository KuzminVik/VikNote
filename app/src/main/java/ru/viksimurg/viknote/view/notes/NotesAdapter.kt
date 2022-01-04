package ru.viksimurg.viknote.view.notes

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.ItemListNoteBinding
import ru.viksimurg.viknote.repository.room.Note
import ru.viksimurg.viknote.view.OnListItemClickListener
import java.util.*

class NotesAdapter(
    private var onListItemClickListener: OnListItemClickListener<Note>
): RecyclerView.Adapter<NotesAdapter.RecyclerItemViewHolder>(), Filterable {

    private var dataFirst: MutableList<Note> = mutableListOf()
    private var values: MutableList<Note> = mutableListOf()
    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Note>) {
        dataFirst = data.toMutableList()
        values = data.toMutableList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): NotesAdapter.RecyclerItemViewHolder {
        val binding = ItemListNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecyclerItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesAdapter.RecyclerItemViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class RecyclerItemViewHolder(private val binding: ItemListNoteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(note: Note){
            if (layoutPosition != RecyclerView.NO_POSITION){
                binding.title.text = note.name
                binding.tvDate.text = note.date
                binding.title.setOnClickListener { onListItemClickListener.onItemClick(note) }
                when(note.priority){
                    0 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_create_grey_24)
                    1 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_create_green_24)
                    2 -> binding.priorityIcon.setImageResource(R.drawable.ic_baseline_create_red_24)
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
                    val resultList: MutableList<Note> = mutableListOf()
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
                values = results?.values as MutableList<Note>
                notifyDataSetChanged()
            }

        }
    }

    fun getCurrentNoteId(position: Int): Int{
        return values[position].id
    }

}