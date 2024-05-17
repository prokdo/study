package ru.prokdo.model.crypto.rsa.key

import java.math.BigInteger

import ru.prokdo.model.crypto.random.PrimeGenerator

object KeyGenerator {
    private const val _PRIMES_BIT_LENGTH = 1024

    fun RSAKeys(): Pair<Key, Key> {
        val p = PrimeGenerator.bigPrime(_PRIMES_BIT_LENGTH)
        val q = PrimeGenerator.bigPrime(_PRIMES_BIT_LENGTH)

        val n = p * q

        val phi = (p - BigInteger.ONE) * (q - BigInteger.ONE)

        var e = BigInteger.valueOf(65537)
        while (phi.gcd(e) != BigInteger.ONE) e += BigInteger.TWO

        val d = e.modInverse(phi)

        val publicKey = Key(e, n)
        val privateKey = Key(d, n)

        return Pair(publicKey, privateKey)
    }
}