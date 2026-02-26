# LifeLift - Premium Fitness & Health Tracker

A high-performance native Android app with Apple-inspired design aesthetic, built with Jetpack Compose and Modern Android Development practices.

## Features

### 🏋️ Iron (Gym Tracker)
- Track workout routines with exercises, sets, reps, and weight
- Automatic volume calculation
- Swipe-to-dismiss functionality
- Performance metrics

### 💊 Vitality (Vitamin Manager)
- Daily supplement checklist
- Smart notifications with WorkManager
- Completion tracking with satisfying animations
- Customizable dosage and timing

### 📊 Progress (Analytics)
- Beautiful charts with Vivo library
- Weight progression tracking
- Volume and strength analytics
- Consistency monitoring

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose with Material 3
- **Architecture:** MVVM + Clean Architecture
- **DI:** Hilt
- **Database:** Room with Coroutines/Flow
- **Navigation:** Type-safe Compose Navigation
- **Charts:** Vico
- **Notifications:** WorkManager

## Design Philosophy

"Apple feel on Android" - Premium minimalist design with:
- Heavy glassmorphism and blur effects
- 24dp+ rounded corners throughout
- Neon Blue (Gym), Soft Mint (Vitamins), Purple (Analytics) accents
- Dark/Light mode support
- Haptic feedback on all interactions

## Languages

- 🇬🇧 English
- 🇷🇺 Russian

## Project Structure

```
app/
├── core/
│   ├── data/local/        # Room database, DAOs, entities
│   ├── di/                # Hilt modules
│   └── ui/
│       ├── components/    # Reusable composables (GlassyCard, etc.)
│       └── theme/         # Color, Typography, Theme
├── feature/
│   ├── gym/              # Workout tracking module
│   ├── vitamins/         # Supplement management module
│   └── analytics/        # Progress visualization module
└── MainActivity.kt
```

## Building

1. Clone the repository
2. Open in Android Studio (Hedgehog or newer)
3. Sync Gradle
4. Run on device or emulator (min SDK 26 / Android 8.0)

## License

Private project - All rights reserved
