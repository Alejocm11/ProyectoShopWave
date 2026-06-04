CAMBIOS AGREGADOS: REGISTRO DE COMPRAS Y VENTAS

1. Registro de compras:
- El comprador puede ver sus compras desde el menú inferior, opción Compras.
- Archivo principal: PurchaseHistoryScreen.kt
- ViewModel: OrderHistoryViewModel.kt
- Modelo: OrderRecord.kt

2. Registro de ventas:
- El vendedor puede ver las ventas desde el botón Ventas en el panel de vendedor.
- Archivo principal: SalesHistoryScreen.kt
- Muestra productos vendidos, cantidad y total vendido.

3. Guardado automático:
- Cuando el comprador finaliza una compra en CheckoutScreen.kt, se ejecuta:
  orderHistoryViewModel.registerPurchase(...)

4. Separación por rol:
- Comprador: Home, Carrito, Compras, Favoritos y Perfil.
- Vendedor: Panel de productos y Registro de ventas.

5. Flujo:
Comprador compra producto
↓
CheckoutScreen registra la compra
↓
OrderHistoryViewModel guarda el pedido
↓
Comprador lo ve en PurchaseHistoryScreen
↓
Vendedor lo ve en SalesHistoryScreen
