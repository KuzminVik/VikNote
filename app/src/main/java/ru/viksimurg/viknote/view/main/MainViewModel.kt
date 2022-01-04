package ru.viksimurg.viknote.view.main

import androidx.lifecycle.LiveData
import ru.viksimurg.viknote.model.FabState
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.BaseViewModel
import ru.viksimurg.viknote.view.OnClickFabListener

class MainViewModel(
    private val sharedPrefs : ShPrefsDataSource
): BaseViewModel<FabState>() {

    private val liveData: LiveData<FabState> = _mutableLiveData
    fun subscribe(): LiveData<FabState> = liveData

    override fun getData() {}

    fun setDataFab(tag: String, onClickFabListener: OnClickFabListener){
        when(tag){
            EDIT_FRAGMENT ->{ _mutableLiveData.postValue(FabState.EditFragmentFab(tag, onClickFabListener)) }
            LIST_ELEMENTS_FRAGMENT ->{ _mutableLiveData.postValue(FabState.ListElementsFragmentFab(tag, onClickFabListener)) }
            DETAILS_FRAGMENT ->{ _mutableLiveData.postValue(FabState.DetailsFragmentFab(tag, onClickFabListener)) }
        }
    }

    fun saveIntPrefs(editingState: String, state: Int){
        sharedPrefs.saveInt(editingState, state)
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(FabState.Error(error))
    }
}