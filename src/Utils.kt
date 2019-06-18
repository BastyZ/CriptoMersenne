
/** hammingWheigth :: int
 *  Returns the number of 1s (ones) in the binary representation of @sample
 */
fun hammingWeight(sample: Long) = when {
    sample < 0L -> throw IllegalArgumentException("n must be non-negative")
    else   -> java.lang.Long.bitCount(sample)
}

internal fun factorial(num: Int): Long {
    var response: Long = 1
    for (i in 1..num) {
        response *= i.toLong()
    }
    return response
}

internal fun combinations(n: Int, h: Int): Long {
    return factorial(n).div(factorial(h) * factorial(n-h))
}
