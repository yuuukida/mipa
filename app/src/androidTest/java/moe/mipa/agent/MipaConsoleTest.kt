package moe.mipa.agent

import androidx.test.platform.app.InstrumentationRegistry
import moe.mipa.MipaAgent
import org.junit.Before
import org.junit.Test

class MipaConsoleTest {
    lateinit var agent: MipaAgent
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
    private fun commomTest(foo:String) {
        // multiple argument test
        assert(agent.runScript("$foo('p1')")=="p1")
        assert(agent.runScript("$foo('p1','p2')")=="p1p2")
        assert(agent.runScript("$foo('p1','p2','p3')")=="p1p2p3")
    }

    @Test
    fun log() {
        commomTest("console.log")
        commomTest("log")
    }
    @Test
    fun debug() {
        commomTest("console.debug")
        commomTest("debug")
    }
    @Test
    fun info() {
        commomTest("console.info")
        commomTest("info")
    }
    @Test
    fun warn() {
        commomTest("console.warn")
        commomTest("warn")
    }
    @Test
    fun error() {
        commomTest("console.error")
        commomTest("error")
    }
}