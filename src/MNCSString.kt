import java.math.BigInteger

fun main(){
    // Test

    val lambdaLimit = 80
    val message = "Computador"
    println("The message is \t\t$message")
    val bitsMessage = stringtToBits(message, lambdaLimit)
    val binary = BigInteger(message.toByteArray()).toString(2)
    val lambda:Int = when {
        bitsMessage.length > lambdaLimit -> lambdaLimit
        else -> bitsMessage.length
    }
    println("\tbinary representation of the message has ${binary.length} bits, and ${(bitsMessage.length/lambda).toInt()} block(s)")
    var n = mersennePrimeBlock(lambda)
    println("KeyGen: for λ = $lambda n is $n and h is $lambda | ratio ${n/lambda}")
    val (pk, sk) = keyGenBlock(n,lambda)
    println("\t Public key (R,T) each one of ${pk.first.length} and secret key of ${sk.length}")
    val cypherText = enc(pk, bitsMessage, lambda, n)
    println("\t Encoding message of size ${binary.length}... \n\t\t gives cyphertext (C1,C2) of ${cypherText.first.length} each one")
    val plainText = dec(sk, cypherText, lambda, n)
    println("\t Decoding cyphertext...\n\t\t gives plain text of size ${plainText.length}")

    println("\t ¿Es correcto el decifrado?: ${binary == plainText}")
    println("decrypted text: \t$plainText")
    println("binary message: \t$binary")
}

fun stringtToBits (s: String, n: Int): String{
    val space = BigInteger("$".toByteArray()).toString(2)
    val binary = BigInteger(s.toByteArray()).toString(2)
    var new = binary + space
    while ((new.length)%n != 0){
        new += '0'
    }
    return new
}

fun enc(pk:Pair<String,String>, message: String, lambda: Int, n: Int): Pair<String, String> {
    var C1 = ""
    var C2 = ""
    for (i in 0 until message.length step lambda) {
        var end = when {
            i+lambda >= message.length -> message.length
            else -> i+lambda
        }
        var block = message.substring(i,end)
        var c = encBlock(pk, block, n, lambda)
        C1 += c.first
        C2 += c.second
    }
    return Pair(C1,C2)
}

fun dec(sk: String, cypherText: Pair<String,String>, lambda:Int, n:Int): String{
    var C = ""
    if (cypherText.first.length > lambda) {
        for (i in 0 until cypherText.first.length step n){
            var end = when {
                i+n >= cypherText.first.length -> cypherText.first.length
                else -> i+n
            }
            var C1 = cypherText.first.substring(i,i+n)
            var C2 = cypherText.second.substring(i, i+n)
            var plainText = decBlock(Pair(C1,C2),sk,n,lambda)
            C += plainText
        }
    }
    else {
        C = decBlock(cypherText,sk,n,lambda)
    }
    val space = BigInteger("$".toByteArray()).toString(2)
    val index = C.findLastAnyOf(listOf(space))!!
    if (index != null){
        return C.substring(0,index.first)
    } else {
        return C
    }
}