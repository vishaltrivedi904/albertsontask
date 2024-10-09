# Albertson Task

The Albertson Task is an Android application that combines traditional XML layouts with modern Jetpack Compose UI components. Users can input the number of results they wish to display and view detailed information about users.

## Main Features

- **Main Screen:** Traditional XML UI to input the number of results to display.
- **Details Page:** Modern Jetpack Compose UI for viewing detailed user information.
- **Pagination:** Efficient data retrieval from the API for handling large datasets.
- **Swipe to Refresh:** Enhanced user experience with pull-to-refresh functionality.
- **Dependency Injection:** Utilizes Hilt for managing dependencies.
- **Basic Test Cases:** Some test cases included, though limited due to time constraints.

## Architecture

- **MVVM Architecture:** Separates concerns into Model, View, and ViewModel for maintainability.
- **Repository Pattern:** Centralizes data management and business logic.

## Technologies Used

- **Kotlin:** Official programming language for Android development.
- **Jetpack Compose:** A modern toolkit for building native UI components.
- **Coroutines:** For handling asynchronous operations.
- **LiveData:** Observes data changes and updates the UI accordingly.
- **ViewModel:** Manages UI-related data in a lifecycle-aware manner.
- **Hilt:** Simplifies dependency injection.
- **Retrofit:** Type-safe HTTP client for making network requests.
- **Material Components:** Provides customizable UI elements following Material Design guidelines.

## Getting Started

### Prerequisites

- Android Studio (Arctic Fox or newer)
- Android SDK
- Kotlin 1.6 or newer

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/vishaltrivedi904/albertsontask.git

2. **Open the Project**
Launch Android Studio and select `File > Open`. Navigate to the cloned repository and open it.

3. **Sync Gradle**
Click `Sync Project` with Gradle Files to download and set up dependencies.

4. **Build and Run**
Build and run the app on an emulator or a physical device.

## Usage

- **Main Screen:** Input the number of results you want to display using the text field and press "Submit."
- **Details Page:** Tap on a user to view detailed information.
- **Swipe to Refresh:** Pull down on the main screen to refresh the list of users and retrieve new data.

## Acknowledgements

- **Jetpack Compose:** Modern UI toolkit for Android.
- **Paging 3:** Efficient paging library.
- **Hilt:** Dependency injection framework.
- **Retrofit:** Type-safe HTTP client.

