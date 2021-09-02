package moe.mipa.agent

import androidx.test.platform.app.InstrumentationRegistry
import moe.mipa.MipaAgent
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.system.measureTimeMillis

class MipaActionTest {

    private lateinit var agent: MipaAgent
    private val ctx = InstrumentationRegistry.getInstrumentation().context
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
    }

    @After
    fun after() {
    }

    @Test
    fun tap() {
        // base test
        agent = MipaAgent(ctx, listOf(MipaConsole::class.java, MipaAction::class.java))
        var res: String
        val duration = measureTimeMillis {
            res = agent.runScript("tap(200,300)") as String
        }

        // duration test
        assert(duration in 1000..2000)

        // position range test
        val (x, y) = res.split(",")
        assert(x.toInt() in 195..205 && y.toInt() in 295..305)
    }

    @Test
    fun tapFullArgs() {

        // full argument test
        agent = MipaAgent(ctx, listOf(MipaConsole::class.java, MipaAction::class.java))
        var res: String
        val duration = measureTimeMillis {
            res = agent.runScript("tap([200,300],2000,1000,10)") as String
        }
        // duration test
        assert(duration in 3000..4000)

        // position range test
        val (x, y) = res.split(",")
        assert(x.toInt() in 190..210 && y.toInt() in 290..310)
    }

    @Test
    fun tapUntil() {
        agent = MipaAgent(ctx, listOf(MipaAction::class.java, MipaConsole::class.java))
        agent.lazyInitScripts = initScript + """
function tapUntil() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        tap.apply(this,args)
    }
}
        """.trimIndent()

        val str = """
            tapUntil(()=>{return gen.next().done},100,200)
        """.trimIndent()
        val duration = measureTimeMillis {
            agent.runScript(str)
        }
        assert(duration in 3000..4000)
    }

    @Test
    fun tapAfter() {
        agent = MipaAgent(
            ctx,
            listOf(MipaAction::class.java, MipaConsole::class.java, MipaThread::class.java)
        )
        agent.lazyInitScripts = initScript + """
function tapAfter() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        sleep(1000)
    }
    tap.apply(this,args)
}
        """.trimIndent()
        val str = """
            tapAfter(()=>{return gen.next().done},100,200)
        """.trimIndent()
        val duration = measureTimeMillis {
            agent.runScript(str)
        }
        assert(duration in 4000..5000)
    }

    @Test
    fun swipe() {
        agent = MipaAgent(ctx, listOf(MipaConsole::class.java, MipaAction::class.java))
        val duration = measureTimeMillis {
            agent.runScript("swipe([200,300],[400,500],2000)") as String
        }

        // duration test
        assert(duration in 2000..3000)
    }


    @Test
    fun swipeAfter() {
        agent = MipaAgent(
            ctx,
            listOf(MipaAction::class.java, MipaConsole::class.java, MipaThread::class.java)
        )
        agent.lazyInitScripts = initScript + """
function swipeAfter() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        sleep(1000)
    }
    swipe.apply(this,args)
}
        """.trimIndent()
        val str = """
            swipeAfter(()=>{return gen.next().done},[200,200],[300,300])
        """.trimIndent()
        val duration = measureTimeMillis {
            agent.runScript(str)
        }
        assert(duration in 4000..5000)
    }

    @Test
    fun swipeUntil() {
        agent = MipaAgent(ctx, listOf(MipaAction::class.java, MipaConsole::class.java))
        agent.lazyInitScripts = initScript + """
function swipeUntil() {
    const args = []
    for(const i of arguments){
        args.push(i)
    }
    foo = args.shift()
    while(!foo()) {
        swipe.apply(this,args)
    }
}
        """.trimIndent()

        val str = """
            swipeUntil(()=>{return gen.next().done},[200,200],[300,300])
        """.trimIndent()
        val duration = measureTimeMillis {
            agent.runScript(str)
        }
        assert(duration in 3000..4000)
    }

}