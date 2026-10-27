package cl.duoc.vitariosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citas")
data class Citas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val professionalId: Int,
    val professionalName: String,
    val specialty: String,
    val dateTimeSlot: String,
    val reason: String,
    val authorizeContextGuide: Boolean,
    val contextSummary: String,
    val status: String = "Solicitada"
)