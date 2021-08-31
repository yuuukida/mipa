package moe.mipa.agent

import androidx.test.platform.app.InstrumentationRegistry
import moe.mipa.MipaAgent
import org.junit.Before
import org.junit.Test

class MipaConsoleTest {
    private lateinit var agent: MipaAgent
    @Before
    fun before() {
        agent = MipaAgent(
            ctx = InstrumentationRegistry.getInstrumentation().context,
            clzList = listOf(
                MipaConsole::class.java,
                MipaScreen::class.java,
                MipaThread::class.java,
                MipaAction::class.java,
            )
        )
    }
    private fun commonTest(foo:String) {
        // multiple argument test
        assert(agent.runScript("$foo('p1')")=="p1")
        assert(agent.runScript("$foo('p1','p2')")=="p1p2")
        assert(agent.runScript("$foo('p1','p2','p3')")=="p1p2p3")
    }

    @Test
    fun log() {
        commonTest("console.log")
        commonTest("log")
    }
    @Test
    fun debug() {
        commonTest("console.debug")
        commonTest("debug")
    }
    @Test
    fun info() {
        commonTest("console.info")
        commonTest("info")
    }
    @Test
    fun warn() {
        commonTest("console.warn")
        commonTest("warn")
    }
    @Test
    fun error() {
        commonTest("console.error")
        commonTest("error")
    }
}