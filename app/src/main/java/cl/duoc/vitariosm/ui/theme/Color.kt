package cl.duoc.vitariosm.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val Navy = Color(0xFF000E2E)
val Gris = Color(0xFF536477)
val Cian = Color(0xFF007F9E)


val VerdeCont = Color(0xFF00C98E)
val CianConexion = Color(0xFF00B9D3)
val AzulDigital = Color(0xFF00A5F4)


val BlancoBase = Color(0xFFFEFEFE)
val FondoSecundario = Color(0xFFF2F8FB)
val MintSuave = Color(0xFFDDF6F0)
val AzulSuave = Color(0xFFE1F3FE)


val Gradiente = Brush.linearGradient(
    colors = listOf(VerdeCont, CianConexion, AzulDigital),
    start = Offset(0f, 0f),
    end = Offset(1000f, 0f)
)