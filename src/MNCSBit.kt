import java.math.BigInteger
import kotlin.math.pow

fun singleBitSchema(lambda:Int): Map<String, Any> {
    // Test
    val timeStart = System.currentTimeMillis()
    val (n, h) = mersennePrimeBit(lambda)
    val (pk,sk) = bitKeyGen(lambda, n, h)
    val timeKeyGen = System.currentTimeMillis()
    val message = true
    val messageStr = when {
        message -> "1"
        else -> "0"
    }
    val cypherText = bitEncryption(pk, message, n, h)
    val timeCypher = System.currentTimeMillis()
    val decypherText = bitDecription(sk,cypherText,n,h)
    val timeDecypher = System.currentTimeMillis()
    val result = messageStr==decypherText
    val resultMap = mapOf(
        "lambda" to lambda, "n" to n, "h" to h, "success" to result,
        "KeyGenTime" to timeKeyGen-timeStart, "CypherTime" to timeCypher-timeKeyGen,
        "DecypherTime" to timeDecypher-timeCypher, "TotalTime" to  timeDecypher - timeStart
        )
    //println("λ:$lambda,\tn:$n,\th:$h,\tsuccess:${messageStr==decypherText}\tKeyGen time(ms):${timeKeyGen-timeStart},\tCypher time:${timeCypher-timeKeyGen},\tDecypher time:${timeDecypher-timeCypher},\tTotal time:${timeDecypher - timeStart}")
    return resultMap
}

fun bitKeyGen(lambda: Int, n:Int, h: Int): Pair<String, String> {
    // Given lambda
    //Choose a mersenne prime p and integer h
    val p = 2.toBigInteger().pow(n).minus(BigInteger.ONE)
    val cond1 = combinations(n, h) >= 2.toBigInteger().pow(lambda)
    val cond2 = n > 4 * (h.toFloat().pow(2)) && n >= 16 * (h.toFloat().pow(2))
//    println("\t Meet the condition (n over h) >= 2^λ? $cond1")
//    println("\t Meet the condition 4h² < n <= 16h²? $cond2")

    //Choose F,G n-bit string of HammingWeigth h
    val F = stringGen(n, h)!!
    val G = stringGen(n, h)!!
//    println("Then, choose F,G $n-bit strings of Hamming weight $h")
//    println("\t F meet this conditions? ${(F.length == n) and (hammingWeight(F) == h)}")
//    println("\t G meet this conditions? ${(G.length == n) and (hammingWeight(G) == h)}")

    val pk = seq(intMod(F, p).div(intMod(G, p)), p, n)
    val sk = G
//    println("\t PK of length ${pk.length} is $pk")
//    println("\t SK of length ${sk.length} is $sk")
    return Pair(pk,sk)
}

fun bitEncryption(pk:String, b:Boolean, n:Int, h:Int): String{
    //Chooses A,B n-bit string with hamming weight h
    val (A,B) = Pair(stringGen(n, h)!!, stringGen(n, h)!!)
//    println("Choose A,B independent $n-bit strings of Hamming weight $h")
//    println("\t Does A meet this conditions? ${A.length == n && hammingWeight(A) ==h}")
//    println("\t Does B meet this conditions? ${B.length == n && hammingWeight(B) ==h}")

    val message =(when{
        b -> 1
        else -> 0
    })
//    println("We encrypt the bit $message making (-1)^bit * (A∙pk + B)")
    // C = A.pk+B
    val C = toOperableString(sumStrings(binaryPoint(A, pk), B)).times(BigInteger("-1").pow(message))
//    println("\t (A∙pk + B) of ${C.bitLength()}")
    return C.toBitString(n)
}

fun bitDecription(sk: String, C: String, n: Int, h:Int): String {
    val polarity = when{
        C[0] == '-' -> -1
        else -> 1
    }
    val newC = when{
        C[0] == '-' -> C.subSequence(1,C.length)
        else -> C
    }.toString()
    val D = binaryPoint(newC, sk)
//    println("We decrypt a cypher text (${newC.length} bits) with the Secret key")
    val d = hammingWeight(D)
//    println("\t C∙SK of lenght ${D.length}")
//    println("\t Ham(C∙SK) = $d")
    val result = when {
        d <= 2*(h.toFloat().pow(2)) -> "0"
        d >= n - 2*(h.toFloat().pow(2)) -> "1"
        else -> "ERROR"
    }
    return when {
        polarity < 0 -> when {
            result == "0" -> "1"
            else -> "0"
        }
        else -> result
    }
}

