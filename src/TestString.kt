import java.math.BigInteger

fun main(){
    val message = "potato"
    println("The message is \t\t$message")
    val bits_message = stringtToBits(message)
    val lambda:Int = when {
        bits_message.length > 100 -> 100
        else -> bits_message.length
    }
    var n = mersennePrimeBlock(lambda)
    println("KeyGen: for λ = $lambda n is $n and h is $lambda | ratio ${n/lambda}")
    val (pk, sk) = keyGenBlock(n,lambda)
    println("\t Public key (R,T) each one of ${pk.first.length} and secret key of ${sk.length}")
    val cypherText = enc(pk, bits_message, lambda, n)
    println("\t Encoding message of size ${message.length}... \n\t\t gives cyphertext (C1,C2) of ${cypherText.first.length} each one")
    val plainText = dec(sk, cypherText, lambda, n)
    println("\t Decoding cyphertext...\n\t\t gives plain text of size ${plainText.length}")
    println("\t ¿Es correcto el decifrado?: ${bits_message == plainText}")
    println("decrypted text: \t$plainText")
    println("binary message: \t$bits_message")
}

fun stringtToBits (s: String): String{
    val binary = BigInteger(s.toByteArray()).toString(2)
    return binary
}

fun enc(pk:Pair<String,String>, message: String, lambda: Int, n: Int): Pair<String, String> {
    var C1 = ""
    var C2 = ""
    for (i in 0 until message.length-1 step lambda) {
        var block = message.substring(i,i+lambda)
        var c = encBlock(pk, block, n, lambda)
        C1 += c.first
        C2 += c.second
    }
    return Pair(C1,C2)
}

fun dec(sk: String, cypherText: Pair<String,String>, lambda:Int, n:Int): String{
//    var C = ""
//    for (i in 0 until cypherText.first.length-1 step lambda){
//        var C1 = cypherText.first.substring(i,i+lambda)
//        var C2 = cypherText.second.substring(i, i+lambda)
//        var plainText = decBlock(Pair(C1,C2),sk,n,lambda)
//        C += plainText
//    }
    return decBlock(cypherText,sk,n,lambda)
}