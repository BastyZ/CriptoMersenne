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
