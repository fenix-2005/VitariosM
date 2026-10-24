package cl.duoc.vitariosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val professionalId: Int,
    val professionalName: String,
    val specialty: String,
    val dateTimeSlot: String,
    val reason: String,
    val authorizeContextGuide: Boolean, // Consentimiento para asociar guía/resumen a la cita
    val contextSummary: String, // Síntesis consentida que verá el perfil Profesional Demo
    val status: String = "Solicitada" // Solicitada, Confirmada, Reprogramada, Cancelada, Realizada
)