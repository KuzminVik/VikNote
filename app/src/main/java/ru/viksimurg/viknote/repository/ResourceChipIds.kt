package ru.viksimurg.viknote.repository

import android.annotation.SuppressLint
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.utils.*

class ResourceChipIds {
    @SuppressLint("ResourceType")
    private val listIdsChips: Map<String, Int> = mapOf(
        CHIPS_NOTE_GREY to R.id.chip_color_grey_note,
        CHIPS_NOTE_GREEN to R.id.chip_color_green_note,
        CHIPS_NOTE_RED to R.id.chip_color_red_note,
        CHIPS_FOLDER_GREY to R.id.chip_color_grey,
        CHIPS_FOLDER_GREEN to R.id.chip_color_green,
        CHIPS_FOLDER_RED to R.id.chip_color_red
    )

    fun getChipIdByKey(key: String): Int = listIdsChips[key] ?: -1

    fun getKeyChipById(id: Int): String {
        var temp = ""
        for (el in listIdsChips){
            if (el.value == id) temp = el.key
        }
        return temp
    }
}