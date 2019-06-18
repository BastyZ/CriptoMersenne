
import java.math.BigInteger
import java.util.*


/** Returns the number of 1s (ones) in the binary representation of @sample
 */
fun hammingWeight(sample: Long): Int = when {
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

fun toBitset(bi: BigInteger): BitSet {
    val bia = bi.toByteArray()
    val l = bia.size
    val bsa = ByteArray(l + 1)
    System.arraycopy(bia, 0, bsa, 0, l)
    bsa[l] = 0x01
    return BitSet.valueOf(bsa)
}

fun toBigInteger(bs: BitSet): BigInteger {
    val bsa = bs.toByteArray()
    val l = bsa.size - 0x01
    val bia = ByteArray(l)
    System.arraycopy(bsa, 0, bia, 0, l)
    return BigInteger(bia)
}
