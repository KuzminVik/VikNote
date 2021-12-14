package ru.viksimurg.viknote.view.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.viksimurg.viknote.model.EditingModeState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.room.Folder
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.BaseViewModel

class EditingViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs: ShPrefsDataSource
) : BaseViewModel<EditingModeState>() {

    private val liveData: LiveData<EditingModeState> = _mutableLiveData
    fun subscribe(): LiveData<EditingModeState> {
        return liveData
    }

    private val _resultCheckNameFolder: MutableLiveData<Boolean> = MutableLiveData()
    private val resultCheckNameFolder: LiveData<Boolean> = _resultCheckNameFolder
    fun getCheckNameFolder(): LiveData<Boolean> {
        return resultCheckNameFolder
    }

    fun getData() {
        when (sharedPrefs.getInt(EDITING_STATE, -1)) {
            STATE_FOLDER_EMPTY -> {
                _mutableLiveData.value = EditingModeState.FolderState(null)
            }
            STATE_NOTE_EMPTY -> {
                cancelJob()
                viewModelCoroutineScope.launch {
                    val list = dataBase.getListFolders()
                    _mutableLiveData.postValue(EditingModeState.NoteState(Pair(list, null)))
                }
            }
            STATE_FOLDER_EDIT -> {
                val id = sharedPrefs.getInt(EDITING_ID, -1)
                cancelJob()
                viewModelCoroutineScope.launch {
                    _mutableLiveData.postValue(
                        EditingModeState.FolderState(
                            dataBase.getFolderById(id)
                        )
                    )
                }
            }
            STATE_NOTE_EDIT -> {
                val id = sharedPrefs.getInt(EDITING_ID, -1)
                cancelJob()
                viewModelCoroutineScope.launch {
                    val list = dataBase.getListFolders()
                    val note = dataBase.getNoteById(id)
                    _mutableLiveData.postValue(EditingModeState.NoteState(Pair(list, note)))
                }
            }
            -1 -> {}
        }
    }

    fun saveFolder(name: String, desc: String?, priority: Int) {
        viewModelCoroutineScope.launch { dataBase.saveNewFolder(name, desc, priority) }
    }

    fun saveNote(name: String, desc: String?, text: String?, folderId: Int, priority: Int) {

    }

    fun checkNameFolder(name: String) {
        viewModelCoroutineScope.launch {
            val list = dataBase.getListFolders()
            var count = 0
            list.forEach {
                if (it.name.equals(name)) {
                    count = 1
                    return@forEach
                }
            }
            if (count==1) _resultCheckNameFolder.postValue(true)
            else _resultCheckNameFolder.postValue(false)
        }
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.postValue(EditingModeState.Error(error))
    }

    override fun onCleared() {
        _mutableLiveData.value = EditingModeState.NoteState(null)
        super.onCleared()
    }
}