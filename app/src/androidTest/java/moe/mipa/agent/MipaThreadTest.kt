package moe.mipa.agent

import androidx.test.platform.app.InstrumentationRegistry
import moe.mipa.MipaAgent
import org.junit.Before
import org.junit.Test

class MipaThreadTest {

    private lateinit var agent: MipaAgent
    private val initScript = """
            function genArr(size){
                const arr = []
                for(let i=0;i<size;i++){
                    arr.push(i)
                }
                return arr
            }
            function* f(){
                yield* genArr(3)
            }
            const gen = f()
        """

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

    @Test
    fun sleep() {
    }

    @Test
    fun waitUntil() {
        assert(agent.runScript(initScript +
                "waitUntil(()=>{return gen.next().done})") ==
                "sleep(1000) * 3")
        assert(agent.runScript(initScript +
                "waitUntil(()=>{return gen.next().done}, 500)") ==
                "sleep(500) * 3")
    }
}