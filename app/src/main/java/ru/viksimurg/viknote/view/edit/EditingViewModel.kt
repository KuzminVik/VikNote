package ru.viksimurg.viknote.view.edit

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.viksimurg.viknote.model.EditingModeState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.BaseViewModel

class EditingViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs : ShPrefsDataSource
):BaseViewModel<EditingModeState>() {

    private val liveData: LiveData<EditingModeState> = _mutableLiveData
    fun subscribe(): LiveData<EditingModeState> {
        return liveData
    }

    fun getData(){
        when(sharedPrefs.getInt(EDITING_STATE, -1)){
            STATE_FOLDER_EMPTY ->{ _mutableLiveData.value = EditingModeState.FolderState(null) }
            STATE_NOTE_EMPTY ->{ _mutableLiveData.value = EditingModeState.NoteState(null) }
            STATE_FOLDER_EDIT ->{
                val id = sharedPrefs.getInt(EDITING_ID, -1)
                cancelJob()
                viewModelCoroutineScope.launch{_mutableLiveData.postValue(EditingModeState.FolderState(dataBase.getFolderById(id)))}
            }
            STATE_NOTE_EDIT ->{
                val id = sharedPrefs.getInt(EDITING_ID, -1)
                cancelJob()
                viewModelCoroutineScope.launch{_mutableLiveData.postValue(EditingModeState.NoteState(dataBase.getNoteById(id)))}
            }
            -1 ->{}
        }
    }

    fun saveFolder(name: String, desc: String?, priority: Int){
        viewModelCoroutineScope.launch {dataBase.saveNewFolder(name, desc, priority)}
    }

    fun saveNote(name: String, desc: String?, text: String?, folderId: Int, priority: Int){

    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(EditingModeState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = EditingModeState.NoteState(null)
        super.onCleared()
    }
}