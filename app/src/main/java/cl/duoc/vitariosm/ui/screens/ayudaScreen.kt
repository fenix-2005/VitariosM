package cl.duoc.vitariosm.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

@Composable
fun AyudaScreen(
    telefonoConfianza: String,
    onDismiss: () -> Unit,
    onIrAProfesionales: () -> Unit
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Apoyo Inmediato y Canales Oficiales", fontWeight = FontWeight.Bold, color = Color(0xFFB00020))
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Si estás pasando por un momento de angustia severa o riesgo inmediato, utiliza estos canales gratuitos de un toque en Chile:")
                Button(
                    onClick = { context.startActivity(Intent(Intent.ACTION_DIAL, "tel:*4141".toUri())) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB00020))
                ) {
                    Text("Llamar a *4141 (Prevención del Suicidio)")
                }
                Button(
                    onClick = { context.startActivity(Intent(Intent.ACTION_DIAL,
                        "tel:6003607777".toUri())) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Llamar a Salud Responde (600 360 7777)")
                }
                OutlinedButton(
                    onClick = {
                        val sms = Intent(Intent.ACTION_SENDTO, "smsto:$telefonoConfianza".toUri()).apply {
                            putExtra("sms_body", "Hola, necesito conversar o recibir apoyo en este momento (Enviado desde VITAIROS-M).")
                        }
                        context.startActivity(sms)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Contactar persona de confianza ($telefonoConfianza)")
                }
                TextButton(onClick = onIrAProfesionales, modifier = Modifier.fillMaxWidth()) {
                    Text("Ver psicólogos especialistas en la app →")
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Cerrar") }
        }
    )
}