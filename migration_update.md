# Pasapote KMP Migration - Progreso

Referencia: `migration_plan.md`

---

## FASE 0: Preparación - COMPLETADA

**Fecha:** 2026-02-11

### Qué se ha hecho

1. **Verificado que la capa de dominio está limpia de Android:**
   - `domain/model/Konpartsa.kt` — Solo usa `kotlinx.serialization.Serializable`. KMP-ready.
   - `domain/repository/KonpartsaRepository.kt` — Solo usa `kotlinx.coroutines.flow.Flow`. KMP-ready.

2. **Creadas 5 interfaces de abstracción de plataforma** en `platform/`:
   - `AssetLoader` — Carga JSON desde assets/bundle (`loadJsonFromAssets`)
   - `ImageStorage` — Guarda, lee y borra imágenes (`saveImage`, `readImageBytes`, `deleteImage`, `deleteAllFiles`)
   - `ShareService` — Comparte imágenes, abre URLs, envía emails (`shareImage`, `openUrl`, `sendEmail`)
   - `LocaleManager` — Gestión de idioma (`changeLanguage`, `getLanguage`, `setLanguageOnCreate`)
   - `UserFeedback` — Mensajes al usuario (`showMessage`)

3. **Estructura de módulos KMP (`:shared` / `:androidApp`) aplazada a Fase 8**, ya que las fases 1-7 migran librerías dentro de la app Android nativa y crear el módulo KMP ahora con Hilt/Room sin migrar complicaría sin necesidad.

4. **Build verificado:** `compileDebugKotlin` BUILD SUCCESSFUL.

### Ficheros nuevos
```
app/src/main/java/com/anderpri/pasapote/platform/
├── AssetLoader.kt
├── ImageStorage.kt
├── ShareService.kt
├── LocaleManager.kt
└── UserFeedback.kt
```

### Mapeo: interfaces → código que las usará (en Fase 4)

| Interfaz | Ficheros que la consumirán | APIs Android que reemplaza |
|---|---|---|
| `AssetLoader` | `KonpartsaViewModel.initKonpartsak()` | `Context.assets.open()` |
| `ImageStorage` | `KonpartsaViewModel.onImageSelected()`, `.deleteImage()`, `.deleteImages()`, `.deleteAllProviderFiles()` | `ContentResolver`, `File`, `FileOutputStream`, `Context.filesDir/cacheDir/getExternalFilesDir` |
| `ShareService` | `KonpartsaViewModel.shareToInstagram()`, `AppInfoDialog`, `DeveloperInfoDialog` | `Intent.ACTION_SEND/VIEW/SENDTO`, `FileProvider`, `Context.startActivity()` |
| `LocaleManager` | `LanguageChangeHelper` (se reescribirá), `SettingsScreen` | `android.app.LocaleManager`, `AppCompatDelegate`, `SharedPreferences` |
| `UserFeedback` | `KonpartsaCard` (borrado), `SettingsScreen` (borrado masivo) | `Toast.makeText()` |

### Notas
- Las interfaces NO se conectan todavía al código existente. Solo se definen. La conexión (implementar + inyectar) se hace en la **Fase 4**.
- Antes de la Fase 4 hay que completar: **Fase 1** (Hilt→Koin), **Fase 2** (SharedPreferences→DataStore), **Fase 3** (Room→Room KMP).

---

## FASE 1: Hilt → Koin — COMPLETADA

**Fecha:** 2026-02-11

### Qué se ha hecho

1. **Dependencias Gradle actualizadas:**
   - `libs.versions.toml`: Eliminadas `hiltAndroid`, `hiltNavigationCompose`, sus libraries y plugin. Añadidas `koin = "4.1.1"`, `koin-bom`, `koin-android`, `koin-androidx-compose`
   - `build.gradle.kts` (root): Eliminado `alias(libs.plugins.hilt.android) apply false`
   - `app/build.gradle.kts`: Eliminado plugin `hilt.android`. Sustituidas dependencias Hilt por `koin-bom`, `koin-android`, `koin-androidx-compose`

