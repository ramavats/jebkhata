# JebKhata (Android)

**जेब का हिसाब, हर दिन आसान।**  
**Pocket expenses, tracked simply every day.**

<img height="600" alt="image" src="https://github.com/user-attachments/assets/ca23c37f-27de-4862-b740-a952e01f15a2" />


Android-only expense tracker built with Kotlin + Jetpack Compose and local SQLite storage.

UI design inspiration credit: **[chillar.me](https://chillar.me)**.

## Availability

JebKhata is currently distributed through **GitHub Releases**.

It is **not on Play Store yet**.

Releases: https://github.com/ramavats/jebkhata/releases

## Install (from GitHub Releases)

1. Open this repository's **Releases** page on GitHub.
2. Download the latest Android APK from the release assets.
3. On your Android device, allow installation from unknown sources if prompted.
4. Open the downloaded APK and install.
5. For updates, install a newer release APK over the existing app.

Note: tag builds now publish a **signed release APK** when required GitHub secrets are configured.

---

## Build From Source

### Prerequisites

- Android Studio (latest stable)
- Android SDK + platform tools
- JDK 17 (Android Studio bundled JBR works)
- Android 8.0+ device (minSdk `26`)

### Run on Device (CLI)

```powershell
.\gradlew.bat :app:installDebug
```

For a clean reinstall during development:

```powershell
.\gradlew.bat :app:installDebugFresh
```

### Build APK

```powershell
.\gradlew.bat :app:assembleDebug
```

## Signed Release Pipeline (GitHub Actions)

Tag pushes (`v*`) trigger release APK build and publish to GitHub Releases.

Required repository secrets:

- `ANDROID_KEYSTORE_BASE64` (base64 of your `.jks` file)
- `ANDROID_KEYSTORE_PASSWORD`
- `ANDROID_KEY_ALIAS`
- `ANDROID_KEY_PASSWORD`

Keystore decode path is handled in CI and provided to Gradle as:

- `ANDROID_KEYSTORE_PATH`

---

## Current Features

- Khatas:
  - Quick command input (`10 chai`)
  - Swipe row actions (edit/delete)
  - Haptic feedback on interactions
- Calendar:
  - Month grid with day selection
  - Long-press + drag date-range selection
  - Range/day entry list
- Insights:
  - This week, last week, trend %, month spend
  - Top item spend breakdown
- Settings:
  - Currency toggle
  - JSON import/export via Android file picker
  - Clear all data
- UI:
  - Dark minimal design
  - Inter + monospace typography controls
  - Animated minimal toast notifications

## Documentation

- [Android App Guide](/docs/ANDROID_APP.md)
- [Features and Roadmap](/docs/FEATURES_ROADMAP.md)

## Troubleshooting

- If APK does not install, confirm the release asset is signed and supports your device ABI.
- If installation is blocked, enable install permission for your browser/file manager (unknown sources).
- If UI/font/theme changes look stale during development, use:
  - `.\gradlew.bat :app:installDebugFresh`

## Data & Privacy

- JebKhata stores data locally in on-device SQLite by default.
- No Play Store/cloud sync pipeline is enabled in this repository at the moment.

## License

MIT License. See [LICENSE](/LICENSE).
