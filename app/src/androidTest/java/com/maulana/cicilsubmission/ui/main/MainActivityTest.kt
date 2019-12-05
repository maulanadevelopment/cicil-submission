package com.maulana.cicilsubmission.ui.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.maulana.cicilsubmission.R
import com.maulana.cicilsubmission.ui.detail.DetailMovieActivity
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {


    @Test
    fun test_progressBar() {
        val activityScenario = ActivityScenario.launch(DetailMovieActivity::class.java)

        onView(withId(R.id.movie_fragment_progressbar))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.toolbar)).check(matches(isDisplayed()))
    }

    @Test
    fun test_recyclerViewInView() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.rv_movie)).check(matches(isDisplayed()))
    }

    
}