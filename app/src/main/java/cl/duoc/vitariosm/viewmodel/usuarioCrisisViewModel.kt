package cl.duoc.vitariosm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitariosm.data.VitariosDatabase
import cl.duoc.vitariosm.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UsuarioCrisisViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VitariosDatabase.getDatabase(application).vitariosDao()

    val usuario: StateFlow<Usuario?> = dao.obtenerUsuario()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val alertaCrisisActiva = MutableStateFlow(false)

    private val palabrasCriticas = listOf(
        "suicidio", "suicidarme", "matarme", "morir", "no quiero vivir",
        "hacerme daño", "autolesion", "crisis", "desesperado"
    )

    fun contieneSenalCritica(texto: String): Boolean {
        val detectado = palabrasCriticas.any { texto.lowercase().contains(it) }
        if (detectado) {
            alertaCrisisActiva.value = true
        }
        return detectado
    }

    fun activarAlertaManual() {
        alertaCrisisActiva.value = true
    }

    fun cerrarAlertaCrisis() {
        alertaCrisisActiva.value = false
    }

    fun ingresarComoDemo(rol: String, alias: String, telefonoConfianza: String) {
        viewModelScope.launch {
            val nuevo = Usuario(
                id = 1,
                alias = alias.ifBlank { "Usuario Demo" },
                emailSimulado = if (rol == "PROFESIONAL") "profesional.demo@gmail.com" else "usuario.demo@gmail.com",
                rol = rol,
                telefonoContactoConfianza = telefonoConfianza.ifBlank { "+56912345678" },
                aceptoDisclaimerOnboarding = true
            )
            dao.guardarUsuario(nuevo)
        }
    }
}