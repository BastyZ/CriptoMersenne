
fun main() {
    val min_lambda = 16//64
    val max_lambda = 512//512
    val samples = 1

    var lambda_array = mutableListOf<Int>(min_lambda)
    var last_val = min_lambda
    while (last_val < max_lambda){
        last_val = lambda_array.last().toFloat().times(2).toInt()
        lambda_array.add(last_val)
    }
//    println(">> Single-bit schema")
//    for (lambda in lambda_array){
//        sampleSingleBitSchema(lambda,samples)
//    }
    println(">> Multi-bit schema")
    for (lambda in lambda_array) {
        sampleMultiBitSchema(lambda,samples)
    }
}

fun sampleSingleBitSchema(lambda:Int, samples:Int){
    var keyGen = ArrayList<Long>()
    var cypher = ArrayList<Long>()
    var decypher = ArrayList<Long>()
    var total = ArrayList<Long>()
    var (n,h) = Pair("","")
    for (t in 1..samples){
        val result=singleBitSchema(lambda)
        n= result.get("n").toString()
        h= result.get("h").toString()
        keyGen.add(result.get("KeyGenTime") as Long)
        cypher.add(result.get("CypherTime") as Long)
        decypher.add(result.get("DecypherTime") as Long)
        total.add(result.get("TotalTime") as Long)
    }
    val avgKG = keyGen.average()
    val avgC = cypher.average()
    val avgD = decypher.average()
    val avgT= total.average()
    println("λ:$lambda,\tn:$n,\th:$h, \tsamples:$samples,\tAVG TIMES(ms)-> \tKeyGen:$avgKG,\tCypher:$avgC,\tDecypher:$avgD,\tTotal:$avgT")
}

fun sampleMultiBitSchema(lambda:Int, samples:Int){
    if (lambda < 10){
        return
    }
    var keyGen = ArrayList<Long>()
    var cypher = ArrayList<Long>()
    var decypher = ArrayList<Long>()
    var total = ArrayList<Long>()
    var (n,h) = Pair("","")
    for (t in 1..samples){
        val result=multiBitSchema(lambda)
        n= result.get("n").toString()
        h= result.get("h").toString()
        keyGen.add(result.get("KeyGenTime") as Long)
        cypher.add(result.get("CypherTime") as Long)
        decypher.add(result.get("DecypherTime") as Long)
        total.add(result.get("TotalTime") as Long)
    }
    val avgKG = keyGen.average()
    val avgC = cypher.average()
    val avgD = decypher.average()
    val avgT= total.average()
    println("λ:$lambda,\tn:$n,\th:$h, \tsamples:$samples,\tAVG TIMES(ms)-> \tKeyGen:$avgKG,\tCypher:$avgC,\tDecypher:$avgD,\tTotal:$avgT")
}