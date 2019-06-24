import java.math.BigInteger
import kotlin.math.pow

fun main(args: Array<String>) {
    val lambda = 40
    val (n, h) = mersennePrimeBit(lambda)
    val (pk,sk) = bitKeyGen(lambda, n, h)
    val message = true
    val cypherText = bitEncryption(pk, message, n, h)
    println("CYPHER TEXT (n=${cypherText.length})= $cypherText")
    val decypherText = bitDecription(sk,cypherText,n,h)
    println("RESULT BIT $decypherText")

}

fun bitKeyGen(lambda: Int, n:Int, h: Int): Pair<String, String> {
    // Given lambda
    //Choose a mersenne prime p and integer h
    val p = 2.toBigInteger().pow(n).minus(BigInteger.ONE)
    val cond1 = combinations(n, h) >= 2.toBigInteger().pow(lambda)
    val cond2 = n > 4 * (h.toFloat().pow(2)) && n >= 16 * (h.toFloat().pow(2))
    println("With λ=$lambda, we choose a Mersenne prime with n=$n and h=$h")
    println("\t Meet the condition (n over h) >= 2^λ? $cond1")
    println("\t Meet the condition 4h² < n <= 16h²? $cond2")

    //Choose F,G n-bit string of HammingWeigth h
    val F = stringGen(n,h)!!
    val G = stringGen(n,h)!!
    println("Then, choose F,G $n-bit strings of Hamming weight $h")
    println("\t F meet this conditions? ${(F.length == n) and (hammingWeight(F) == h)}")
    println("\t G meet this conditions? ${(G.length == n) and (hammingWeight(G) == h)}")

    val pk = seq(intMod(F,p).div(intMod(G,p)),p,n)
    val sk = G
    println("Now we have a Public Key and a Secret Key of length $n")
    println("\t PK of length ${pk.length} is $pk")
    println("\t SK of length ${sk.length} is $sk")
    return Pair(pk,sk)
}

fun bitEncryption(pk:String, b:Boolean, n:Int, h:Int): String{
    //Chooses A,B n-bit string with hamming weight h
    val (A,B) = Pair(stringGen(n,h)!!,stringGen(n,h)!!)
    println("Choose A,B independent $n-bit strings of Hamming weight $h")
    println("\t Does A meet this conditions? ${A.length == n && hammingWeight(A)==h}")
    println("\t Does B meet this conditions? ${B.length == n && hammingWeight(B)==h}")

    val message =(when{
        b -> "1"
        else -> "0"
    }).toInt()
    println("We encrypt the bit $message making (-1)^bit * (A.pk + B)")
    // H = A.pk+B
    val H = (aBigInteger(A).times(aBigInteger(pk))).plus(aBigInteger(B))
    println("\t (A.pk + B) of ${H.bitLength()}")
    val C = (BigInteger("-1").pow(message)).times(H)
    println("\t Cypher text of ${C.bitLength()}")
    return C.toBitString(n)
}

fun bitDecription(sk: String, C: String, n: Int, h:Int): String {
    val D = aBigInteger(C).times(aBigInteger(sk))
    println("We decrypt a cypher text (${C.length} bits) with the Secret key")
    val d = hammingWeight(D.toBitString(n))
    println("\t C.SK of lenght ${D.bitLength()}")
    println("\t Ham(C.SK) = $d")
    val result = when {
        d <= 2*(h.toFloat().pow(2)) -> "0"
        d >= n - 2*(h.toFloat().pow(2)) -> "1"
        else -> ""
    }
    return result
}

