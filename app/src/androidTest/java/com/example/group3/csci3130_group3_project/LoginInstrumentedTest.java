package com.example.group3.csci3130_group3_project;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    String goodUsername = "kaari.landry@gmail.com";
    String goodPassword = "Agr34Tpw!";
    @Rule
    public ActivityTestRule<CredentialActivity> mActivityTestRule =
            new ActivityTestRule<CredentialActivity>(CredentialActivity.class);

    private Activity loginActivity = null;
    @Before
    public void logIn() throws Exception  {
        loginActivity = mActivityTestRule.getActivity();
        //Activity startActivity =  CredentialActivity.getActivity();
        Espresso.onView(withId(R.id.editText_name)).perform(ViewActions.typeText(goodUsername));
        Espresso.onView(withId(R.id.editText_psw)).perform(ViewActions.typeText(goodPassword));
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());
        //this should redirect you to map

    }

    @Test
    public void isOnMapActivity()throws InterruptedException{
        Thread.sleep(5000);
        Espresso.onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

}
