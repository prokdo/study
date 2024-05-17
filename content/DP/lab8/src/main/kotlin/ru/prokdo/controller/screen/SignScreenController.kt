package ru.prokdo.controller.screen

import java.math.BigInteger

import ru.prokdo.model.crypto.rsa.RSA
import ru.prokdo.model.crypto.rsa.key.Key
import ru.prokdo.model.crypto.rsa.key.KeyGenerator

class SignScreenController : ScreenController() {
    var secretKey: Key? = null

    var messageHash: BigInteger? = null

    fun verifyUInt(strInt: String?): Int? {
        if (strInt == null) return null

        if (strInt.isEmpty() || strInt.isBlank()) return null

        try {
            val value = strInt.toInt()
            if (value < 0) return null

            return value
        } catch (exception: Exception) { return null }
    }

    fun verifyBigInteger(strBigInt: String?): BigInteger? {
        if (strBigInt == null) return null

        if (strBigInt.isEmpty() || strBigInt.isBlank()) return null

        try { return BigInteger(strBigInt) } 
        catch (exception: Exception) { return null }
    }

    fun generateKeys() { secretKey = KeyGenerator.RSAKeys().second }

    fun calculateSignature() = RSA.sign(messageHash!!, secretKey!!)
}