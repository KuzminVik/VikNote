package ru.viksimurg.viknote.view.edit

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class EditDialogFragment: DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Вы не добавили заголовок")
                .setPositiveButton("ОК") {
                        dialog, id ->  dialog.cancel()
                }
            builder.create()
        } ?: throw IllegalStateException("AlertDialog: Activity cannot be null")
    }

}