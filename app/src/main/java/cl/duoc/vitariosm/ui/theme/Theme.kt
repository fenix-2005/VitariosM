package cl.duoc.vitariosm.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val VitariosLightColorScheme = lightColorScheme(
    primary = AzulDigital,
    secondary = CianConexion,
    tertiary = VerdeCont,
    background = BlancoBase,
    surface = BlancoBase,
    surfaceVariant = FondoSecundario,
    onPrimary = BlancoBase,
    onSecondary = BlancoBase,
    onBackground = Navy,
    onSurface = Navy,
    onSurfaceVariant = Gris
)

@Composable
fun VitariosMTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VitariosLightColorScheme,
        typography = Typography,
        content = content
    )
}