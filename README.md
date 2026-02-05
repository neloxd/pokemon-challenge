# Pokémon Challenge (Compose + MVVM + Hilt)

# Pokémon Challenge by Tala

**Author:** Jesús Villa

A clear and concise README is key so the team can evaluate your working process and how you structure your solution. Don’t skip it.

---

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM (UI State + ViewModel)
- **DI:** Hilt
- **Networking:** Retrofit + OkHttp
- **JSON:** Kotlinx Serialization (or Moshi, depending on project setup)
- **Async:** Kotlin Coroutines + Flow
- **Images:** Coil (`AsyncImage`)
- **Persistence (Favorites / cache):** Room (KSP) + Flow
- **Build System:** Gradle Kotlin DSL (`.kts`)
- **Min/Target SDK:** minSdk 24, targetSdk 34
- **Testing (if included):**
    - Unit: JUnit
    - Coroutines test
    - (Optional) Compose UI test

## Brief description
1. Open this folder in Android Studio.
2. Let Gradle sync (it will download Gradle 8.7 via wrapper).
3. Run the app on an emulator/device with internet access.

## App Features
- **Pokédex list screen**
    - Displays Pokémon in a 3-column grid.
    - Search by name (real-time filter).
    - Sort by **Number** or **Name** (UI modal / dropdown).
- **Detail screen**
    - Loads and displays Pokémon details by id.
- **State handling**
    - Loading / error / success UI states.
- **Image loading**
    - Sprite images loaded asynchronously with caching via Coil.
- Optional - Favorites: toggle from home card + detail (persisted in Room) (is off)


## Architecture Overview (High Level)

### Layers

- **UI Layer (Compose)**
    - Stateless composables driven by state from ViewModels.
    - Screens: `HomeScreen`, `DetailScreen` (names may vary).
- **Presentation Layer**
    - `ViewModel` exposes a single `StateFlow<UiState>` (loading/error/data/query/sort).
    - UI events (search, sort, navigation) are dispatched to the ViewModel.
- **Domain Layer**
    - Models: `PokemonListItem`, `PokemonDetail`
    - Repository contract: `PokemonRepository`
    - (Optional) UseCases for each action (list, detail, toggle favorite)
- **Data Layer**
    - Retrofit service for Pokémon API calls.
    - Mappers DTO → domain models.
    - Room database for local persistence (favorites / caching), exposed as Flow.


### State Model

The Home screen uses:
- `items` (all loaded Pokémon)
- `query` (search text)
- `sort` (Number/Name)
- derived `visibleItems()` for filtered + sorted list

---

## Libraries Used

- **Hilt:** Dependency injection for ViewModels, repositories, and data sources.
- **Retrofit + OkHttp:** API client and HTTP stack.
- **Coroutines + Flow:** Async operations + reactive streams.
- **Coil:** Image loading and caching in Compose.
- **Room (KSP):** Local persistence with compile-time generated code.

