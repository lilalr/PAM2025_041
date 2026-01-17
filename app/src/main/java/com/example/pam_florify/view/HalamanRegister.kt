package com.example.pam_florify.view

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.pam_florify.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pam_florify.ui.theme.GreenDark
import com.example.pam_florify.ui.theme.GreenLight
import com.example.pam_florify.viewmodel.PenyediaViewModel
import com.example.pam_florify.viewmodel.RegisterViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HalamanRegister(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit,
    viewModel: RegisterViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val message = viewModel.snackbarMessage

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.resetSnackbarMessage()
            if (it == "Registrasi Berhasil!") {
                delay(1500)
                onRegisterSuccess()
            }
        }
    }

    // ✅ GUNAKAN BOX WRAPPER
    Box(modifier = Modifier.fillMaxSize().background(GreenDark)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(GreenLight)
        ) {
            // --- HEADER LOGO ---
            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(65.dp),
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.logoflorify),
                            contentDescription = null,
                            modifier = Modifier.size(55.dp),
                            tint = GreenDark
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Florify",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Nurture Your Plants, Nurture Life",
                    color = Color.White
                )
            }

            // --- FORM REGISTRASI ---
            Surface(
                modifier = Modifier.fillMaxWidth().weight(2.5f),
                color = GreenDark,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp).fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Registrasi",
                        style = MaterialTheme.typography.displaySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Welcome!", color = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.height(24.dp))

                    // FIELD 1: USERNAME
                    OutlinedTextField(
                        value = viewModel.username,
                        onValueChange = { viewModel.username = it },
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = GreenDark
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // FIELD 2: EMAIL
                    OutlinedTextField(
                        value = viewModel.email,
                        onValueChange = { viewModel.email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = GreenDark
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // FIELD 3: PASSWORD
                    OutlinedTextField(
                        value = viewModel.password,
                        onValueChange = { viewModel.password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation = if (passwordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.Visibility
                            else
                                Icons.Filled.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = image,
                                    contentDescription = null,
                                    tint = GreenDark
                                )
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.Gray,
                            focusedBorderColor = Color.White,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = GreenDark
                        )
                    )

                    Button(
                        onClick = { viewModel.register() },
                        interactionSource = interactionSource,
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isPressed) GreenLight else Color.White,
                            contentColor = GreenDark
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Daftar", fontWeight = FontWeight.Bold)
                    }

                    TextButton(onClick = onLoginClick) {
                        Text("Sudah punya akun? Login!", color = Color.White)
                    }
                }
            }
        }

        // ✅ SNACKBAR DI ATAS DENGAN BOX
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (message == "Registrasi Berhasil!")
                        Color(0xFF4CAF50)
                    else
                        Color(0xFFE53935),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}