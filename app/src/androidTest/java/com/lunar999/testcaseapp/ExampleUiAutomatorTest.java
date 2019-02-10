package com.lunar999.testcaseapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 18)
public class ExampleUiAutomatorTest {
    private static final String BASIC_SAMPLE_PACKAGE = "com.lunar999.testcaseapp";
    private static final int LAUNCH_TIMEOUT = 5000;
    private static final String STRING_TO_BE_TYPED = "count:0";
    private UiDevice mDevice;

    @Before
    public void startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mDevice.pressHome();
        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mDevice.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void testCase1() {
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "txtHelloWorld")).setText(STRING_TO_BE_TYPED);
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,"btnClick")).click();
//        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE,"btnClick")).click();
        UiObject2 changedText = mDevice.wait(Until.findObject(By.res(BASIC_SAMPLE_PACKAGE, "txtHelloWorld")), 500);
//        assertThat(changedText.getText(), is(equalTo(STRING_TO_BE_TYPED)));
        assertThat(changedText.getText(), is(equalTo("count:0")));
    }

    @Test
    public void testCase2() throws UiObjectNotFoundException {
        mDevice.pressHome();
        UiObject appsTab = mDevice.findObject(new UiSelector().resourceId("com.google.android.apps.nexuslauncher:id/all_apps_handle"));
        appsTab.click();
        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.setAsHorizontalList();
        UiObject settingsApp = appViews.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()), "Settings");
        settingsApp.clickAndWaitForNewWindow();
        UiObject settingsValidation = mDevice.findObject(new UiSelector().packageName("com.android.settings"));
        assertThat(settingsValidation.exists(), equalTo(true));
    }

    private String getLauncherPackageName() {
       return "com.google.android.apps.nexuslauncher";
    }
}
