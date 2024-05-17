package ru.prokdo.model.crypto.random

import java.math.BigInteger
import java.security.SecureRandom

object PrimeGenerator {
    fun bigPrime(bitLength: Int): BigInteger = BigInteger.probablePrime(bitLength, SecureRandom())
}