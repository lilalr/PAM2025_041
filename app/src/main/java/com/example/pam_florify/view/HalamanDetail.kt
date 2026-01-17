package com.example.pam_florify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.pam_florify.ui.theme.GreenDark
import com.example.pam_florify.ui.theme.GreenLight
import com.example.pam_florify.viewmodel.DetailTanamanViewModel
import com.example.pam_florify.viewmodel.PenyediaViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanDetail(
    navigateBack: () -> Unit,
    navigateToEditItem: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailTanamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiStateDetailTanaman
    val uiStateAktivitas = viewModel.uiStateAktivitas
    val srsLightGreen = Color(0xFFD1DBC5)

    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage = viewModel.snackbarMessage

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetSnackbarMessage()
            // Jika tanaman dihapus, otomatis kembali ke Home
            if (it.contains("dihapus", ignoreCase = true)) {
                navigateBack()
            }
        }
    }

    // --- STATE MENU TITIK TIGA ---
    var menuExpanded by remember { mutableStateOf(false) }
    var showDeleteTanamanDialog by remember { mutableStateOf(false) }

    var expandedDropdown by remember { mutableStateOf(false) }
    val opsiAktivitas = listOf("Penyiraman", "Pemupukan", "Pemangkasan", "Repotting")
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var showDeleteAktivitasDialog by remember { mutableStateOf(false) }
    var selectedAktivitasId by remember { mutableIntStateOf(0) }

    if (showDeleteTanamanDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteTanamanDialog = false },
            title = { Text(
                "Hapus Tanaman") },
            text = { Text(
                "Apakah Anda yakin ingin menghapus '${uiState.detailTanaman.nama}'? Data ini tidak bisa dikembalikan.") },
            confirmButton = {
                TextButton(
                    onClick = {
                    viewModel.hapusTanaman()
                    showDeleteTanamanDialog = false
                }) { Text(
                    "Hapus", color = Color.Red) }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                    showDeleteTanamanDialog = false }) {
                    Text(
                    "Batal") }
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis
                    if (selectedDate != null) {
                        val formattedDate = formatter.format(Date(selectedDate))
                        viewModel.updateAktivitasUiState(uiStateAktivitas.detailAktivitas.copy(tanggalAktivitas = formattedDate))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Batal") } }
        ) { DatePicker(state = datePickerState) }
    }

    if (showDeleteAktivitasDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAktivitasDialog = false },
            title = {
                Text(
                    "Hapus Aktivitas") },
            text = {
                Text(
                    "Hapus catatan aktivitas ini?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.hapusAktivitas(selectedAktivitasId)
                    showDeleteAktivitasDialog = false
                }) { Text(
                    "Hapus", color = Color.Red) }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteAktivitasDialog = false }) {
                    Text(
                        "Batal") } }
        )
    }

    Scaffold(
        containerColor = srsLightGreen,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(color = GreenLight, shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = "Back", tint = GreenDark)
                    }
                    Text(
                        text = "Detail Tanaman",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenDark,
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    )

                    Box {
                        IconButton(
                            onClick = { menuExpanded = true }) {
                            Icon(
                                Icons.Default.MoreVert, contentDescription = "Menu", tint = GreenDark)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Edit Tanaman") },
                                onClick = {
                                    menuExpanded = false
                                    navigateToEditItem(uiState.detailTanaman.tanamanId)
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Edit, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Hapus Tanaman", color = Color.Red) },
                                onClick = {
                                    menuExpanded = false
                                    showDeleteTanamanDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
                            )
                        }
                    }
                }
                Text(
                    text = uiState.detailTanaman.nama,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenDark,
                    modifier = Modifier.align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 12.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier
                        .padding(16.dp)) {
                        AsyncImage(
                            model = uiState.detailTanaman.foto,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier
                            .height(12.dp))
                        Surface(color = GreenDark, shape = RoundedCornerShape(50)) {
                            Text(
                                text = "ID Kategori: ${uiState.detailTanaman.kategoriId}",
                                color = Color.White, fontSize = 11.sp,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }
                        Text(text = uiState.detailTanaman.nama, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                    }
                }
                Spacer(modifier = Modifier
                    .height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Aktivitas Perawatan", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = GreenDark)
                    if (!viewModel.showFormAktivitas) {
                        Button(
                            onClick = { viewModel.toggleFormAktivitas() },
                            colors = ButtonDefaults.buttonColors(containerColor = GreenDark),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Tambah", fontSize = 14.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            items(uiState.listAktivitas) { aktivitas ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(color = GreenDark, shape = RoundedCornerShape(50)) {
                                    Text(text = aktivitas.tipeAktivitas, color = Color.White, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                }
                                Spacer(Modifier.width(8.dp))
                                Text(text = aktivitas.tanggalAktivitas, fontSize = 12.sp, color = Color.Gray)
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(text = aktivitas.notes, fontSize = 14.sp, color = Color.Black)
                        }
                        Row {
                            IconButton(onClick = { viewModel.toggleFormAktivitas(aktivitas) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = {
                                selectedAktivitasId = aktivitas.aktivitasId
                                showDeleteAktivitasDialog = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Gray, modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }
            }

            if (viewModel.showFormAktivitas) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = if (viewModel.isEditMode) "Edit Aktivitas" else "Tambah Aktivitas Baru", fontWeight = FontWeight.Bold, color = GreenDark)
                            Spacer(Modifier.height(12.dp))
                            ExposedDropdownMenuBox(
                                expanded = expandedDropdown,
                                onExpandedChange = { expandedDropdown = !expandedDropdown }
                            ) {
                                OutlinedTextField(
                                    value = uiStateAktivitas.detailAktivitas.tipeAktivitas,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Pilih Jenis *") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(expanded = expandedDropdown, onDismissRequest = { expandedDropdown = false }) {
                                    opsiAktivitas.forEach { opsi ->
                                        DropdownMenuItem(
                                            text = { Text(opsi) },
                                            onClick = {
                                                viewModel.updateAktivitasUiState(uiStateAktivitas.detailAktivitas.copy(tipeAktivitas = opsi))
                                                expandedDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiStateAktivitas.detailAktivitas.tanggalAktivitas,
                                onValueChange = {},
                                label = { Text("Tanggal Aktivitas *") },
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(Icons.Default.DateRange, contentDescription = "Pilih Tanggal")
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                                shape = RoundedCornerShape(12.dp),
                                enabled = false,
                                colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = Color.Gray, disabledLabelColor = GreenDark)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = uiStateAktivitas.detailAktivitas.notes,
                                onValueChange = { viewModel.updateAktivitasUiState(uiStateAktivitas.detailAktivitas.copy(notes = it)) },
                                label = { Text("Catatan") },
                                modifier = Modifier.fillMaxWidth().height(100.dp),
                                shape = RoundedCornerShape(12.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(onClick = { viewModel.toggleFormAktivitas() }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp)) { Text("Batal") }
                                Button(onClick = { viewModel.simpanAktivitas() }, enabled = uiStateAktivitas.isEntryValid, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = GreenDark), shape = RoundedCornerShape(12.dp)) {
                                    Text(if (viewModel.isEditMode) "Perbarui" else "Simpan")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}