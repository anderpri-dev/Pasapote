# Plan de Migración a Kotlin Multiplatform (KMP) - Pasapote

## Resumen del Proyecto Actual

**Pasapote** es una app Android nativa en Compose (44 ficheros Kotlin) con arquitectura Clean (MVVM + Repository). Usa Room, Hilt, Navigation Compose, Coil, Firebase Crashlytics y kotlinx.serialization.

### Estructura del proyecto

```
app/src/main/java/com/anderpri/pasapote/
├── common/                 # Utilidades (LanguageChangeHelper, ShareUtils)
├── data/                   # Capa de datos
│   ├── local/
│   │   ├── dao/            # KonpartsaDao, KonpartsaImageDao
│   │   ├── entity/         # KonpartsaEntity, KonpartsaImageEntity, KonpartsaWithImage
│   │   ├── mapper/         # Mappers.kt (Entity ↔ Domain)
│   │   ├── AppDatabase.kt
│   │   ├── Converters.kt
│   │   └── Migrations.kt
│   └── repository/         # KonpartsaRepositoryImpl
├── di/                     # Hilt modules (DatabaseModule, AppModule)
├── domain/
│   ├── model/              # Konpartsa (@Serializable)
│   └── repository/         # KonpartsaRepository (interface)
└── ui/
    ├── activities/          # MainActivity, PasapoteApp, ScreenCoverLanguageChangeActivity
    ├── composables/
    │   ├── card/            # KonpartsaCard, CardArgazkia, CardIzena, CardZenbakia
    │   ├── lista/           # KonpartsaListaCard
    │   ├── overlay/         # DialogFullScreenImageOverlay, DialogOverlayImage, OverlayIzena, OverlayZenbakia, OverlayKamiseta, OverlayEsteka
    │   └── settings/        # AppInfoDialog, DeveloperInfoDialog
    ├── navigation/          # ApplicationNavigation
    ├── screens/             # AppDrawer, KonpartsaCarousel, KonpartsaMapScreen, KonpartsaListaScreen, SettingsScreen
    ├── state/               # DrawerTitleState
    ├── theme/               # Color, Theme, Type
    └── viewmodel/           # KonpartsaViewModel, DrawerTitleViewModel
```

---

## Librerías a Migrar

| Librería actual (Android) | Reemplazo KMP-compatible | Motivo |
|---|---|---|
| **Hilt** (DI) | **Koin** | Hilt depende de Dagger/codegen Android. Koin es multiplataforma nativo |
| **Room** (BD) | **Room KMP** (androidx.room 2.7+) | Room 2.7 ya soporta KMP oficialmente (SQLite multiplataforma) |
| **Navigation Compose** | **Navigation Compose Multiplatform** (JetBrains) | La versión de JetBrains ya soporta KMP |
| **Coil Compose** | **Coil 3 KMP** | Coil 3.x ya tiene soporte multiplataforma |
| **Firebase Crashlytics** | **Crashlytics** (solo Android) / **Crashlytics + CrashKiOS** | Queda como dependencia platform-specific via `expect/actual` |
| **SharedPreferences** | **DataStore** o **multiplatform-settings** | SharedPreferences es solo Android |
| **AndroidX Lifecycle ViewModel** | **ViewModel KMP** (androidx.lifecycle 2.8+) | AndroidX ViewModel ya soporta KMP |
| **kotlinx.serialization** | **kotlinx.serialization** (sin cambios) | Ya es multiplataforma |
| **Kotlin Coroutines/Flow** | **Kotlin Coroutines** (sin cambios) | Ya es multiplataforma |

---

## APIs Android a Abstraer con `expect/actual`

