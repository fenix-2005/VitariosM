package cl.duoc.vitariosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkins_recordatorios")
data class Recoradatorio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null,
    val moodLevel: String? = null,
    val category: String? = null,
    val emotionTags: String = "",
    val energyLevel: Int? = null,
    val note: String = "",
    val syncStatus: String = "Datos respaldados"
)