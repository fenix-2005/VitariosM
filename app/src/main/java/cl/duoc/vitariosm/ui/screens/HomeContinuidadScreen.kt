package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.model.Citas
import cl.duoc.vitariosm.model.Recoradatorio
import cl.duoc.vitariosm.model.Usuario
import cl.duoc.vitariosm.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeContinuidadScreen(
    usuario: Usuario,
    ultimoCheckIn: Recoradatorio?,
    proximaCita: Citas?,
    proximoPaso: String,
    onIrCheckIn: () -> Unit,
    onIrChat: () -> Unit,
    onIrCitas: () -> Unit,
    onCambiarVistaProfesional: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Hola, ${usuario.alias}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Navy)
                }

            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF6F0)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("TU PRÓXIMO PASO SUGERIDO", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CianConexion)
                    Spacer(Modifier.height(4.dp))
                    Text(proximoPaso, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Navy)
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onIrCheckIn, shape = RoundedCornerShape(20.dp)) { Text("Check-in de hoy") }
                        OutlinedButton(onClick = onIrChat, shape = RoundedCornerShape(20.dp)) { Text("Hablar con especialista") }
                    }
                }
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Estado de tu Check-in Emocional", fontWeight = FontWeight.Bold, color = Navy)
                    if (ultimoCheckIn == null) {
                        Text("Aún no registras cómo te sientes hoy.", fontSize = 13.sp, color = Gris)
                    } else {
                        val fechaFormateada = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(ultimoCheckIn.timestamp))
                        Text("Ánimo: ${ultimoCheckIn.moodLevel ?: "Estable"} · ${ultimoCheckIn.category ?: "General"}", fontWeight = FontWeight.SemiBold, color = CianConexion)
                        Text("Registrado el $fechaFormateada · ${ultimoCheckIn.syncStatus}", fontSize = 12.sp, color = Gris)
                    }
                }
            }
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F3FE)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Próxima Cita con Especialista", fontWeight = FontWeight.Bold, color = Navy)
                    if (proximaCita == null) {
                        Text("Sin citas agendadas por ahora.", fontSize = 13.sp, color = Gris)
                    } else {
                        Text("${proximaCita.professionalName} (${proximaCita.dateTimeSlot})", fontWeight = FontWeight.SemiBold, color = Navy)
                        Text("Estado: ${proximaCita.status}", fontSize = 13.sp, color = CianConexion)
                    }
                    TextButton(onClick = onIrCitas) { Text("Explorar profesionales y agenda →") }
                }
            }
        }

        item {
            OutlinedButton(onClick = onCambiarVistaProfesional, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar a Vista Profesional de Salud (Demo Docente)")
            }
        }
    }
}