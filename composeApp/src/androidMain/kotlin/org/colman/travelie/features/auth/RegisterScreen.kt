package org.colman.travelie.features.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.colman.travelie.features.destinations.ErrorContent
import org.colman.travelie.features.destinations.LoadingContent
import org.koin.androidx.compose.koinViewModel

private val Navy = Color(0xFF263238)
private val LightGray = Color(0xFFECEFF1)
private val Lavender = Color(0xFFDAD4DA)
private val Beige = Color(0xFFD7B8A5)
private val Terracotta = Color(0xFFC97C5D)

@Composable
fun RegisterScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val uiState = viewModel.uiState.collectAsState().value
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Register", fontSize = 24.sp, color = Navy)

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it.trim() },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = Lavender,
                        cursorColor = Terracotta
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Terracotta,
                        unfocusedBorderColor = Lavender,
                        cursorColor = Terracotta
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { viewModel.register(email.trim(), password.trim()) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Text("Register", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Already have an account? Log in",
                    color = Navy,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )

                when (uiState) {
                    is AuthState.Error -> ErrorContent(uiState.errorMessage)
                    is AuthState.Loading -> LoadingContent()
                    is AuthState.Loaded -> {
                        if (uiState.user != null) {
                            Text(
                                text = "Registration successful! Welcome, ${uiState.user!!.email}",
                                color = Navy,
                                modifier = Modifier.padding(top = 16.dp)
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
