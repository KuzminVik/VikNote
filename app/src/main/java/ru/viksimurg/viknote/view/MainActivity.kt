package ru.viksimurg.viknote.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import ru.viksimurg.viknote.R
import ru.viksimurg.viknote.databinding.ActivityMainBinding
import ru.viksimurg.viknote.repository.shprefs.IShPrefs
import ru.viksimurg.viknote.repository.shprefs.ShPrefsDataSource
import ru.viksimurg.viknote.utils.EDITING_STATE
import ru.viksimurg.viknote.utils.STATE_FOLDER_EDIT
import ru.viksimurg.viknote.utils.STATE_FOLDER_EMPTY
import ru.viksimurg.viknote.utils.STATE_NOTE_EMPTY
import ru.viksimurg.viknote.view.edit.EditFragment
import ru.viksimurg.viknote.view.folders.FoldersFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var shPrefs: IShPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setFabButtons()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FoldersFragment.newInstance())
                .commitNow()
        }

//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    private fun setFabButtons(){
        shPrefs = ShPrefsDataSource(this)
        binding.fabMenu.shrink()
        binding.fabMenu.setOnClickListener { view ->
            expandOrCollapseFAB()
        }
        binding.fabFolder.setOnClickListener {
            shPrefs.saveInt(EDITING_STATE, STATE_FOLDER_EMPTY)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EditFragment.newInstance())
                .addToBackStack(" ")
                .commitAllowingStateLoss()
            expandOrCollapseFAB()
        }
        binding.fabNote.setOnClickListener {
            shPrefs.saveInt(EDITING_STATE, STATE_NOTE_EMPTY)
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, EditFragment.newInstance())
                .addToBackStack(" ")
                .commitAllowingStateLoss()
            expandOrCollapseFAB()
        }
    }

    private fun expandOrCollapseFAB(){
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