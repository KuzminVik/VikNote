package ru.viksimurg.viknote.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.viksimurg.viknote.repository.DataBaseImpl
import ru.viksimurg.viknote.repository.room.DataBase
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.view.edit.EditingViewModel
import ru.viksimurg.viknote.view.folders.FoldersViewModel

val appModules = module {
    single { Room.databaseBuilder(androidApplication(), DataBase::class.java, "VikNoteDB").build() }
    single { ShPrefsDataSource(context = get()) }
    single { get<DataBase>().foldersDao() }
    single { get<DataBase>().notesDao() }
    single { DataBaseImpl(notesDao = get(), foldersDao = get()) }

}

val viewModels = module {
    viewModel { FoldersViewModel(dataBase = get(), sharedPrefs = get()) }
    viewModel { EditingViewModel(dataBase = get(), sharedPrefs = get()) }
}

