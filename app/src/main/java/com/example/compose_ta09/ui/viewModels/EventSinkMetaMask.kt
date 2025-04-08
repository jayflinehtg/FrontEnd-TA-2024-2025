package com.example.compose_ta09.ui.viewModels

// EventSink untuk menghubungkan dan memutuskan koneksi MetaMask
sealed class EventSinkMetaMask {
    object Connect : EventSinkMetaMask()   // Event untuk memulai koneksi
    object Disconnect : EventSinkMetaMask() // Event untuk memutuskan koneksi
    object WalletConnected : EventSinkMetaMask() // Event untuk tanda wallet berhasil terhubung
    object ConnectionFailed : EventSinkMetaMask() // Event untuk tanda koneksi gagal
}
