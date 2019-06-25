import java.math.BigInteger

fun encBit(pk: String, b: Boolean, n: Int, h: Int): String {
    //Chooses A,B n-bit string with hamming weight h
    val (A,B) = Pair(stringGen(n, h)!!, stringGen(n, h)!!)
    val message =(when{
        b -> 1
        else -> 0
    })
    // C = A.pk+B
    val C = toOperableString(sumStrings(binaryPoint(A, pk), B)).times(BigInteger("-1").pow(message))
    return C.toBitString(n)
}

fun encode (message: String, n: Int): String {
    val ratio: Int = when {
        n/(message.length)%2 == 0 ->  n/(message.length) - 1
        else -> n/(message.length)
    }
    var result = ""
    for (bit in message) {
        for (index in 0 until ratio){
            result += bit
        }
    }
    while (result.length < n){
        result += "0"
    }
    return result
}

fun encBlock (pk: Pair<String, String>, message: String, n: Int, h: Int): Pair<String, String> {
    //Chooses A,B n-bit string with hamming weight h
    val (A,B1) = Pair(stringGen(n, h)!!, stringGen(n, h)!!)
    val B2 = stringGen(n, h)!!
    // C1 = A.R+B1
    val C1 = sumStrings(binaryPoint(A, pk.first), B1)
    // E(m)
    val E = encode(message, n)
    val C= sumStrings(binaryPoint(A, pk.second), B2)
    // C2 = (A.T+B2) xor E(m)
    val C2 = xorString(C, E)
    return Pair(C1,C2)
}