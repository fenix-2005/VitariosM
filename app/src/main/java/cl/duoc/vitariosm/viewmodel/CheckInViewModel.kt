package cl.duoc.vitariosm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitariosm.data.VitariosDatabase
import cl.duoc.vitariosm.model.Recoradatorio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CheckInViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VitariosDatabase.getDatabase(application).vitariosDao()

    val checkIns: StateFlow<List<Recoradatorio>> = dao.obtenerRecordatoriosCheckIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun guardarCheckIn(
        moodLevel: String?,
        category: String?,
        emotionTags: String,
        energyLevel: Int?,
        note: String
    ) {
        viewModelScope.launch {
            val nuevo = Recoradatorio(
                timestamp = System.currentTimeMillis(),
                updatedAt = null,
                moodLevel = moodLevel ?: "3 · Estable",
                category = category ?: "Estudios/Académico",
                emotionTags = emotionTags,
                energyLevel = energyLevel,
                note = note,
                syncStatus = "Datos respaldados"
            )
            dao.insertarRecordatorioCheckIn(nuevo)
        }
    }

    fun editarCheckIn(item: Recoradatorio, nuevoMood: String?, nuevaNota: String) {
        viewModelScope.launch {
            dao.actualizarRecordatorioCheckIn(
                item.copy(
                    moodLevel = nuevoMood,
                    note = nuevaNota,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun eliminarCheckIn(item: Recoradatorio) {
        viewModelScope.launch {
            dao.eliminarRecordatorioCheckIn(item)
        }
    }
}