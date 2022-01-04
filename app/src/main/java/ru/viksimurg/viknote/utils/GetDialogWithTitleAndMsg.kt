package ru.viksimurg.viknote.utils

import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment

fun Fragment.showDialogWithInformation(msg: String, title: String){
    val builder = AlertDialog.Builder(requireContext())
    with(builder) {
        setTitle(title)
        setMessage(msg)
        setPositiveButton("OK", null)
        show()
    }
}

fun Fragment.showDialogWithConfirmation(msg: String, title: String, listener: DialogInterface.OnClickListener){
    val builder = AlertDialog.Builder(requireContext())
    with(builder) {
        setTitle(title)
        setMessage(msg)
        setPositiveButton("Да", listener)
        setNegativeButton("Нет", null)
        show()
    }
}

