package com.udacity.gradle.builditbigger;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.udacity.gradle.builditbigger.asynctask.EndpointsAsyncTask;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.concurrent.TimeUnit;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EndpointsAsyncTaskTestUnit{

   @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule(MainActivity.class);


   @Test
   public void testJokeIsNotEmpty() throws Exception {

       EndpointsAsyncTask mServiceTest =  new EndpointsAsyncTask(InstrumentationRegistry.getContext());
       mServiceTest.execute();
       String joke = mServiceTest.get(7, TimeUnit.SECONDS);
       Assert.assertNotNull(joke);
   }

    @Test
    public void testVerifyResponse() {
        onView(withId(R.id.bt_action)).perform(click());
        onView(withId(R.id.joke_textview)).check(matches(isDisplayed()));
    }
}

