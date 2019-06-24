import java.math.BigInteger
import kotlin.math.pow

fun main(args: Array<String>) {

    val lambda = 40
    val h = lambda
    val n = mersennePrimeBlock(lambda)
    val (pk,sk) = blockKeyGen(lambda, n, h)
    val messageBlock = stringGen(lambda, lambda/2)!!
    val cypherText = blockEncryption(pk, messageBlock, n, h)
//    val decypherText = blockDecription(sk,cypherText,n,h)
//    println("RESULT BIT $decypherText")

}

fun blockKeyGen(lambda: Int, n:Int, h: Int): Pair<Pair<String,String>, String> {
    // Given lambda
    //Choose a mersenne prime p such that h=λ
    val p = 2.toBigInteger().pow(n).minus(BigInteger.ONE)
    val cond1 = 16*(h.toFloat().pow(2))>=n
    val cond2 = n > 10*(h.toFloat().pow(2))
    println("With λ=$lambda, we choose a Mersenne prime with n=$n and h=λ=$h")
    println("\t Meet the condition 16h² >= n? $cond1")
    println("\t Meet the condition n < 10h²? $cond2")

    //Choose F,G n-bit string of HammingWeigth h
    val F = stringGen(n,h)!!
    val G = stringGen(n,h)!!
    val R = stringGen(n,h)!!
    println("Then, choose F,G $n-bit strings of Hamming weight $h, and R a $n-bit string")
    println("\t F meet this conditions? ${(F.length == n) and (hammingWeight(F) == h)}")
    println("\t G meet this conditions? ${(G.length == n) and (hammingWeight(G) == h)}")
    println("\t R meet this conditions? ${(R.length == n)}")

    val T = (toOperableString(F).times(toOperableString(R))).plus(toOperableString(G))
    val pk = Pair(R,T.toBitString(n))
    val sk = G
    println("Now we have a Public Key and a Secret Key of length $n")
    println("\t PK = (R,T) of T length ${T.bitLength()} and R length ${R.length}")
    println("\t SK of length ${sk.length}")
    return Pair(pk,sk)
}

fun blockEncryption(pk:Pair<String,String>, message: String, n:Int, h:Int): Pair<String,String>{
    //Chooses A,B n-bit string with hamming weight h
    val (A,B1) = Pair(stringGen(n,h)!!,stringGen(n,h)!!)
    val B2 = stringGen(n,h)!!
    println("Choose A,B1,B2 independent $n-bit strings of Hamming weight $h")
    println("\t Does A meet this conditions? ${A.length == n && hammingWeight(A)==h}")
    println("\t Does B1 meet this conditions? ${B1.length == n && hammingWeight(B1)==h}")
    println("\t Does B2 meet this conditions? ${B1.length == n && hammingWeight(B2)==h}")

    val R = toOperableString(pk.first)
    val T = toOperableString(pk.second)

    println("We encrypt the message of lenght ${message.length} given the pair of cypher texts C1 and C2")
    // C1 = A.R+B1
    val C1 = (toOperableString(A).times(R)).plus(toOperableString(B1))
    // E(m)
    val E = encode(message,n)
    val C= (toOperableString(A).times(T)).plus(toOperableString(B2))
    // C2 = (A.T+B2) xor E(m)
    val C2 = C.xor(toOperableString(E))
    println("\t C1= A∙R + B1 of ${C1.bitLength()} bits")
    println("\t C2= (A∙T + B2)⊕E(message) of ${C2.bitLength()} bits")
    println("\t\t with the error corrector code E(m) of length ${E.length}")
    println("\t\t and the counterpart of length ${C.bitLength()}")
    return Pair(C1.toBitString(n),C2.toBitString(n))
}

fun blockDecription(sk: String, C: String, n: Int, h:Int): String {
    val D = toOperableString(C).times(toOperableString(sk))
    println("We decrypt a cypher text (${C.length} bits) with the Secret key")
    val d = hammingWeight(D.toBitString(n))
    println("\t C∙SK of lenght ${D.bitLength()}")
    println("\t Ham(C∙SK) = $d")
    val result = when {
        d <= 2*(h.toFloat().pow(2)) -> "0"
        d >= n - 2*(h.toFloat().pow(2)) -> "1"
        else -> "ERROR"
    }
    return result
}

