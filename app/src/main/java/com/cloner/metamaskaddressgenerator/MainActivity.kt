package com.cloner.metamaskaddressgenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.cloner.metamaskaddressesgenerator.SWFMetaMaskAddressGenerator
import com.cloner.metamaskaddressesgenerator.enums.SWFMetaMaskCoin
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mnemonic = "clap fine burger bus boat used route camera plate palace wise hand"
        val coins = SWFMetaMaskCoin.entries

        coins.forEach { coin ->
            val address = SWFMetaMaskAddressGenerator.generateAddress(
                coin = coin.toString(),
                mnemonic = mnemonic,
                passphrase = "",
                isTestNet = false
            )

            Log.e("SWFMetaMask Addresses", coin.toString() + ": " + (address ?: "null"))
        }

        runBlocking {
            SWFMetaMaskAddressGenerator.generateAddresses(
                coins = listOf("BITCOIN", "TRON"),
                mnemonic = mnemonic,
                passphrase = "",
                isTestNet = false
            ).collect { addressInfo ->
                Log.e("SWFMetaMask Addresses", "${addressInfo.coin}: ${addressInfo.address}" )
            }
        }
    }
}