2. **Módulos DI reescritos:**
   - `DatabaseModule.kt` eliminado
   - `AppModule.kt` reescrito como un `val appModule = module { }` de Koin con: database (single), DAOs (single), repository (single), DrawerTitleState (singleOf), ViewModels (viewModelOf)

3. **Application reescrita:**
   - `PasapoteApp.kt`: Eliminado `@HiltAndroidApp`. Añadido `startKoin { androidContext(...); modules(appModule) }` en `onCreate()`

4. **Activity limpiada:**
   - `MainActivity.kt`: Eliminado `@AndroidEntryPoint` e import de Hilt

5. **ViewModels limpiados:**
   - `KonpartsaViewModel.kt`: Eliminados `@HiltViewModel`, `@Inject`, imports de `dagger.hilt` y `javax.inject`
   - `DrawerTitleViewModel.kt`: Igual

6. **State limpiado:**
   - `DrawerTitleState.kt`: Eliminados `@Singleton`, `@Inject`

7. **Composables migrados (6 ficheros):**
   - `KonpartsaCarousel.kt`, `AppDrawer.kt`, `KonpartsaListaScreen.kt`, `KonpartsaMapScreen.kt`, `SettingsScreen.kt`, `KonpartsaCard.kt`
   - Import: `androidx.hilt.navigation.compose.hiltViewModel` → `org.koin.androidx.compose.koinViewModel`
   - Llamadas: `hiltViewModel()` → `koinViewModel()`

8. **Verificación:** `grep hilt|dagger|javax.inject` → 0 resultados. `compileDebugKotlin` BUILD SUCCESSFUL.

### Ficheros modificados
```
gradle/libs.versions.toml          — Koin deps añadidas, Hilt deps eliminadas
build.gradle.kts                   — Plugin Hilt eliminado
app/build.gradle.kts               — Plugin + deps Hilt → Koin
di/AppModule.kt                    — Reescrito como módulo Koin
di/DatabaseModule.kt               — ELIMINADO
ui/activities/PasapoteApp.kt       — startKoin en onCreate
ui/activities/MainActivity.kt      — @AndroidEntryPoint eliminado
ui/viewmodel/KonpartsaViewModel.kt — Anotaciones Hilt eliminadas
ui/viewmodel/DrawerTitleViewModel.kt — Anotaciones Hilt eliminadas
ui/state/DrawerTitleState.kt       — Anotaciones Hilt eliminadas
ui/screens/KonpartsaCarousel.kt    — koinViewModel()
ui/screens/AppDrawer.kt            — koinViewModel()
ui/screens/KonpartsaListaScreen.kt — koinViewModel()
ui/screens/KonpartsaMapScreen.kt   — koinViewModel()
ui/screens/SettingsScreen.kt       — koinViewModel()
ui/composables/card/KonpartsaCard.kt — koinViewModel()
```

---

## FASE 2: SharedPreferences → multiplatform-settings — COMPLETADA

**Fecha:** 2026-02-11

### Qué se ha hecho

1. **Dependencia añadida:**
   - `libs.versions.toml`: Añadida `multiplatformSettings = "1.3.0"` y library `multiplatform-settings`
   - `app/build.gradle.kts`: Añadida `implementation(libs.multiplatform.settings)`

2. **`LanguageChangeHelper.kt` reescrito:**
   - Lectura/escritura ahora usa la API multiplataforma `Settings.putString()` / `Settings.getString()` en vez de `SharedPreferences.edit { putString() }` / `getString()`
   - La implementación Android (`SharedPreferencesSettings`) queda aislada en el método factoría `getSettings()`, que en KMP se proporcionará via DI/expect-actual
   - Compatible hacia atrás: usa el mismo fichero SharedPreferences `"settings"` por debajo, así que los usuarios existentes no pierden su preferencia de idioma

