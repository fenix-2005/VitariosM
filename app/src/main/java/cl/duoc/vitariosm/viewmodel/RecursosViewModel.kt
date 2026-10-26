package cl.duoc.vitariosm.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.vitariosm.data.VitariosDatabase
import cl.duoc.vitariosm.model.RecursosPublicos
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecursosViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VitariosDatabase.getDatabase(application).vitariosDao()

    val recursos: StateFlow<List<RecursosPublicos>> = dao.obtenerRecursos()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        precargarRecursosDemo()
    }

    fun alternarFavorito(recurso: RecursosPublicos) {
        viewModelScope.launch {
            dao.actualizarRecurso(recurso.copy(isFavorite = !recurso.isFavorite))
        }
    }

    private fun precargarRecursosDemo() {
        viewModelScope.launch {
            if (dao.contarRecursos() == 0) {
                val listaInicial = listOf(
                    RecursosPublicos(
                        id = 1,
                        title = "Respiración guiada 4-7-8 para bajar tensión",
                        category = "Respiración y relajación",
                        format = "TEMPORIZADOR",
                        readingTimeMinutes = 2,
                        content = "Inhala 4 segundos, mantén 7 segundos y exhala suavemente en 8 segundos. Ayuda a disminuir la tensión aguda.",
                        officialSource = "MINSAL / Recurso Demo VITAIROS-M",
                        isFavorite = true
                    ),
                    RecursosPublicos(
                        id = 2,
                        title = "Organización ante sobrecarga académica",
                        category = "Estrés académico",
                        format = "FICHA",
                        readingTimeMinutes = 2,
                        content = "Divide una entrega grande en micro-pasos de 15 minutos. Elige solo una tarea pequeña para comenzar hoy.",
                        officialSource = "Guía Bienestar Estudiantil Demo",
                        isFavorite = false
                    ),
                    RecursosPublicos(
                        id = 3,
                        title = "Higiene del sueño y desconexión digital",
                        category = "Sueño y descanso",
                        format = "ARTICULO",
                        readingTimeMinutes = 2,
                        content = "Evitar pantallas 30 minutos antes de dormir mejora la calidad del descanso en semanas de alta exigencia.",
                        officialSource = "MINSAL Salud Responde (Demo)",
                        isFavorite = false
                    ),
                    RecursosPublicos(
                        id = 4,
                        title = "¿Cuándo considerar apoyo de un profesional?",
                        category = "Apoyo profesional",
                        format = "ARTICULO",
                        readingTimeMinutes = 2,
                        content = "Si el malestar persiste o dificulta tu rutina diaria, agendar con un especialista te permite recibir acompañamiento personalizado.",
                        officialSource = "VITAIROS-M Orientación General",
                        isFavorite = false
                    )
                )
                dao.insertarRecursos(listaInicial)
            }
        }
    }
}