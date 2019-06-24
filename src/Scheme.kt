class Scheme {
    val b = 0b1111111111_0000000000_0000000000_0000000000_0000000000_0000000000_010
    val c = b + 1
}

fun main(args: Array<String>) {
    val lambda: Int = 80

    println("-------------Bit-by-bit test---------------")
    var (n, h) = mersennePrimeBit(lambda)
    println("Init: for λ = $lambda n is $n and h is $h")
    val bit = true
    val (bitPK,bitSK) = keyGenBit(n,h)
    println("\t Public key of ${bitPK.length} and secret key of ${bitSK.length}")
    val bitEncode = encBit(bitPK,bit,n,h)
    println("\t Encoding bit $bit... gives cyphertext of ${bitEncode.length}")
    val bitDecode = decBit(bitEncode,bitSK,n,h)
    println("\t Decoding cyphertext... gives bit $bitDecode")
    println("\t ¿Es correcto el decifrado?: ${bit == bitDecode}")
    // TODO check

    println("-------------BLock message test---------------")
    val message: String = stringGen(lambda, lambda/2)!!
    n = mersennePrimeBlock(lambda)
    h = lambda
    println("Init: for λ = $lambda n is $n and h is $h | ratio ${n/lambda}")
    val (pk,sk) = keyGenBlock(n,lambda)
    println("\t Public key (R,T) each one of ${pk.first.length} and secret key of ${sk.length}")
    val encodeBlock = encBlock(pk, message, n, lambda)
    println("\t Encoding message of size ${message.length}... \n\t\t gives cyphertext (C1,C2) of ${encodeBlock.first.length} each one")
    val decodeBlock = decBlock(encodeBlock, sk, n, lambda)
    println("\t Decoding cyphertext...\n\t\t gives plain text of size ${decodeBlock.length}")
    println("\t ¿Es correcto el decifrado?: ${message == decodeBlock}")
    if (message != decodeBlock){
        println(message)
        println(decodeBlock)
    }

}