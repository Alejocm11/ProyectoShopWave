# ShopWave - Modificaciones realizadas

Se modificó el proyecto para cumplir con los nuevos requerimientos:

## 1. Arquitectura MVVM / Clean Architecture

Nueva estructura principal:

```text
com.taller.proyectofinalcomponentes
├── core
│   ├── localization
│   └── notifications
├── data
│   ├── local
│   └── repository
├── domain
│   ├── model
│   ├── repository
│   └── usecase
├── presentation
│   ├── ui/screens
│   ├── ui/theme
│   └── viewmodels
└── navigation
```

## 2. Notificaciones

Se agregó soporte para notificaciones locales en:

```text
core/notifications/ShopWaveNotificationHelper.kt
```

También se agregó el permiso en:

```text
AndroidManifest.xml
```

```xml
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

El canal de notificación se crea en:

```text
MainActivity.kt
```

Se muestran notificaciones al:

- tocar el ícono de notificaciones en Home.
- agregar productos al carrito.
- confirmar un pedido.

## 3. Doble idioma manual

Se agregó selector manual Español/Inglés en Home.

Archivos principales:

```text
core/localization/AppLanguage.kt
core/localization/AppStrings.kt
presentation/viewmodels/LanguageViewModel.kt
```

El botón `ES / EN` en Home cambia el idioma manualmente.

## 4. MVVM aplicado

La UI ya no contiene directamente el catálogo principal de productos.
Ahora los datos pasan por:

```text
ShopWaveDataSource
↓
ProductRepositoryImpl
↓
UseCases
↓
ShopViewModel
↓
Screens
```
