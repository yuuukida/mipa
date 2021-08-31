package moe.mipa

import android.content.Context
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.utils.MemoryManager
import io.alicorn.v8.V8JavaAdapter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import moe.mipa.annotation.GlobalFunction
import moe.mipa.annotation.OverloadFunction
import java.io.BufferedReader
import java.io.InputStreamReader


class MipaAgent(
    private val ctx: Context,
    private val clzList: List<Class<*>>,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

) {
    private var initScripts = ""
    private fun getScriptString(): String {
        val buff = BufferedReader(InputStreamReader(ctx.assets.open("index.js")))
        var str = ""
        buff.readLines().forEach { str += "$it\n" }
        return str
    }


    fun runScript(str: String): Any? {
        val runtime = V8.createV8Runtime()
        val mm = MemoryManager(runtime)
        injectAsClassFunction(clzList, runtime)
        runtime.executeScript(initScripts)
        val res = runtime.executeScript(str)
        mm.release()
        runtime.close()
        return res
    }

    suspend fun runScript() {
        withContext(defaultDispatcher) {
            val runtime = V8.createV8Runtime()
            val mm = MemoryManager(runtime)
            injectAsClassFunction(clzList, runtime)
            runtime.executeScript(initScripts)
            runtime.executeScript(getScriptString())
            mm.release()
            runtime.close()
        }
    }

    /**
     * Scan & inject special function;
     * 1. global function [GlobalFunction]
     *      foo(args)
     * 2. overload function [OverloadFunction]
     */
    private fun scanAndInjectGlobalFunction(obj: Any, clz: Class<*>, runtime: V8) {
        clz.declaredMethods.forEach { m ->
            run {
                var isGlobal = false
                if (m.isAnnotationPresent(GlobalFunction::class.java)) {
                    runtime.registerJavaMethod(obj, m.name, m.name, m.parameterTypes)
                    isGlobal = true
                }
                if (m.isAnnotationPresent(OverloadFunction::class.java)) {
                    val clzName = clz.simpleName.lowercase().substring(4)
                    var nullConfig = ""
                    fun getNullJsParams(count: Int): String {
                        var jsParams = ""
                        for (i in 0 until m.parameterTypes.count()) {
                            jsParams += if (count > i) {
                                "arg${i},"
                            } else {
                                "null,"
                            }
                        }
                        return jsParams
                    }

                    var overloadMethodName = "${clzName}.${m.name}Overload"
                    var methodName = "${clzName}.${m.name}"
                    if (isGlobal) {
                        methodName = "${m.name} = ${clzName}.${m.name}"
                        overloadMethodName = "${m.name}Overload"
                    }
                    for (i in 0 until m.parameterTypes.count()) {
                        nullConfig += "if (!arg${i} )  return $overloadMethodName(${
                            getNullJsParams(i)
                        })\n"
                    }
                    nullConfig += "return $overloadMethodName(${getNullJsParams(m.parameterTypes.count())})"
                    initScripts += """
                       $methodName = function(${getNullJsParams(m.parameterTypes.count())}) {
                            $nullConfig
                        }
                    """
                }
            }
        }

    }

    /**
     * Inject java class to runtime as class function;
     * Call function like:
     *      class.foo(args)  ;
     */
    private fun injectAsClassFunction(clzList: List<Class<*>>, runtime: V8) {
        /**
         * if constructor with single Context argument exists
         *      inject clz(ctx) to runtime
         * else
         *      inject clz() to runtime
         */
        for (clz in clzList) {
            try {
                val obj = clz.getConstructor(Context::class.java).newInstance(ctx)
                V8JavaAdapter.injectObject(
                    clz.simpleName.lowercase().substring(4),
                    clz.getConstructor(Context::class.java).newInstance(ctx),
                    runtime
                )
                scanAndInjectGlobalFunction(obj, clz, runtime)
            } catch (e: NoSuchMethodException) {
                val obj = clz.newInstance()
                V8JavaAdapter.injectObject(
                    clz.simpleName.lowercase().substring(4),
                    obj,
                    runtime
                )
                scanAndInjectGlobalFunction(obj, clz, runtime)
            }
        }
    }


}