package com.cloner.metamaskaddressgenerator

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.cloner.metamaskaddressesgenerator.SWFMetaMaskAddressGenerator
import com.cloner.metamaskaddressesgenerator.enums.SWFMetaMaskCoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val seed = "clap fine burger bus boat used route camera plate palace wise hand"
        val coins = SWFMetaMaskCoin.entries

        coins.forEach { coin ->
            val address = SWFMetaMaskAddressGenerator.generateAddress(
                coin = coin.toString(),
                mnemonic = seed,
                passphrase = "",
                isTestNet = false
            )

            Log.e("SWFMetaMask Addresses", coin.toString() + ": " + (address ?: "null"))
        }
    }
}