3. **Build verificado:** `compileDebugKotlin` BUILD SUCCESSFUL.

### Ficheros modificados
```
gradle/libs.versions.toml          — multiplatform-settings 1.3.0 añadida
app/build.gradle.kts               — dependencia añadida
common/LanguageChangeHelper.kt     — reescrito con Settings API
```

---
## FASE 3: Room → Room KMP — COMPLETADA

**Fecha:** 2026-02-11

### Qué se ha hecho

1. **Dependencia `sqlite-bundled` añadida:**
   - `libs.versions.toml`: Añadida `sqliteBundled = "2.5.0"` y library `sqlite-bundled`
   - `app/build.gradle.kts`: Añadida `implementation(libs.sqlite.bundled)`

2. **`Migrations.kt` actualizado a API KMP:**
   - `SupportSQLiteDatabase` (Android-only) → `SQLiteConnection` (multiplataforma)
   - Import: `androidx.sqlite.SQLiteConnection` + `androidx.sqlite.execSQL`
   - Método: `migrate(db: SupportSQLiteDatabase)` → `migrate(connection: SQLiteConnection)`

3. **`AppModule.kt` (Koin) actualizado:**
   - Añadido `.setDriver(BundledSQLiteDriver())` al `Room.databaseBuilder`
   - Esto usa SQLite compilado de fuente (consistente entre plataformas) en vez del SQLite del sistema

4. **`@ConstructedBy` aplazado a Fase 8:**
   - Requiere `expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>` lo cual necesita el plugin KMP
   - En Android-only, `Room.databaseBuilder(context, Class, name)` sigue funcionando perfectamente
   - Se añadirá cuando se cree el módulo `:shared` con KMP

5. **Entidades, DAOs, Converters:** Sin cambios necesarios — ya son KMP-compatibles (solo usan APIs de `androidx.room` que están disponibles en commonMain)

6. **Build verificado:** `compileDebugKotlin` BUILD SUCCESSFUL. 0 referencias a `SupportSQLiteDatabase`.

### Ficheros modificados
```
gradle/libs.versions.toml      — sqlite-bundled 2.5.0 añadida
app/build.gradle.kts            — dependencia sqlite-bundled añadida
data/local/Migrations.kt       — SQLiteConnection en vez de SupportSQLiteDatabase
di/AppModule.kt                 — BundledSQLiteDriver en Room builder
```

### Notas para Fase 8 (KMP)
- Añadir `@ConstructedBy(AppDatabaseConstructor::class)` a `AppDatabase`
- Crear `expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>`
- KSP generará las implementaciones `actual` por plataforma
- El builder pasará de `Room.databaseBuilder(context, Class, name)` a un factory expect/actual

---
## FASE 4: Abstraer APIs Android — COMPLETADA

**Fecha:** 2026-02-11

### Qué se ha hecho

1. **Interfaces actualizadas:**
   - `ImageStorage`: `saveImage`+`readImageBytes` → `copyImageToStorage(platformUri, id)` (más eficiente, copia directa sin cargar todo en memoria)
   - `ShareService`: sin cambios en la interfaz

2. **Implementaciones Android creadas** en `platform/android/`:
   - `AndroidAssetLoader` — usa `Context.assets.open()` para cargar JSON
   - `AndroidImageStorage` — usa `ContentResolver`, `filesDir`, `cacheDir`, `getExternalFilesDir`
   - `AndroidShareService` — usa `Intent.ACTION_SEND/VIEW/SENDTO`, `FileProvider`

3. **Registradas en Koin** (`AppModule.kt`):
   - `single<AssetLoader> { AndroidAssetLoader(androidContext()) }`
   - `single<ImageStorage> { AndroidImageStorage(androidContext()) }`
   - `single<ShareService> { AndroidShareService(androidContext()) }`

