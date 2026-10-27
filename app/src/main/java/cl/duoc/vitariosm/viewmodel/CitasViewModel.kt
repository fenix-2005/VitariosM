package cl.duoc.vitariosm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitariosm.data.VitariosDatabase
import cl.duoc.vitariosm.model.Citas
import cl.duoc.vitariosm.model.Recoradatorio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CitasViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VitariosDatabase.getDatabase(application).vitariosDao()

    val citas: StateFlow<List<Citas>> = dao.obtenerCitas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun agendarCitaFicticia(
        professionalId: Int,
        professionalName: String,
        specialty: String,
        dateTimeSlot: String,
        reason: String,
        authorizeContextGuide: Boolean,
        ultimoCheckIn: Recoradatorio?
    ) {
        viewModelScope.launch {
            val sintesis = if (authorizeContextGuide) {
                "Síntesis autorizada: Último ánimo '${ultimoCheckIn?.moodLevel ?: "3 · Estable"}' en '${ultimoCheckIn?.category ?: "Estudios"}'. Motivo: $reason"
            } else {
                "Privado: El usuario no autorizó compartir guía de contexto previa."
            }

            val nuevaCita = Citas(
                professionalId = professionalId,
                professionalName = professionalName,
                specialty = specialty,
                dateTimeSlot = dateTimeSlot,
                reason = reason,
                authorizeContextGuide = authorizeContextGuide,
                contextSummary = sintesis,
                status = "Confirmada"
            )
            dao.insertarCita(nuevaCita)
        }
    }

    fun cambiarEstadoCita(cita: Citas, nuevoEstado: String, nuevoHorario: String = cita.dateTimeSlot) {
        viewModelScope.launch {
            dao.actualizarCita(cita.copy(status = nuevoEstado, dateTimeSlot = nuevoHorario))
        }
    }
    fun eliminarCita(cita: Citas) {
        viewModelScope.launch {
            dao.eliminarCita(cita)
        }
    }
}