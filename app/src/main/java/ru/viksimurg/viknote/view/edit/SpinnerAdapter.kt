package ru.viksimurg.viknote.view.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.repository.room.Folder

class SpinnerAdapter(ctx: Context, listData: List<Folder>) :
    ArrayAdapter<Folder>(ctx, 0, listData) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent);
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return if (position == 0) {
            convertView ?: LayoutInflater.from(context).inflate(
                R.layout.spinner_row,
                parent,
                false
            )
        }else{
            createItemView(position, convertView, parent)
        }
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.spinner_row,
            parent,
            false
        )
        val item = getItem(position)
        val label = view.findViewById(R.id.spinner_text) as TextView
        val image = view.findViewById<ImageView>(R.id.spinner_image)
        item?.let {
            label.text = it.name
            when (it.priority) {
                0 -> image.setImageResource(R.drawable.ic_baseline_bookmark_grey_24)
                1 -> image.setImageResource(R.drawable.ic_baseline_bookmark_green_24)
                2 -> image.setImageResource(R.drawable.ic_baseline_bookmark_red_24)
                -1 -> image.setImageResource(R.drawable.ic_hidden_bookmark_24)
            }
        }
        return view
    }
}