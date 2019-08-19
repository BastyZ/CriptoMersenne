
fun main() {
    val min_lambda = 4//64
    val max_lambda = 512//512

    var lambda_array = mutableListOf<Int>(min_lambda)
    var last_val = min_lambda
    while (last_val < max_lambda){
        last_val = lambda_array.last().toFloat().times(2).toInt()
        lambda_array.add(last_val)
    }
    println(">> Single-bit schema")
    for (lambda in lambda_array){
        singleBitSchema(lambda)
    }

    println(">> Multi-bit schema")
    for (lambda in lambda_array){
        multiBitSchema(lambda)
    }

}