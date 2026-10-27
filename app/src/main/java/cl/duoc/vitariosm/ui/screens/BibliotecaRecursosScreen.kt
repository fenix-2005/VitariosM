package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.model.RecursosPublicos
import cl.duoc.vitariosm.ui.theme.*

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