| API Android | Uso en el proyecto | Ficheros afectados | Solución KMP |
|---|---|---|---|
| `Context.assets.open()` | Cargar `konpartsak.json` | `KonpartsaViewModel.kt` | `expect fun loadJsonFromAssets(): String` (Android: assets, iOS: Bundle) |
| `Context.filesDir` / `cacheDir` | Guardar/borrar fotos | `KonpartsaViewModel.kt` | `expect` para rutas del filesystem |
| `ContentResolver.openInputStream()` | Leer imagen desde URI | `KonpartsaViewModel.kt` | `expect` para acceso a imágenes seleccionadas |
| `ActivityResultContracts` (GetContent/TakePicture) | Picker de galería/cámara | `KonpartsaCard.kt` | `expect` para selección de imagen (iOS: PHPicker/UIImagePicker) |
| `Intent.ACTION_SEND/VIEW` | Compartir, abrir URLs, email | `KonpartsaViewModel.kt`, `AppInfoDialog.kt`, `DeveloperInfoDialog.kt` | `expect fun shareImage()`, `expect fun openUrl()` |
| `FileProvider` | URIs seguras para compartir | `ShareUtils.kt`, `KonpartsaViewModel.kt` | Platform-specific en la implementación de share |
| `LocaleManager` / `AppCompatDelegate` | Cambio de idioma | `LanguageChangeHelper.kt` | `expect` para gestión de locale |
| `SharedPreferences` | Persistir idioma | `LanguageChangeHelper.kt` | Migrar a DataStore/multiplatform-settings |
| `Toast.makeText()` | Feedback al usuario | `KonpartsaCard.kt`, `SettingsScreen.kt` | `expect fun showToast()` o Snackbar en Compose |
| `android.graphics.Bitmap` | Comprimir imagen para compartir | `ShareUtils.kt` | `expect` para renderizado bitmap |
| `GraphicsLayer.toImageBitmap().asAndroidBitmap()` | Captura de composable para compartir | `ShareUtils.kt` | `expect` para captura y export |
| `ActivityInfo.SCREEN_ORIENTATION_PORTRAIT` | Bloquear orientación | `MainActivity.kt` | Platform-specific en Activity/ViewController |
| `android.util.Log` | Logging | `KonpartsaViewModel.kt` | **Napier** o **Kermit** (logging multiplataforma) |
| `painterResource()` / `stringResource()` | Recursos UI | Múltiples composables | `org.jetbrains.compose.resources` |

---

## FASE 0: Preparación (sin cambios funcionales)

**Objetivo:** Limpiar el proyecto y preparar la estructura modular.

**Tareas:**
1. Crear módulo `:shared` (KMP library) y módulo `:androidApp`
2. Mover el modelo de dominio (`Konpartsa.kt`, `KonpartsaRepository.kt`) al módulo `:shared` en `commonMain`
3. Extraer interfaces para todas las operaciones platform-specific (file I/O, sharing, locale, image picking)
4. Verificar que la app sigue compilando idéntica en Android tras la reestructuración

**Ficheros a mover a `commonMain`:**
- `domain/model/Konpartsa.kt`
- `domain/repository/KonpartsaRepository.kt`

---

## FASE 1: Migrar DI — Hilt a Koin

**Objetivo:** Eliminar la dependencia de Hilt (incompatible con KMP) y sustituirla por Koin.

**Ficheros afectados:**
- `di/DatabaseModule.kt` — Reescribir como módulo Koin
- `di/AppModule.kt` — Reescribir como módulo Koin
- `ui/activities/PasapoteApp.kt` — Quitar `@HiltAndroidApp`, inicializar Koin
- `ui/activities/MainActivity.kt` — Quitar `@AndroidEntryPoint`
- `ui/viewmodel/KonpartsaViewModel.kt` — Quitar `@HiltViewModel` + `@Inject`, usar `koinViewModel()`
- `ui/viewmodel/DrawerTitleViewModel.kt` — Igual
- `ui/state/DrawerTitleState.kt` — Quitar `@Singleton` + `@Inject`, registrar como singleton en Koin

