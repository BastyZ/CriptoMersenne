
fun main() {
    val lambda = 100

    println("-------------Bit-by-bit test---------------")
    var (n, h) = mersennePrimeBit(lambda)
    println("Init: for λ = $lambda n is $n and h is $h")
    val bit = true
    val (bitPK,bitSK) = keyGenBit(n, h)
    println("\t Public key of ${bitPK.length} and secret key of ${bitSK.length}")
    val bitEncode = encBit(bitPK, bit, n, h)
    println("\t Encoding bit $bit... gives cyphertext of ${bitEncode.length}")
    val bitDecode = decBit(bitEncode, bitSK, n, h)
    println("\t Decoding cyphertext... gives bit $bitDecode")
    println("\t ¿Es correcto el decifrado?: ${bit == bitDecode}")

    println("-------------BLock message test---------------")
    val message: String = stringGen(lambda, lambda / 2)!!
    println("The message is \t\t$message")
    n = mersennePrimeBlock(lambda)
    h = lambda
    println("Init: for λ = $lambda n is $n and h is $h | ratio ${n/lambda}")
    val (pk,sk) = keyGenBlock(n, lambda)
    println("\t Public key (R,T) each one of ${pk.first.length} and secret key of ${sk.length}")
    val encodeBlock = encBlock(pk, message, n, lambda)
    println("\t Encoding message of size ${message.length}... \n" +
            "\t\t gives cyphertext (C1,C2) of ${encodeBlock.first.length} each one")
    val decodeBlock = decBlock(encodeBlock, sk, n, lambda)
    println("\t Decoding cyphertext...\n\t\t gives plain text of size ${decodeBlock.length}")
    println("\t ¿Es correcto el decifrado?: ${message == decodeBlock}")
    println(" decrypted text \t$decodeBlock")
    println("plaintext message\t$message")

}