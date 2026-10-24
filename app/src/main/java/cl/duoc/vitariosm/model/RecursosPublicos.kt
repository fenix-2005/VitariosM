package cl.duoc.vitariosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "resources")
data class ResourceEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val category: String,
    val format: String,
    val readingTimeMinutes: Int,
    val content: String,
    val officialSource: String,
    val isFavorite: Boolean = false
)