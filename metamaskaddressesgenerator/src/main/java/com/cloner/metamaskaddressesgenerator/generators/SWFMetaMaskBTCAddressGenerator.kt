package com.cloner.metamaskaddressesgenerator.generators

import org.bitcoinj.base.ScriptType
import org.bitcoinj.crypto.ChildNumber
import org.bitcoinj.crypto.DeterministicKey
import org.bitcoinj.crypto.HDKeyDerivation
import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.params.MainNetParams
import org.bitcoinj.params.TestNet3Params
import org.web3j.crypto.MnemonicUtils


object SWFMetaMaskBTCAddressGenerator {

    private const val PURPOSE = 84
    private const val COIN_TYPE_MAINNET = 0
    private const val COIN_TYPE_TESTNET = 1

    fun generateAddress(
        mnemonic: String,
        passphrase: String,
        isTestNet: Boolean
    ): String? = runCatching {
        require(MnemonicUtils.validateMnemonic(mnemonic))

        val masterKey = getMasterKey(mnemonic, passphrase)
        val path = getPath(isTestNet)

        val derivedKey = path.fold(masterKey) { currentKey, childNumber ->
            HDKeyDerivation.deriveChildKey(currentKey, childNumber)
        }

        val network = if (isTestNet) TestNet3Params.get().network() else MainNetParams.get().network()

        derivedKey.toAddress(ScriptType.P2WPKH, network).toString()
    }.getOrNull()

    private fun getMasterKey(mnemonic: String, passphrase: String): DeterministicKey {
        val words = mnemonic.trim().lowercase().split("\\s+".toRegex())
        val seedBytes = MnemonicCode.toSeed(words, passphrase)
        return HDKeyDerivation.createMasterPrivateKey(seedBytes)
    }

    private fun getPath(isTestNet: Boolean): List<ChildNumber> {
        val coinType = if (isTestNet) COIN_TYPE_TESTNET else COIN_TYPE_MAINNET
        return listOf(
            ChildNumber(PURPOSE, true),
            ChildNumber(coinType, true),
            ChildNumber(0, true),
            ChildNumber(0, false),
            ChildNumber(0, false)
        )
    }
}