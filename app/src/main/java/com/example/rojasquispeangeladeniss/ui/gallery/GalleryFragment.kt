package com.example.rojasquispeangeladeniss.ui.gallery

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.rojasquispeangeladeniss.Colaborador
import com.example.rojasquispeangeladeniss.MainActivity
import com.example.rojasquispeangeladeniss.R
import com.example.rojasquispeangeladeniss.databinding.FragmentGalleryBinding

/**
 * Fragmento de Perfil (GalleryFragment por defecto en la plantilla)
 *
 * Criterios implementados:
 * 1. Uso de la clase Colaborador para representar datos estructurados.
 * 2. Recepción de argumentos de navegación (nombre enviado desde Inicio).
 * 2. Intent Implícito: abrir el navegador web con una búsqueda en Google.
 * 4. Notificación al cargar/mostrar el perfil del colaborador.
 */
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Recepción del nombre pasado como argumento de navegación desde HomeFragment
        val nombreRecibido = arguments?.getString("nombreColaborador")
            ?.takeIf { it.isNotBlank() } ?: "Angela Deniss Rojas"

        // 2. Instanciación del objeto Colaborador con el nombre recibido y atributos requeridos
        val colaboradorActual = Colaborador(
            nombre = nombreRecibido,
            area = "Transporte de Pasajeros y Encomiendas",
            añosExperiencia = 3
        )

        // 3. Mostrar los atributos en la interfaz gráfica
        binding.tvNombreColaborador.text = getString(R.string.profile_name_label, colaboradorActual.nombre)
        binding.tvAreaColaborador.text = getString(R.string.profile_area_label, colaboradorActual.area)
        binding.tvExpColaborador.text = getString(R.string.profile_exp_label, colaboradorActual.añosExperiencia)

        // 4. Ejercicio 4: Emisión de notificación al cargar/mostrar la pantalla de Perfil
        lanzarNotificacionPerfilCargado(colaboradorActual.nombre)

        // 5. Intent Implícito: Abrir el navegador buscando el nombre en Google
        binding.btnBuscarGoogle.setOnClickListener {
            val queryBusqueda = Uri.encode(colaboradorActual.nombre)
            val urlGoogle = "https://www.google.com/search?q=$queryBusqueda"

            // Crear el Intent implícito con la acción ACTION_VIEW
            val intentNavegador = Intent(Intent.ACTION_VIEW, Uri.parse(urlGoogle))

            try {
                startActivity(intentNavegador)
            } catch (e: Exception) {
                Toast.makeText(
                    requireContext(),
                    "No se pudo abrir el navegador web: ${e.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Ejercicio 4: Lanza una notificación nativa cuando se carga el perfil del colaborador
     */
    private fun lanzarNotificacionPerfilCargado(nombre: String) {
        val context = requireContext()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_profile)
            .setContentTitle("Perfil de Colaborador Cargado")
            .setContentText("Se ha visualizado la ficha de perfil de: $nombre")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Se ha visualizado la ficha de perfil de: $nombre"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(200, builder.build())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
