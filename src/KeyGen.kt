import javafx.scene.text.FontWeight
import java.math.BigInteger
import kotlin.math.pow
import kotlin.random.Random.Default.nextInt

// All possible values of N such that 2^N - 1 is a Mersenne number
val possibleN = listOf(
    2, 3, 5, 7, 13, 17, 19, 31, 61, 89, 107, 127, 521, 607, 1279, 2203, 2281, 3217, 4253, 4423, 9689, 9941, 11213,
    19937, 21701, 23209, 44497, 86243, 110503, 132049, 216091, 756839, 859433, 1257787, 1398269, 2976221, 3021377,
    6972593, 13466917, 20996011, 24036583, 25964951, 30402457, 32582657, 37156667, 42643801, 4311260
)

// String while we lack of a better representation
fun keyGenBit(n: Int, h: Int): Pair<String, String> {
    //Choose a mersenne prime p and integer h
    val p = 2.toBigInteger().pow(n).minus(BigInteger.ONE)
    //Choose F,G n-bit string of HammingWeigth h
    val F = stringGen(n,h)!!
    val G = stringGen(n,h)!!
    // return pk:= H = seq( int(F) / int(G) ) and sk:= G
    val pk = seq(intMod(F,p).div(intMod(G,p)),p,n)
    val sk = G
    return Pair(pk,sk)
}

fun keyGenBlock(n: Int, h: Int): Pair<Pair<String, String>, String> {
    // get two n-bit strings off hamming weight h
    val (F, G) = Pair(stringGen(n, h)!!, stringGen(n, h)!!)
    // get a n-bit random, string
    val R = stringGen(n, nextInt(n))!!
    val p = BigInteger("2").pow(n).dec()                        // 2^n - 1
    val pk = Pair(R, seq(int(F).times(int(R).plus(int(G))), p, n) ) // pk := (R,F ∙ R + G)
    // return pk := (R,F ∙ R + G) := (R,T) and sk := F
    return Pair(pk, F)
}

/** Choses an mersenne exponent _n_, such that _(n over h) >= 2^λ_ and _4h² < n <= 16h²_ and an h
 *
 * @param lambda security parameter
 */
internal fun mersennePrimeBit(lambda: Int): Pair<Int, Int> {
    return chooseBit(lambda,0 ,1)
}

internal fun mersennePrimeBlock(lambda: Int): Int {
    return chooseBlock(lambda, 0)
}

private fun chooseBit(lambda: Int, i: Int, h: Int): Pair<Int, Int> {
    return when {
        conditionsBit(lambda, possibleN[i], h)  -> Pair(possibleN[i],h)
        possibleN[i] > h + 1                    -> chooseBit(lambda, i, h+1)
        else                                    -> chooseBit(lambda, i+1, 1)
    }
}

private fun chooseBlock(lambda: Int, i: Int): Int {
    return when {
        conditionsBlock(lambda, possibleN[i])   -> possibleN[i]
        else                                    -> chooseBlock(lambda, i + 1)
    }
}

/** Returns True if the conditions to a valid n and h are met for a given lambda
 *
 */
private fun conditionsBit(lambda: Int, n: Int, h: Int): Boolean {
    val combinationCond: Boolean = combinations(n,h).toBigDecimal() >= 2f.pow(lambda).toBigDecimal()   //(n h) >= 2^λ
    val minLevelCond: Boolean = 4*h.toFloat().pow(2) < n                                            // 4h² < n
    val maxLevelCond: Boolean = n >= 16*h.toFloat().pow(2)                                          // n <= 16h²

    return  combinationCond and minLevelCond and maxLevelCond
}

private fun conditionsBlock(h: Int, n: Int): Boolean {
    val maxLevelCond: Boolean = 16*h.toFloat().pow(2) >= n  // 16h² >= n
    val minLevelCond: Boolean = n > 10*h.toFloat().pow(2)   // n > 10h²

    return minLevelCond and maxLevelCond
}
