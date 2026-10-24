package cl.duoc.vitariosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emotional_checkins")
data class EmotionalCheckInEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long? = null,
    val moodLevel: Int, //
    val category: String? = null,
    val emotionTags: String = "",
    val energyLevel: Int? = null,
    val note: String = "",
    val syncStatus: String = "Datos respaldados"
)