import java.lang.Math.log
import java.math.BigInteger
import kotlin.math.floor

fun main(){
    val lambda = 20
    val (n,w)=instanceMNCS(lambda)
    val (G,H)=keyGenMNCS(lambda,n,w)
    val (Ds,Gs)=initClassicAtk(n,w,H)
    val Hrot = Ds.first
    val b=Ds.second
    val (nX,wX)=Gs.first
    val (nY,wY)=Gs.second
    val (F2,collisions) = attackClassic(Hrot, b, Gs.first,Gs.second,w,n)
    val pred_coll = combinations(nX,wX) * combinations(nY,wY) / BigInteger("2").pow(b)
    var success = false

    // TODO
//    if F2 is not None:
//    G2 = F2 * inst.inv(H) % (2**n - 1)
//    FoF2 = G2 * inst.inv(G) % (2**n - 1)
//    if bin(FoF2).count('1') == 1:
//    succ += True
//    return succ, collisions/pred_coll

}

fun instanceMNCS(lambda:Int): Pair<Int, Int> {
    val (n,w) = mersennePrimeBit(lambda)
    return Pair(n,w)
}

fun keyGenMNCS(lambda: Int,n: Int, w: Int): Pair<String, String> {
    val (SK,PK) = bitKeyGen(lambda, n, w)
    return Pair(SK,PK)
}

fun initClassicAtk(n:Int, w:Int, PK:String): Pair<Pair<ArrayList<String>, Int>, Pair<Pair<Int, Int>, Pair<Int, Int>>> {
    val N = BigInteger("2").pow(n)- BigInteger("1")
    var H = PK
    var Hrot = arrayListOf<String>()

    for (i in 0 until n){
        Hrot.add(i,H)
        val temp = toOperableString(H).times(BigInteger("2")).mod(N)
        H = temp.toBitString(n)
    }

    var wX = w/2
    var nX = n/2
    var nY = n-nX
    val wY = w-wX
    if (wX < wY){
        while(combinations(nX,wX) < combinations(nY,wY)){
            nX += 1
            nY -= 1
        }
    }
    println("\t g1 of length $nX and HW $wX")
    println("\t g2 of length $nY and HW $wY")

    val b = floor(log(combinations(nX,wX).toDouble())/log(2.toDouble())).toInt()
    val bMask = 2*b -1

    val g1 = Pair(nX,wX)
    val g2 = Pair(nY,wY)
    val g = Pair(g1,g2)
    val c = Pair(Hrot, b)
    return Pair(c,g)
}

fun attackClassic(Hrot: ArrayList<String>, b:Int, g1:Pair<Int,Int>, g2:Pair<Int,Int>, w:Int, n:Int): Pair<String, Int> {

    val (nX,wX) = g1
    val (nY,wY) = g2
    val N = BigInteger("2").pow(n)- BigInteger("1")

    var collisions = 0
    val arraySize = BigInteger("2").pow(b).toInt()
    var database = ArrayList<ArrayList<String>>(initialCapacity = arraySize)

    var solF: String

    for (IX in lowHammingWeightStrings(nX,wX)){
        val vGXH = GXH(IX,Hrot, N, nX)
        val hGXH = LShash(vGXH,b)
        database[hGXH].add(vGXH)
    }

    for (IY in lowHammingWeightStrings(nY,wY)){
        val vGYH = GYH(IY,Hrot,wX,N,nX)
        val A = ((-toOperableString(vGYH)).mod(N)).toBitString(nY)
        val hGYH = LShash(A, b)
        for (vGXH in database[hGYH]){
            collisions += 1
            val S = (toOperableString(vGXH) + toOperableString(vGYH)).mod(N)
            if (hammingWeight(S.toBitString(n)) >= w){
                solF = S.toBitString(n)
            }

        }
    }

    return Pair(solF,collisions)

}

fun GXH(I:Int, Hrot: ArrayList<String>, N:BigInteger, n:Int): String {
    var res = BigInteger("0")
    for (i in 0..I){
        res += toOperableString(Hrot[i])
    }
    return res.mod(N).toBitString(n)
}

fun GYH(i:Int, Hrot:ArrayList<String>, wX:Int, N:BigInteger, n:Int): String {
    var res = BigInteger("0")
    for (j in 0 until i){
        res += toOperableString(Hrot[j + wX])
    }
    return res.mod(N).toBitString(n)
}

fun lowHammingWeightStrings(n: Int, w: Int): ArrayList<Int> {
    var p = arrayListOf<Int>()
    var oldP = arrayListOf<Int>()
    for (i in 0 until p.size){
        p.add(i, 0)
        oldP.add(i,0)
    }
    var last = false

    while (!last){
        if (last){ break }
        if (w==0){
            last = true
            return p
        }

        for (i in 0 until w){
            oldP[i] = p[i]
        }
        var j = w-1
        var m = 0
        while(true){
            p[j] += 1
            if (p[j] >= (n-m)){
                j -= 1
                m += 1
                if (j < 0){
                    last = true
                    break
                }
            } else {
                break
            }
        }

        for (k in j+1 until w){
            p[k] = p[k-1] + 1
        }
    }

    return oldP
}

// Localty-sensitive hash function
fun LShash(A: String, b:Int): Int { //TODO
    var res = 0
    for (i in 0 until b){
        res *= 2
        val B = toOperableString(shiftString(A,i,false))
        val C = B.and(BigInteger("1"))
        res += C.toInt()
    }
    return res
}