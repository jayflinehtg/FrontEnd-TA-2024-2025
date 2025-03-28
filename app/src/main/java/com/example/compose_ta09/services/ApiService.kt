package com.example.compose_ta09.services

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

object ApiService {
    private val client = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json { ignoreUnknownKeys = true })
        }
    }

    // Endpoint untuk login
    suspend fun login(password: String): HttpResponse {
        return client.post("http://10.0.2.2:5000/api/auth/login") {
            header(HttpHeaders.ContentType, "application/json")
            body = """
                {
                    "password": "$password"
                }
            """
        }
    }

    // Endpoint untuk register
    suspend fun register(fullName: String, password: String): HttpResponse {
        return client.post("http://10.0.2.2:5000/api/auth/register") {
            header(HttpHeaders.ContentType, "application/json")
            body = """
                {
                    "fullName": "$fullName",
                    "password": "$password"
                }
            """
        }
    }
}
