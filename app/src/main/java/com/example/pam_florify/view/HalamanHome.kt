package com.example.pam_florify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pam_florify.R
import com.example.pam_florify.modeldata.Tanaman
import com.example.pam_florify.ui.theme.GreenDark
import com.example.pam_florify.ui.theme.GreenLight
import com.example.pam_florify.viewmodel.HomeUiState
import com.example.pam_florify.viewmodel.HomeViewModel
import com.example.pam_florify.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanHome(
    userId: Int,
    username: String,
    onStart: (HomeViewModel) -> Unit,
    navigateToItemEntry: () -> Unit,
    onDetailClick: (Int) -> Unit,
    navigateToEditItem: (Int) -> Unit,
    onLogoutClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tanamanToDelete by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(userId) {
        onStart(viewModel)
    }

    val srsLightGreen = Color(0xFFD1DBC5)
    val snackbarHostState = remember { SnackbarHostState() }
    val message = viewModel.snackbarMessage

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetSnackbarMessage()
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    "Konfirmasi Logout",
                    fontWeight = FontWeight.Bold,
                    color = GreenDark
                )
            },
            text = {
                Text("Apakah Anda yakin ingin keluar dari aplikasi?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Ya, Keluar", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = GreenDark
                    )
                ) {
                    Text("Batal")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    if (showDeleteDialog && tanamanToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    "Hapus Tanaman",
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            },
            text = {
                Text("Apakah Anda yakin ingin menghapus tanaman ini? Data yang dihapus tidak dapat dikembalikan.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        tanamanToDelete?.let { id ->
                            viewModel.deleteTanaman(id, userId)
                        }
                        showDeleteDialog = false
                        tanamanToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )
                ) {
                    Text("Hapus", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        showDeleteDialog = false
                        tanamanToDelete = null
                    }
                ) {
                    Text("Batal")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }

    Scaffold(
        containerColor = srsLightGreen,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = if (message?.contains("berhasil", ignoreCase = true) == true)
                        Color(0xFF4CAF50)
                    else
                        Color(0xFFE53935),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(
                        color = GreenLight,
                        shape = RoundedCornerShape(bottomStart = 35.dp, bottomEnd = 35.dp)
                    )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(38.dp),
                            shape = CircleShape,
                            color = Color.White
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logoflorify),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = GreenDark
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Florify",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenDark
                        )
                    }

                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout,
                            contentDescription = "Logout",
                            tint = GreenDark
                        )
                    }
                }

                Text(
                    text = "Halo, $username! 👋",
                    fontSize = 16.sp,
                    color = GreenDark,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 24.dp, bottom = 12.dp)
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                containerColor = GreenDark,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Spacer(modifier = Modifier.height(30.dp))
            when (val state = viewModel.homeUiState) {
                is HomeUiState.Loading -> Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        color = GreenDark,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HomeUiState.Error -> Box(Modifier.fillMaxSize()) {
                    Text(
                        "Gagal memuat data",
                        color = GreenDark,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is HomeUiState.Success -> {
                    if (state.tanaman.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(id = R.drawable.logoflorify),
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = GreenDark.copy(alpha = 0.3f)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Belum ada data tanaman",
                                    color = GreenDark.copy(alpha = 0.6f),
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tap tombol + untuk menambah tanaman",
                                    color = GreenDark.copy(alpha = 0.4f),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            item {
                                Text(
                                    text = "Tanaman Saya",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GreenDark
                                )
                            }
                            items(state.tanaman) { item ->
                                ItemTanamanCard(
                                    tanaman = item,
                                    onDetailClick = { onDetailClick(item.tanamanId) },
                                    onEditClick = { navigateToEditItem(item.tanamanId) },
                                    onDeleteClick = {
                                        tanamanToDelete = item.tanamanId
                                        showDeleteDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemTanamanCard(
    tanaman: Tanaman,
    onDetailClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onDetailClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            AsyncImage(
                model = tanaman.foto,
                contentDescription = tanaman.nama,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(Color(0xFFF0F0F0), RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.logoflorify)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = tanaman.nama,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = GreenDark
                    )
                    Text(
                        text = "Kategori: ${tanaman.kategoriNama}",
                        fontSize = 13.sp,
                        color = GreenDark
                    )
                    Text(
                        text = tanaman.deskripsi,
                        fontSize = 13.sp,
                        maxLines = 2,
                        color = Color.Black
                    )
                }

                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.Gray
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                expanded = false
                                onEditClick()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Edit, contentDescription = null)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Hapus", color = Color.Red) },
                            onClick = {
                                expanded = false
                                onDeleteClick()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}