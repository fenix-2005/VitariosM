package cl.duoc.vitariosm.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val alias: String,
    val emailSimulado: String,
    val rol: String = "USUARIO",
    val telefonoContactoConfianza: String,
    val objetivoPersonal: String = "",
    val horarioRecordatorio: String = "20:00",
    val recordatorioDiarioActivo: Boolean = true,
    val pinSeguridad: String? = null,
    val aceptoDisclaimerOnboarding: Boolean = false
)