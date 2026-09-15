package com.cloner.metamaskaddressesgenerator.generators

import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.MnemonicUtils


object SWFMetaMaskETHAddressGenerator {

    private const val PURPOSE = 44
    private const val COIN_TYPE_MAINNET = 60
    private const val COIN_TYPE_TESTNET = 1

    fun generateAddress(
        mnemonic: String,
        passphrase: String,
        isTestNet: Boolean
    ): String? = runCatching {
        require(MnemonicUtils.validateMnemonic(mnemonic))

        val credentials = getCredentials(mnemonic, passphrase, isTestNet)
        Keys.toChecksumAddress(credentials.address)
    }.getOrNull()

    private fun getMasterKey(mnemonic: String, passphrase: String): Bip32ECKeyPair {
        val seed = MnemonicUtils.generateSeed(mnemonic, passphrase)
        return checkNotNull(Bip32ECKeyPair.generateKeyPair(seed))
    }

    private fun getPath(isTestNet: Boolean): IntArray {
        val coinType = if (isTestNet) COIN_TYPE_TESTNET else COIN_TYPE_MAINNET
        return intArrayOf(
            PURPOSE or Bip32ECKeyPair.HARDENED_BIT,
            coinType or Bip32ECKeyPair.HARDENED_BIT,
            0 or Bip32ECKeyPair.HARDENED_BIT,
            0,
            0
        )
    }

    private fun getCredentials(
        mnemonic: String,
        passphrase: String,
        isTestNet: Boolean
    ): Credentials {
        val masterKey = getMasterKey(mnemonic, passphrase)
        val path = getPath(isTestNet)
        val derivedKeyPair = Bip32ECKeyPair.deriveKeyPair(masterKey, path)
        return Credentials.create(derivedKeyPair)
    }
}