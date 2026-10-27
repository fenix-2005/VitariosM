package cl.duoc.vitariosm.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cl.duoc.vitariosm.ui.theme.*
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import cl.duoc.vitariosm.R

@Composable
fun OnboardingScreen(
    onIngresarDemo: (rol: String, alias: String, correo: String, fonoConfianza: String) -> Unit,
    onAyudaUrgente: () -> Unit
) {
    var alias by remember { mutableStateOf("") }
    var correoSimulado by remember { mutableStateOf("") }
    var fonoConfianza by remember { mutableStateOf("+56987654321") }
    var aceptaCondiciones by remember { mutableStateOf(false) }


    val esAliasValido = alias.trim().count { it.isLetter() } > 3


    val dominiosPermitidos = listOf(
        "@gmail.com", "@hotmail.com", "@outlook.com", "@yahoo.com",
        "@duocuc.cl", "@profesor.duoc.cl", "@vitairos.cl", "@icloud.com"
    )
    val correoLimpio = correoSimulado.trim().lowercase()
    val tieneUsuarioAntesDelArroba = correoLimpio.substringBefore("@").length >= 2
    val esCorreoValido = tieneUsuarioAntesDelArroba && dominiosPermitidos.any { correoLimpio.endsWith(it) }


    val fonoSinEspacios = fonoConfianza.trim().replace(" ", "")
    val regexTelefonoChile = Regex("^(\\+?56)?9\\d{8}$")
    val esFonoValido = regexTelefonoChile.matches(fonoSinEspacios)


    val formularioValido = esAliasValido && esCorreoValido && esFonoValido && aceptaCondiciones

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BlancoBase)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.logo_vitairos),
                contentDescription = "Logotipo oficial VITAIROS-M Salud Mental Digital",
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .heightIn(max = 90.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.CenterStart
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Que pedir ayuda no signifique empezar de cero. Registra cómo te sientes y recibe un próximo paso claro.",
                color = Gris
            )
        }

        item {
            Card(colors = CardDefaults.cardColors(containerColor = FondoSecundario)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Límites éticos y privacidad local", fontWeight = FontWeight.Bold, color = Navy)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = "VITAIROS-M es una herramienta de orientación y acompañamiento general. No realiza diagnósticos, no prescribe tratamientos, no entrega terapia y no reemplaza la atención de profesionales de salud. Si existe riesgo inmediato o una emergencia, busca ayuda presencial y utiliza los canales oficiales de emergencia disponibles en Chile.",
                        fontSize = 13.sp,
                        color = Gris
                    )
                }
            }
        }

        item {
            OutlinedTextField(
                value = alias,
                onValueChange = { alias = it },
                label = { Text("Tu alias o nombre de preferencia") },
                isError = !esAliasValido,
                supportingText = {
                    if (!esAliasValido) {
                        Text("El nombre debe tener más de 3 letras.", color = Color(0xFFB00020))
                    } else {
                        Text("✓ Nombre válido", color = VerdeCont)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = correoSimulado,
                onValueChange = { correoSimulado = it },
                label = { Text("Correo simulado (gmail, hotmail, duocuc, etc.)") },
                isError = !esCorreoValido,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                supportingText = {
                    if (!esCorreoValido) {
                        Text("Ingresa un correo válido (ej. usuario@gmail.com o @hotmail.com).", color = Color(0xFFB00020))
                    } else {
                        Text("✓ Formato de correo válido", color = VerdeCont)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // CAMPO 3: TELÉFONO DE CONFIANZA
            OutlinedTextField(
                value = fonoConfianza,
                onValueChange = { nuevoTexto ->
                    // Solo permite escribir '+', números y espacios para evitar letras en el teléfono
                    if (nuevoTexto.all { it.isDigit() || it == '+' || it == ' ' }) {
                        fonoConfianza = nuevoTexto
                    }
                },
                label = { Text("Teléfono contacto de confianza (+569...)") },
                isError = !esFonoValido,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                supportingText = {
                    if (!esFonoValido) {
                        Text("Debe ser un número válido (ej. +56912345678 o 912345678).", color = Color(0xFFB00020))
                    } else {
                        Text("✓ Número de contacto válido", color = VerdeCont)
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = aceptaCondiciones, onCheckedChange = { aceptaCondiciones = it })
                Text("Comprendo los límites éticos y acepto las condiciones de uso.", fontSize = 13.sp, color = Navy)
            }
        }

        item {
            Button(
                onClick = { onIngresarDemo("USUARIO", alias.trim(), correoLimpio, fonoSinEspacios) },
                enabled = formularioValido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AzulDigital)
            ) {
                Text("Ingresar como Usuario Demo", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { onIngresarDemo("PROFESIONAL", "Ps. Martín Rojas (Demo)", correoLimpio, fonoSinEspacios) },
                enabled = formularioValido,
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