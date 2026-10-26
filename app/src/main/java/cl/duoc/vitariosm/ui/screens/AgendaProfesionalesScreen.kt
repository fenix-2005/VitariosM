package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.model.Citas
import cl.duoc.vitariosm.ui.theme.*

@Composable
fun AgendaProfesionalesScreen(
    citas: List<Citas>,
    onAgendar: (
        professionalId: Int,
        professionalName: String,
        specialty: String,
        dateTimeSlot: String,
        reason: String,
        authorizeContextGuide: Boolean
    ) -> Unit,
    onCambiarEstado: (Citas, String, String) -> Unit
) {
    val catalogoDemo = listOf(
        Triple(101, "Ps. Camila Valdés (Demo)", "Ansiedad y estrés académico"),
        Triple(102, "Ps. Martín Rojas (Demo)", "Hábitos de sueño y organización"),
        Triple(103, "Ps. Valentina Soto (Demo)", "Acompañamiento emocional")
    )

    var elegido by remember { mutableStateOf(catalogoDemo.first()) }
    var horario by remember { mutableStateOf("Martes 16:00 hrs") }
    var motivo by remember { mutableStateOf("") }
    var autorizaContexto by remember { mutableStateOf(true) }

    val bloques = listOf("Martes 16:00 hrs", "Miércoles 11:30 hrs", "Jueves 18:00 hrs")

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Explorar Profesionales (Demo)", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("Selecciona un profesional ficticio, horario y decide si autorizas asociar tu guía de contexto.", fontSize = 12.sp, color = Gris)
        }

        items(catalogoDemo) { prof ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (elegido.first == prof.first) Color(0xFFE1F3FE) else FondoSecundario
                ),
                modifier = Modifier.fillMaxWidth().clickable { elegido = prof }
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(prof.second, fontWeight = FontWeight.Bold, color = Navy)
                    Text("Modalidad Online (Demo)", fontSize = 12.sp, color = CianConexion)
                    Text("Especialidad: ${prof.third}", fontSize = 13.sp, color = Gris)
                }
            }
        }

        item {
            Text("Selecciona un bloque horario:", fontWeight = FontWeight.SemiBold, color = Navy)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(bloques) { b ->
                    FilterChip(selected = horario == b, onClick = { horario = b }, label = { Text(b) })
                }
            }
            OutlinedTextField(
                value = motivo,
                onValueChange = { motivo = it },
                label = { Text("Motivo breve de consulta") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(checked = autorizaContexto, onCheckedChange = { autorizaContexto = it })
                Spacer(Modifier.width(8.dp))
                Text("Autorizo asociar una guía de contexto a esta cita.", fontSize = 13.sp, color = Navy)
            }
            Button(
                onClick = {
                    onAgendar(
                        elegido.first,
                        elegido.second,
                        elegido.third,
                        horario,
                        motivo.ifBlank { "Orientación y continuidad" },
                        autorizaContexto
                    )
                    motivo = ""
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Confirmar Cita Ficticia")
            }
        }

        items(citas) { cita ->
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("${cita.professionalName} · [${cita.status}]", fontWeight = FontWeight.Bold, color = Navy)
                    Text("Especialidad: ${cita.specialty} · Horario: ${cita.dateTimeSlot}", color = CianConexion, fontSize = 13.sp)
                    Text("Motivo: ${cita.reason}", fontSize = 13.sp, color = Navy)
                    Text(cita.contextSummary, fontSize = 12.sp, color = Gris)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
                        OutlinedButton(onClick = { onCambiarEstado(cita, "Reprogramada", "Viernes 15:00 hrs") }) {
                            Text("Reprogramar")
                        }
                        OutlinedButton(onClick = { onCambiarEstado(cita, "Cancelada", cita.dateTimeSlot) }) {
                            Text("Cancelar", color = Color(0xFFB00020))
                        }
                    }
                }
            }
        }
    }
}