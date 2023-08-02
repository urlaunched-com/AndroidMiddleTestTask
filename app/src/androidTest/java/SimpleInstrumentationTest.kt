import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.youarelaunched.challenge.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SimpleInstrumentationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun vendorsListDisplayed() {
        composeTestRule.onNode(hasTestTag("VENDORS_SEARCH_FIELD_TEST_TAG")).assertIsDisplayed()

        composeTestRule.onNodeWithText("Search…").assertIsDisplayed()

        composeTestRule.onNode(hasTestTag("VENDORS_LIST_TEST_TAG")).assertIsDisplayed()
    }

    @Test
    fun emptyScreenDisplayed() {
        composeTestRule.onNode(hasTestTag("VENDORS_SEARCH_FIELD_TEST_TAG")).performTextInput("test")
        composeTestRule.onNodeWithContentDescription("Search Icon").performClick()

        composeTestRule.onNodeWithText("Sorry! No results found…").assertIsDisplayed()

        composeTestRule.onNodeWithText("Please try a different search request or browse businesses from the list")
            .assertIsDisplayed()

        composeTestRule.onNode(hasTestTag("VENDORS_EMPTY_SCREEN_TEST_TAG")).assertIsDisplayed()

    }

    @Test
    fun searchResultDisplayed() {
        composeTestRule.onNode(hasTestTag("VENDORS_SEARCH_FIELD_TEST_TAG")).performTextInput("cat")
        composeTestRule.onNodeWithContentDescription("Search Icon").performClick()

        composeTestRule.onNode(hasTestTag("VENDORS_LIST_TEST_TAG")).assertIsDisplayed()
    }
}
