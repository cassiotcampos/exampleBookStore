package com.cassio.cassiobookstore


import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.cassio.cassiobookstore.mock.MY_MOCKS_BOOKS_LIST
import com.cassio.cassiobookstore.mock.MockApiCall
import com.cassio.cassiobookstore.view.activity.ItemListActivityBase
import org.hamcrest.core.StringContains.containsString
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * (by Cassio Ribeiro)
 * Testing especific devices and orientation changes
 * (layout changes) by detecting its kind
 * --------------------------
 */
@RunWith(AndroidJUnit4::class)
class ClickMasterDetailTest {

    private final val MILIS_DELAY: Long =
        2000 // increase this for test with animation or slow devices (I recomend 2000)

    @get:Rule
    var itemListRuleBase: ActivityTestRule<ItemListActivityBase> =
        ActivityTestRule(ItemListActivityBase::class.java)

    @Before
    fun shouldMockDataBefore() {
        MockApiCall.mockApiCallBefore()
    }

    @After
    fun shouldMockDataAfter() {
        MockApiCall.mockApiCallAfter()
    }

    @Test
    fun masterDetailSimpleTest() {

        MockApiCall.mockApiCallEnqueue(MY_MOCKS_BOOKS_LIST, 1)
        itemListRuleBase.launchActivity(Intent())

        Thread.sleep(MILIS_DELAY)

        onView(withText("Android Best Title Ever")).perform(click())

        onView(withId(R.id.item_detail))
            .check(matches(withText(containsString("Android is a movement that has transferred data from laptop to hand-held devices like mobiles."))))

        Thread.sleep(MILIS_DELAY)
    }
}