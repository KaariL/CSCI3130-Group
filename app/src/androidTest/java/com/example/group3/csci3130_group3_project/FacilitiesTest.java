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

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class FacilitiesTest {
    @Rule
    public ActivityTestRule<MainActivity> activityActivityTestRule = new ActivityTestRule<MainActivity>(MainActivity.class);

    @Test
    public void navigateToFacilities() throws InterruptedException {
        Thread.sleep(7500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("Dal Services")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("Facilities")).perform((click()));
        Thread.sleep(1500);
        Espresso.onView(withText("Registrar")).perform((click()));
    }

    @Test
    public void navigateToFoodAndBeverage() throws InterruptedException {
        Thread.sleep(7500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("Dal Services")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("Food and Beverage")).perform((click()));
        Thread.sleep(1500);
        Espresso.onView(withText("Coburg Coffee")).perform((click()));
    }

    @Test
    public void navigateToHealthAndWellness() throws InterruptedException {
        Thread.sleep(4500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("Dal Services")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("Health and Wellness")).perform((click()));
        Thread.sleep(1500);
        Espresso.onView(withText("Dalplex")).perform((click()));
        Thread.sleep(1500);
    }




}