import java.math.BigInteger
import java.util.*
import kotlin.random.Random.Default.nextInt


/** Returns the number of 1s (ones) in the binary representation of @sample
 *
 *  @param sample: sample string
 */
internal fun hammingWeight(sample: String): Int {
    return sample.count { bit -> bit == '1' }
}

/** Generates an n-bit binary string of hamming weight h
 *
 */
internal fun stringGen(length: Int, weight: Int): String? {
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

// int(x) on the paper
internal fun intMod(str: String, p: BigInteger): BigInteger {
    return BigInteger(str, 2).mod(p)
}

internal fun int(str: String): BigInteger {
    return BigInteger(str, 2)
}

/**
 *  Seq (x) = x mod p as a n-bit string
 */
internal fun seq(num: BigInteger, p: BigInteger, n: Int): String {
    return num.mod(p).toBitString(n, 2)
}

internal fun BigInteger.toBitString(nbits: Int, radix: Int): String {
    var string = this.toString(radix)
    while (string.length < nbits) string = "0$string"
    return string
}
