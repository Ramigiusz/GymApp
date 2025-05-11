Nasza apka treningowa

Rami Matouk
Jan Galicki

## Mockup aplikacji Colossus

https://www.figma.com/design/bDmddT8Lrk0nivnTjOQsMZ/GymApp?node-id=1-6&t=UbVZhWUv40pXeFEa-1

## 🔀 Diagram nawigacji – Aplikacja Colossus

```mermaid
flowchart TD
    %% Ekrany główne
    Start["🏠 StartScreen<br/><small>Szybki dostęp do rutyn</small>"]
    Settings["⚙️ SettingsScreen<br/><small>Jednostki, baza ćwiczeń</small>"]
    Routines["📋 RoutinesScreen<br/><small>Lista i zarządzanie rutynami</small>"]
    EditRoutine["📝 EditRoutineScreen<br/><small>Budowa planu treningowego</small>"]
    Training["🏋️ TrainingScreen<br/><small>Trening aktywny, timer</small>"]
    ExerciseDB["📚 ExerciseDatabase<br/><small>Zarządzanie bazą ćwiczeń</small>"]

    %% Nawigacja główna
    Start --> Routines
    Start --> Settings

    Settings --> ExerciseDB
    Settings --> Start
    Settings --> Routines

    Routines --> EditRoutine
    Routines --> Training
    Routines --> Settings

    EditRoutine --> Routines
    Training --> Start

    %% Przypis do EditRoutine
    subgraph Uwaga
        Note1[/"📎 EditRoutineScreen korzysta z bazy ćwiczeń,<br/>ale jej nie edytuje"/]
    end
    EditRoutine -.-> Note1
```



