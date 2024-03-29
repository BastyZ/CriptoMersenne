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
                    set.get(i)  -> "1"
                    else        -> "0"
                }
            }
            return setString}
    }
}

internal fun factorial(n: BigInteger): BigInteger = when{
    n < BigInteger("0") -> throw IllegalArgumentException("No negative")
    else -> {
        var ans = BigInteger("1")
        // for (i in 2 until n.toInt()) ans = ans.times(BigInteger(i.toString()))
        var i = BigInteger("2")
        while (i < n) {
            ans = ans.times(i)
            i = i.inc()
        }

        ans
    }
}

internal fun combinations(n: Int, h: Int): BigInteger = when {
    n<0 || h<0 -> throw IllegalArgumentException("n or h are negatives")
    n == h -> BigInteger("1")
    else -> {
        var ans = BigInteger("1")
        for (i in n-h+1..n) ans = ans.times(BigInteger(i.toString()))
        ans.divide(factorial(h.toBigInteger()))
    }
}

fun toOperableString(str: String): BigInteger {
    return BigInteger(str, 2)
}

// int(x) on the paper
internal fun intMod(str: String, p: BigInteger): BigInteger {
    return BigInteger(str, 2).mod(p)
}

/**
 *  Seq (x) = x mod p as a n-bit string
 */
internal fun seq(num: BigInteger, p: BigInteger, n: Int): String {
    var se = num.mod(p).toBitString(n)
    return se
}

internal fun BigInteger.toBitString(nbits: Int): String {
    val result: String
    when {
        this < BigInteger("0") -> {
            result = "-" + this.times(BigInteger("-1")).toBitString(nbits)
        }
        else -> {
            var string = this.toString(2)
            while (string.length < nbits) string = "0$string"
            result = string
        }
    }
    return result
}

internal fun makeBlocks (text: String, ratio: Int): List<String> {
    val result = emptyList<String>().toMutableList()
    for (index in 0 until text.length - 1 step ratio) {
        val end =  when {
            index + ratio > text.length -> text.length
            else                        -> index + ratio
        }
        result += text.substring(index, end)
    }
    return result
}

internal fun mostFrequentOf (text: String): Char {
    var countZeros = 0
    for (char in text) {
        if (char == '0') {
            countZeros++
        }
    }
    return when {
        countZeros < text.length/2 -> '1'
        else -> '0'
    }
}

//OPERADORES CON STRINGS
//Producto punto X ∙ Y
fun binaryPoint(X:String, Y:String): String {
    when {
        X.length != Y.length -> throw IllegalArgumentException("Producto punto de vectores con distinto largo")
        else -> {
            var result = ""
            for (i in 0 until X.length) {
                val Z = when {
                    X[i] == '1' && Y[i] == '1' -> "1"
                    else -> "0"
                }
                result += Z
            }
            return result
        }
    }
}

fun invString(X:String): String {
    var result =""
    for (i in 0 until X.length){
        val y =when {
            X[i] == '0' -> '1'
            X[i] == '-' -> '-'
            else -> '0'
        }
        result += y
    }
    return result
}

fun shiftString(X:String, pase:Int, left:Boolean): String {
    var result = ""
    if (left){
        for (i in pase until X.length){
            result += X[i]
        }
        for (i in 0 until pase){
            result += '0'
        }
    }
    else{
        for (i in 0 until pase){
            result += '0'
        }
        for (i in 0 until (X.length-pase)){
            result += X[i]
        }
    }
    return result
}

fun sumStrings(X:String, Y:String): String {
    when {
        X.length != Y.length -> throw IllegalArgumentException("Suma de vectores con distinto largo")
        else -> {
            var result = ""
            var carry = '0'
            for (i in 0 until X.length) {
                val sum = charSum(X[i], Y[i], carry)
                carry = sum.second
                result += sum.first
            }
            return result
        }
    }
}

fun charSum(A:Char, B:Char, carry:Char): Pair<Char,Char> {
    return when(carry) {
        '1' -> {
            when {
                A == '0' &&  B == '0' -> Pair('1','0')
                A == '0' &&  B == '1' -> Pair('1','1')
                A == '1' &&  B == '0' -> Pair('1','1')
                else -> Pair('0','1')
            }
        }
        else -> {
            when {
                A == '0' &&  B == '0' -> Pair('0','0')
                A == '0' &&  B == '1' -> Pair('1','0')
                A == '1' &&  B == '0' -> Pair('1','0')
                else -> Pair('1','1')
            }
        }
    }
}

fun xorString(X:String, Y:String): String {
    when {
        X.length != Y.length -> throw IllegalArgumentException("XOR de vectores con distinto largo")
        else -> {
            var result = ""
            for (i in 0 until X.length) {
                val ans = when {
                    X[i] == '0' && Y[i] == '0' -> '0'
                    X[i] == '0' && Y[i] == '1' -> '1'
                    X[i] == '1' && Y[i] == '0' -> '1'
                    else -> '0'
                }
                result += ans
            }
            return result
        }
    }
}