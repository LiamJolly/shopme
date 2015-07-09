package com.liamdjolly.shopme;

import android.app.KeyguardManager;
import android.test.ActivityInstrumentationTestCase2;
import com.robotium.solo.Solo;

public class TestMainActivity extends ActivityInstrumentationTestCase2<MainActivity> {
  private Solo solo;

  public TestMainActivity() {
    super(MainActivity.class);
  }

  protected void setUp() throws Exception {
    solo = new Solo(getInstrumentation(), getActivity());

    // Nasty hack to allow the screen to be unlocked when running unit test
    // See http://developer.android.com/tools/testing/activity_testing.html#UnlockDevice for more info
    KeyguardManager mKeyGuardManager = (KeyguardManager) getActivity().getSystemService(getActivity().KEYGUARD_SERVICE);
    KeyguardManager.KeyguardLock mLock = mKeyGuardManager.newKeyguardLock("activity_classname");
    mLock.disableKeyguard();
  }

  @Override
  public void tearDown() throws Exception {
    solo.finishOpenedActivities();
  }

  public void testSimpleAdd() {
    solo.waitForActivity(MainActivity.class);
    solo.clickOnButton("New");

    assertTrue("Dialog displayed.", solo.searchText("List name"));

    int id = R.id.edittext_newListDialog;

    solo.enterText(id, "New list");
    solo.clickOnButton("Ok");

  }
}