**Pasos:**
1. Añadir dependencias: `io.insert-koin:koin-android`, `io.insert-koin:koin-androidx-compose`
2. Crear `appModule` con `single { }`, `factory { }`, `viewModel { }`
3. Inicializar Koin en `PasapoteApp.onCreate()` con `startKoin { modules(appModule) }`
4. Inyectar ViewModels con `koinViewModel()` en los composables
5. Eliminar plugins y dependencias de Hilt/KSP-Hilt del `build.gradle.kts` y `libs.versions.toml`
6. Verificar que todo funciona igual

**Dependencias a eliminar:**
- `com.google.dagger:hilt-android`
- `com.google.dagger:hilt-android-compiler`
- `androidx.hilt:hilt-navigation-compose`
- Plugin `com.google.dagger.hilt.android`

**Dependencias a añadir:**
- `io.insert-koin:koin-android:4.x`
- `io.insert-koin:koin-androidx-compose:4.x`

---

## FASE 2: Migrar almacenamiento local — SharedPreferences a DataStore/multiplatform-settings

**Objetivo:** Eliminar SharedPreferences (solo Android).

**Ficheros afectados:**
- `common/LanguageChangeHelper.kt` — Reescribir persistencia de idioma

**Pasos:**
1. Añadir `com.russhwolf:multiplatform-settings:1.x` o `androidx.datastore:datastore-preferences`
2. Migrar lectura/escritura de la preferencia `"language"` (key: `"settings"/"language"`, default: `"eu"`)
3. Verificar que el cambio de idioma persiste correctamente tras reinicio

---

## FASE 3: Migrar Room a Room KMP

**Objetivo:** Preparar Room para ser multiplataforma.

**Ficheros afectados:**
- `data/local/AppDatabase.kt`
- `data/local/entity/KonpartsaEntity.kt`
- `data/local/entity/KonpartsaImageEntity.kt`
- `data/local/entity/KonpartsaWithImage.kt`
- `data/local/dao/KonpartsaDao.kt`
- `data/local/dao/KonpartsaImageDao.kt`
- `data/local/Converters.kt`
- `data/local/Migrations.kt`
- `data/local/mapper/Mappers.kt`
- `di/` (módulo Koin para DB)

**Pasos:**
1. Verificar que Room está en 2.7+ (ya está en 2.7.2 ✓)
2. Mover entidades, DAOs, converters y migraciones a `commonMain` en `:shared`
3. Crear `expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>` en `commonMain`
4. Implementar `actual fun` en `androidMain` usando `Room.databaseBuilder(context, ...)`
5. (Futuro) Implementar `actual fun` en `iosMain` usando `Room.databaseBuilder(...)` con path NSDocumentDirectory
6. Adaptar el módulo DI (Koin) para crear la BD de forma platform-specific
7. Verificar que las queries, relaciones y migraciones siguen funcionando

**Nota:** Room KMP usa SQLite driver multiplataforma. En Android usa `AndroidSQLiteDriver`, en iOS usa `NativeSQLiteDriver`.

---

## FASE 4: Abstraer operaciones platform-specific

**Objetivo:** Crear interfaces/`expect` para todo el código que usa APIs de Android directamente.

**Este es el paso más grande.** El fichero más afectado es `KonpartsaViewModel.kt`, que usa `Context` extensivamente.

### Interfaces a crear en `commonMain`

```kotlin
// 1. Carga de assets
interface AssetLoader {
    fun loadKonpartsaJson(): String
}

// 2. Almacenamiento de imágenes
interface ImageStorage {
    suspend fun saveImage(konpartsaId: String, year: String, imageBytes: ByteArray): String
    suspend fun deleteImage(imagePath: String)
    suspend fun deleteAllImages()
    suspend fun readImageBytes(imageUri: String): ByteArray?
    fun getImagePath(konpartsaId: String, year: String): String
}

// 3. Compartir contenido
interface ShareService {
    fun shareImage(imagePath: String)
    fun openUrl(url: String)
    fun sendEmail(address: String)
}

// 4. Gestión de idioma
interface LocaleManager {
    fun setLocale(languageCode: String)
    fun getCurrentLocale(): String
    fun getPersistedLocale(): String
}

// 5. Feedback al usuario
interface UserFeedback {
    fun showMessage(message: String)
}
```

