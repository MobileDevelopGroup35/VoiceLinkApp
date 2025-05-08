# VoiceLink: Voice-to-Task Manager

VoiceLink is an Android app that transforms your spoken words into organized tasks. Using natural speech recognition, it intelligently extracts task details without requiring specific commands or formats.

## Features

- **Speech-to-Task Conversion**: Simply speak to create fully-formed tasks
- **Smart Categorization**: Automatically detects task categories based on context
- **Priority Detection**: Identifies task urgency from your natural speech
- **Date Recognition**: Understands time references like "tomorrow" or "next Monday"
- **Clean Material Design**: Modern UI with visual category and priority indicators

## Technical Details

Built with modern Android development practices:

- **Kotlin & Jetpack Compose**: Declarative UI framework
- **Room Database**: Local SQLite storage with abstraction layer
- **MVVM Architecture**: Clean separation of concerns
- **Android SpeechRecognizer API**: Native voice processing
- **Hilt**: Dependency injection framework

## Project Structure

```
com.l4kt.voicelink/
├── data/            # Dependency injection modules
├── di/              # Database, entities, and repositories
├── domain/          # Business logic and models
├── ui/              # UI components and screens
│   ├── components/  # Material3 theming
│   ├── screens/     # Main app screens
│   └── theme/       # Reusable UI components
└── util/            # Utility classes
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox (2021.3.1) or newer
- Android SDK 24+ (Android 7.0 Nougat)
- Kotlin 2.0.0+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/MobileDevelopGroup35/VoiceLinkApp.git
   ```

2. Open the project in Android Studio

3. Sync Gradle files and build the project

4. Run on an emulator or physical device with microphone access

## Usage

1. Tap the microphone button at the bottom of the screen
2. Speak your task naturally (e.g. "Buy groceries tomorrow")
3. The app will automatically categorize and prioritize your task
4. View and manage your tasks in the list

## Permissions

The app requires the following permissions:
- `RECORD_AUDIO`: For voice recording functionality

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License

## Acknowledgements

- [Android Jetpack](https://developer.android.com/jetpack)
- [Material Design](https://material.io/design)
