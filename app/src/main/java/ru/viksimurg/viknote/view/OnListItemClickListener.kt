package ru.viksimurg.viknote.view

interface OnListItemClickListener<T> {
    fun onItemClick(data: T)
    fun onPriorityClick()
    fun onChoosePriorityClick(priority: Int)
}