### Ficheros afectados y cambios

- **`KonpartsaViewModel.kt`** — El más complejo:
  - Eliminar todos los parámetros `context: Context` de las funciones
  - Inyectar `AssetLoader`, `ImageStorage`, `ShareService` via constructor (Koin)
  - `initKonpartsak()` → usa `AssetLoader.loadKonpartsaJson()` en vez de `context.assets.open()`
  - `onImageSelected()` → usa `ImageStorage.readImageBytes()` + `saveImage()` en vez de `ContentResolver`
  - `deleteImage()` → usa `ImageStorage.deleteImage()` en vez de `File.delete()`
  - `shareToInstagram()` → usa `ShareService.shareImage()`
  - `deleteAllProviderFiles()` → usa `ImageStorage.deleteAllImages()`

- **`common/LanguageChangeHelper.kt`** — Reescribir usando `LocaleManager`

- **`common/ShareUtils.kt`** — Reescribir usando `ShareService` + expect/actual para bitmap

- **`ui/composables/card/KonpartsaCard.kt`** — Las llamadas a `rememberLauncherForActivityResult()` quedan en la capa UI Android-specific o se abstraen con un `ImagePicker` expect/actual

- **`ui/composables/settings/AppInfoDialog.kt`** — Usar `ShareService.openUrl()` en vez de `Intent.ACTION_VIEW`

- **`ui/composables/settings/DeveloperInfoDialog.kt`** — Igual

**Pasos:**
1. Crear las 5 interfaces en `commonMain` del módulo `:shared`
2. Implementar cada interfaz en `androidMain` usando las APIs actuales de Android
3. Registrar las implementaciones en el módulo Koin de Android
4. Inyectar las interfaces en los ViewModels via constructor
5. Eliminar todo uso directo de `Context` en ViewModels y utilidades
6. Para los composables que usan `LocalContext.current` para intents: pasar la acción via callback al ViewModel
7. Añadir **Napier** o **Kermit** para sustituir `android.util.Log`
8. Verificar que todo funciona idéntico

---

## FASE 5: Migrar Navigation a Navigation Compose Multiplatform

**Objetivo:** Usar la navegación de JetBrains compatible con KMP.

**Ficheros afectados:**
- `ui/navigation/ApplicationNavigation.kt`
- `ui/screens/AppDrawer.kt`
- `build.gradle.kts` (dependencias)

**Pasos:**
1. Sustituir `androidx.navigation:navigation-compose` por `org.jetbrains.androidx.navigation:navigation-compose`
2. Adaptar imports (suelen ser los mismos o muy similares)
3. Verificar transiciones (fadeIn/fadeOut) y navegación del drawer
4. Verificar que `BackHandler` sigue funcionando (usar versión multiplataforma si es necesario)

---

## FASE 6: Migrar ViewModel a AndroidX Lifecycle KMP

**Objetivo:** Hacer los ViewModels compartidos entre plataformas.

**Ficheros afectados:**
- `ui/viewmodel/KonpartsaViewModel.kt`
- `ui/viewmodel/DrawerTitleViewModel.kt`
- `build.gradle.kts` (dependencias)

**Pasos:**
1. Usar `androidx.lifecycle:lifecycle-viewmodel` KMP (2.8+) en `commonMain`
2. Mover los ViewModels a `commonMain` en `:shared`
3. Actualizar Koin para usar `koinViewModel()` multiplataforma (`io.insert-koin:koin-compose-viewmodel`)
4. Verificar que `StateFlow`, `viewModelScope` y la inyección funcionan correctamente

**Requisito previo:** Las interfaces de la FASE 4 deben estar completas, ya que los ViewModels no pueden tener dependencias Android.

---

## FASE 7: Verificación final Android

**Objetivo:** La app Android funciona exactamente igual con todas las tecnologías KMP-compatibles.

