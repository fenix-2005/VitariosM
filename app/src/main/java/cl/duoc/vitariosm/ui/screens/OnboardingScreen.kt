package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.model.RecursosPublicos
import cl.duoc.vitariosm.ui.theme.*

@Composable
fun OnboardingScreen(
    onIngresarDemo: (rol: String, alias: String, fonoConfianza: String) -> Unit,
    onAyudaUrgente: () -> Unit
) {
    var alias by remember { mutableStateOf("Javier Demo") }
    var correoSimulado by remember { mutableStateOf("usuario.demo@vitairos.cl") }
    var fonoConfianza by remember { mutableStateOf("+56987654321") }
    var aceptaCondiciones by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(BlancoBase).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("VITAIROS-M", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Navy)
            Text("SALUD MENTAL DIGITAL · LA VIDA NO OCURRE SOLO EN LA SESIÓN", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Cian)
            Spacer(Modifier.height(8.dp))
            Text("Que pedir ayuda no signifique empezar de cero. Registra cómo te sientes y recibe un próximo paso claro.", color = Gris)
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Límites éticos y privacidad local", fontWeight = FontWeight.Bold, color = Navy)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        "VITAIROS-M es una herramienta de orientación y acompañamiento general. No realiza diagnósticos, no prescribe tratamientos, no entrega terapia y no reemplaza la atención de profesionales de salud. Si existe riesgo inmediato o una emergencia, busca ayuda presencial y utiliza los canales oficiales de emergencia disponibles en Chile.",
                        fontSize = 13.sp,
                        color = Gris
                    )
                }
            }
        }

        item {
            OutlinedTextField(value = alias, onValueChange = { alias = it }, label = { Text("Tu alias de preferencia") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = correoSimulado, onValueChange = { correoSimulado = it }, label = { Text("Correo simulado (Ficticio)") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = fonoConfianza, onValueChange = { fonoConfianza = it }, label = { Text("Teléfono contacto de confianza") }, modifier = Modifier.fillMaxWidth())
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = aceptaCondiciones, onCheckedChange = { aceptaCondiciones = it })
                Text("Comprendo los límites éticos y acepto las condiciones de uso.", fontSize = 13.sp)
            }
        }

        item {
            Button(
                onClick = { onIngresarDemo("USUARIO", alias, fonoConfianza) },
                enabled = aceptaCondiciones,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulDigital)
            ) {
                Text("Ingresar como Usuario Demo", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onIngresarDemo("PROFESIONAL", "Ps. Martín Rojas (Demo)", fonoConfianza) },
                enabled = aceptaCondiciones,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ingresar como Profesional de Salud (Demo)")
            }
            TextButton(onClick = onAyudaUrgente, modifier = Modifier.fillMaxWidth()) {
                Text("Necesito ayuda urgente ahora (*4141)", color = Color(0xFFB00020), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BibliotecaRecursosScreen(
    recursos: List<RecursosPublicos>,
    onToggleFavorito: (RecursosPublicos) -> Unit
) {
    var busqueda by remember { mutableStateOf("") }
    var soloFavoritos by remember { mutableStateOf(false) }

    val filtrados = recursos.filter {
        (it.title.contains(busqueda, true) || it.category.contains(busqueda, true)) &&
                (!soloFavoritos || it.isFavorite)
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Biblioteca de Recursos (Offline)", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Navy)
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar por tema o categoría...") },
                modifier = Modifier.fillMaxWidth()
            )
            FilterChip(
                selected = soloFavoritos,
                onClick = { soloFavoritos = !soloFavoritos },
                label = { Text("Mostrar solo mis Favoritos offline") }
            )
        }

        items(filtrados) { rec ->
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "${rec.category.uppercase()} · ${rec.readingTimeMinutes} MIN (${rec.format})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CianConexion
                        )
                        IconButton(onClick = { onToggleFavorito(rec) }) {
                            Icon(
                                imageVector = if (rec.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Favorito",
                                tint = AzulDigital
                            )
                        }
                    }
                    Text(rec.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Navy)
                    Spacer(Modifier.height(4.dp))
                    Text(rec.content, fontSize = 14.sp, color = Navy)
                    Spacer(Modifier.height(6.dp))
                    Text("Fuente: ${rec.officialSource}", fontSize = 11.sp, color = Gris)
                }
            }
        }
    }
}