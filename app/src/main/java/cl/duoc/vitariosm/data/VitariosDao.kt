package cl.duoc.vitariosm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cl.duoc.vitariosm.model.Citas
import cl.duoc.vitariosm.model.Recoradatorio
import cl.duoc.vitariosm.model.RecursosPublicos
import cl.duoc.vitariosm.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface VitariosDao {
    // --- USUARIO / PERFIL ---
    @Query("SELECT * FROM usuarios WHERE id = 1 LIMIT 1")
    fun obtenerUsuario(): Flow<Usuario?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun guardarUsuario(usuario: Usuario)

    // --- CHECK-IN EMOCIONAL Y RECORDATORIOS (BITÁCORA) ---
    @Query("SELECT * FROM checkins_recordatorios ORDER BY id DESC")
    fun obtenerRecordatoriosCheckIn(): Flow<List<Recoradatorio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRecordatorioCheckIn(item: Recoradatorio)

    @Update
    suspend fun actualizarRecordatorioCheckIn(item: Recoradatorio)

    @Delete
    suspend fun eliminarRecordatorioCheckIn(item: Recoradatorio)

    // --- CITAS FICTICIAS ---
    @Query("SELECT * FROM citas ORDER BY id DESC")
    fun obtenerCitas(): Flow<List<Citas>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCita(cita: Citas)

    @Update
    suspend fun actualizarCita(cita: Citas)

    // --- RECURSOS PÚBLICOS ---
    @Query("SELECT * FROM recursos_publicos ORDER BY id ASC")
    fun obtenerRecursos(): Flow<List<RecursosPublicos>>

    @Query("SELECT COUNT(*) FROM recursos_publicos")
    suspend fun contarRecursos(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarRecursos(recursos: List<RecursosPublicos>)

    @Update
    suspend fun actualizarRecurso(recurso: RecursosPublicos)
}