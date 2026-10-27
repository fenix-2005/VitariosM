package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.model.Citas
import cl.duoc.vitariosm.ui.theme.*

@Composable
fun VistaProfesionalDemoScreen(
    citas: List<Citas>,
    onActualizarEstadoCita: (Citas, String) -> Unit,
    onVolverModoUsuario: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F3FE))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("PANEL CLÍNICO · PERFIL PROFESIONAL (DEMO)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CianConexion)
                    Text("Bandeja de Continuidad y Síntesis Consentidas", fontSize = 19.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Propósito de esta vista: Demostrar que el profesional NO tiene acceso libre a la bitácora privada del usuario, sino únicamente a la síntesis autorizada de cada cita y a la gestión del estado de atención.",
                        fontSize = 12.sp,
                        color = Gris
                    )
                    Spacer(Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = onVolverModoUsuario) {
                            Text("Ir a Vista Paciente")
                        }
                        OutlinedButton(onClick = onCerrarSesion) {
                            Text("Cerrar sesión")
                        }
                    }
                }
            }
        }

        if (citas.isEmpty()) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("No hay pacientes agendados todavía", fontWeight = FontWeight.Bold, color = Navy)
                        Text("Entra como Usuario Demo, agenda una cita en la pestaña 'Citas' y vuelve aquí para ver cómo la recibe el especialista.", fontSize = 13.sp, color = Gris)
                    }
                }
            }
        }

        items(citas) { cita ->
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Horario: ${cita.dateTimeSlot}", fontWeight = FontWeight.Bold, color = Navy)
                        AssistChip(
                            onClick = {},
                            label = { Text(cita.status, fontWeight = FontWeight.Bold) }
                        )
                    }
                    Text("Especialista: ${cita.professionalName}", fontSize = 13.sp, color = CianConexion)
                    Text("Motivo declarado: ${cita.reason}", fontSize = 13.sp, color = Navy)
                    Spacer(Modifier.height(8.dp))

                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (cita.authorizeContextGuide) Color(0xFFDDF6F0) else Color(0xFFFFF3E0)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (cita.authorizeContextGuide) Icons.Default.VerifiedUser else Icons.Default.Lock,
                                contentDescription = null,
                                tint = if (cita.authorizeContextGuide) VerdeCont else Color(0xFFE65100)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(cita.contextSummary, fontSize = 12.sp, color = Navy)
                        }
                    }

                    Spacer(Modifier.height(8.dp))
                    Text("Acciones del Profesional:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Gris)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                        Button(
                            onClick = { onActualizarEstadoCita(cita, "Confirmada") },
                            colors = ButtonDefaults.buttonColors(containerColor = AzulDigital)
                        ) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Confirmar", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { onActualizarEstadoCita(cita, "Realizada") }
                        ) {
                            Icon(Icons.Default.DoneAll, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Marcar Realizada", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}