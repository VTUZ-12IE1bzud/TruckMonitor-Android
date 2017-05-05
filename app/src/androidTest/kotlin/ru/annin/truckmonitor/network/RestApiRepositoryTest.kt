package ru.annin.truckmonitor.network

import android.support.test.runner.AndroidJUnit4
import junit.framework.TestCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import ru.annin.truckmonitor.data.repository.RestApiRepository
import ru.annin.truckmonitor.domain.model.LoginResponse
import rx.observers.TestSubscriber

/**
 * Тест REST API.
 *
 * @author Pavel Annin.
 */
@RunWith(AndroidJUnit4::class)
class RestApiRepositoryTest : TestCase() {

    companion object {
        lateinit var repository: RestApiRepository

        @JvmStatic
        @BeforeClass
        fun setUpClass() {
            repository = RestApiRepository
        }
    }

    @Before
    public override fun setUp() {
        super.setUp()
    }

    @After
    public override fun tearDown() {
        super.tearDown()
    }

    @Test
    fun testLogin() {
        val subscriber: TestSubscriber<LoginResponse> = TestSubscriber()

        val email = "annin@truck.ru"
        val password = "annin"

        repository.login(email, password).subscribe(subscriber)

        // Request
        subscriber.assertNoErrors()
        subscriber.assertCompleted()
        assertTrue(subscriber.onNextEvents.isNotEmpty())

        // Data
        val data = subscriber.onNextEvents[0]
        assertThat(data, notNullValue())
    }
}