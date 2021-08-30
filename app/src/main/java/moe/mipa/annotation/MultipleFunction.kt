package moe.mipa.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MultipleFunction(val size: Int = 0)