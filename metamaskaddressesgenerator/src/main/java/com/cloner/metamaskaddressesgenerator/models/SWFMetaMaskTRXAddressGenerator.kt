package com.cloner.metamaskaddressesgenerator.models

import org.bitcoinj.base.Base58
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.MnemonicUtils
import org.web3j.utils.Numeric
import java.security.MessageDigest


object SWFMetaMaskTRXAddressGenerator {

    private const val PURPOSE = 44
    private const val COIN_TYPE = 195
    private const val ADDRESS_PREFIX = 0x41.toByte()

    fun generateAddress(
        mnemonic: String,
        passphrase: String = ""
    ): String? = runCatching {
        require(MnemonicUtils.validateMnemonic(mnemonic))

        val masterKeyPair = getMasterKey(mnemonic, passphrase)
        val path = buildDerivationPath()
        val derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path)

        val pubKeyBytes = Numeric.toBytesPadded(derivedKeyPair.publicKey, 64)
        val keccakHash = Hash.sha3(pubKeyBytes)
        val address20Bytes = keccakHash.copyOfRange(12, 32)

        val rawAddress = byteArrayOf(ADDRESS_PREFIX) + address20Bytes
        val checksum = doubleSha256(rawAddress).copyOfRange(0, 4)

        Base58.encode(rawAddress + checksum)
    }.getOrNull()

    private fun getMasterKey(mnemonic: String, passphrase: String): Bip32ECKeyPair {
        val seed = MnemonicUtils.generateSeed(mnemonic, passphrase)
        return checkNotNull(Bip32ECKeyPair.generateKeyPair(seed))
    }

    private fun buildDerivationPath(): IntArray {
        return intArrayOf(
            PURPOSE or Bip32ECKeyPair.HARDENED_BIT,
            COIN_TYPE or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
    }

    private fun doubleSha256(input: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(md.digest(input))
    }
}