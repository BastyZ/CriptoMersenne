import kotlin.math.pow

fun multiBitSchema(lambda:Int) {
    // Test
//    val lambda = 14
    val h = lambda
    val timeStart = System.currentTimeMillis()
    val n = mersennePrimeBlock(lambda)
//    println("With λ=$lambda, we choose a Mersenne prime with n=$n and h=λ=$h")
    val (pk,sk) = blockKeyGen(lambda, n, h)
    val timeKgen = System.currentTimeMillis()
//    println("Now we have a Public Key and a Secret Key of length $n")
//    println(">> KeyGen time : ${timeKgen-timeStart}")
    val messageBlock = stringGen(lambda, lambda / 2)!!
    val timeStartCypher = System.currentTimeMillis()
    val cypherText = blockEncryption(pk, messageBlock, n, h)
    val timeCypher = System.currentTimeMillis()
//    println(">> Cypher time : ${timeCypher-timeStartCypher}")
    val obtaintedText = blockDecription(sk,cypherText,n,lambda)
    val timeDecypher = System.currentTimeMillis()
//    println(">> Decypher time : ${timeDecypher-timeCypher}")
    val result = messageBlock == obtaintedText
//    println(">> Encryption/Decryption result: $result")
////    println(">> Message v/s Decrypted Text")
////    println(messageBlock)
////    println(obtaintedText)
//    println(">> Total time : ${timeDecypher-timeStart}")
    println("λ:$lambda,\tn:$n,\th:$h,\tsuccess:${result}\tKeyGen time(ms):${timeKgen-timeStart},\tCypher time:${timeCypher-timeKgen},\tDecypher time:${timeDecypher-timeCypher},\tTotal time:${timeDecypher - timeStart}")
}

fun blockKeyGen(lambda: Int, n:Int, h: Int): Pair<Pair<String,String>, String> {
    // Given lambda
    //Choose a mersenne prime p such that h=λ
    val cond1 = 16*(h.toFloat().pow(2))>=n
    val cond2 = n > 10*(h.toFloat().pow(2))

//    println("\t Meet the condition 16h² >= n? $cond1")
//    println("\t Meet the condition n < 10h²? $cond2")

    //Choose F,G n-bit string of HammingWeigth h
    val F = stringGen(n, h)!!
    val G = stringGen(n, h)!!
    val R = stringGen(n, h)!!
//    println("Then, choose F,G $n-bit strings of Hamming weight $h, and R a $n-bit string")
//    println("\t F meet this conditions? ${(F.length == n) and (hammingWeight(F) == h)}")
//    println("\t G meet this conditions? ${(G.length == n) and (hammingWeight(G) == h)}")
//    println("\t R meet this conditions? ${(R.length == n)}")

    //T=F∙R + G
    val T = sumStrings(binaryPoint(F, R), G)
    val pk = Pair(R,T)
    val sk = G

//    println("\t PK = (R,T) with T=F∙R + G of length ${T.length} and R length ${R.length}")
//    println("\t SK of length ${sk.length}")
    return Pair(pk,sk)
}

fun blockEncryption(pk:Pair<String,String>, message: String, n:Int, h:Int): Pair<String,String>{
    //Chooses A,B n-bit string with hamming weight h
    val (A,B1) = Pair(stringGen(n, h)!!, stringGen(n, h)!!)
    val B2 = stringGen(n, h)!!
//    println("Choose A,B1,B2 independent $n-bit strings of Hamming weight $h")
//    println("\t Does A meet this conditions? ${A.length == n && hammingWeight(A) ==h}")
//    println("\t Does B1 meet this conditions? ${B1.length == n && hammingWeight(B1) ==h}")
//    println("\t Does B2 meet this conditions? ${B1.length == n && hammingWeight(B2) ==h}")

    val R = pk.first
    val T = pk.second

//    println("We encrypt the message of lenght ${message.length} given the pair of cypher texts C1 and C2")
    // C1 = A.R+B1
    val C1 = sumStrings(binaryPoint(A, R), B1)
    // E(m)
    val E = encode(message, n)
    val C= sumStrings(binaryPoint(A, T), B2)
    // C2 = (A.T+B2) xor E(m)
    val C2 = xorString(C, E)
//    println("\t C1= A∙R + B1 of ${C1.length} bits")
//    println("\t C2= (A∙T + B2)⊕E(message) of ${C2.length} bits")
//    println("\t\t with the error corrector code encoder E(m) of length ${E.length}")
//    println("\t\t and the counterpart of length ${C.length}")
    return Pair(C1,C2)
}

fun blockDecription(sk: String, C: Pair<String,String>, n: Int, lambda: Int): String {
    val (C1,C2) = C
    // SK∙C1⊕C2
    val args = xorString(binaryPoint(sk, C1), C2)
    // plaintext = D(SK∙C1⊕C2)
    val result = decode(args, n, lambda)
//    println("We decrypt a cypher texts (${C1.length} bits) with the Secret key")
//    println("\t SK∙C1⊕C2 of length ${args.length}")
//    println("\t PLain text obtained of length ${result.length}")
    return result
}

