import com.youarelaunched.challenge.data.repository.VendorsRepository
import com.youarelaunched.challenge.data.repository.model.Vendor
import com.youarelaunched.challenge.ui.screen.view.VendorsVM
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SimpleTest {

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    @Test
    fun `Vendor Response Successful`() = runBlocking {
        val viewModel = VendorsVM(MockData())
        Assert.assertEquals(viewModel.fetchVendors().isNotEmpty(), true)
        Assert.assertEquals(viewModel.fetchVendors()[0].companyName, "testName")
        Assert.assertEquals(viewModel.fetchVendors()[0].coverPhoto, "testPhoto")
        Assert.assertEquals(viewModel.fetchVendors()[0].area, "testArea")
        Assert.assertEquals(viewModel.fetchVendors()[0].tags, "testTag")
    }

    @Test
    fun `Vendor Response By Company Name Successful`() = runBlocking {
        val viewModel = VendorsVM(MockData())
        viewModel.onTextChange("testNameByName")
        Assert.assertEquals(viewModel.fetchVendors().isNotEmpty(), true)
        Assert.assertEquals(viewModel.fetchVendors()[0].companyName, "testNameByName")
        Assert.assertEquals(viewModel.fetchVendors()[0].coverPhoto, "testPhotoByName")
        Assert.assertEquals(viewModel.fetchVendors()[0].area, "testAreaByName")
        Assert.assertEquals(viewModel.fetchVendors()[0].tags, "testTagByName")
    }

    //if Vendor Response By Company Name  Empty we will show the vendors list
    @Test
    fun `Vendor Response By Company Name  Empty`() = runTest {
        val viewModel = VendorsVM(MockData())
        viewModel.onTextChange("")
        Assert.assertEquals(viewModel.fetchVendors().isNotEmpty(), true)
        Assert.assertEquals(viewModel.fetchVendors()[0].companyName, "testName")
        Assert.assertEquals(viewModel.fetchVendors()[0].coverPhoto, "testPhoto")
        Assert.assertEquals(viewModel.fetchVendors()[0].area, "testArea")
        Assert.assertEquals(viewModel.fetchVendors()[0].tags, "testTag")
    }
}

class MockData : VendorsRepository {

    override suspend fun getVendorsByCompanyName(companyName: String): List<Vendor> {
        return listOf(
            Vendor(
                id = 1,
                companyName = "testNameByName",
                coverPhoto = "testPhotoByName",
                area = "testAreaByName",
                favorite = true,
                categories = null,
                tags = "testTagByName"
            ),
            Vendor(
                id = 2,
                companyName = "testNameByName2",
                coverPhoto = "testPhotoByName2",
                area = "testAreaByName2",
                favorite = true,
                categories = null,
                tags = "testTagByName2"
            )
        )
    }

    override suspend fun getVendors(): List<Vendor> {
        return listOf(
            Vendor(
                id = 1,
                companyName = "testName",
                coverPhoto = "testPhoto",
                area = "testArea",
                favorite = true,
                categories = null,
                tags = "testTag"
            )
        )
    }
}

@ExperimentalCoroutinesApi
class MainCoroutineRule(
    private val dispatcher: CoroutineDispatcher = StandardTestDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}