package com.example.callbackflowapinetworkcheck.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

//To check if application is connected to Internet or not

/*Extension function*/
@ExperimentalCoroutinesApi
fun Context.checkNetwork():Flow<Boolean> = callbackFlow {    //we directly wanna use this fun in MainActivity/Frag ..so this way it'll be easy to do so.
    //1. Callback
    val networkCallbacks = object : ConnectivityManager.NetworkCallback() { //It's a default callback, which checks if App is connected to Internet or not

        override fun onAvailable(network: Network) {    //Connected with Internet
            super.onAvailable(network)
            offer(true) //To Emit the data ..which will be collected by Collector.
        }

        override fun onLost(network: Network) {     //Isn't Connected
            super.onLost(network)
            offer(false)
        }
    }

    //2. Manager - for checking Internet status we have to make System call..manager ee System call kari aapshe
    val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //3. Manager ne callback thi register pan karavu padshe
    //Manager will do system call(using getSystemService()) and onAvailable/onLost ne Internet connectivity nu status aapshe
    manager.registerNetworkCallback(NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build(), networkCallbacks)  //NetworkCapabilities.NET_CAPABILITY_INTERNET => Internet status get karva

    //4. To prevent memory leak, remove callback as when we remove App, Internet use na thavu joie => awaitClose{}
    awaitClose {    //It will understand when you close Application...and block ma mentioned task execute kari deshe
        manager.unregisterNetworkCallback(networkCallbacks)
    }
}

//callbackFlow -> Convert callbackFlow{} ma used callback into FLow.

/* What we did here ?
* 1. Created Network callback..to get the network status (Aa check krvanu kaam manager kari aapshe)
* 2. Added Manager(ConnectivityManager) which will check Connectivity status
* 3. Registered callback with Manager
* 4. awaitClose =>  awaitClose is mandatory in order to prevent memory leaks when the flow collection is cancelled,
*                    otherwise the callback may keep running even when the flow collector is already completed. To avoid such leaks,
*                    this method throws IllegalStateException if block returns, but the channel is not closed yet.
* */

fun EditText.checkEtTextChange(): Flow<Editable?> = callbackFlow { //Editable -> when we type new text we get this type od data, Its a NEW text.
    //1. Callback
    val textWatcherCallback = object  : TextWatcher{
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun afterTextChanged(newText: Editable?) {
            //Je text type thahe, eene aapde Emit karavishu..jene collector collect kari lishe
            offer(newText)
        }
    }
    //2. Attached callback with text change listener
    addTextChangedListener(textWatcherCallback)

    /*3. unregister callback*/
    awaitClose {    //Application destroy thata, badha callbacks remove karva
        removeTextChangedListener(textWatcherCallback)
    }
}