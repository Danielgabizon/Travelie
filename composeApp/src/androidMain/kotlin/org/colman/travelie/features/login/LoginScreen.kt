package org.colman.travelie.features.login
import org.colman.travelie.ui.theme.*

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
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import org.colman.travelie.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    when (uiState) {
        is LoginState.Idle,
        is LoginState.Error -> {
            LoginForm(
                email = email,
                password = password,
                onEmailChange = { email = it.trim() },
                onPasswordChange = { password = it },
                onLoginClick = { viewModel.login(email, password) },
                onNavigateToRegister = onNavigateToRegister,
                errorMessage = (uiState as? LoginState.Error)?.errorMessage
            )
        }
        is LoginState.Loading -> {
            Spinner(modifier = Modifier.fillMaxSize())
        }
        else -> {}
    }
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit,
    errorMessage: String? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.satic_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(200.dp)
        )

        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Login", fontSize = 24.sp, color = Navy)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
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
                    onValueChange = onPasswordChange,
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
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White,
                        disabledContainerColor = Beige,
                        disabledContentColor = Navy.copy(alpha = 0.3f)
                    )
                ) {
                    Text("Log In", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    buildAnnotatedString {
                        append("Don't have an account? ")
                        withStyle(
                            style = SpanStyle(
                                color = Terracotta,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Register")
                        }
                    },
                    modifier = Modifier.clickable { onNavigateToRegister() },
                    color = Navy
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        errorMessage?.let {
            Error(it, modifier = Modifier.fillMaxWidth())
        }
    }
}