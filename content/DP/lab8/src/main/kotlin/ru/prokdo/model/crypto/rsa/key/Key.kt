package ru.prokdo.model.crypto.rsa.key

import java.math.BigInteger

data class  Key(
                val exponent: BigInteger,
                val module: BigInteger
            ) {
    override fun toString(): String = "<$exponent, $module>"

    fun toString(rowsParse: Boolean): String {
        if (rowsParse) return   """
                                    |<
                                    |   $exponent,
                                    |   $module
                                    |>
                                """.trimMargin()
        
        return this.toString()
    } 
}