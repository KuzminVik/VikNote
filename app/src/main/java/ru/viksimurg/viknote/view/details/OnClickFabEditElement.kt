package ru.viksimurg.viknote.view.details

import ru.viksimurg.viknote.view.OnClickFabListener

interface OnClickFabEditElement: OnClickFabListener {
    fun onClickEditNote()
    fun onClickCloseNote()
    fun onClickDeleteNote()
}