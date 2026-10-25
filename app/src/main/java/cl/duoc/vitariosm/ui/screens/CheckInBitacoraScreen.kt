package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.model.Recoradatorio
import cl.duoc.vitariosm.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CheckInBitacoraScreen(
    checkIns: List<Recoradatorio>,
    onGuardar: (moodLevel: String?, category: String?, emotionTags: String, energyLevel: Int?, note: String) -> Unit,
    onEditar: (item: Recoradatorio, nuevoMood: String?, nuevaNota: String) -> Unit,
    onEliminar: (Recoradatorio) -> Unit,
    onIrOrientacion: () -> Unit
) {
    val nivelesAnimo = listOf(
        "1 · Muy bajo",
        "2 · Desanimado",
        "3 · Estable",
        "4 · Bien",
        "5 · Positivo"
    )
    val categoriasFijas = listOf(
        "Estudios/Académico", "Trabajo", "Relaciones/Pareja",
        "Familia", "Sueño/Descanso", "Salud/Bienestar", "Otro"
    )

    var moodLevel by remember { mutableStateOf<String?>(nivelesAnimo[2]) }
    var category by remember { mutableStateOf<String?>(categoriasFijas[0]) }
    var emotionTags by remember { mutableStateOf("") }
    var energyLevel by remember { mutableIntStateOf(3) }
    var note by remember { mutableStateOf("") }
    var mostrarConfirmacion by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Check-in Emocional y Bitácora", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("Solo el estado de ánimo es obligatorio. No constituye un puntaje clínico.", fontSize = 12.sp, color = Gris)
        }

        // 1. moodLevel (Obligatorio)
        item {
            Text("1. ¿Cómo te sientes hoy? (Obligatorio)", fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(nivelesAnimo) { opcion ->
                    FilterChip(
                        selected = moodLevel == opcion,
                        onClick = { moodLevel = opcion },
                        label = { Text(opcion) }
                    )
                }
            }
        }

        // 2. category (Recomendado)
        item {
            Text("2. Categoría de seguimiento (Recomendado)", fontWeight = FontWeight.SemiBold)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categoriasFijas) { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) }
                    )
                }
            }
        }

        // 3. emotionTags, energyLevel y note (Opcionales)
        item {
            OutlinedTextField(
                value = emotionTags,
                onValueChange = { emotionTags = it },
                label = { Text("Etiquetas emocionales opcionales (ej. Ansiedad, Calma)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(6.dp))
            Text("Nivel de energía (Opcional): $energyLevel/5", fontSize = 13.sp, color = Gris)
            Slider(
                value = energyLevel.toFloat(),
                onValueChange = { energyLevel = it.toInt() },
                valueRange = 1f..5f,
                steps = 3
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Nota breve personal (Opcional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Button(
                onClick = {
                    onGuardar(moodLevel, category, emotionTags, energyLevel, note)
                    note = ""
                    emotionTags = ""
                    mostrarConfirmacion = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = VerdeCont)
            ) {
                Text("Guardar Check-in", color = Navy, fontWeight = FontWeight.Bold)
            }
        }

        if (mostrarConfirmacion) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = MintSuave)) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text("Gracias por registrarlo. Si te sirve, puedes elegir un pequeño paso para ahora.", fontWeight = FontWeight.Bold)
                        TextButton(onClick = onIrOrientacion) { Text("Ir a Orientación Conversacional →") }
                    }
                }
            }
        }

        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Así has registrado tu ánimo recientemente", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Herramienta de reflexión personal, no evaluación clínica.", fontSize = 11.sp, color = Gris)
        }

        items(checkIns) { item ->
            var editando by remember { mutableStateOf(false) }
            var notaEditada by remember(item) { mutableStateOf(item.note) }
            var moodEditado by remember(item) { mutableStateOf(item.moodLevel) }

            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "${formatearTimestamp(item.timestamp)} · Ánimo: ${item.moodLevel ?: "No especificado"}",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Categoría: ${item.category ?: "Sin categoría"} · ${item.syncStatus}",
                        fontSize = 12.sp,
                        color = Cian
                    )
                    if (item.emotionTags.isNotBlank()) {
                        Text("Etiquetas: ${item.emotionTags} · Energía: ${item.energyLevel ?: 3}/5", fontSize = 12.sp, color = Gris)
                    }
                    if (item.note.isNotBlank()) {
                        Text(item.note, modifier = Modifier.padding(top = 4.dp))
                    }
                    if (item.updatedAt != null) {
                        Text("Actualizado el ${formatearTimestamp(item.updatedAt)}", fontSize = 10.sp, color = Gris)
                    }

                    if (editando) {
                        Spacer(Modifier.height(6.dp))
                        OutlinedTextField(
                            value = notaEditada,
                            onValueChange = { notaEditada = it },
                            label = { Text("Editar nota") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        TextButton(onClick = {
                            onEditar(item, moodEditado, notaEditada)
                            editando = false
                        }) { Text("Guardar edición") }
                    }

                    Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = { editando = !editando }) { Icon(Icons.Default.Edit, "Editar") }
                        IconButton(onClick = { onEliminar(item) }) { Icon(Icons.Default.Delete, "Eliminar", tint = Color(0xFFB00020)) }
                    }
                }
            }
        }
    }
}

fun formatearTimestamp(timestamp: Long): String {
    return SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(timestamp))
}