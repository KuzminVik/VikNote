package ru.viksimurg.viknote.view.notes

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.CURRENT_FOLDER_ID
import ru.viksimurg.viknote.utils.EDITING_ID
import ru.viksimurg.viknote.utils.OPEN_NOTE_ID
import ru.viksimurg.viknote.view.BaseViewModel

class NotesViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs : ShPrefsDataSource
): BaseViewModel<AppState>() {

    private val liveData: LiveData<AppState> = _mutableLiveData
    fun subscribe(): LiveData<AppState> {
        return liveData
    }

    override fun getData() {
        val folderId = sharedPrefs.getInt(CURRENT_FOLDER_ID, -1)
        _mutableLiveData.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch {
            val list = dataBase.getListNotesByFolder(folderId)
            val name = dataBase.getNameFolder(folderId)
            _mutableLiveData.postValue(AppState.SuccessListNotes(list, name))
        }
    }

    fun setNoteIdForEdit(){
        sharedPrefs.saveInt(EDITING_ID, getCurrentNoteId())
    }

    private fun getCurrentNoteId(): Int{
        return sharedPrefs.getInt(OPEN_NOTE_ID, -1)
    }

    fun saveOpenNoteId(id: Int){
        sharedPrefs.saveInt(OPEN_NOTE_ID, id)
    }

    fun deleteNote(idNote: Int){
        viewModelCoroutineScope.launch{
            dataBase.deleteNote(idNote)
        }
    }

    fun upCountNotesByFolderId() {
        val idFolder = getCurrentFolderId()
        viewModelCoroutineScope.launch{
            dataBase.updateCountNotes(idFolder, -1)
        }
    }

    private fun getCurrentFolderId(): Int{
        return sharedPrefs.getInt(CURRENT_FOLDER_ID, -1)
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = AppState.SuccessListNotes(null, "null")
        super.onCleared()
    }

}