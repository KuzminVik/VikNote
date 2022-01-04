package ru.viksimurg.viknote.model

import ru.viksimurg.viknote.view.OnClickFabListener

sealed class FabState{
    data class EditFragmentFab(val tag: String, val onClickFabListener: OnClickFabListener): FabState()
    data class ListElementsFragmentFab(val tag: String, val onClickFabListener: OnClickFabListener): FabState()
    data class DetailsFragmentFab(val tag: String, val onClickFabListener: OnClickFabListener): FabState()
    data class Error(val error: Throwable): FabState()
}
