package ru.viksimurg.viknote.view.folders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.viksimurg.viknote.R
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
        return RecyclerItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.dictionary_recyclerview_item, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: RecyclerItemViewHolder, position: Int) {
        holder.bind(values[position])
    }

    override fun getItemCount(): Int {
        return values.size
    }


    inner class RecyclerItemViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(folder: Folder){
            if (layoutPosition != RecyclerView.NO_POSITION){

            }
        }
    }
}