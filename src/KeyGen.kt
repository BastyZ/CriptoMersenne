import kotlin.math.pow

// All possible values of N such that 2^N - 1 is a Mersenne number
val possibleN = listOf(
    2, 3, 5, 7, 13, 17, 19, 31, 61, 89, 107, 127, 521, 607, 1279, 2203, 2281, 3217, 4253, 4423, 9689, 9941, 11213,
    19937, 21701, 23209, 44497, 86243, 110503, 132049, 216091, 756839, 859433, 1257787, 1398269, 2976221, 3021377,
    6972593, 13466917, 20996011, 24036583, 25964951, 30402457, 32582657, 37156667, 42643801, 4311260
)

/** Choses an mersenne exponent _n_, such that _(n over h) >= 2^λ_ and _4h² < n <= 16h²_
 *
 * @param lambda security parameter
 */
internal fun mersennePrimeBit(lambda: Int): Int {
    val (n,h) = chooserBit(lambda)
    return 0
}

private fun chooserBit(lambda: Int): Pair<Int, Int> {
    return chooseBit(lambda,0,1)
}

private fun chooseBit(lambda: Int, i: Int, h: Int): Pair<Int, Int> {
    return when {
        conditionsBit(lambda, i, h) -> Pair(i,h)
        possibleN[i] > h            -> chooseBit(lambda, i, h+1)
        else                        -> chooseBit(lambda, i+1, 1)
    }
}

/** Returns True if the conditions to a valid n and h are met for a given lambda
 *
 */
fun conditionsBit(lambda: Int, i: Int, h: Int): Boolean {
    val n = possibleN[i]
    val combinationCond: Boolean = combinations(n,h) >= 2f.pow(lambda).toLong()     //(n h) >= 2^λ
    val minLevelCond: Boolean = 4*h.toFloat().pow(2) < n                        // 4h² < n
    val maxLevelCond: Boolean = n >= 16*h.toFloat().pow(2)                      // n <= 16h²

    return  combinationCond and minLevelCond and maxLevelCond
}
