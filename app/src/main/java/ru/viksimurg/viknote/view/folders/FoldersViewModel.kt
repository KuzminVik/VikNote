package ru.viksimurg.viknote.view.folders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.CURRENT_FOLDER_ID
import ru.viksimurg.viknote.utils.EDITING_ID
import ru.viksimurg.viknote.utils.FIRST_LAUNCH_DB
import ru.viksimurg.viknote.view.BaseViewModel

class FoldersViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs : ShPrefsDataSource
): BaseViewModel<AppState>() {

    private val liveData: LiveData<AppState> = _mutableLiveData
    fun subscribe(): LiveData<AppState> = liveData

    private val _resultCheckCountNotes: MutableLiveData<Boolean> = MutableLiveData()
    fun getResultCheckCountNotes(): LiveData<Boolean> = _resultCheckCountNotes

    override fun getData() {
        val v = sharedPrefs.getInt(FIRST_LAUNCH_DB, -1)
        if(v == -1){
            cancelJob()
            viewModelCoroutineScope.launch {
                startFirstDb()
                _mutableLiveData.postValue(AppState.SuccessListFolders(dataBase.getListFolders()))
            }
            sharedPrefs.saveInt(FIRST_LAUNCH_DB, 1)
        }else{
            _mutableLiveData.value = AppState.Loading(null)
            cancelJob()
            viewModelCoroutineScope.launch {
                _mutableLiveData.postValue(AppState.SuccessListFolders(dataBase.getListFolders()))
            }
        }
    }

    private suspend fun startFirstDb() {
        dataBase.saveFolder(Folder(name = "Записки охотника"))
        dataBase.saveFolder(Folder(name = "Список покупок"))
    }

    fun saveFolderId(id: Int){
        sharedPrefs.saveInt(CURRENT_FOLDER_ID, id)
    }

    fun saveFolderIdForEdit(id: Int){
        sharedPrefs.saveInt(EDITING_ID, id)
    }

    fun deleteFolder(id: Int){
        viewModelCoroutineScope.launch{
            dataBase.deleteFolder(id)
        }
    }

    fun checkCountNotesInFolder(id: Int){
        viewModelCoroutineScope.launch{
            val count = dataBase.getCountNotes(id)
            if (count>0) _resultCheckCountNotes.postValue(true)
            else _resultCheckCountNotes.postValue(false)
        }
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = AppState.SuccessListFolders(null)
        super.onCleared()
    }
}