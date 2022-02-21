package ru.viksimurg.viknote.view.edit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.viksimurg.viknote.model.EditingModeState
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.ResourceChipIds
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.BaseViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class EditingViewModel(
    private val dataBase: DataBaseImpl,
    private val sharedPrefs: ShPrefsDataSource,
    private val resourceChipIds: ResourceChipIds,
) : BaseViewModel<EditingModeState>() {

    private val liveData: LiveData<EditingModeState> = _mutableLiveData
    fun subscribe(): LiveData<EditingModeState> = liveData

    private val _resultCheckNameFolder: MutableLiveData<Boolean> = MutableLiveData()
    private val resultCheckNameFolder: LiveData<Boolean> = _resultCheckNameFolder
    fun getCheckNameFolder(): LiveData<Boolean> = resultCheckNameFolder

    private val _choiceChipId: MutableLiveData<Int> = MutableLiveData()
    private val choiceChipId: LiveData<Int> = _choiceChipId
    fun getChoiceChipId(): LiveData<Int> = choiceChipId

    private var _currentPriority = 0
    private val currentPriority get() = _currentPriority

    private var updNoteId: Int? = null
    private var updNoteDate: String? = null
    private var updatingFolderId: Int? = null

    override fun getData() {
        when (sharedPrefs.getInt(EDITING_STATE, -1)) {
            STATE_FOLDER_EMPTY -> {
                _mutableLiveData.value = EditingModeState.FolderState(null)
                getChipToChoice(CHIPS_NOTE_GREY)
            }
            STATE_NOTE_EMPTY -> {
                cancelJob()
                viewModelCoroutineScope.launch {
                    val list = dataBase.getListFolders()
                    _mutableLiveData.postValue(EditingModeState.NoteState(Pair(list, null)))
                    getChipToChoice(CHIPS_NOTE_GREY)
                }
            }
            STATE_FOLDER_EDIT -> {
                updatingFolderId = sharedPrefs.getInt(EDITING_ID, -1)
                cancelJob()
                viewModelCoroutineScope.launch {
                    val folder = dataBase.getFolderById(updatingFolderId!!)
                    _currentPriority = folder.priority
                    when(_currentPriority){
                        PRIORITY_GREY ->{ getChipToChoice(CHIPS_FOLDER_GREY) }
                        PRIORITY_GREEN ->{ getChipToChoice(CHIPS_FOLDER_GREEN) }
                        PRIORITY_RED ->{ getChipToChoice(CHIPS_FOLDER_RED) }
                    }
                    _mutableLiveData.postValue(EditingModeState.FolderState(folder))
                }
            }
            STATE_NOTE_EDIT -> {
                updNoteId  = sharedPrefs.getInt(EDITING_ID, -1)
                cancelJob()
                viewModelCoroutineScope.launch {
                    val note = dataBase.getNoteById(updNoteId!!)
                    val list = dataBase.getListFolders()
                    updNoteDate = note.date
                    _currentPriority = note.priority
                    when(_currentPriority){
                        PRIORITY_GREY ->{ getChipToChoice(CHIPS_NOTE_GREY) }
                        PRIORITY_GREEN ->{ getChipToChoice(CHIPS_NOTE_GREEN) }
                        PRIORITY_RED ->{ getChipToChoice(CHIPS_NOTE_RED) }
                    }
                    _mutableLiveData.postValue(EditingModeState.NoteState(Pair(list, note)))
                }
            }
            -1 -> {}
        }
    }

    fun saveFolder(name: String) {
        cancelJob()
        viewModelCoroutineScope.launch {
            if (updatingFolderId == null){
                dataBase.saveNewFolder(name = name, priority = currentPriority)
            }else{
                val count = dataBase.getCountNotes(updatingFolderId!!)
                dataBase.updateFolder(id = updatingFolderId!!, name = name, priority = currentPriority, countNotes = count)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveNote(name: String, text: String?, folderId: Int) {
        val dateTime = LocalDateTime.now().format(
            DateTimeFormatter.ofLocalizedTime (
                FormatStyle.SHORT))
        if (updNoteId == null){
            viewModelCoroutineScope.launch {
                dataBase.saveNewNote(name = name, text = text, folderId = folderId, priority = currentPriority, date = dateTime)
                upCountNotesByFolderId(folderId)
            }
        }else{
            viewModelCoroutineScope.launch {
                dataBase.updateNote(id = updNoteId!!, name = name, text = text, folderId = folderId, priority = currentPriority, date = updNoteDate!!)
            }
        }
    }

    fun checkNameFolder(name: String) {
        if (updatingFolderId == null){
            cancelJob()
            viewModelCoroutineScope.launch {
                val list = dataBase.getListFolders()
                var flag = 0
                list.forEach {
                    if (it.name.equals(name)) {
                        flag = 1
                        return@forEach
                    }
                }
                if (flag==1) _resultCheckNameFolder.postValue(true)
                else _resultCheckNameFolder.postValue(false)
            }
        }else{
            _resultCheckNameFolder.postValue(false)
        }
    }

    fun checkNameNote(name: String): Boolean{
        return !(name.isEmpty() || name == " " || name == "  ")
    }

    fun saveFolderId(id: Int) = sharedPrefs.saveInt(CURRENT_FOLDER_ID, id)

    fun setChipToChoice(id: Int) {
        when(resourceChipIds.getKeyChipById(id)){
            CHIPS_NOTE_GREY ->{ _currentPriority = PRIORITY_GREY }
            CHIPS_FOLDER_GREY ->{ _currentPriority = PRIORITY_GREY }
            CHIPS_NOTE_GREEN ->{ _currentPriority = PRIORITY_GREEN }
            CHIPS_FOLDER_GREEN ->{ _currentPriority = PRIORITY_GREEN }
            CHIPS_NOTE_RED ->{ _currentPriority = PRIORITY_RED }
            CHIPS_FOLDER_RED ->{ _currentPriority = PRIORITY_RED }
        }
        _choiceChipId.value = id
    }

    private fun getChipToChoice(key: String) {
        _choiceChipId.postValue(resourceChipIds.getChipIdByKey(key))
    }

    override fun handleError(error: Throwable) {
        _mutableLiveData.value = EditingModeState.Error(error)
    }

    override fun onCleared() {
        _mutableLiveData.value = EditingModeState.NoteState(null)
        super.onCleared()
    }

    private fun upCountNotesByFolderId(id: Int) {
        cancelJob()
        viewModelCoroutineScope.launch{
            dataBase.updateCountNotes(id, +1)
        }
    }
}