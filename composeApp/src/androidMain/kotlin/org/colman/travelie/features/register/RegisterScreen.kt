package org.colman.travelie.features.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import org.colman.travelie.R
import org.colman.travelie.ui.shared_components.Error
import org.colman.travelie.ui.shared_components.Spinner
import org.colman.travelie.ui.theme.*
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var bio by remember { mutableStateOf("") }

    when (uiState) {

        is RegisterState.Idle,
        is RegisterState.Error -> {
            RegisterForm(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                bio = bio,
                onEmailChange = { email = it.trim() },
                onPasswordChange = { password = it },
                onFirstNameChange = { firstName = it.trim() },
                onLastNameChange = { lastName = it.trim() },
                onBioChange = { bio = it },
                onRegisterClick = {
                    viewModel.register(
                        email = email,
                        password = password,
                        firstName = firstName.lowercase().replaceFirstChar { it.uppercaseChar() },
                        lastName = lastName.lowercase().replaceFirstChar { it.uppercaseChar() },
                        bio = bio
                    )
                },
                onNavigateToLogin = onNavigateToLogin,
                errorMessage = (uiState as? RegisterState.Error)?.errorMessage,
                scrollState = scrollState
            )
        }

        is RegisterState.Loading -> {
            Spinner(modifier = Modifier.fillMaxSize())
        }

        else -> {}
    }
}

@Composable
private fun RegisterForm(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
    bio: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onNavigateToLogin: () -> Unit,
    errorMessage: String? = null,
    scrollState: ScrollState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
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
            shape = MaterialTheme.shapes.large,
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Register", style = MaterialTheme.typography.titleLarge, color = Navy)
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = registerTextFieldColors()
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
                    colors = registerTextFieldColors()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = onFirstNameChange,
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = registerTextFieldColors()
                    )

                    OutlinedTextField(
                        value = lastName,
                        onValueChange = onLastNameChange,
                        label = { Text("Last Name") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = registerTextFieldColors()
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = onBioChange,
                    label = { Text("Bio (optional)") },
                    modifier = Modifier.fillMaxWidth().height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = registerTextFieldColors()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = email.isNotBlank() && password.isNotBlank() &&
                            firstName.isNotBlank() && lastName.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracotta,
                        contentColor = Color.White,
                        disabledContainerColor = Beige,
                        disabledContentColor = Navy.copy(alpha = 0.3f)
                    )
                ) {
                    Text("Register", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    buildAnnotatedString {
                        append("Already have an account? ")
                        withStyle(
                            style = SpanStyle(color = Terracotta, fontWeight = FontWeight.Bold)
                        ) {
                            append("Login")
                        }
                    },
                    modifier = Modifier.clickable { onNavigateToLogin() },
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

@Composable
fun registerTextFieldColors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Terracotta,
    unfocusedBorderColor = Lavender,
    cursorColor = Terracotta
)


