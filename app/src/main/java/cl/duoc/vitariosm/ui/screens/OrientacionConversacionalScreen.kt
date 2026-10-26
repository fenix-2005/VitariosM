package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.ui.theme.*

@Composable
fun OrientacionConversacionalScreen(
    onDetectarCrisis: (String) -> Boolean,
    onDefinirProximoPaso: (paso: String, pantallaDestino: String) -> Unit
) {
    var escenario by remember { mutableStateOf<String?>(null) }
    var textoUsuario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Orientación Conversacional", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Navy)
        Text("Orientación general de autocuidado. No reemplaza atención profesional.", fontSize = 12.sp, color = Gris)

        Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario)) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("VITAIROS-M", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = CianConexion)
                Text("Hola. Estoy aquí para ayudarte a definir un paso breve y concreto para hoy. ¿Qué situación se acerca más a lo que sientes?", color = Navy)
            }
        }

        OutlinedButton(onClick = { escenario = "SOBRECARGA" }, modifier = Modifier.fillMaxWidth()) {
            Text("Sobrecarga académica o laboral")
        }
        OutlinedButton(onClick = { escenario = "SUENO" }, modifier = Modifier.fillMaxWidth()) {
            Text("Cansancio o dificultad para descansar")
        }
        OutlinedButton(onClick = { escenario = "PROFESIONAL" }, modifier = Modifier.fillMaxWidth()) {
            Text("Quiero explorar apoyo con un profesional")
        }

        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = textoUsuario,
                onValueChange = { textoUsuario = it },
                label = { Text("O escribe cómo te sientes...") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (!onDetectarCrisis(textoUsuario) && textoUsuario.isNotBlank()) {
                    escenario = "SOBRECARGA"
                    textoUsuario = ""
                }
            }) {
                Text("Enviar")
            }
        }

        if (escenario != null) {
            Card(colors = CardDefaults.cardColors(containerColor = Color(0xFFDDF6F0)), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("TU PRÓXIMO PASO", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CianConexion)
                    Spacer(Modifier.height(4.dp))
                    when (escenario) {
                        "SOBRECARGA" -> {
                            Text("Realizar una pausa de respiración 4-7-8 de 2 minutos.", fontWeight = FontWeight.Bold, color = Navy)
                            Text("Por qué sirve: Bajar la tensión física te ayuda a retomar una tarea pequeña con calma.", fontSize = 13.sp, color = Gris)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { onDefinirProximoPaso("Practicar respiración 4-7-8", "RECURSOS") }) {
                                Text("Hacer actividad")
                            }
                        }
                        "SUENO" -> {
                            Text("Revisar la guía breve de desconexión nocturna.", fontWeight = FontWeight.Bold, color = Navy)
                            Text("Por qué sirve: Ajustar tu rutina 30 minutos antes de dormir mejora tu recuperación.", fontSize = 13.sp, color = Gris)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { onDefinirProximoPaso("Revisar guía de higiene del sueño", "RECURSOS") }) {
                                Text("Ver recurso")
                            }
                        }
                        "PROFESIONAL" -> {
                            Text("Agendar con un especialista demo asociando tu síntesis de contexto.", fontWeight = FontWeight.Bold, color = Navy)
                            Text("Por qué sirve: Así no tienes que explicar todo desde cero en tu primera sesión.", fontSize = 13.sp, color = Gris)
                            Spacer(Modifier.height(8.dp))
                            Button(onClick = { onDefinirProximoPaso("Agendar cita con especialista", "CITAS") }) {
                                Text("Explorar profesionales")
                            }
                        }
                    }
                }
            }
        }
    }
}