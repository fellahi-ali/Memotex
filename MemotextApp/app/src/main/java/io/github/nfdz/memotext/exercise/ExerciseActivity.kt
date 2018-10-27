package io.github.nfdz.memotext.exercise

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.LinearLayout
import io.github.nfdz.memotext.R
import io.github.nfdz.memotext.common.*
import kotlinx.android.synthetic.main.activity_exercise.*


fun Context.startExerciseActivity(text: Text) {
    val starter = Intent(this, ExerciseActivity::class.java)
    starter.putExtra(EXTRA_TEXT_TITLE, text.title)
    starter.putExtra(EXTRA_TEXT_CONTENT, text.content)
    starter.putExtra(EXTRA_TEXT_LEVEL, text.level.name)
    startActivity(starter)
}

private val EXTRA_TEXT_TITLE = "text_title"
private val EXTRA_TEXT_CONTENT = "text_content"
private val EXTRA_TEXT_LEVEL = "text_level"
private val EXTRA_EXERCISE_ANSWERS = "exercise_answers"
private val EXTRA_SCROLL_POSITION = "scroll_position"
private val EXTRA_EXERCISE = "exercise"

class ExerciseActivity : AppCompatActivity(), ExerciseView, AdapterListener {

    val presenter: ExercisePresenter by lazy { ExercisePresenterImpl(this, ExerciseInteractorImpl(this)) }
    val adapter = ExerciseAdapter(listener = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupView()
        presenter.onCreate(intent.getStringExtra(EXTRA_TEXT_TITLE, ""),
            intent.getStringExtra(EXTRA_TEXT_CONTENT, ""),
            Level.valueOf(intent.getStringExtra(EXTRA_TEXT_LEVEL, Level.BRONZE.name)),
            savedInstanceState?.getParcelable(EXTRA_EXERCISE))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(EXTRA_EXERCISE, adapter.exercise)
        outState?.putParcelable(EXTRA_EXERCISE_ANSWERS, adapter.getExerciseAnswers())
        val llm = (exercise_rv.layoutManager as? LinearLayoutManager)
        outState?.putInt(EXTRA_SCROLL_POSITION, llm?.findFirstCompletelyVisibleItemPosition() ?: 0)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.setExerciseAnswers(savedInstanceState?.getParcelable<ExerciseAnswers>(EXTRA_EXERCISE_ANSWERS))
        exercise_rv.scrollToPosition(savedInstanceState?.getInt(EXTRA_SCROLL_POSITION) ?: 0)
    }

    private fun setupView() {
        setContentView(R.layout.activity_exercise)
        exercise_rv.adapter = adapter
        exercise_iv_font_big.setOnClickListener { presenter.onIncreaseFontSizeClick() }
        exercise_iv_font_small.setOnClickListener { presenter.onDecreaseFontSizeClick() }
        exercise_fab_check.setOnClickListener { presenter.onCheckExerciseClick() }
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun showLoading() {
        exercise_loading.visibility = View.VISIBLE
        exercise_group_ongoing.visibility = View.GONE
    }

    override fun showExercise(title: String, exercise: Exercise) {
        exercise_loading.visibility = View.GONE
        exercise_group_ongoing.visibility = View.VISIBLE
        exercise_tv_title.text = title
        adapter.exercise = exercise
    }

    override fun setExerciseProgress(progress: Int) {
        exercise_tv_progress.text = "${progress.toString()}%"
        exercise_pb.progress = progress
    }

    override fun increaseFontSize() {
        // TODO
    }

    override fun decreaseFontSize() {
        // TODO
    }

    override fun navigateToResult() {
        // TODO
    }

    override fun showChangeAnswerDialog(position: Int, currentAnswer: String) {
        val input = AppCompatEditText(this)
        input.maxLines = 1
        input.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        AlertDialog.Builder(this).apply {
            title = getString(R.string.exercise_change_answer_title)
            input.append(currentAnswer)
            setView(input)
        }.setNegativeButton(android.R.string.cancel) { dialog: DialogInterface, _: Int ->
            dialog.cancel()
        }.setPositiveButton(R.string.exercise_change_answer_ok) { dialog: DialogInterface, _: Int ->
            adapter.putAnswer(position, input.text.toString())
            dialog.dismiss()
        }.show()
    }

//    override fun onProgressChanged(progress: Int) {
//        presenter.onProgressChanged(progress)
//    }

    override fun onChangeAnswerClick(position: Int, currentAnswer: String) {
        presenter.onChangeAnswerClick(position, currentAnswer)
    }

}