package io.github.nfdz.memotex.exercise

import io.github.nfdz.memotex.common.Exercise
import io.github.nfdz.memotex.common.ExerciseAnswers
import io.github.nfdz.memotex.common.ExerciseResult
import io.github.nfdz.memotex.common.Level

interface ExerciseView {
    fun showLoading()
    fun showExercise(title: String, exercise: Exercise)
    fun setExerciseProgress(progress: Int)
    fun showChangeAnswerDialog(position: Int, currentAnswer: String)
    fun increaseFontSize()
    fun decreaseFontSize()
    fun navigateToResult(result: ExerciseResult)
    fun navigateToError()
}

interface ExercisePresenter {
    fun onCreate(title: String, content: String, level: Level, exercise: Exercise?)
    fun onDestroy()
    fun onChangeAnswerClick(position: Int, currentAnswer: String)
    fun onIncreaseFontSizeClick()
    fun onDecreaseFontSizeClick()
    fun onProgressChanged(progress: Int)
    fun onCheckExerciseClick(exercise: Exercise, answers: ExerciseAnswers)
}

interface ExerciseInteractor {
    fun prepareExercise(content: String, level: Level, success: (exercise: Exercise) -> Unit, error: () -> Unit)
    fun checkAnswers(title: String,
                     content: String,
                     level: Level,
                     exercise: Exercise,
                     exerciseAnswers: ExerciseAnswers,
                     callback: (exerciseResult: ExerciseResult) -> Unit)
}