package com.BlackBox.Wifi_Login;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class Normal {

    @Rule
    public ActivityTestRule<Splash_Screen> mActivityTestRule = new ActivityTestRule<>(Splash_Screen.class);

    @Test
    public void normal() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction editText = onView(
                allOf(withId(R.id.eT_UserName), isDisplayed()));
        editText.perform(click());

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.eT_UserName), isDisplayed()));
        editText2.perform(click());

        ViewInteraction editText3 = onView(
                allOf(withId(R.id.eT_UserName), isDisplayed()));
        editText3.perform(replaceText("fddfdf"), closeSoftKeyboard());

        pressBack();

        ViewInteraction editText4 = onView(
                allOf(withId(R.id.eT_Password), isDisplayed()));
        editText4.perform(replaceText("fddfs"), closeSoftKeyboard());

        ViewInteraction checkBox = onView(
                allOf(withId(R.id.cB_saveCred), withText("Remember Password"), isDisplayed()));
        checkBox.perform(click());

        pressBack();

        ViewInteraction checkBox2 = onView(
                allOf(withId(R.id.cB_startService), withText("Automatically Login"), isDisplayed()));
        checkBox2.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.btn_Login), withText("Log In"), isDisplayed()));
        button.perform(click());

    }

}
