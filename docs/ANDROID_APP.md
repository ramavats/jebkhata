# Android App Guide

## Path

Repository root

## Stack

- Kotlin
- Jetpack Compose (Material3)
- SQLite (`SQLiteOpenHelper`)
- Gradle Kotlin DSL

## Main Modules

- `app/src/main/java/com/localfirst/jebkhata/nativeui/MainActivity.kt`
  - App entry point
  - Wires Compose UI to `SqliteGateway`

- `app/src/main/java/com/localfirst/jebkhata/nativeui/ui/ExpenseApp.kt`
  - Full app UI and interaction logic
  - Navigation tabs: Khatas, Calendar, Insights, Settings

- `app/src/main/java/com/localfirst/jebkhata/nativeui/SqliteGateway.kt`
  - Data access layer
  - Entry CRUD, budgets, settings, import/export

- `app/src/main/res/font/`
  - Inter variable fonts and black-weight mapping

## Build and Install

From repository root:

```powershell
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:installDebug
```

Fresh install for reliable resource refresh:

```powershell
.\gradlew.bat :app:installDebugFresh
```

## Packaging

Debug build:

```powershell
.\gradlew.bat :app:assembleDebug
```

## Notes

- Data is local-first and stored on device.
- Import/export uses Android Storage Access Framework pickers.
- UI style and spacing are tokenized in `ExpenseApp.kt` for easy tuning.
