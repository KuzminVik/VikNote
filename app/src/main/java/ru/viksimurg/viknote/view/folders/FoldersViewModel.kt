package ru.viksimurg.viknote.view.folders

import androidx.lifecycle.LiveData
import kotlinx.coroutines.launch
import ru.viksimurg.viknote.model.AppState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.FIRST_LAUNCH_DB
import ru.viksimurg.viknote.view.BaseViewModel

class FoldersViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs : ShPrefsDataSource
): BaseViewModel<AppState>() {

    private val liveData: LiveData<AppState> = _mutableLiveData
    fun subscribe(): LiveData<AppState> {
        return liveData
    }

    fun getData() {
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
        dataBase.saveFolder(Folder(name = "Записки охотника", desc = ""))
        dataBase.saveFolder(Folder(name = "Список покупок", desc = ""))
        //  dataBase.saveNote(Note(name = "Заголовок", "Краткое описание", "Текст", 1))
        //  dataBase.saveNote(Note(name = "Заголовок", "Краткое описание", "Текст", 2))
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(AppState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = AppState.SuccessListFolders(null)
        super.onCleared()
    }
}