package com.example.gymapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.R
import com.example.gymapp.data.db.AppDatabase
import com.example.gymapp.data.model.Exercise
import com.example.gymapp.data.model.ExerciseWithTags
import com.example.gymapp.data.model.Tag
import com.example.gymapp.data.model.ExerciseTagCrossRef
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).exerciseDao()

    // lista prostych ćwiczeń (do CRUD)
    var exercises by mutableStateOf<List<Exercise>>(emptyList())
        private set

    // ćwiczenia wraz z tagami
    var allExercisesWithTags by mutableStateOf<List<ExerciseWithTags>>(emptyList())
        private set

    // lista wszystkich tagów
    var allTags by mutableStateOf<List<Tag>>(emptyList())
        private set

    // pola do dodawania nowego ćwiczenia
    var newExerciseName by mutableStateOf("")
    var newExerciseDescription by mutableStateOf("")

    init {
        // zaladowanie domyślnych danych
        loadExercises()
        seedDefaults()
    }

    // załadowanie ćwiczeń
    fun loadExercises() = viewModelScope.launch {
        exercises = dao.getAllExercises()
    }

    fun addExercise(name: String, description: String = "") = viewModelScope.launch {
        dao.insertExercise(Exercise(name = name, description = description))
        loadExercises()
    }

    fun deleteExercise(exercise: Exercise) = viewModelScope.launch {
        dao.deleteExercise(exercise)
        loadExercises()
    }

    fun updateExercise(exercise: Exercise) = viewModelScope.launch {
        dao.updateExercise(exercise)
        loadExercises()
    }

    /** Dodaje domyślne ćwiczenia przy pierwszym uruchomieniu */
    private fun seedDefaults() = viewModelScope.launch {
        if (dao.getAllExercises().isEmpty()) {
            val squatId = dao.insertExercise(
                Exercise(name = "Przysiad ze sztangą", description = "Stań szeroko, sztanga na barkach, schodź w dół aż uda będą równolegle. Działa na: uda, pośladki, dolny grzbiet, core.",
                    drawableRes = R.drawable.squat))
                .toInt()
            val pushUpId = dao.insertExercise(
                Exercise(name = "Pompka klasyczna", description = "Utrzymuj ciało prosto, opuszczaj się zginając łokcie, wracaj do góry. Działa na: klatkę piersiową, tricepsy, barki, core.",
                    drawableRes = R.drawable.push_up))
                .toInt()

            val benchPressId = dao.insertExercise(
                Exercise(name = "Wyciskanie na ławce", description = "Leżąc na ławce, opuść sztangę do klatki, wypchnij dynamicznie w górę. Działa na: klatkę piersiową, tricepsy, przednie barki.",
                    drawableRes = R.drawable.barbell_bench_press))
                .toInt()

            val deadliftId = dao.insertExercise(
                Exercise(name = "Martwy ciąg", description = "Stopy pod sztangą, plecy proste, unieś sztangę aż do wyprostu bioder. Działa na: pośladki, dwugłowe uda, plecy, core.",
                    drawableRes = R.drawable.barbell_deadlift))
                .toInt()

            val pullUpId = dao.insertExercise(
                Exercise(name = "Podciąganie nachwytem", description = "Zawis na drążku, podciągaj się aż broda znajdzie się nad drążkiem. Działa na: plecy, bicepsy, barki, core.",
                    drawableRes = R.drawable.pull_up))
                .toInt()

            val rowId = dao.insertExercise(
                Exercise(name = "Wiosłowanie sztangą", description = "Pochyl tułów, ciągnij sztangę do brzucha, łokcie blisko ciała. Działa na: mięśnie pleców, bicepsy, tylny akton barków.",
                    drawableRes = R.drawable.barbell_row))
                .toInt()

            val ohpId = dao.insertExercise(
                Exercise(name = "OHP – wyciskanie nad głowę", description = "Sztanga na barkach, wypchnij ją nad głowę, utrzymaj prosty tułów. Działa na: barki, tricepsy, górna część klatki.",
                    drawableRes = R.drawable.barbell_shoulder_press))
                .toInt()

            val lateralRaiseId = dao.insertExercise(
                Exercise(name = "Unoszenie hantli bokiem", description = "Ramiona lekko zgięte, unoś hantle bokiem do wysokości barków. Działa na: boczny akton barków.",
                    drawableRes = R.drawable.lateral_raise))
                .toInt()

            val plankId = dao.insertExercise(
                Exercise(name = "Plank (deska)", description = "Oprzyj się na przedramionach, ciało w jednej linii, napnij brzuch. Działa na: mięśnie głębokie brzucha, core, stabilizację tułowia.",
                    drawableRes = R.drawable.plank))
                .toInt()

            val calfRaiseId = dao.insertExercise(
                Exercise(name = "Wspięcia na palce ze sztangą", description = "Stój prosto, sztanga na barkach, wspinaj się powoli na palce kontrolowanym ruchem. Działa na: łydki.",
                    drawableRes = R.drawable.calf_raise))
                .toInt()

            val curlId = dao.insertExercise(
                Exercise(name = "Uginanie ramion ze sztangą", description = "Stój prosto, zginaj ramiona z ciężarem kontrolowanym ruchem, nie kołysz tułowiem. Działa na: bicepsy.",
                    drawableRes = R.drawable.barbell_curl))
                .toInt()

            val tricepsPushdownId = dao.insertExercise(
                Exercise(name = "Prostowanie ramion na wyciągu", description = "Łokcie przy tułowiu, prostuj ramiona do pełnego wyprostu kontrolowanym ruchem. Działa na: tricepsy.",
                    drawableRes = R.drawable.cable_rope_pushdown))
                .toInt()

            val legRaiseId = dao.insertExercise(
                Exercise(name = "Przyciąganie nóg do klatki w zwisie", description = "Zawis na drążku, przyciągaj kolana do brzucha, napnij mięśnie. Działa na: mięśnie brzucha, biodra.",
                    drawableRes = R.drawable.leg_raise))
                .toInt()

            val hipThrustId = dao.insertExercise(
                Exercise(name = "Hip thrust ze sztangą", description = "Plecy na ławce, sztanga na biodrach, wypchnij biodra do góry. Działa na: pośladki, dwugłowe uda, core.",
                    drawableRes = R.drawable.hip_thrust))
                .toInt()

            val crunchMachineId = dao.insertExercise(
                Exercise(name = "Maszyna do brzuszków", description = "Zegnij tułów w przód, napnij brzuch i przyciągnij klatkę w kierunku ud. Działa na: Mięsień prosty brzucha, mięśnie skośne.",
                    drawableRes = R.drawable.crunch_machine))
                .toInt()

            loadExercises()
        }
    }
}
