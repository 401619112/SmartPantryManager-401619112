
# Smart Pantry Manager

Smart Pantry Manager is a Java Android application designed to help people use ingredients they already have at home and reduce food waste.

## Technology
- Java and Android Studio
- SQLite (`SQLiteOpenHelper`) for local persistent storage
- RecyclerView with custom adapters
- Activities and Intents for navigation

SQLite was chosen because it works offline, is covered by standard Android development workflows, and keeps pantry information on the device without requiring a server account.

## Open and run
1. Install Android Studio and a compatible JDK (Android Studio's bundled JDK is recommended).
2. Extract this project folder.
3. In Android Studio choose **Open**, then select the `SmartPantryManager` folder.
4. Allow Gradle to sync. If prompted, install Android SDK Platform 35 and the Android build tools.
5. Create an emulator or connect an Android device with USB debugging enabled.
6. Press **Run**.

The database and 20 starter recipes are created automatically on first launch. Pantry entries and settings are stored locally.

## Main features
- Create, read, update and delete pantry ingredients
- Quantity, unit and optional expiry date
- 20 preloaded recipes with method steps
- Strict recipe matching: every required ingredient and quantity must be available
- Ingredient name normalization for common plural forms
- Recipe details and expiry-alert setting
- Empty-state message when there are no recipes available

## Suggested development workflow
Use Git from the beginning and commit meaningful steps as you implement and test each feature. Examples:
1. `Create Android Java project structure`
2. `Add SQLite database helper`
3. `Seed starter recipe collection`
4. `Implement pantry CRUD operations`
5. `Build pantry RecyclerView adapter`
6. `Add ingredient form validation`
7. `Implement strict recipe matching`
8. `Create recipe detail screen`
9. `Add settings persistence`
10. `Improve empty states and navigation`

Only make commits that reflect work you actually completed. Push your repository to GitHub and follow your lecturer's rules for making it public or adding the marker as a collaborator.
