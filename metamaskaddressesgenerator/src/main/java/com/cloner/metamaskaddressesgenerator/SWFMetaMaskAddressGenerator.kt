package com.cloner.metamaskaddressesgenerator

import com.cloner.metamaskaddressesgenerator.enums.SWFMetaMaskCoin
import com.cloner.metamaskaddressesgenerator.models.SWFMetaMaskBNBAddressGenerator
import com.cloner.metamaskaddressesgenerator.models.SWFMetaMaskBTCAddressGenerator
import com.cloner.metamaskaddressesgenerator.models.SWFMetaMaskETHAddressGenerator
import com.cloner.metamaskaddressesgenerator.models.SWFMetaMaskSOLAddressGenerator
import com.cloner.metamaskaddressesgenerator.models.SWFMetaMaskTRXAddressGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object SWFMetaMaskAddressGenerator {


    fun generateAddresses(
        coins: List<String>,
        mnemonic: String,
        passphrase: String,
        isTestNet: Boolean
    ): Flow<String> = flow {
        if(coins.isEmpty() || mnemonic.isEmpty()) return@flow

        coins.forEach { coin ->
            val address = generateAddress(
                coin = coin,
                mnemonic = mnemonic,
                passphrase = passphrase,
                isTestNet = isTestNet
            )
            if(address != null) emit(value = address)
        }
    }

    fun generateAddress(
        coin: String,
        mnemonic: String,
        passphrase: String,
        isTestNet: Boolean
    ): String? = runCatching {
        require(isValidCoin(coin))

        val coinType = SWFMetaMaskCoin.valueOf(coin)
        when (coinType) {
            SWFMetaMaskCoin.ETHEREUM -> return SWFMetaMaskETHAddressGenerator.generateAddress(
                mnemonic = mnemonic,
                passphrase = passphrase,
                isTestNet = isTestNet
            )

            SWFMetaMaskCoin.BITCOIN -> return SWFMetaMaskBTCAddressGenerator.generateAddress(
                mnemonic = mnemonic,
                passphrase = passphrase,
                isTestNet = isTestNet
            )

            SWFMetaMaskCoin.SOLANA -> return SWFMetaMaskSOLAddressGenerator.generateAddress(
                mnemonic = mnemonic,
                passphrase = passphrase
            )

            SWFMetaMaskCoin.TRON -> return SWFMetaMaskTRXAddressGenerator.generateAddress(
                mnemonic = mnemonic,
                passphrase = passphrase
            )

            SWFMetaMaskCoin.LINEA,
            SWFMetaMaskCoin.ARBITRUM,
            SWFMetaMaskCoin.BASE,
            SWFMetaMaskCoin.MONAD,
            SWFMetaMaskCoin.OPTIMISM,
            SWFMetaMaskCoin.POLYGON,
            SWFMetaMaskCoin.BINANCE -> return SWFMetaMaskBNBAddressGenerator.generateAddress(
                mnemonic = mnemonic,
                passphrase = passphrase,
                isTestNet = isTestNet
            )
        }
    }.getOrNull()


    fun isValidCoin(coin: String): Boolean = runCatching {
        SWFMetaMaskCoin.valueOf(coin)
    }.isSuccess
}