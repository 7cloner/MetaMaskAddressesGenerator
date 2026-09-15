package com.cloner.metamaskaddressesgenerator.models

import org.bitcoinj.base.Base58
import org.bitcoinj.crypto.MnemonicCode
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import java.nio.ByteBuffer
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


object SWFMetaMaskSOLAddressGenerator {

    private const val PURPOSE = 44
    private const val COIN_TYPE = 501
    private const val HARDENED_BIT = 0x80000000.toInt()
    private const val SEED_KEY = "ed25519 seed"

    fun generateAddress(
        mnemonic: String,
        passphrase: String
    ): String? = runCatching {
        val words = mnemonic.trim().lowercase().split("\\s+".toRegex())
        MnemonicCode.INSTANCE.check(words)

        val seedBytes = MnemonicCode.toSeed(words, passphrase)
        val path = buildDerivationPath()

        var (privateKey, chainCode) = hmacSha512(SEED_KEY.toByteArray(Charsets.UTF_8), seedBytes)
        for (index in path) {
            val buffer = ByteBuffer.allocate(37)
            buffer.put(0x00.toByte())
            buffer.put(privateKey)
            buffer.putInt(index)

            val (nextKey, nextChainCode) = hmacSha512(chainCode, buffer.array())
            privateKey = nextKey
            chainCode = nextChainCode
        }

        val publicKeyBytes = Ed25519PrivateKeyParameters(privateKey, 0).generatePublicKey().encoded
        Base58.encode(publicKeyBytes)
    }.getOrNull()

    private fun buildDerivationPath(): IntArray {
        return intArrayOf(
            PURPOSE or HARDENED_BIT,
            COIN_TYPE or HARDENED_BIT,
            0 or HARDENED_BIT,
            0 or HARDENED_BIT
        )
    }

    private fun hmacSha512(key: ByteArray, data: ByteArray): Pair<ByteArray, ByteArray> {
        val mac = Mac.getInstance("HmacSHA512")
        mac.init(SecretKeySpec(key, "HmacSHA512"))
        val hmac = mac.doFinal(data)
        return hmac.copyOfRange(0, 32) to hmac.copyOfRange(32, 64)
    }
}