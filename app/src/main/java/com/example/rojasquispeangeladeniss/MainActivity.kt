package com.example.rojasquispeangeladeniss

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rojasquispeangeladeniss.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        // ID constante para el canal de notificaciones de RUTAS S.A.C.
        const val CHANNEL_ID = "rutas_notification_channel"
    }

    // Launcher para solicitar el permiso POST_NOTIFICATIONS en tiempo de ejecución (Android 13+)
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Las notificaciones están deshabilitadas", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Inicialización de ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2. Configurar la Toolbar superior
        setSupportActionBar(binding.appBarMain.toolbar)

        // 3. Crear el Canal de Notificaciones (Compatible con Android 8.0+)
        crearCanalDeNotificacion()

        // 4. Solicitar permiso de notificaciones si la versión es Android 13 (API 33) o superior
        verificarPermisoNotificaciones()

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView

        // Obtener el NavController de forma segura desde el NavHostFragment para evitar el crash al arrancar
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // 5. Configurar AppBarConfiguration con las 3 secciones del Drawer (Inicio, Perfil, Ajustes)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )

        // 6. Conectar el Controller de Navegación con la ActionBar y el Drawer
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    /**
     * Criterio 4: Crear Canal de Notificación para Android 8.0 (Oreo / API 26) o superior
     */
    private fun crearCanalDeNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nombreCanal = getString(R.string.notification_channel_name)
            val descripcionCanal = getString(R.string.notification_channel_desc)
            val importancia = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(CHANNEL_ID, nombreCanal, importancia).apply {
                description = descripcionCanal
            }

            // Registrar el canal en el sistema mediante NotificationManager
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Criterio 4: Verificar y solicitar permiso POST_NOTIFICATIONS en tiempo de ejecución (Android 13+)
     */
    private fun verificarPermisoNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Solicita el permiso al usuario mediante el diálogo del sistema
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    /**
     * Criterio 3: Inflar el menú de la Toolbar superior con las opciones "Cerrar sesión" y "Soporte"
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    /**
     * Manejar la navegación del botón 'Hamburguesa' / Atrás en la Toolbar
     */
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
