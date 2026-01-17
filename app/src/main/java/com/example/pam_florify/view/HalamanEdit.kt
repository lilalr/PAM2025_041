package com.example.pam_florify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* 
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pam_florify.viewmodel.EditTanamanViewModel
import com.example.pam_florify.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanEdit(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditTanamanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val message = viewModel.snackbarMessage
    val scope = rememberCoroutineScope() // Coroutine scope untuk snackbar

    LaunchedEffect(message) {
        message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.resetSnackbarMessage()

                if (it.contains("berhasil", ignoreCase = true)) {
                    onNavigateUp()
                }
            }
        }
    }
    
    val srsGreenBackground = Color(0xFFD1DBC5)
    val srsGreenHeader = Color(0xFF90A48A)
    val srsDarkGreen = Color(0xFF2D4029)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = srsGreenBackground,
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
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = srsDarkGreen
                        )
                    }
                    Text(
                        text = "Edit Data Tanaman",
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
                viewModel.updateTanaman()
            },
            onCancelClick = onNavigateUp,
            primaryColor = srsDarkGreen,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}
