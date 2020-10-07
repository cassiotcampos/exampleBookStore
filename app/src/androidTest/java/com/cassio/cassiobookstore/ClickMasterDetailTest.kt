package com.br.cassio.cassiobookstore


import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.cassio.cassiobookstore.R
import com.cassio.cassiobookstore.mock.MY_MOCKS_BOOKS_LIST
import com.cassio.cassiobookstore.mock.mockApiCallAfter
import com.cassio.cassiobookstore.mock.mockApiCallBefore
import com.cassio.cassiobookstore.mock.mockApiCallEnqueue
import com.cassio.cassiobookstore.view.ItemListActivity
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

    private final val MILIS_DELAY: Long = 2000 // increase this for test with animation or slow devices (I recomend 2000)
    private final val USE_MOCK_DATA: Boolean = true // change this to test with API or MockData

    @get:Rule
    var itemListRule: ActivityTestRule<ItemListActivity> =
        ActivityTestRule(ItemListActivity::class.java)

    @Before
    fun shouldMockDataBefore() {
        if (USE_MOCK_DATA)
            mockApiCallBefore()
    }

    @After
    fun shouldMockDataAfter() {
        if (USE_MOCK_DATA)
            mockApiCallAfter()
    }

    @Test
    fun masterDetailSimpleTest() {

        if (USE_MOCK_DATA) {
            mockApiCallEnqueue(MY_MOCKS_BOOKS_LIST, 0)
            itemListRule.launchActivity(Intent())
        }

        Thread.sleep(2000)

        onView(withText("Android Best Title Ever")).perform(click())

        onView(withId(R.id.item_detail))
            .check(matches(withText(containsString("Android is a movement that has transferred data from laptop to hand-held devices like mobiles."))))

        Thread.sleep(5000)
    }
}