4. **`KonpartsaViewModel` reescrito SIN ningún import Android:**
   - Constructor: `(repository, assetLoader, imageStorage, shareService)`
   - `initKonpartsak()` — sin `Context`, usa `assetLoader.loadJsonFromAssets()`
   - `onImageSelected(konpartsa, platformUri: String)` — sin `Context`/`Uri`, usa `imageStorage.copyImageToStorage()`
   - `deleteImage(konpartsa)` — usa `imageStorage.deleteImage()`
   - `shareImage(imageBytes, title)` — usa `shareService.shareImage()`
   - `deleteImages()` — sin `Context`, usa `imageStorage.deleteAllFiles()`
   - Eliminadas: `saveImageToInternalStorage`, `deleteImageFromInternalStorage`, `deleteAllProviderFiles`, `shareToInstagram`

5. **Composables actualizados:**
   - `KonpartsaCard.kt`: `viewModel.onImageSelected(konpartsa, uri.toString())` en vez de `(konpartsa, uri, context)`. Share: captura bytes de GraphicsLayer en el composable, pasa a `viewModel.shareImage(bytes, title)`
   - `KonpartsaCarousel.kt`: `viewModel.initKonpartsak()` sin context. Eliminado `LocalContext.current`
   - `SettingsScreen.kt`: `viewModel.deleteImages()` sin context
   - `AppInfoDialog.kt`: `shareService.openUrl()` via `koinInject()` en vez de `Intent.ACTION_VIEW`
   - `DeveloperInfoDialog.kt`: `shareService.openUrl()` y `.sendEmail()` via `koinInject()` en vez de Intents

6. **`ShareUtils.kt` simplificado:**
   - Solo contiene `GraphicsLayer.toImageBytes(): ByteArray` (bitmap → PNG bytes)
   - Eliminadas: `saveAsShareableFile`, `getShareableUri` (movidas a `AndroidShareService`)

7. **Build verificado:** `compileDebugKotlin` BUILD SUCCESSFUL. 0 imports Android en ViewModels. 0 imports Intent en composables de settings/card.

### Ficheros nuevos
```
platform/android/AndroidAssetLoader.kt
platform/android/AndroidImageStorage.kt
platform/android/AndroidShareService.kt
```

### Ficheros modificados
```
platform/ImageStorage.kt               — interfaz actualizada
di/AppModule.kt                         — platform services registrados en Koin
ui/viewmodel/KonpartsaViewModel.kt     — 0 imports Android, inyecta interfaces
ui/composables/card/KonpartsaCard.kt   — uri.toString(), share via bytes
ui/screens/KonpartsaCarousel.kt        — initKonpartsak() sin context
ui/screens/SettingsScreen.kt           — deleteImages() sin context
ui/composables/settings/AppInfoDialog.kt — shareService via koinInject
ui/composables/settings/DeveloperInfoDialog.kt — shareService via koinInject
common/ShareUtils.kt                   — solo GraphicsLayer → ByteArray
```

### Lo que queda como Android-specific en composables (se moverá en Fase 9)
- `rememberLauncherForActivityResult` / `ActivityResultContracts` en KonpartsaCard (image picker)
- `FileProvider.getUriForFile()` para cámara en KonpartsaCard
- `Toast.makeText()` en KonpartsaCard y SettingsScreen
- `LocalContext.current` para Coil `ImageRequest.Builder` en diálogos y drawer
- `LanguageChangeHelper` en SettingsScreen y MainActivity (inherentemente platform-specific)
- `GraphicsLayer.toImageBytes()` usa `asAndroidBitmap()` (ShareUtils.kt)

---
## FASE 5: Navigation Compose Multiplatform — PENDIENTE
## FASE 6: ViewModel KMP — PENDIENTE
## FASE 7: Verificación final Android — PENDIENTE
## FASE 8: Reestructurar como proyecto KMP — PENDIENTE
## FASE 9: Compose Multiplatform (UI compartida) — PENDIENTE
## FASE 10: Implementaciones iOS — PENDIENTE