**Checklist de verificación:**
- [ ] Carrusel de konpartsas (HorizontalPager) funciona
- [ ] Puntos del slider se colorean correctamente (verde si tiene imagen)
- [ ] Selección de imagen desde galería funciona
- [ ] Captura de foto con cámara funciona
- [ ] Imagen se guarda y se muestra correctamente
- [ ] Borrado de imagen individual funciona
- [ ] Borrado masivo de imágenes funciona
- [ ] Compartir imagen (Instagram, WhatsApp, etc.) funciona
- [ ] Vista de mapa muestra los círculos de colores correctamente
- [ ] Filtros del mapa (iluminar/oscurecer) funcionan
- [ ] Tap en círculo del mapa abre el diálogo con KonpartsaCard
- [ ] Lista de konpartsas se muestra correctamente
- [ ] Cards de la lista se expanden/contraen con animación
- [ ] Cambio de idioma (Euskara ↔ Gaztelera) funciona y persiste
- [ ] Drawer de navegación funciona correctamente
- [ ] Título del drawer se actualiza al navegar
- [ ] Diálogos de información (app y desarrollador) funcionan
- [ ] Links externos (URLs, email) se abren correctamente
- [ ] Overlay de imagen (compartir) se renderiza correctamente
- [ ] Firebase Crashlytics reporta crashes automáticamente
- [ ] La app se bloquea en orientación portrait
- [ ] Fuente personalizada (GasoekOne) se muestra correctamente
- [ ] Tema claro/oscuro funciona según sistema

**Si todo pasa ✓ → Se puede proceder a las fases KMP.**

---

## FASE 8: Reestructurar como proyecto KMP real

**Objetivo:** Convertir el proyecto en multiplataforma con módulos compartidos.

**Estructura objetivo:**
```
Pasapote/
├── shared/                          # Módulo KMP compartido
│   ├── src/
│   │   ├── commonMain/kotlin/       # Código compartido
│   │   │   ├── domain/model/
│   │   │   ├── domain/repository/
│   │   │   ├── data/local/          # Room entities, DAOs, converters, migrations
│   │   │   ├── data/repository/
│   │   │   ├── data/mapper/
│   │   │   ├── ui/viewmodel/
│   │   │   ├── ui/composables/      # Compose Multiplatform UI
│   │   │   ├── ui/screens/
│   │   │   ├── ui/navigation/
│   │   │   ├── ui/theme/
│   │   │   └── platform/            # expect declarations
│   │   ├── androidMain/kotlin/      # actual implementations Android
│   │   └── iosMain/kotlin/          # actual implementations iOS (stubs iniciales)
│   └── build.gradle.kts
├── androidApp/                      # App Android
│   ├── src/main/
│   │   ├── java/.../
│   │   │   ├── MainActivity.kt
│   │   │   └── PasapoteApp.kt
│   │   ├── assets/
│   │   ├── res/
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── iosApp/                          # App iOS (SwiftUI host)
│   ├── iosApp/
│   │   ├── ContentView.swift
│   │   └── iOSApp.swift
│   └── iosApp.xcodeproj
└── build.gradle.kts
```

**Pasos:**
1. Configurar `shared/build.gradle.kts` con `kotlin("multiplatform")`
2. Configurar targets: `androidTarget()`, `iosArm64()`, `iosSimulatorArm64()`
3. Mover a `commonMain`: modelos, repositorios, ViewModels, interfaces, composables
4. Mover implementaciones Android a `androidMain`
5. Crear stubs vacíos de `actual` en `iosMain` (para que compile)
6. Configurar `androidApp/build.gradle.kts` para depender de `:shared`
7. Verificar que la app Android compila y funciona desde la nueva estructura

---

## FASE 9: UI compartida con Compose Multiplatform

**Objetivo:** Reutilizar la UI en iOS via Compose Multiplatform.

**Ficheros afectados:** Todos los composables, theme, navigation.

