import java.math.BigInteger

fun encBit(pk: String, b: Boolean, n: Int, h: Int): String {
    val bit: String = when {
        b -> "-1"
        else -> "1"
    }
    val (A, B) =  Pair(
        aBigInteger( stringGen(n, h)!! ),
        aBigInteger( stringGen(n, h)!! )
    )
    return BigInteger(bit).times( A.times( aBigInteger(pk).plus( B ) ) )
        .toString(2)
}

fun encode (message: String, n: Int): String {
    val ratio: Int = when {
        n/(message.length)%2 == 0 ->  n/(message.length) - 1
        else -> n/(message.length)
    }
    var result = ""
    for (bit in message) {
        for (index in 0..ratio){
            result += bit
        }
    }
    while (result.length < n){
        result += "0"
    }
    return result
}

fun encBlock (pk: Pair<String, String>, message: String, n: Int, h: Int): Pair<String, String> {
    val (R, T) = pk
    val A = aBigInteger(stringGen(n, h)!!)
    val (B1,B2) = Pair(aBigInteger(stringGen(n, h)!!), aBigInteger(stringGen(n, h)!!))
    val E = aBigInteger(encode(message, n))
    val p = aBigInteger("10").pow(n).dec()

    val C1: String = (A.times(aBigInteger(R))).plus(B1).mod(p).toBitString(n)   // A*R + B1
    val C2: String = (A.times(aBigInteger(T)).plus(B2)).xor(E).mod(p).toBitString(n)   // (A*T+B2) xor E(m)
    return Pair(C1, C2)
}