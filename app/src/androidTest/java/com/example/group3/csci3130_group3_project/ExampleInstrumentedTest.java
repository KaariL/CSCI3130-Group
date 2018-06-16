package com.example.group3.csci3130_group3_project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);
    @Test
    public void opensMenu() {
        Espresso.onView(withId(R.id.toolbar)).perform(ViewActions.click());
        Espresso.onView(withId(R.id.nav_view)).check(matches(withId(R.id.drawer_layout)));
    }
    //@Test
    //public void navigatesCorrect() {
    //    Espresso.onView(withId(R.id.)).perform(ViewActions.click());
    //}
}
