package cl.duoc.vitariosm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cl.duoc.vitariosm.model.Citas
import cl.duoc.vitariosm.model.Recoradatorio
import cl.duoc.vitariosm.model.RecursosPublicos
import cl.duoc.vitariosm.model.Usuario

@Database(
    entities = [
        Citas::class,
        Recoradatorio::class,
        RecursosPublicos::class,
        Usuario::class
    ],
    version = 1,
    exportSchema = false
)
abstract class VitariosDatabase : RoomDatabase() {

    abstract fun vitariosDao(): VitariosDao

    companion object {
        @Volatile
        private var INSTANCE: VitariosDatabase? = null

        fun getDatabase(context: Context): VitariosDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    VitariosDatabase::class.java,
                    "vitarios_m_database"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}