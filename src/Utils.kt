import java.math.BigInteger
import java.util.*
import kotlin.random.Random.Default.nextInt


/** Returns the number of 1s (ones) in the binary representation of @sample
 */
fun hammingWeight(sample: Long): Int = when {
    sample < 0L -> throw IllegalArgumentException("n must be non-negative")
    else   -> java.lang.Long.bitCount(sample)
}

fun hammingString(length: Int, weight: Int): String? {
    when {
        weight > length -> return null
        else -> {
            val set = BitSet(length)
            while (set.cardinality() < weight) {
                set.set(nextInt(length))
            }
            var setString = ""
            for (i in 0 until length) {
                setString += when {
                    set.get(i) -> "1"
                    else -> "0"
                }
            }
            return setString}
    }
}

internal fun factorial(num: Int): BigInteger {
    var response = BigInteger("1")
    for (i in 1..num) {
        response = response.times(BigInteger(i.toString()))
    }
    return response
}

internal fun combinations(n: Int, h: Int): BigInteger {
    return factorial(n).div(
        factorial(h).times( factorial(n-h) )
    )
}

fun aBigInteger(str: String): BigInteger {
    return BigInteger(str, 2)
}

internal fun int(str: String, p: BigInteger): BigInteger {
    return BigInteger(str, 2).mod(p)
}

internal fun seq(num: BigInteger): String {
    // TODO: return seq(num)
    return ""
}
