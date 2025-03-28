package com.example.compose_ta09

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.compose_ta09.ui.theme.COMPOSE_TA09Theme

@Composable
fun TambahScreenContent(navController: NavController) { // Renamed to avoid confusion
    var namaTanaman by remember { mutableStateOf("") }
    var namaLatin by remember { mutableStateOf("") }
    var komposisi by remember { mutableStateOf("") }
    var manfaat by remember { mutableStateOf("") }
    var caraPengolahan by remember { mutableStateOf("") }
    var gambarUri by remember { mutableStateOf<Uri?>(null) }

    var namaTanamanError by remember { mutableStateOf(false) }
    var manfaatError by remember { mutableStateOf(false) }
    var caraPengolahanError by remember { mutableStateOf(false) }
    var gambarError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            gambarUri = uri
        }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.soft_green))
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        // ====================
        // HEADER DENGAN LOGO
        // ====================
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Spacer(modifier = Modifier.height(7.dp))
            Image(
                painter = painterResource(id = R.drawable.plant),
                contentDescription = "Logo Tanaman",
                modifier = Modifier.size(70.dp)
            )

            // Header
            Column (
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text(
                    text = "Tambah Data Tanaman",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green),
                )
                Text(
                    text = "Tanaman Herbal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.dark_green),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                InputField("Nama Tanaman *", namaTanaman, { namaTanaman = it }, singleLine = true, isError = namaTanamanError)
                InputField("Nama Latin Tanaman", namaLatin, { namaLatin = it }, singleLine = true)
                InputField("Komposisi/Kandungan", komposisi, { komposisi = it })
                InputField("Manfaat Tanaman *", manfaat, { manfaat = it }, isError = manfaatError)
                InputField("Cara Pengolahan *", caraPengolahan, { caraPengolahan = it }, isError = caraPengolahanError)

                Spacer(modifier = Modifier.height(8.dp))

                // Pilih Gambar
                Text("Gambar Tanaman *", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                Button(
                    onClick = { pickImageLauncher.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.green)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Pilih Gambar", color = Color.Black)
                }
                if (gambarError) {
                    Text("Gambar harus dipilih!", color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Button Simpan
                Button(
                    onClick = {
                        namaTanamanError = namaTanaman.isBlank()
                        manfaatError = manfaat.isBlank()
                        caraPengolahanError = caraPengolahan.isBlank()
                        gambarError = gambarUri == null

                        if (!(namaTanamanError || manfaatError || caraPengolahanError || gambarError)) {
                            // Handle save data jika valid
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.dark_green)),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text(text = "Simpan", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, singleLine: Boolean = false, isError: Boolean = false) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorResource(id = R.color.dark_green),
                unfocusedBorderColor = Color.Gray,
                cursorColor = Color.Black
            ),
            textStyle = TextStyle(color = Color.Black),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else Int.MAX_VALUE,
            modifier = Modifier.fillMaxWidth()
        )
        if (isError) {
            Text("Field ini harus diisi!", color = Color.Red, fontSize = 12.sp)
        }
    }
}