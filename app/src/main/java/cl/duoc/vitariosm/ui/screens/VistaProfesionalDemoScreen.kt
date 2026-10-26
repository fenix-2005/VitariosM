package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
    onVolverModoUsuario: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F3FE))) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("MODO PROFESIONAL DE SALUD (DEMO)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CianConexion)
                    Text("Síntesis de Continuidad Autorizadas", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Navy)
                    Text("El profesional solo visualiza la guía de contexto cuando la persona dio su consentimiento expreso.", fontSize = 13.sp, color = Gris)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onVolverModoUsuario) {
                        Text("Volver al Flujo Usuario Final")
                    }
                }
            }
        }

        items(citas) { cita ->
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Cita: ${cita.dateTimeSlot} · Estado: ${cita.status}", fontWeight = FontWeight.Bold, color = Navy)
                    Text("Profesional: ${cita.professionalName} (${cita.specialty})", fontSize = 13.sp, color = CianConexion)
                    Text("Motivo: ${cita.reason}", fontSize = 13.sp, color = Navy)
                    Spacer(Modifier.height(4.dp))
                    Text(cita.contextSummary, fontSize = 13.sp, color = Gris)
                }
            }
        }
    }
}