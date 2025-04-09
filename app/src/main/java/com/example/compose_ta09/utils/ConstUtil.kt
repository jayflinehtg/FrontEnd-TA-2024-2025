package com.example.compose_ta09.utils

object ConstUtil {
    // Base URL untuk backend API
    val BASEURL = "http://192.168.1.18"

    // URL API backend
    val apiBackend = "${BASEURL}:4001/v1/"

    // API key untuk Infura (Ethereum node provider)
    val ethInfuraApiKey = "98144ec0a3b54b3582ccdd2e99921cf5"  // Ganti dengan kunci Infura-mu

    // Network ID untuk Ethereum, pastikan sesuai dengan jaringan yang digunakan
    val ethNetworkId = "0x16B98" // Ganti dengan network ID yang sesuai (Testnet atau Mainnet)

    // URL RPC untuk Ethereum (untuk berkomunikasi dengan node Ethereum)
    val ethRpcUrl = "https://sepolia.infura.io/v3/98144ec0a3b54b3582ccdd2e99921cf5" // Ganti dengan URL RPC Infura yang sesuai

    // Ethereum contract address (misalnya alamat kontrak token)
    val ethContractAddress = "0xc43F5dE07e0Ea6754329365D1A89A2f6fc53Ab9C" // Ganti dengan alamat kontrak Ethereum yang sesuai
}
