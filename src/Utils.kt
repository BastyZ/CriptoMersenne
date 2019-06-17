
/** hammingWheigth :: int
 *  Returns the number of 1s (ones) in the binary representation of @sample
 */
fun hammingWeight(sample: Long) = when {
    sample < 0L -> throw IllegalArgumentException("n must be non-negative")
    else   -> java.lang.Long.bitCount(sample)
}
