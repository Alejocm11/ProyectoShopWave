MODIFICACIONES REALIZADAS EN SHOPWAVE

1. Login por tipo de cuenta
- Se agregó selección de cuenta Comprador / Vendedor en LoginScreen.kt.
- Se creó AuthViewModel.kt y UserRole.kt.
- Si el usuario entra como comprador, va al Home.
- Si entra como vendedor, va al panel de vendedor.

2. Panel de vendedor
- Se agregó SellerProductScreen.kt.
- El vendedor puede crear productos con:
  nombre,
  descripción,
  precio en pesos colombianos,
  precio anterior opcional,
  categoría obligatoria.
- El producto agregado se guarda en la lista local y queda visible para compradores.

3. Relación producto-categoría
- Cada producto nuevo queda asociado a una categoría: Electronics, Fashion, Home, Beauty, Sports o Food.
- En pantalla se muestra traducido según idioma.

4. Cierre de sesión
- Se agregó botón de cerrar sesión en Home y en el panel de vendedor.
- Permite volver al Login y cambiar entre comprador y vendedor.

5. Pesos colombianos
- Se ajustaron los precios de productos existentes a COP.
- Se agregó formatCop() para mostrar precios como moneda colombiana.
- Se ajustó el envío del carrito a COP.

6. Idioma Español / Inglés
- Se amplió AppStrings.kt.
- Se tradujeron labels del menú inferior, categorías, login, vendedor y textos principales.
- Se agregó categoryLabel() para traducir categorías.
- Se agregó productDescription() para mostrar descripción en español o inglés.

7. Arquitectura
- Se mantiene MVVM / Clean Architecture:
  presentation: pantallas y viewmodels
  domain: modelos, repositorios e use cases
  data: datasource local y repository implementation
  core: localización y notificaciones
