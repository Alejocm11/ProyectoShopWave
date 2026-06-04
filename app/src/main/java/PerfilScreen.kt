import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun PerfilScreen(viewModel: AuthViewModel) {
    Column {
        Text("Perfil de usuario")
        Button(onClick = { viewModel.logout() }) {
            Text("Cerrar sesión")
        }
    }
}
