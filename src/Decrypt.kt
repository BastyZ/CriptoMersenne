import kotlin.math.pow

fun decBit(C: String, sk:String, n:Int, h: Int): Boolean? {
    val polarity = when{
        C[0] == '-' -> -1
        else -> 1
    }
    val newC = when{
        C[0] == '-' -> C.subSequence(1,C.length)
        else -> C
    }.toString()
    val D = binaryPoint(newC,sk)
    //Ham(C∙SK) = d
    val d = hammingWeight(D)
    val result = when {
        d <= 2*(h.toFloat().pow(2)) -> false     // d <= 2h
        d >= n - 2*(h.toFloat().pow(2)) -> true  // d >= n - 2h²
        else -> null
    }
    return when {
        polarity < 0 -> when {
            result == true -> false
            result == false -> true
            else -> null
        }
        else -> result
    }
}

internal fun decode (text: String, n: Int, lambda: Int): String {
    val ratio: Int = when {
        n/(lambda)%2 == 0 ->  n/(lambda) - 1
        else -> n/(lambda)
    }
    val blocks = makeBlocks(text, ratio)
    var result = ""
    for (block in blocks) result += mostFrequentOf(block)
    if (result.length > lambda) return result.substring(0,lambda)
    return result
}

fun decBlock (cypherText: Pair<String, String>, sk:String, n: Int, lambda: Int): String {
    val (C1,C2) = cypherText
    // SK∙C1⊕C2
    val args = xorString(binaryPoint(sk,C1),C2)
    // plaintext = D(SK∙C1⊕C2)
    return decode(args,n,lambda)
}