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

@RunWith(AndroidJUnit4.class)
public class LoginPageInstrumentedTest {
    private Activity loginActivity = null;
    @Rule
    public ActivityTestRule<CredentialActivity> mActivityTestRule =
            new ActivityTestRule<CredentialActivity>(CredentialActivity.class);
    @Test
    public void logIn() throws Exception  {
        loginActivity = mActivityTestRule.getActivity();
        //Espresso.onView(withId(R.id.imageViewDal)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.editText_name)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.editText_psw)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_login)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_regs)).check(matches(isDisplayed()));
        Espresso.onView(withId(R.id.button_fid)).check(matches(isDisplayed()));
    }
}
