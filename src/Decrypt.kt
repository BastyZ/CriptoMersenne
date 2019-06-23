import kotlin.math.pow

fun decBit(c: String, sk:String, n:Int, h: Int): Boolean? {
    val d = hammingWeight(c).toFloat()
    return when {
        d <= 2*h.toFloat().pow(2)       -> false    // d <= 2h
        d >= n - 2*h.toFloat().pow(2)   -> true     // d >= n - 2h²
        else -> null
    }
}

internal fun decode (text: String, n: Int, lambda: Int): String {
    val ratio: Int = when {
        n/(lambda)%2 == 0 ->  n/(lambda) - 1
        else -> n/(lambda)
    }
    val blocks = makeBlocks(text, ratio)
    var result = ""
    for (block in blocks) {
        result += mostFrequentOf(block)
    }
    return result
}

fun decBlock (cypherText: Pair<String, String>, sk:String, n: Int, lambda: Int): String {
    val c1 = aBigInteger(cypherText.first).times(aBigInteger(sk))
    val c2 = aBigInteger(cypherText.second)

    val xor = c1.xor(c2)
    return decode(xor.toBitString(n), n, lambda)
}