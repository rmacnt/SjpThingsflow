package com.sjp.framework.widget.utils

private const val tag = "ReflectionExtention"

fun <T : Any> T.invokeDeclaredMethodsAnnotation(
    annotation: Class<out Annotation>,
    vararg param: Any?
): Any? {
    try {
        return this::class.java.declaredMethods
            .find { method -> method.getAnnotation(annotation) != null }
            ?.invoke(this, *param)
    } catch (e: java.lang.Exception) {
    }
    return null
}