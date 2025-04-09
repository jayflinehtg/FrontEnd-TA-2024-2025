package com.example.compose_ta09.ui.viewModels

import android.content.Context
import android.util.Log
import com.example.compose_ta09.services.SharedPreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.metamask.androidsdk.EthereumFlow
import io.metamask.androidsdk.EthereumMethod
import io.metamask.androidsdk.EthereumRequest
import io.metamask.androidsdk.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigInteger
import javax.inject.Inject

data class UiStateMetaMask(
    val ethAddress: String? = null,
    val isConnecting: Boolean = false,
    val isTransactionSuccess: Boolean = false,
    val balance: String? = null,
)

sealed class UiEventMetaMask {
    data class Message(val error: String) : UiEventMetaMask()
}

sealed class EventSinkMetaMask {
    data object Connect : EventSinkMetaMask()
    data object WalletConnected : EventSinkMetaMask() // Event untuk WalletConnected
    data object ConnectionFailed : EventSinkMetaMask() // Event untuk ConnectionFailed
    data class SendRequest(val transactionData: String) : EventSinkMetaMask()
    data object GetBalance : EventSinkMetaMask()
    data object Disconnect : EventSinkMetaMask()
    data object CheckMetaMaskInstalled : EventSinkMetaMask() // Event untuk memeriksa apakah MetaMask terpasang
    data class SendWalletAddress(val address: String) : EventSinkMetaMask() // Event untuk mengirim alamat wallet
}

class EventSinkMetaMaskHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ethereum: EthereumFlow
) {

    private val _uiEvent = MutableSharedFlow<UiEventMetaMask>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow(UiStateMetaMask())
    val uiState = _uiState.asStateFlow()

    private fun showMessage(message: String) {
        // Gunakan coroutineScope.launch di sini karena ini membutuhkan coroutine context
        CoroutineScope(Dispatchers.Main).launch {
            _uiEvent.emit(UiEventMetaMask.Message(message))
        }
    }

    fun eventSink(eventSink: EventSinkMetaMask) {
        // Pastikan menggunakan coroutine scope yang benar untuk meluncurkan coroutine
        CoroutineScope(Dispatchers.Main).launch {
            when (val event = eventSink) {
                EventSinkMetaMask.Connect -> {
                    val result = ethereum.connect()
                    when (result) {
                        is Result.Error -> {
                            _uiState.update { it.copy(isConnecting = false) }
                            showMessage(result.error.message)
                            Log.e("EventSinkMetaMask", "Connection failed: ${result.error.message}")
                        }
                        is Result.Success -> {
                            _uiState.update { it.copy(isConnecting = true, ethAddress = ethereum.selectedAddress) }
                            Log.d("EventSinkMetaMask", "Connected successfully!")
                        }
                    }
                }

                EventSinkMetaMask.WalletConnected -> {
                    // Handle wallet connected logic
                    _uiState.update { it.copy(isConnecting = false) }
                    showMessage("Wallet Connected Successfully!")
                }

                EventSinkMetaMask.ConnectionFailed -> {
                    // Handle connection failure logic
                    _uiState.update { it.copy(isConnecting = false) }
                    showMessage("Connection to MetaMask Failed!")
                }

                is EventSinkMetaMask.SendRequest -> {
                    val (transactionData) = event
                    when (val result = ethereum.sendRequest(
                        EthereumRequest(
                            method = EthereumMethod.ETH_SEND_TRANSACTION.value,
                            params = listOf(
                                mapOf(
                                    "to" to "YOUR_CONTRACT_ADDRESS", // Ganti dengan alamat kontrak kamu
                                    "from" to ethereum.selectedAddress,
                                    "data" to transactionData
                                )
                            )
                        )
                    )) {
                        is Result.Success.Item -> {
                            _uiState.update { it.copy(isTransactionSuccess = true) }
                            Log.d("EventSinkMetaMask", result.toString())
                        }
                        else -> {
                            _uiState.update { it.copy(isTransactionSuccess = false) }
                            Log.d("EventSinkMetaMask", result.toString())
                        }
                    }
                }

                EventSinkMetaMask.GetBalance -> {
                    val balanceResult = ethereum.sendRequest(
                        EthereumRequest(
                            method = EthereumMethod.ETH_GET_BALANCE.value,
                            params = listOf(ethereum.selectedAddress, "latest")
                        )
                    )
                    when (balanceResult) {
                        is Result.Error -> showMessage(balanceResult.error.message)
                        is Result.Success.Item -> {
                            val cleanHexString = if (balanceResult.value.startsWith("0x")) {
                                balanceResult.value.substring(2)
                            } else {
                                balanceResult.value
                            }
                            _uiState.update { it.copy(balance = "${BigInteger(cleanHexString, 16)} ETH") }
                        }
                        else -> _uiState.update { it.copy(balance = "NA") }
                    }
                }

                EventSinkMetaMask.Disconnect -> {
                    _uiState.update { it.copy(isConnecting = false) }
                    ethereum.disconnect(true)
                    showMessage("Disconnected!")
                }

                EventSinkMetaMask.CheckMetaMaskInstalled -> {
                    val isInstalled = isMetaMaskInstalled(context)
                    if (isInstalled) {
                        showMessage("MetaMask is installed.")
                    } else {
                        showMessage("MetaMask is not installed.")
                    }
                }

                is EventSinkMetaMask.SendWalletAddress -> {
                    val (address) = event
                    showMessage("Wallet address received: $address")
                    // You can process the wallet address here (e.g., save it or use it)
                }

                else -> {
                    showMessage("Unknown event: $event")
                    Log.e("EventSinkMetaMask", "Unknown event: $event")
                }
            }
        }
    }

    private fun isMetaMaskInstalled(context: Context): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo("io.metamask", 0)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun updateBalance() {
        if (ethereum.selectedAddress.isNotEmpty()) {
            eventSink(EventSinkMetaMask.GetBalance)
            showMessage("Fetching the wallet balance")
        } else {
            showMessage("The wallet is not connected!")
        }
    }

    fun sendRequest(transactionData: String) {
        if (ethereum.selectedAddress.isNotEmpty()) {
            showMessage("Melakukan Request Transaksi")
            eventSink(EventSinkMetaMask.SendRequest(transactionData))
        } else {
            showMessage("Kamu belum terhubung dengan MetaMask")
        }
    }

    fun resetIsTransaction() {
        _uiState.update { it.copy(isTransactionSuccess = false) }
    }
}
