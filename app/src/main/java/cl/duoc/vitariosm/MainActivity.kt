package cl.duoc.vitariosm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.vitariosm.ui.screens.*
import cl.duoc.vitariosm.ui.theme.*
import cl.duoc.vitariosm.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VitariosMTheme {
                val usuarioVM: UsuarioCrisisViewModel = viewModel()
                val checkInVM: CheckInViewModel = viewModel()
                val citasVM: CitasViewModel = viewModel()
                val recursosVM: RecursosViewModel = viewModel()

                VitariosAppCompleta(
                    usuarioVM = usuarioVM,
                    checkInVM = checkInVM,
                    citasVM = citasVM,
                    recursosVM = recursosVM
                )
            }
        }
    }
}

@Composable
fun VitariosAppCompleta(
    usuarioVM: UsuarioCrisisViewModel,
    checkInVM: CheckInViewModel,
    citasVM: CitasViewModel,
    recursosVM: RecursosViewModel
) {
    val usuario by usuarioVM.usuario.collectAsState()
    val alertaCrisis by usuarioVM.alertaCrisisActiva.collectAsState()
    val checkIns by checkInVM.checkIns.collectAsState()
    val citas by citasVM.citas.collectAsState()
    val recursos by recursosVM.recursos.collectAsState()

    var pantallaActual by remember { mutableStateOf("HOME") }
    var proximoPaso by remember { mutableStateOf("Realizar tu check-in emocional rápido de hoy") }

    if (alertaCrisis) {
        AyudaScreen(
            telefonoConfianza = usuario?.telefonoContactoConfianza ?: "+56912345678",
            onDismiss = { usuarioVM.cerrarAlertaCrisis() },
            onIrAProfesionales = {
                usuarioVM.cerrarAlertaCrisis()
                pantallaActual = "CITAS"
            }
        )
    }

    if (usuario == null || usuario?.aceptoDisclaimerOnboarding == false) {
        Surface(modifier = Modifier.fillMaxSize().systemBarsPadding().imePadding() , color = BlancoBase) {
            OnboardingScreen(
                onIngresarDemo = { rol, alias, fono ->
                    usuarioVM.ingresarComoDemo(rol, alias, fono)
                    pantallaActual = if (rol == "PROFESIONAL") "PROFESIONAL_DEMO" else "HOME"
                },
                onAyudaUrgente = { usuarioVM.activarAlertaManual() }
            )
        }
        return
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            Surface(color = BlancoBase, shadowElevation = 2.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(VerdeCont, CianConexion, AzulDigital)
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Spa,
                                contentDescription = "Isotipo VITAIROS-M",
                                tint = BlancoBase,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(SpanStyle(color = Navy, fontWeight = FontWeight.ExtraBold)) {
                                        append("VITAIROS-")
                                    }
                                    withStyle(SpanStyle(color = VerdeCont, fontWeight = FontWeight.ExtraBold)) {
                                        append("M")
                                    }
                                },
                                fontSize = 21.sp
                            )
                            Text(
                                text = "SALUD MENTAL DIGITAL",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = CianConexion,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // Acciones: Ayuda urgente + Botón Logout (Cerrar sesión)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FilledTonalButton(
                            onClick = { usuarioVM.activarAlertaManual() },
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFFFFE5E5)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.Emergency, contentDescription = null, tint = Color(0xFFB00020), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Ayuda", color = Color(0xFFB00020), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        IconButton(
                            onClick = {
                                usuarioVM.cerrarSesion()
                                pantallaActual = "HOME"
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Cerrar sesión y volver al Onboarding",
                                tint = Gris
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (pantallaActual != "PROFESIONAL_DEMO") {
                NavigationBar(
                    containerColor = FondoSecundario,
                    modifier = Modifier.height(64.dp)
                ) {
                    NavigationBarItem(selected = pantallaActual == "HOME", onClick = { pantallaActual = "HOME" }, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Inicio", fontSize = 10.sp) })
                    NavigationBarItem(selected = pantallaActual == "CHECKIN", onClick = { pantallaActual = "CHECKIN" }, icon = { Icon(Icons.Default.FavoriteBorder, null) }, label = { Text("Bitácora", fontSize = 10.sp) })
                    NavigationBarItem(selected = pantallaActual == "CHAT", onClick = { pantallaActual = "CHAT" }, icon = { Icon(Icons.Default.ChatBubbleOutline, null) }, label = { Text("Chat", fontSize = 10.sp) })
                    NavigationBarItem(selected = pantallaActual == "CITAS", onClick = { pantallaActual = "CITAS" }, icon = { Icon(Icons.Default.CalendarMonth, null) }, label = { Text("Citas", fontSize = 10.sp) })
                    NavigationBarItem(selected = pantallaActual == "RECURSOS", onClick = { pantallaActual = "RECURSOS" }, icon = { Icon(Icons.Default.MenuBook, null) }, label = { Text("Recursos", fontSize = 10.sp) })
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(BlancoBase).imePadding()) {
            when (pantallaActual) {
                "HOME" -> HomeContinuidadScreen(
                    usuario = usuario!!,
                    ultimoCheckIn = checkIns.firstOrNull(),
                    proximaCita = citas.firstOrNull { it.status != "Cancelada" },
                    proximoPaso = proximoPaso,
                    onIrCheckIn = { pantallaActual = "CHECKIN" },
                    onIrChat = { pantallaActual = "CHAT" },
                    onIrCitas = { pantallaActual = "CITAS" },
                    onCambiarVistaProfesional = { pantallaActual = "PROFESIONAL_DEMO" }
                )
                "CHECKIN" -> CheckInBitacoraScreen(
                    checkIns = checkIns,
                    onGuardar = { mood, cat, tags, energia, nota ->
                        val critico = usuarioVM.contieneSenalCritica(nota)
                        checkInVM.guardarCheckIn(mood, cat, tags, energia, nota)
                        if (!critico) proximoPaso = "Revisar ejercicio breve para ${cat ?: "tu bienestar"}"
                    },
                    onEditar = { item, nuevoMood, nuevaNota -> checkInVM.editarCheckIn(item, nuevoMood, nuevaNota) },
                    onEliminar = { checkInVM.eliminarCheckIn(it) },
                    onIrOrientacion = { pantallaActual = "CHAT" }
                )
                "CHAT" -> OrientacionConversacionalScreen(
                    onDetectarCrisis = { usuarioVM.contieneSenalCritica(it) },
                    onDefinirProximoPaso = { paso, destino ->
                        proximoPaso = paso
                        pantallaActual = destino
                    }
                )
                "CITAS" -> AgendaProfesionalesScreen(
                    citas = citas,
                    onAgendar = { profId, profName, esp, horario, motivo, autoriza ->
                        citasVM.agendarCitaFicticia(profId, profName, esp, horario, motivo, autoriza, checkIns.firstOrNull())
                        proximoPaso = "Preparar sesión online con $profName ($horario)"
                    },
                    onCambiarEstado = { cita, estado, horario -> citasVM.cambiarEstadoCita(cita, estado, horario) },
                    onEliminarCita = { cita -> citasVM.eliminarCita(cita) }
                )
                "RECURSOS" -> BibliotecaRecursosScreen(
                    recursos = recursos,
                    onToggleFavorito = { recursosVM.alternarFavorito(it) }
                )
                "PROFESIONAL_DEMO" -> VistaProfesionalDemoScreen(
                    citas = citas,
                    onActualizarEstadoCita = { cita, nuevoEstado -> citasVM.cambiarEstadoCita(cita, nuevoEstado) },
                    onVolverModoUsuario = { pantallaActual = "HOME" },
                    onCerrarSesion = { usuarioVM.cerrarSesion() }
                )
            }
        }
    }
}