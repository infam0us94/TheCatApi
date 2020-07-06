package com.exemple.thecatapi

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.exemple.thecatapi.Fragments.CatsListFragment
import com.exemple.thecatapi.Fragments.FavListFragment
import com.google.android.material.navigation.NavigationView
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawer: DrawerLayout
    lateinit var imageView: ImageView
    lateinit var btn_save: Button
    lateinit var outputStream: OutputStream

    lateinit var drawable: BitmapDrawable
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawerLayout)
        imageView = findViewById(R.id.imageView)
        btn_save = findViewById(R.id.downloadBtn)

        saveImage()

        val navigationView: NavigationView = findViewById(R.id.navView)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.fragment_container,
                CatsListFragment()
            ).commit()
            navigationView.setCheckedItem(R.id.cat_list)
        }
    }

    private fun saveImage() {
        btn_save.setOnClickListener {
            drawable = imageView.drawable as BitmapDrawable
            bitmap = drawable.bitmap
            val filepath = Environment.getExternalStorageDirectory()
            val dir = File(filepath.absolutePath + "/Downloads")
            dir.mkdir()
            val file =
                File(dir, System.currentTimeMillis().toString() + ".jpg")
            try {
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                Toast.makeText(applicationContext, "Image Save", Toast.LENGTH_SHORT).show()
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.cat_list -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    CatsListFragment()
                ).commit()
            }
            R.id.fav_list -> {
                supportFragmentManager.beginTransaction().replace(
                    R.id.fragment_container,
                    FavListFragment()
                ).commit()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
