package ru.viksimurg.viknote.view.details

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.CURRENT_FOLDER_ID
import ru.viksimurg.viknote.utils.EDITING_ID
import ru.viksimurg.viknote.utils.OPEN_NOTE_ID
import ru.viksimurg.viknote.view.BaseViewModel

class DetailsViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs : ShPrefsDataSource
): BaseViewModel<AppState>() {

    private val liveData: LiveData<AppState> = _mutableLiveData
    fun subscribe(): LiveData<AppState> = liveData

    override fun getData() {
        val noteId = sharedPrefs.getInt(OPEN_NOTE_ID, -1)
        _mutableLiveData.value = AppState.Loading(null)
        cancelJob()
        viewModelCoroutineScope.launch{
            _mutableLiveData.postValue(AppState.SuccessNote(dataBase.getNoteById(noteId)))
        }
    }

    fun setNoteId(){
        sharedPrefs.saveInt(EDITING_ID, getCurrentNoteId())
    }

    fun deleteNote(){
        val idNote = getCurrentNoteId()
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

    private fun getCurrentNoteId(): Int{
        return sharedPrefs.getInt(OPEN_NOTE_ID, -1)
    }

    private fun getCurrentFolderId(): Int{
        return sharedPrefs.getInt(CURRENT_FOLDER_ID, -1)
    }

    fun saveFolderId(id: Int){
        sharedPrefs.saveInt(CURRENT_FOLDER_ID, id)
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = AppState.SuccessListFolders(null)
        super.onCleared()
    }

}