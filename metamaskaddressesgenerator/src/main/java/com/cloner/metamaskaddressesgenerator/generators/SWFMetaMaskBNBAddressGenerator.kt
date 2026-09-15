package com.cloner.metamaskaddressesgenerator.generators

import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.MnemonicUtils

object SWFMetaMaskBNBAddressGenerator {

    private const val PURPOSE = 44
    private const val COIN_TYPE_MAINNET = 60
    private const val COIN_TYPE_TESTNET = 1

    fun generateAddress(
        mnemonic: String,
        passphrase: String,
        isTestNet: Boolean
    ): String? = runCatching {
        require(MnemonicUtils.validateMnemonic(mnemonic))

        val masterKeyPair = buildMasterKey(mnemonic, passphrase)
        val path = buildDerivationPath(isTestNet)

        val derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKeyPair, path)
        val credentials = Credentials.create(derivedKeyPair)

        return Keys.toChecksumAddress(credentials.address)
    }.getOrNull()

    private fun buildMasterKey(mnemonic: String, passphrase: String): Bip32ECKeyPair? {
        val seed = MnemonicUtils.generateSeed(mnemonic, passphrase)
        return Bip32ECKeyPair.generateKeyPair(seed)
    }

    private fun buildDerivationPath(
        isTestNet: Boolean
    ): IntArray {
        val coinType = if (isTestNet) COIN_TYPE_TESTNET else COIN_TYPE_MAINNET
        return intArrayOf(
            PURPOSE or Bip32ECKeyPair.HARDENED_BIT,
            coinType or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
    }
}