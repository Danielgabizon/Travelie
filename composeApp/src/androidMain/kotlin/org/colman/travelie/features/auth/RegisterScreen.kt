package org.colman.travelie.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.colman.travelie.R
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.koin.androidx.compose.koinViewModel
import org.colman.travelie.ui.theme.*




@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
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
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.satic_logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(256.dp)
                .padding(bottom = 24.dp)
        )
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Register", fontSize = 24.sp, color = Navy,modifier = Modifier.align(Alignment.CenterHorizontally))

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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White,
                        disabledContainerColor = Beige,
                        disabledContentColor = Navy.copy(alpha = 0.3f)))
                {
                    Text("Register", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    buildAnnotatedString {
                        append("Already have an account? ")

                        withStyle(
                            style = SpanStyle(
                                color = Terracotta, // your highlight color
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append("Login")
                        }
                    },
                    modifier = Modifier
                        .clickable { onNavigateToLogin() }
                        .align(Alignment.CenterHorizontally),
                    color = Navy // base color for other text
                )

            }

        }
        Spacer(modifier = Modifier.height(32.dp))

        when (uiState) {
            is AuthState.Error -> Error(uiState.errorMessage, modifier = Modifier.fillMaxWidth())
            is AuthState.Loading -> Spinner(modifier = Modifier.fillMaxWidth())
            else -> {}
        }
    }

}


