package com.example.rojasquispeangeladeniss.ui.gallery

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.rojasquispeangeladeniss.Colaborador
import com.example.rojasquispeangeladeniss.R
import com.example.rojasquispeangeladeniss.databinding.FragmentGalleryBinding

/**
 * Fragmento de Perfil (GalleryFragment por defecto en la plantilla)
 *
 * Criterios implementados:
 * 1. Uso de la clase Colaborador para representar datos estructurados.
 * 2. Recepción de argumentos de navegación (nombre enviado desde Inicio).
 * 2. Intent Implícito: abrir el navegador web con una búsqueda en Google.
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

        // 4. Intent Implícito: Abrir el navegador buscando el nombre en Google
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
