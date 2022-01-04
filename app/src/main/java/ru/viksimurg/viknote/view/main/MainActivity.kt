package ru.viksimurg.viknote.view.main

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.ActivityMainBinding
import ru.viksimurg.viknote.model.FabState
import ru.viksimurg.viknote.utils.*
import ru.viksimurg.viknote.view.details.OnClickFabEditElement
import ru.viksimurg.viknote.view.folders.OnClickFabListElements
import ru.viksimurg.viknote.view.edit.OnClickFabSaveElement
import ru.viksimurg.viknote.view.folders.FoldersFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val sharedViewModelFab by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedViewModelFab.subscribe().observe(this, { renderDataFab(it) })
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FoldersFragment.newInstance(), LIST_ELEMENTS_FRAGMENT)
                .commitNow()
        }
    }

    private fun renderDataFab(state: FabState){
        initExtendedFab()
        when(state){
            //Fab НА СПИСКЕ ЭЛЕМЕНТОВ - ДОБАВИТЬ НОВУЮ ПАПКУ ИЛИ ЗАМЕТКУ
            is FabState.ListElementsFragmentFab ->{
                binding.fabAddContainer.visibility = View.VISIBLE
                binding.fabEditContainer.visibility = View.GONE
                binding.fabDetailsContainer.visibility = View.GONE
                binding.fabMenu.shrink()
                binding.fabMenu.setOnClickListener {
                    expandOrCollapseFabMenu()
                }
                state.onClickFabListener as OnClickFabListElements
                binding.fabFolder.setOnClickListener{
                    state.onClickFabListener.onClickAddFolder()
                }
                binding.fabNote.setOnClickListener {
                    state.onClickFabListener.onClickAddNote()
                }
            }
            //Fab НА ЭКРАНЕ РЕДАКТИРОВАНИИ И ДОБАВЛЕНИИ - СОХРАНИТЬ ПАПКУ ИЛИ ЗАМЕТКУ
            is FabState.EditFragmentFab ->{
                binding.fabAddContainer.visibility = View.GONE
                binding.fabEditContainer.visibility = View.VISIBLE
                binding.fabDetailsContainer.visibility = View.GONE
                state.onClickFabListener as OnClickFabSaveElement
                binding.fabButtonSaveElement.setOnClickListener {
                    state.onClickFabListener.onClickSave()
                }
            }
            //Fab НА ПРОСМОТРЕ ЗАМЕТКИ - РЕДАКТИРОВАТЬ, ЗАКРЫТЬ, УДАЛИТЬ
            is FabState.DetailsFragmentFab ->{
                binding.fabAddContainer.visibility = View.GONE
                binding.fabEditContainer.visibility = View.GONE
                binding.fabDetailsContainer.visibility = View.VISIBLE
                binding.fabButtonDetails.shrink()
                binding.fabButtonDetails.setOnClickListener {
                    expandOrCollapseFabDetails()
                }
                state.onClickFabListener as OnClickFabEditElement
                binding.fabCloseDetails.setOnClickListener {
                    state.onClickFabListener.onClickCloseNote()
                }
                binding.fabDeleteDetails.setOnClickListener {
                    state.onClickFabListener.onClickDeleteNote()
                }
                binding.fabEditDetails.setOnClickListener {
                    state.onClickFabListener.onClickEditNote()
                }
            }
            else -> {}
        }
    }

    private fun initExtendedFab(){
        if(binding.fabMenu.isExtended){
            binding.fabMenu.shrink()
            binding.fabNote.hide()
            binding.fabFolder.hide()
            binding.fabLabelNote.visibility = View.GONE
            binding.fabLabelFolder.visibility = View.GONE
        }
        if (binding.fabButtonDetails.isExtended){
            binding.fabButtonDetails.shrink()
            binding.fabCloseDetails.hide()
            binding.fabDeleteDetails.hide()
            binding.fabEditDetails.hide()
            binding.labelFabDetailsClose.visibility = View.GONE
            binding.labelFabDetailsDelete.visibility = View.GONE
            binding.labelFabDetailsEdit.visibility = View.GONE
        }
    }

    private fun expandOrCollapseFabMenu(){
        if(binding.fabMenu.isExtended){
            binding.fabMenu.shrink()
            binding.fabNote.hide()
            binding.fabFolder.hide()
            binding.fabLabelNote.visibility = View.GONE
            binding.fabLabelFolder.visibility = View.GONE
        }else{
            binding.fabMenu.extend()
            binding.fabNote.show()
            binding.fabFolder.show()
            binding.fabLabelNote.visibility = View.VISIBLE
            binding.fabLabelFolder.visibility = View.VISIBLE
        }
    }

    private fun expandOrCollapseFabDetails(){
        if (binding.fabButtonDetails.isExtended){
            binding.fabButtonDetails.shrink()
            binding.fabCloseDetails.hide()
            binding.fabDeleteDetails.hide()
            binding.fabEditDetails.hide()
            binding.labelFabDetailsClose.visibility = View.GONE
            binding.labelFabDetailsDelete.visibility = View.GONE
            binding.labelFabDetailsEdit.visibility = View.GONE
        }else{
            binding.fabButtonDetails.extend()
            binding.fabCloseDetails.show()
            binding.fabDeleteDetails.show()
            binding.fabEditDetails.show()
            binding.labelFabDetailsClose.visibility = View.VISIBLE
            binding.labelFabDetailsDelete.visibility = View.VISIBLE
            binding.labelFabDetailsEdit.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}