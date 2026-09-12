package com.example.rojasquispeangeladeniss.ui.slideshow

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.rojasquispeangeladeniss.R
import com.example.rojasquispeangeladeniss.databinding.FragmentSlideshowBinding

/**
 * Fragmento de Ajustes (SlideshowFragment por defecto en la plantilla)
 *
 * Criterios implementados:
 * 3. Cambio de tema claro/oscuro dinámico usando AppCompatDelegate.
 * Ejercicio 5. Animación en bucle usando ObjectAnimator de Android Views.
 */
class SlideshowFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private val binding get() = _binding!!

    // Referencia al ObjectAnimator para cancelar la animación al destruir la vista y evitar memory leaks
    private var loopAnimator: ObjectAnimator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Verificar y actualizar el estado visual del tema en el TextView
        actualizarTextoEstadoTema()

        // 2. Configurar la acción del botón para alternar entre Tema Claro y Tema Oscuro
        binding.btnCambiarTema.setOnClickListener {
            val esModoOscuroActual = esModoOscuroActivo()

            if (esModoOscuroActual) {
                // Si el tema actual es Oscuro, cambiar a Tema Claro (MODE_NIGHT_NO)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                // Si el tema actual es Claro, cambiar a Tema Oscuro (MODE_NIGHT_YES)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }

        // 3. Ejercicio 5: Iniciar la animación en bucle continuo sobre la figura visual (ivAnimatedShape)
        iniciarAnimacionEnBucle()
    }

    /**
     * Ejercicio 5: Implementa una animación de traslación horizontal continua de un lado a otro en bucle infinito.
     */
    private fun iniciarAnimacionEnBucle() {
        // Se desplaza la figura visual ivAnimatedShape de -120px a +120px horizontalmente
        loopAnimator = ObjectAnimator.ofFloat(binding.ivAnimatedShape, View.TRANSLATION_X, -120f, 120f).apply {
            duration = 1500L // Duración de 1.5 segundos por trayecto
            repeatCount = ValueAnimator.INFINITE // Bucle infinito
            repeatMode = ValueAnimator.REVERSE // Oscilación ida y vuelta
            start()
        }
    }

    /**
     * Determina si la aplicación está funcionando actualmente en modo oscuro
     */
    private fun esModoOscuroActivo(): Boolean {
        val defaultMode = AppCompatDelegate.getDefaultNightMode()
        if (defaultMode == AppCompatDelegate.MODE_NIGHT_YES) {
            return true
        } else if (defaultMode == AppCompatDelegate.MODE_NIGHT_NO) {
            return false
        }

        // Si el modo es MODE_NIGHT_FOLLOW_SYSTEM o no definido, se consulta la configuración actual del sistema
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    /**
     * Actualiza el TextView indicando si el tema actual es Claro u Oscuro
     */
    private fun actualizarTextoEstadoTema() {
        if (esModoOscuroActivo()) {
            binding.tvTemaEstado.text = getString(R.string.theme_status_dark)
        } else {
            binding.tvTemaEstado.text = getString(R.string.theme_status_light)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Cancelar el animator para evitar fugas de memoria al salir del fragmento
        loopAnimator?.cancel()
        loopAnimator = null
        _binding = null
    }
}