**Pasos:**
1. Añadir plugin `org.jetbrains.compose` al módulo `:shared`
2. Mover todos los composables de `androidApp` a `shared/commonMain`
3. Reemplazar `painterResource(R.drawable.xxx)` por `org.jetbrains.compose.resources.painterResource(Res.drawable.xxx)`
4. Reemplazar `stringResource(R.string.xxx)` por `org.jetbrains.compose.resources.stringResource(Res.string.xxx)`
5. Mover recursos (drawables, strings) al directorio `shared/src/commonMain/composeResources/`
6. Adaptar los composables que usan `LocalContext.current`:
   - Sustituir por interfaces inyectadas o callbacks
   - `rememberLauncherForActivityResult()` → `expect/actual` composable para image picker
7. Mover assets (JSON, imágenes de konpartsas, fuente) a `commonMain/composeResources/files/`
8. Verificar que la UI se renderiza correctamente en Android

**Recursos a migrar:**
- `res/drawable/` → `commonMain/composeResources/drawable/`
- `res/values/strings.xml` → `commonMain/composeResources/values/strings.xml`
- `res/values-es/strings.xml` → `commonMain/composeResources/values-es/strings.xml`
- `assets/` → `commonMain/composeResources/files/`

---

## FASE 10: Implementaciones iOS

**Objetivo:** La app funciona en iOS.

### Implementaciones `actual` en `iosMain`

1. **AssetLoader** — Usar `NSBundle.mainBundle.pathForResource()` para cargar JSON
2. **ImageStorage** — Usar `NSFileManager` + `NSDocumentDirectory` para guardar/borrar imágenes
3. **ShareService** — Usar `UIActivityViewController` para compartir, `UIApplication.shared.open()` para URLs
4. **LocaleManager** — Usar `NSUserDefaults` + `NSLocale` para gestión de idioma
5. **UserFeedback** — Usar alertas nativas o Snackbar en Compose
6. **Room Database** — Implementar `actual fun getDatabaseBuilder()` con `NativeSQLiteDriver` y path a NSDocumentDirectory
7. **Image Picker** — Implementar picker con `PHPickerViewController` o `UIImagePickerController`

### Configuración iOS

1. Crear proyecto Xcode `iosApp` con SwiftUI entry point
2. Integrar Compose Multiplatform via `UIViewController` embedding
3. Configurar CocoaPods o SPM para dependencias iOS
4. Configurar Firebase Crashlytics para iOS (GoogleService-Info.plist) o usar alternativa (CrashKiOS/Bugsnag)
5. Configurar signing, capabilities y permisos (camera, photo library)
6. Mover assets de konpartsas al bundle iOS

### Checklist de verificación iOS
- [ ] App se lanza correctamente en simulador iOS
- [ ] Carrusel funciona con gestos
- [ ] Mapa se muestra correctamente
- [ ] Lista funciona
- [ ] Selección de imagen (galería + cámara) funciona
- [ ] Compartir imagen funciona
- [ ] Cambio de idioma funciona
- [ ] Base de datos persiste datos
- [ ] Tema se adapta al sistema (claro/oscuro)
- [ ] Fuente personalizada se carga correctamente

---

## Resumen Visual de Fases

```
FASE 0   Preparar estructura modular
FASE 1   Hilt → Koin
FASE 2   SharedPreferences → DataStore/multiplatform-settings
FASE 3   Room → Room KMP
FASE 4   Abstraer APIs Android (expect/actual interfaces)
FASE 5   Navigation → Navigation Compose Multiplatform
FASE 6   ViewModel → AndroidX Lifecycle KMP
FASE 7   ✅ Verificación completa: Android funciona igual
──────── Punto de corte: todo KMP-compatible ────────
FASE 8   Reestructurar como proyecto KMP (shared + androidApp + iosApp)
FASE 9   Compose Multiplatform (UI compartida + recursos)
FASE 10  Implementaciones iOS + app iOS funcional
```

**Nota:** Las fases 1-7 se ejecutan sobre la app Android nativa. Cada fase es independiente y verificable. A partir de la fase 8 se empieza el trabajo multiplataforma real.
