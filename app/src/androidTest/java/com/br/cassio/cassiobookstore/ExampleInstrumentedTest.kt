package com.br.cassio.cassiobookstore

import android.content.Context
import android.content.res.Configuration
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.UiDevice
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    var itemListRule: ActivityTestRule<ItemListActivity> =
        ActivityTestRule(ItemListActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.br.cassio.cassiobookstore", appContext.packageName)
    }


    /**
     * (by Cassio Ribeiro)
     * Testing especific devices and orientation changes
     * (layout changes) by detecting its kind
     * --------------------------
     * SOMENTE Telemoveis/Handphones em retrato/portrait separam a LISTA dos DETALHES
     * PARA TODOS OS OUTROS CASOS exibe AMBOS! A lista e os detalhes LADO A LADO!
     * --------------------------
     * ATENCAO: Mantenha dispositivo em PORTRAIT antes
     * de realizar os testes E/OU desative mudanca de orientacao no S.O.
     * --------------------------
     * [Desative os 3 efeitos de animacoes nas opcoes de desenvolvedor, de acordo com a documentacao oficial]
     * Animação da janela, Animação de transição e Escala de Animacoes
     * (https://developer.android.com/training/testing/ui-testing/espresso-testing)
     */
    @Test
    fun testOrientationChangesAtMainScreen() {

        // object that makes orientation changes
        val device = UiDevice.getInstance(getInstrumentation())

        // handphone (telemovel)
        if (!isTablet(itemListRule.activity)) {
            // this object make orientation changes during the test
            testHandphoneOrientationChangesAtMainScreenAndCheckLayoutChanges(device)
        } else {
            testOtherCasesOrientationChangesAtMainScreenAndCheckLayoutChanges(device)
        }
    }

    fun testHandphoneOrientationChangesAtMainScreenAndCheckLayoutChanges(device: UiDevice) {

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
        onView(withId(R.id.item_list_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
        onView(withId(R.id.item_detail_container))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

        // checking landscape collapse of the list
        onView(withId(R.id.fab))
            .perform(click())
        onView(withId(R.id.item_list))
            .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))

        // back to portrait
        device.setOrientationNatural()
        onView(withId(R.id.fab))
            .check(doesNotExist())
        onView(withId(R.id.item_list_container))
            .check(doesNotExist())
        onView(withId(R.id.item_detail_container))
            .check(doesNotExist())
    }

    private fun testOtherCasesOrientationChangesAtMainScreenAndCheckLayoutChanges(device: UiDevice) {

    }


    /**
     * Determine if the device is a tablet (i.e. it has a large screen).
     *
     * @param context The calling context.
     */
    fun isTablet(context: Context): Boolean {
        return ((context.getResources().getConfiguration().screenLayout
                and Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }


    /**
     * Determine if the device is in landscape or portrait mode. Returns true for portrait, and false
     * for landscape.
     */
    fun isPortrait(context: Context): Boolean {
        return context.getResources().getConfiguration().screenHeightDp > context.getResources()
            .getConfiguration().screenWidthDp
    }
}