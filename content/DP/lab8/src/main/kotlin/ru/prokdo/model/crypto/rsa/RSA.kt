package ru.prokdo.model.crypto.rsa

import java.math.BigInteger

import ru.prokdo.model.crypto.rsa.key.Key

object RSA {
    fun sign(messageHash: BigInteger, privateKey: Key): BigInteger = messageHash.modPow(privateKey.exponent, privateKey.module)

    fun verify(messageHash: BigInteger, signature: BigInteger, publicKey: Key): Boolean {
        val decryption = signature.modPow(publicKey.exponent, publicKey.module)

        return messageHash == decryption
    }
}