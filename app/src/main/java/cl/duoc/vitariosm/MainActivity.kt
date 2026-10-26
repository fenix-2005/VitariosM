package cl.duoc.vitariosm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cl.duoc.vitariosm.ui.screens.AyudaScreen
import cl.duoc.vitariosm.ui.screens.CheckInBitacoraScreen
import cl.duoc.vitariosm.ui.screens.OnboardingScreen
import cl.duoc.vitariosm.ui.theme.BlancoBase
import cl.duoc.vitariosm.ui.theme.Cian
import cl.duoc.vitariosm.ui.theme.Navy
import cl.duoc.vitariosm.ui.theme.VitariosMTheme
import cl.duoc.vitariosm.viewmodel.CheckInViewModel
import cl.duoc.vitariosm.viewmodel.UsuarioCrisisViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VitariosMTheme {
                val usuarioVM: UsuarioCrisisViewModel = viewModel()
                val checkInVM: CheckInViewModel = viewModel()

                AppActualConLoQueTienes(
                    usuarioVM = usuarioVM,
                    checkInVM = checkInVM
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppActualConLoQueTienes(
    usuarioVM: UsuarioCrisisViewModel,
    checkInVM: CheckInViewModel
) {
    val usuario by usuarioVM.usuario.collectAsState()
    val alertaCrisis by usuarioVM.alertaCrisisActiva.collectAsState()
    val checkIns by checkInVM.checkIns.collectAsState()


    if (alertaCrisis) {
        AyudaScreen(
            telefonoConfianza = usuario?.telefonoContactoConfianza ?: "+56912345678",
            onDismiss = { usuarioVM.cerrarAlertaCrisis() },
            onIrAProfesionales = { usuarioVM.cerrarAlertaCrisis() }
        )
    }


    if (usuario == null || usuario?.aceptoDisclaimerOnboarding == false) {
        OnboardingScreen(
            onIngresarDemo = { rol, alias, fono ->
                usuarioVM.ingresarComoDemo(rol, alias, fono)
            },
            onAyudaUrgente = { usuarioVM.activarAlertaManual() }
        )
        return
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("VITAIROS-M", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Navy)
                        Text("SALUD MENTAL DIGITAL", fontSize = 10.sp, color = Cian)
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { usuarioVM.activarAlertaManual() },
                        colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFFFFE5E5))
                    ) {
                        Icon(Icons.Default.Emergency, contentDescription = null, tint = Color(0xFFB00020))
                        Spacer(Modifier.width(4.dp))
                        Text("Ayuda urgente", color = Color(0xFFB00020), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BlancoBase)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(BlancoBase)
        ) {
            CheckInBitacoraScreen(
                checkIns = checkIns,
                onGuardar = { mood, cat, tags, energia, nota ->
                    usuarioVM.contieneSenalCritica(nota)
                    checkInVM.guardarCheckIn(mood, cat, tags, energia, nota)
                },
                onEditar = { item, nuevoMood, nuevaNota ->
                    checkInVM.editarCheckIn(item, nuevoMood, nuevaNota)
                },
                onEliminar = { item ->
                    checkInVM.eliminarCheckIn(item)
                },
                onIrOrientacion = { /* Se conectará en el siguiente paso */ }
            )
        }
    }
}