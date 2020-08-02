package com.artyomefimov.pocketdictionary

import android.view.View
import android.widget.CheckBox
import androidx.annotation.NonNull
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import com.artyomefimov.pocketdictionary.model.DictionaryRecord
import com.artyomefimov.pocketdictionary.view.MainActivity
import com.artyomefimov.pocketdictionary.view.adapters.WordListViewHolder
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.OkHttpClient
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
class CommonTestCasesUiTest {
    @get:Rule
    var mainActivityRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    var grantPermissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    lateinit var uiDevice: UiDevice

    @Before
    fun setup() {
        val client = OkHttpClient()
        val resource = OkHttp3IdlingResource.create(OkHttpClient::javaClass.name, client)
        IdlingRegistry.getInstance().register(resource)

        uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun addingWord() {
        val originalWord = "Building"

        addNewWordAndGoToWordList(originalWord)

        onView(withId(R.id.recycler_view_word_list))
            .check(
                matches(
                    containItem(
                        hasDescendant(
                            withText(originalWord)
                        )
                    )
                )
            )
    }

    @Test
    fun searchingBetweenThreeElements() {
        addNewWordAndGoToWordList("Android")
        addNewWordAndGoToWordList("Test")
        addNewWordAndGoToWordList("Testing")

        onView(allOf(withId(R.id.menu_action_search)))
            .perform(click())

        onView(withClassName(`is`(SearchView::class.java.name)))
            .perform(typeSearchQuery("Test"))

        onView(withId(R.id.recycler_view_word_list))
            .check(
                matches(
                    containItem(
                        hasDescendant(
                            withText("Test")
                        )
                    )
                )
            )
        onView(withId(R.id.recycler_view_word_list))
            .check(
                matches(
                    containItem(
                        hasDescendant(
                            withText("Testing")
                        )
                    )
                )
            )
    }

    @Test
    fun addingCustomTranslationAsFavorite() {
        val buildingOriginalWord = "Building"
        val customTranslation = "custom translation"

        addNewWordAndGoToWordList(buildingOriginalWord)

        onView(withId(R.id.recycler_view_word_list))
            .perform(
                actionOnItem<WordListViewHolder<DictionaryRecord>>(
                    hasDescendant(withText(buildingOriginalWord)),
                    click()
                )
            )

        onView(withId(R.id.fab_add_translation))
            .perform(click())

        onView(withId(R.id.translation_edit))
            .perform(typeText(customTranslation))

        onView(withText(android.R.string.ok))
            .perform(click())

        onView(withId(R.id.recycler_view_translations))
            .check(
                matches(
                    containItem(
                        hasDescendant(
                            withText(customTranslation)
                        )
                    )
                )
            )

        onView(withId(R.id.recycler_view_translations))
            .perform(
                actionOnItem<WordListViewHolder<DictionaryRecord>>(
                    hasDescendant(withText(customTranslation)),
                    toggleCheckBox()
                )
            )

        uiDevice.pressBack()

        onView(withId(R.id.recycler_view_word_list))
            .check(
                matches(
                    containItem(
                        hasDescendant(
                            withText(customTranslation)
                        )
                    )
                )
            )
    }

    private fun addNewWordAndGoToWordList(originalWord: String) {
        onView(withId(R.id.fab_new_word))
            .perform(click())

        onView(withId(R.id.original_word_text))
            .perform(typeText(originalWord), closeSoftKeyboard())

        onView(withId(R.id.action_edit))
            .perform(click())

        onView(withText(android.R.string.yes))
            .perform(click())

        uiDevice.pressBack()
    }


    private fun containItem(@NonNull itemMatcher: Matcher<View>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) = Unit

            override fun matchesSafely(view: RecyclerView): Boolean {
                for (i in 0..view.adapter!!.itemCount) {
                    val viewHolder =
                        view.findViewHolderForAdapterPosition(i)
                            ?: // has no item on such position
                            return false
                    if (itemMatcher.matches(viewHolder.itemView))
                        return true
                }
                return false
            }
        }
    }

    private fun typeSearchQuery(query: String): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String = "Change search view quey"

            override fun getConstraints(): Matcher<View> =
                allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))

            override fun perform(uiController: UiController?, view: View?) {
                (view as? SearchView)?.setQuery(query, true)
            }
        }
    }

    private fun toggleCheckBox(): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String = "toggle check box"

            override fun getConstraints(): Matcher<View>? = null

            override fun perform(uiController: UiController?, view: View?) {
                val checkBox = view?.findViewById<CheckBox>(R.id.favorite_translation)
                checkBox?.isChecked = true
            }
        }
    }
}
