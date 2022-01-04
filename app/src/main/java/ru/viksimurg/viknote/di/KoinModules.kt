package ru.viksimurg.viknote.di

import android.annotation.SuppressLint
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.ResourceChipIds
import ru.viksimurg.viknote.repository.room.DataBase
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.utils.swipe.SwipeHelper
import ru.viksimurg.viknote.view.details.DetailsViewModel
import ru.viksimurg.viknote.view.edit.EditingViewModel
import ru.viksimurg.viknote.view.folders.FoldersViewModel
import ru.viksimurg.viknote.view.main.MainViewModel
import ru.viksimurg.viknote.view.notes.NotesViewModel

val appModules = module {
    single { Room.databaseBuilder(androidApplication(), DataBase::class.java, "VikNoteDB").build() }
    single { ShPrefsDataSource(context = get()) }
    single { get<DataBase>().foldersDao() }
    single { get<DataBase>().notesDao() }
    single { DataBaseImpl(notesDao = get(), foldersDao = get()) }

}

val resourceModules = module {
    single { ResourceChipIds() }
}

val viewModels = module {
    viewModel { FoldersViewModel(dataBase = get(), sharedPrefs = get()) }
    viewModel { EditingViewModel(resourceChipIds = get(), dataBase = get(), sharedPrefs = get()) }
    viewModel { NotesViewModel(dataBase = get(), sharedPrefs = get()) }
    viewModel { DetailsViewModel(dataBase = get(), sharedPrefs = get()) }
    viewModel { MainViewModel(sharedPrefs = get()) }
}

