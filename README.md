# Yakshagana Loka: A Digital Framework for the Preservation and Promotion of Traditional Folk Theatre

**Abstract**—This report details the design and implementation of *Yakshagana Loka*, a modern mobile application developed to preserve and promote Yakshagana, a traditional folk theatre of Karnataka, India. Leveraging state-of-the-art technologies including Android Jetpack Compose, Firebase Authentication, and OpenRouter AI, the platform provides a comprehensive ecosystem for enthusiasts to track performances, discover artists, and engage with scholarly interpretations of dialogues. The system integrates real-time geospatial data with generative AI to bridge the gap between traditional art forms and modern digital consumers.

---

## I. Introduction
Yakshagana is a vibrant, multi-dimensional art form combining dance, music, dialogue, and elaborate costumes. Despite its cultural significance, the lack of a centralized digital repository for performance schedules, artist profiles, and pedagogical resources has hindered its reach among younger audiences. *Yakshagana Loka* addresses this by providing a unified digital stage that enhances accessibility and understanding of this heritage.

## II. System Architecture
The application is architected following the **Clean Architecture** principles, ensuring scalability, maintainability, and testability.

### A. Presentation Layer
The UI is built entirely using **Jetpack Compose**, facilitating a reactive and declarative UI development process. The interface is optimized for rich media consumption and intuitive navigation.

### B. Domain Layer
Business logic is encapsulated within **Use Cases**, promoting a decoupled architecture. This layer handles complex operations such as the initial data seeding and AI-driven content generation logic.

### C. Data Layer
The application utilizes a multi-source data strategy:
- **Authentication:** Firebase Auth serves as the primary system for user accounts and identity.
- **Local Source:** An offline-first approach where static assets in JSON format are used for all cultural content, ensuring immediate availability.
- **Media Hosting:** Firebase Storage is utilized for hosting high-resolution images and audio clips.

## III. Technical Implementation

### A. Dependency Injection (DI)
**Hilt** is implemented to manage dependency lifecycles across the application, significantly reducing boilerplate code and enhancing modularity.

### B. Artificial Intelligence Integration
A core innovation is the integration of **OpenRouter REST APIs** (utilizing models like GPT-4o-mini). By utilizing generative AI, the application provides real-time "Dialogue Explanations." This feature analyzes Talamaddale (dialogue-centric) transcripts and generates cultural and narrative contexts, making the art form more accessible to non-experts.

### C. Media and Geospatial Services
- **Media3 (ExoPlayer):** Provides a robust foundation for the 'Talamaddale Radio' feature, supporting high-fidelity audio playback.
- **Google Maps SDK:** Integrated via the Compose Maps library to provide a geospatial visualization of performance venues across the coastal regions of Karnataka.

## IV. Key Features

1. **Tonight's Shows:** A dynamic listing of performances scheduled for the current date, backed by local offline JSON data with smart fallbacks.
2. **Artist Directory:** A comprehensive database containing profiles, specializations, and career achievements of legendary and contemporary artists.
3. **Performance Map:** An interactive mapping interface that allows users to find venues geographically and navigate to show locations.
4. **Talamaddale Radio & AI Scholar:** An audio streaming platform enhanced with an AI-driven scholar that explains complex mythological dialogues in simple English and Kannada.

## V. Conclusion
*Yakshagana Loka* represents a significant step in the digital transformation of traditional Indian folk arts. By combining robust mobile development practices with cutting-edge AI, the platform not only archives the art form but also revitalizes it for the digital age. Future iterations will focus on community engagement features and augmented reality (AR) costume visualizations.

---

## VI. Technical Stack Summary
- **Language:** Kotlin 2.x
- **UI:** Jetpack Compose
- **DI:** Dagger-Hilt
- **Backend:** Firebase (Auth, Storage)
- **AI:** OpenRouter REST API via Retrofit
- **Maps:** Google Maps Compose
- **Media:** Android Media3

## VII. Project Structure

The project is organized into a modular package structure under `com.example.yakshaganaloka`:

```text
├── data/
│   ├── local/          # Asset-based JSON data sources
│   ├── model/          # Core domain entities (Artist, Mela, etc.)
│   ├── remote/         # Firebase Auth & API service definitions
│   ├── repository/     # Data abstraction layer
│   └── worker/         # Background tasks
├── di/                 # Hilt dependency injection modules
├── domain/
│   └── usecase/        # Pure business logic (e.g., SeedDataUseCase)
├── presentation/       # Feature-based UI modules
│   ├── artist/         # Artist Directory and Profile screens
│   ├── home/           # Main landing and "Tonight's Shows"
│   ├── map/            # Google Maps integration
│   └── radio/          # Audio playback and AI Dialogue Scholar
├── ui/                 # Global theme and reusable components
│   ├── theme/          # Material3 color schemes and typography
│   └── components/     # Shared UI widgets (EventCard, etc.)
├── MainActivity.kt      # Application entry point & Navigation host
└── YakshaganaLokaApp.kt # Application class and Hilt initialization
```

## VIII. Installation and Setup

To deploy the *Yakshagana Loka* application in a development environment, follow the procedures outlined below:

### A. Prerequisites
- **Android Studio Koala** or newer.
- **JDK 17** or higher.
- A **Firebase Project** with Authentication and Storage enabled.
- An **OpenRouter** API key.

### B. Configuration Steps
1. **Clone the Repository:**
   ```bash
   git clone https://github.com/darshan3104/yakshagana-loka-70.git
   ```
2. **Firebase Integration:**
   - Place the `google-services.json` file provided by the Firebase Console into the `/app` directory.
3. **API Key Setup:**
   - Open the root `gradle.properties` file (create it if it doesn't exist).
   - Add the following entry:
     ```properties
     OPENROUTER_API_KEY="your_actual_api_key_here"
     ```
4. **Build Process:**
   - Sync the project with Gradle files.
   - Execute the `app` module on a physical device or emulator (API Level 24+).

## IX. References

[1] Android Developers, "Jetpack Compose Documentation," [Online]. Available: [https://developer.android.com/compose](https://developer.android.com/compose)

[2] OpenRouter, "API Documentation," [Online]. Available: [https://openrouter.ai/docs](https://openrouter.ai/docs)

[3] Retrofit, "Type-safe HTTP client for Android and Java," [Online]. Available: [https://square.github.io/retrofit/](https://square.github.io/retrofit/)

[4] Android Developers, "Media3 ExoPlayer Guide," [Online]. Available: [https://developer.android.com/guide/topics/media/exoplayer](https://developer.android.com/guide/topics/media/exoplayer)

[5] Dagger Hilt, "Dependency Injection for Android," [Online]. Available: [https://dagger.dev/hilt/](https://dagger.dev/hilt/)

[6] IEEE, "Preparation of Papers for IEEE TRANSACTIONS and JOURNALS," [Online]. Available: [https://ieeeauthorcenter.ieee.org/](https://ieeeauthorcenter.ieee.org/)
