package com.cassio.cassiobookstore

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import com.cassio.cassiobookstore.mock.MY_MOCKS_BOOKS_LIST
import com.cassio.cassiobookstore.mock.MockApiCall
import com.cassio.cassiobookstore.view.ItemListActivity
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
 * Funcionamento desejado do APP: SOMENTE Telemoveis/Handphones EM POSICAO retrato/portrait SEPARAM a LISTA dos DETALHES E
 * PARA OUTROS CASOS (tablets em qualquer posicao/orientacao) exibe AMBOS! A lista e os detalhes LADO A LADO!
 * --------------------------
 * [Para testes sem delay (delay 0), DESATIVE os 3 efeitos de animacoes nas opcoes de desenvolvedor, de acordo com a documentacao oficial]
 * (https://developer.android.com/training/testing/ui-testing/espresso-testing)
 * observacao: Em emuladores ou telemoveis rapidos NAO é necessario DESATIVAR
 * basta meter um tempo maior de delay entre as animacoes e mudancas de orientacao, layout, etc (1500 por exemplo)
 *
 */
@RunWith(AndroidJUnit4::class)
class OrientationChangeAndLayoutTest {

    private final val MILIS_DELAY: Long = 1500 // increase this for test with animation or slow devices (I recommend 1500)
    private final val USE_MOCK_DATA: Boolean = true // change this to test with API or MockData

    // makes orientation changes during the tests
    private val device = UiDevice.getInstance(getInstrumentation())

    @get:Rule
    var itemListRule: ActivityTestRule<ItemListActivity> =
        ActivityTestRule(ItemListActivity::class.java)


    @Before
    fun shouldMockDataBefore() {
        if (USE_MOCK_DATA)
            MockApiCall.mockApiCallBefore()
    }

    @After
    fun shouldMockDataAfter() {
        if (USE_MOCK_DATA)
            MockApiCall.mockApiCallAfter()
    }

    @Test
    fun testOrientationChangesAtMainScreen() {
        
        if (USE_MOCK_DATA) {
            MockApiCall.mockApiCallEnqueue(MY_MOCKS_BOOKS_LIST, 50)
            itemListRule.launchActivity(Intent())
        }

        device.setOrientationNatural()
        Thread.sleep(MILIS_DELAY)

        // handphone (telemovel)
        if (!isTablet(itemListRule.activity)) {
            // this object make orientation changes during the test
            testHandphoneOrientationChangesAtMainScreenAndCheckLayoutChanges()
        } else {
            testTabletsOrientationChangesAtMainScreenAndCheckLayoutChanges()
        }

        Thread.sleep(5000)
    }


    fun testHandphoneOrientationChangesAtMainScreenAndCheckLayoutChanges() {

        // Handphone Portrait
        onView(withId(R.id.fab))
            .check(doesNotExist())
        onView(withId(R.id.item_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        // these views only exists after change to landscape
        onView(withId(R.id.item_list_container))
            .check(doesNotExist())
        onView(withId(R.id.item_detail_container))
            .check(doesNotExist())

        // change to landscape
        device.setOrientationLeft()
        Thread.sleep(MILIS_DELAY)

        onView(withId(R.id.item_list_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_detail_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        // checking landscape collapse of the list
        onView(withId(R.id.fab))
            .perform(click())
        Thread.sleep(MILIS_DELAY)
        onView(withId(R.id.item_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        // back to portrait
        device.setOrientationNatural()

        Thread.sleep(MILIS_DELAY)

        onView(withId(R.id.fab))
            .check(doesNotExist())
        onView(withId(R.id.item_list_container))
            .check(doesNotExist())
        onView(withId(R.id.item_detail_container))
            .check(doesNotExist())

    }

    private fun testTabletsOrientationChangesAtMainScreenAndCheckLayoutChanges() {


        onView(withId(R.id.fab))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        onView(withId(R.id.item_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_list_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_list_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_detail_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        device.setOrientationLeft()
        Thread.sleep(MILIS_DELAY)

        // checking landscape collapse of the list
        onView(withId(R.id.fab))
            .perform(click())
        Thread.sleep(MILIS_DELAY)

        onView(withId(R.id.item_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.item_list_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
        onView(withId(R.id.item_detail_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        device.setOrientationNatural()
        Thread.sleep(MILIS_DELAY)

        onView(withId(R.id.item_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_list_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_detail_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }


    fun isTablet(context: Context): Boolean {
        return ((context.getResources().getConfiguration().screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    fun isPortrait(context: Context): Boolean {
        return context.getResources().getConfiguration().screenHeightDp > context.getResources()
            .getConfiguration().screenWidthDp
    }
}