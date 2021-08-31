package moe.mipa.agent

import androidx.test.platform.app.InstrumentationRegistry
import moe.mipa.MipaAgent
import org.junit.Before
import org.junit.Test

class MipaActionTest {

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
    fun tap() {
        assert(agent.runScript("tap(300,400)") == "tap(300,400,1000)")
        assert(agent.runScript("tap(300,400,10)") == "tap(300,400,10)")
    }

    @Test
    fun tapUntil() {
        assert(agent.runScript(initScript + """
            tapUntil(()=>{return gen.next().done},300,400)
        """) == "tap(300,400,1000)x3"
        )
    }

    @Test
    fun tapAfter() {
        assert(agent.runScript(initScript + """
            tapAfter(()=>{return gen.next().done},300,400)
        """) == "tap(300,400,0)...3"
        )
    }

    @Test
    fun swipe() {
    }

    @Test
    fun launch() {
    }

    @Test
    fun text() {
    }
}