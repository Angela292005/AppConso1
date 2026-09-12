package com.example.rojasquispeangeladeniss.ui.home

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rojasquispeangeladeniss.MainActivity
import com.example.rojasquispeangeladeniss.R
import com.example.rojasquispeangeladeniss.databinding.FragmentHomeBinding

/**
 * Fragmento de Inicio (HomeFragment)
 *
 * Criterios implementados:
 * 1. Diseño base funcional: ingreso del nombre del colaborador.
 * 2. Navegación: envío de datos (nombre) al fragmento de Perfil usando Navigation Component.
 * 4. Notificaciones: emisión de notificaciones personalizadas con NotificationManagerCompat.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Contador simple para asignar IDs únicos a las notificaciones
    private var notificationIdCount = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Botón 1: Enviar notificación con mensaje de saludo personalizado
        binding.btnNotificacionSaludo.setOnClickListener {
            val nombre = obtenerNombreIngresado()
            lanzarNotificacion(
                titulo = "Notificación RUTAS S.A.C.",
                mensaje = "¡Hola $nombre! Bienvenido al sistema interno de transporte y encomiendas."
            )
        }

        // Botón 2: Registrar actividad y disparar notificación
        binding.btnRegistrarActividad.setOnClickListener {
            val nombre = obtenerNombreIngresado()
            lanzarNotificacion(
                titulo = "Registro de Actividad",
                mensaje = "Actividad operativa registrada con éxito para: $nombre."
            )
        }

        // Botón 3: Ver mi perfil (Navegación enviando el nombre como argumento)
        binding.btnVerPerfil.setOnClickListener {
            val nombre = obtenerNombreIngresado()
            
            // Creamos el Bundle con la clave "nombreColaborador" que espera mobile_navigation.xml
            val bundle = bundleOf("nombreColaborador" to nombre)

            // Navegamos hacia el fragmento Perfil (nav_gallery) pasando el Bundle de datos
            findNavController().navigate(R.id.action_nav_home_to_nav_gallery, bundle)
        }

        // Ejercicio 5: Botón para mostrar / ocultar el gráfico de rendimiento
        binding.btnTogglePerformance.setOnClickListener {
            if (binding.layoutPerformanceContainer.visibility == View.VISIBLE) {
                // Si el gráfico está visible, se oculta usando View.GONE
                binding.layoutPerformanceContainer.visibility = View.GONE
            } else {
                // Si el gráfico está oculto, se hace visible usando View.VISIBLE
                binding.layoutPerformanceContainer.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Obtiene el texto ingresado en el EditText o devuelve un valor por defecto si está vacío
     */
    private fun obtenerNombreIngresado(): String {
        val texto = binding.etNombreColaborador.text.toString().trim()
        return if (texto.isNotEmpty()) texto else "Angela Deniss Rojas"
    }

    /**
     * Criterio 4: Construye y muestra una notificación usando NotificationManagerCompat
     */
    private fun lanzarNotificacion(titulo: String, mensaje: String) {
        val context = requireContext()

        // En Android 13+ (API 33+), se requiere verificar que el permiso POST_NOTIFICATIONS esté concedido
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context, "Permiso de notificaciones no concedido", Toast.LENGTH_SHORT).show()
                return
            }
        }

        // Construcción de la notificación
        val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_home)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setStyle(NotificationCompat.BigTextStyle().bigText(mensaje))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        // Disparar la notificación mediante NotificationManagerCompat
        with(NotificationManagerCompat.from(context)) {
            notify(notificationIdCount++, builder.build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
