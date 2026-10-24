package cl.duoc.vitariosm.data

import androidx.room.*
import cl.duoc.vitariosm.models.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VitairosDao {
    // --- PERFIL ---
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun obtenerPerfil(): Flow<UserProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarPerfil(perfil: UserProfile)

    // --- CHECK-IN EMOCIONAL Y BITÁCORA ---
    @Query("SELECT * FROM emotional_checkins ORDER BY id DESC")
    fun obtenerCheckIns(): Flow<List<EmotionalCheckIn>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCheckIn(checkIn: EmotionalCheckIn)

    @Update
    suspend fun actualizarCheckIn(checkIn: EmotionalCheckIn)

    @Delete
    suspend fun eliminarCheckIn(checkIn: EmotionalCheckIn)

    // --- CITAS FICTICIAS CON PROFESIONALES ---
    @Query("SELECT * FROM appointments ORDER BY id DESC")
    fun obtenerCitas(): Flow<List<Appointment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCita(cita: Appointment)

    @Update
    suspend fun actualizarCita(cita: Appointment)

    // --- BIBLIOTECA DE RECURSOS ---
    @Query("SELECT * FROM resources ORDER BY esFavorito DESC, id ASC")
    fun obtenerRecursos(): Flow<List<ResourceItem>>

    @Query("SELECT COUNT(*) FROM resources")
    suspend fun contarRecursos(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRecursos(recursos: List<ResourceItem>)

    @Update
    suspend fun actualizarRecurso(recurso: ResourceItem)
}