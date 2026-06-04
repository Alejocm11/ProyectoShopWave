CORRECCIONES REALIZADAS

1. Login validado:
- Ya no permite iniciar sesión con campos vacíos.
- Solo acepta:
  comprador@test.com / 1234 como comprador.
  vendedor@test.com / 1234 como vendedor.
- Si el correo, contraseña o tipo de cuenta no coincide, muestra error.

2. AuthViewModel:
- Se agregó validación real en presentation/viewmodels/AuthViewModel.kt.
- Se guarda rol y correo del usuario activo.

3. Favoritos:
- El botón Favoritos del menú inferior ahora navega a WishlistScreen.kt.
- Muestra productos favoritos de ejemplo.

4. Perfil:
- El botón Perfil ahora navega a ProfileScreen.kt.
- Muestra correo, tipo de usuario, opción de idioma y cerrar sesión.

5. Navegación:
- Se agregaron rutas Wishlist y Profile en AppRoutes.kt.
- Se conectaron en AppNavHost.kt.
- HomeScreen.kt ahora recibe acciones para Favoritos y Perfil.
