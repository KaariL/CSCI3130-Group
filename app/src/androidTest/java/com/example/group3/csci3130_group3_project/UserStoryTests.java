package com.example.group3.csci3130_group3_project;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.assertEquals;


/**
 * User Story Tests
 *
 * This class contains a system wide series of integration tests for the
 *
 * Note: A standard used throughout the entire class is creating delay's using Thread.sleep(millis)
 *          If an activity switch is to occur we delay the test by 1500 millis
 *          If an animation such as closing a keyboard or opening the menu is to occur we use a delay of 500 millis
 *
 * @author Serena D, George F
 */

@RunWith(AndroidJUnit4.class)
public class UserStoryTests {
    private String goodUsername = "espresso@dal.ca";
    private String goodPassword = "qawsedrftgyh1";
    private String randomStreetAddress = "1333 South Park Street";
    private String favoriteName = "Espresso Generated Location";

    @Rule
    public ActivityTestRule<CredentialActivity> mActivityTestRule =
            new ActivityTestRule<CredentialActivity>(CredentialActivity.class);

    /*
     *The LogIn function runs before every test to encapsulate the user experience of logging in (E.i. Starting from the beginning)
     * */
    @Before
    public void LogIn() throws Exception  {
        Espresso.onView(withId(R.id.editText_name)).perform(ViewActions.typeText(goodUsername));
        Espresso.onView(withId(R.id.editText_psw)).perform(ViewActions.typeText(goodPassword),closeSoftKeyboard());
        Thread.sleep(500);
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click());
        Thread.sleep(2000);
    }

    /*
     * To confirm a successful log out the user should be taken to the CredentialActivity
     * */
    @Test
    public void canLogOut()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Logout")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.editText_name)).check( matches( isDisplayed() ) );
        Espresso.onView(withId(R.id.editText_psw)).check( matches( isDisplayed() ) );
    }

    /*
     * If a user clicks on Forgot Password they should be taken to the Email Activity
     * */
    @Test
    public void forgetPassword()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Logout")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.button_fid)).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.button_reset)).check( matches( isDisplayed() ) );
        Espresso.onView(withId(R.id.editText_email)).check( matches( isDisplayed() ) );
    }

    /*
     * If a user enters a address on the bar the Activity doesn't change and the map displays a new marker/direction
     * */
    @Test
    public void canUseSearchBar()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.searchBar)).perform(ViewActions.typeText(randomStreetAddress),closeSoftKeyboard());
        Thread.sleep(500);
        Espresso.onView(withId(R.id.map)).check( matches( isDisplayed() ) );
    }


    /*
     * If a user goes to view their favorites the recyclerview containing their favourite locations should be displayed
     * */
    @Test
    public void seeFavorites()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Favorites")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.recyclerview)).check( matches( isDisplayed() ) );
    }

    /*
     * If a user goes to view their profile data they should be taken to the Profile Activity
     *
     * */
    @Test
    public void seeProfile()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Profile")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.profile_username)).check( matches( isDisplayed() ) );
        Espresso.onView(withId(R.id.profile_passwordButton)).check( matches( isDisplayed() ) );
    }


    /*
     * If a user performs a long click on the map the location should be added to their favorites and can be removed from the favorite activity
     *
     * */
    @Test
    public void addAndRemoveFavorite()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.map)).perform(longClick());
        Thread.sleep(500);
        onView(withHint("Enter a nickname")).perform(typeText(favoriteName),closeSoftKeyboard());
        Thread.sleep(500);
        onView(withText("Add to Favorites")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Favorites")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText(favoriteName)).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Remove")).perform(click());
        Espresso.onView(withText(favoriteName)).check( doesNotExist() );
    }

    /*
     * If a user performs adds a location they should be able to navigate to it and see the marker/destination on the map
     * */
    @Test
    public void addNavRemoveFavorite()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.map)).perform(longClick());
        Thread.sleep(500);
        onView(withHint("Enter a nickname")).perform(typeText(favoriteName),closeSoftKeyboard());
        Thread.sleep(500);
        onView(withText("Add to Favorites")).perform(click());

        Thread.sleep(500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Favorites")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText(favoriteName)).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Navigate")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.map)).check(matches(isDisplayed()));


        Thread.sleep(500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Favorites")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText(favoriteName)).perform(click());
        Espresso.onView(withText("Remove")).perform(click());
        Espresso.onView(withText(favoriteName)).check( doesNotExist() );


    }

    /*
     * If a user changes their profile information the change is reflected on the profile page
     * */
    @Test
    public void changeProfileData()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Profile")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.profile_username)).perform(clearText(),typeText("NotEssy"),closeSoftKeyboard());
        Thread.sleep(500);
        Espresso.onView(withId(R.id.profile_favoriteColor)).perform(clearText(),typeText("Blue"),closeSoftKeyboard());
        Thread.sleep(500);
        Espresso.onView(withId(R.id.profile_updateButton)).perform(click());
        Thread.sleep(500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Home")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Profile")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withText("NotEssy")).check( matches( isDisplayed() ) );
        Espresso.onView(withText("Blue")).check( matches( isDisplayed() ) );
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.profile_username)).perform(clearText(),typeText("Essy"),closeSoftKeyboard());
        Thread.sleep(500);
        Espresso.onView(withId(R.id.profile_favoriteColor)).perform(clearText(),typeText("Red"),closeSoftKeyboard());
        Thread.sleep(500);
        Espresso.onView(withId(R.id.profile_updateButton)).perform(click());
    }


    /*
     * If a user wants to register they can navigate to the network
     * */
    @Test
    public void registerPage()throws InterruptedException {
        Thread.sleep(1500);
        Espresso.onView(withContentDescription("Navigate up")).perform(click());
        Thread.sleep(500);
        Espresso.onView(withText("Logout")).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.button_regs)).perform(click());
        Thread.sleep(1500);
        Espresso.onView(withId(R.id.register_username)).check( matches( isDisplayed() ) );
        Espresso.onView(withId(R.id.email)).check(matches(isDisplayed()));
    }
}

