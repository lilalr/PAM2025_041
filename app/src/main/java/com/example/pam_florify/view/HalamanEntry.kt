package com.example.pam_florify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pam_florify.modeldata.DetailTanaman
import com.example.pam_florify.modeldata.UIStateTanaman
import com.example.pam_florify.viewmodel.EntryViewModel
import com.example.pam_florify.viewmodel.PenyediaViewModel
import com.example.pam_florify.ui.theme.GreenDark
import com.example.pam_florify.ui.theme.GreenLight
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEntry(
    userId: Int,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EntryViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val srsGreenBackground = Color(0xFFD1DBC5)
    val srsGreenHeader = GreenLight
    val srsDarkGreen = GreenDark

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(viewModel.isSaveCompleted) {
        if (viewModel.isSaveCompleted) {
            navigateBack()
            viewModel.onSaveCompletedHandled()
        }
    }

    Scaffold(
        containerColor = srsGreenBackground,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) 
        },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        color = srsGreenHeader,
                        shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                    )
                    .padding(top = 24.dp, start = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = srsDarkGreen
                        )
                    }
                    Text(
                        text = "Form Tanaman",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = srsDarkGreen
                    )
                }
            }
        }
    ) { innerPadding ->
        EntryTanamanBody(
            uiStateTanaman = viewModel.uiStateTanaman,
            onTanamanValueChange = viewModel::updateUiState,
            onSaveClick = {
                 scope.launch {
                    viewModel.addTanaman(userId)
                    snackbarHostState.showSnackbar("Tanaman berhasil ditambahkan!")
                }
            },
            onCancelClick = navigateBack,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            primaryColor = srsDarkGreen
        )
    }
}

// EntryTanamanBody dan FormInputTanaman tetap sama seperti sebelumnya...

@Composable
fun EntryTanamanBody(
    uiStateTanaman: UIStateTanaman,
    onTanamanValueChange: (DetailTanaman) -> Unit,
    onSaveClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    primaryColor: Color
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onTanamanValueChange(uiStateTanaman.detailTanaman.copy(foto = it.toString())) }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- BAGIAN UPLOAD FOTO ---
                Text(text = "Foto Tanaman", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { launcher.launch("image/*") },
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray),
                    color = Color(0xFFF9F9F9)
                ) {
                    if (uiStateTanaman.detailTanaman.foto.isNotEmpty()) {
                        AsyncImage(
                            model = uiStateTanaman.detailTanaman.foto,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.FileUpload, contentDescription = null, tint = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Ketuk untuk pilih foto", color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }

                // --- FORM INPUT ---
                FormInputTanaman(
                    detailTanaman = uiStateTanaman.detailTanaman,
                    onValueChange = onTanamanValueChange,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- TOMBOL AKSI ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onCancelClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, primaryColor)
                    ) {
                        Text("Batal", color = primaryColor)
                    }
                    Button(
                        onClick = onSaveClick,
                        enabled = uiStateTanaman.isEntryValid,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            disabledContainerColor = Color.LightGray
                        )
                    ) {
                        Text("Simpan", color = Color.White)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInputTanaman(
    detailTanaman: DetailTanaman,
    modifier: Modifier = Modifier,
    onValueChange: (DetailTanaman) -> Unit = {},
    enabled: Boolean = true
) {
    val daftarKategori = listOf("Tanaman Hias", "Tanaman Obat", "Tanaman Buah")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Nama Tanaman *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        OutlinedTextField(
            value = detailTanaman.nama,
            onValueChange = { onValueChange(detailTanaman.copy(nama = it)) },
            placeholder = { Text("Contoh: Lidah Mertua") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenDark,
                focusedLabelColor = GreenDark
            )
        )

        Text(text = "Kategori Tanaman *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = when (detailTanaman.kategoriId) {
                    1 -> "Tanaman Hias"
                    2 -> "Tanaman Obat"
                    3 -> "Tanaman Buah"
                    else -> ""
                },
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Pilih Kategori") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenDark,
                    unfocusedBorderColor = Color.LightGray
                )
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                daftarKategori.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item, color = Color.Black) },
                        onClick = {
                            val idKategori = when (item) {
                                "Tanaman Hias" -> 1
                                "Tanaman Obat" -> 2
                                "Tanaman Buah" -> 3
                                else -> 0
                            }
                            onValueChange(detailTanaman.copy(kategoriId = idKategori))
                            expanded = false
                        }
                    )
                }
            }
        }

        Text(text = "Deskripsi *", fontWeight = FontWeight.Bold, fontSize = 14.sp)
        OutlinedTextField(
            value = detailTanaman.deskripsi,
            onValueChange = { onValueChange(detailTanaman.copy(deskripsi = it)) },
            placeholder = { Text("Ceritakan tentang tanaman Anda...") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            enabled = enabled,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GreenDark,
                focusedLabelColor = GreenDark
            )
        )
    }
}