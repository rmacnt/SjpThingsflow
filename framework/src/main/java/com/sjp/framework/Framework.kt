package com.sjp.framework


fun Long?.nonnull(): Long {
    if (this != null) {
        return this
    }
    return 0
}


fun Boolean?.nonnull(): Boolean {
    if (this != null) {
        return this
    }
    return false
}

fun Int?.nonnull(): Int {
    if (this != null) {
        return this
    }
    return 0
}

fun Int?.nonnull(value: Int): Int {
    if (this != null) {
        return this
    }
    return value
}

fun Float?.nonnull(): Float {
    if (this != null) {
        return this
    }
    return 0f
}

fun Float?.nonnull(value: Float): Float {
    if (this != null) {
        return this
    }
    return value
}

fun Double?.nonnull(): Double {
    if (this != null) {
        return this
    }
    return 0.0
}


fun String?.nonnull(): String {
    return this.nonnull("")
}

fun String?.nonnull(default: String): String {
    if (this.isNullOrEmpty() == false) {
        return this
    }
    return default
}


fun <T> List<T>.findIndex(compare: ((value: T) -> Boolean)): Int {
    this.forEachIndexed { index, t ->
        if (compare(t) == true) {
            return index
        }
    }
    return -1
}

fun <T> List<T>.removeIndex(compare: ((value: T) -> Boolean)): Int {
    var result = -1
    this.forEachIndexed { index, t ->
        if (compare(t) == true) {
            result = index
            return@forEachIndexed
        }
    }
    if (result != -1) {
        if (this is ArrayList<T>) {
            this.removeAt(result)
        }
        return result
    }
    return -1
}

fun <T> List<T>.replace(compare: ((value: T) -> Boolean), dst: T): Int {
    var result = -1
    this.forEachIndexed { index, t ->
        if (compare(t) == true) {
            result = index
            if (this is ArrayList<T>) {
                this.removeAt(index)
                this.add(index, dst)
            }
            return@forEachIndexed
        }
    }
    return result
}

fun <T> ArrayList<T>.replace(compare: ((value: T) -> Boolean), dst: T): Int {
    var findIndex = -1
    this.forEachIndexed { index, t ->
        if (compare(t) == true) {
            findIndex = index
            return@forEachIndexed
        }
    }
    if (findIndex > -1) {
        this.removeAt(findIndex)
        this.add(findIndex, dst)
    }
    return findIndex
}

fun <T> List<T>.replaceItem(newItem: T, target: (T) -> Boolean) = map {
    if (target(it)) {
        newItem
    } else {
        it
    }
}

fun String?.parseDouble(replaceValue: String? = null): Double {
    try {
        return this?.replace(replaceValue.orEmpty(), "")?.toDouble().nonnull()
    } catch (e: Exception) {
        // Nothing
    }
    return 0.0
}

fun String?.parseInt(replaceValue: String? = null): Int {
    try {
        return this?.replace(replaceValue.orEmpty(), "")?.toInt().nonnull()
    } catch (e: Exception) {
        // Nothing
    }
    return 0
}


//======================================================================
// Collection
//======================================================================

fun <T> Collection<T>?.isUse(): Boolean {
    if (this != null && this.isNotEmpty()) {
        return true
    }
    return false
}
