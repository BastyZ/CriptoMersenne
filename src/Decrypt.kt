import kotlin.math.pow

fun decBit(c: String, sk:String, n:Int, h: Int): Boolean? {
    val d = hammingWeight(c).toFloat()
    return when {
        d <= 2*h.toFloat().pow(2)       -> false    // d <= 2h
        d >= n - 2*h.toFloat().pow(2)   -> true     // d >= n - 2h²
        else -> null
    }
}