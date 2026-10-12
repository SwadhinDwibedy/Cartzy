package com.example.cartzy.u_i.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cartzy.R
import com.example.cartzy.navigation.Screen
import com.example.cartzy.ui.theme.DarkTextColor
import com.example.cartzy.ui.theme.PurpleLavender
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

private enum class LoginMethod {
    Password,
    Otp,
    Call
}

private fun isValidPhone(phone: String): Boolean {
    return phone.length == 10 && phone.all { it.isDigit() }
}

private fun isStrongPassword(password: String): Boolean {
    val hasMinLength = password.length >= 8
    val hasLetter = password.any { it.isLetter() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    return hasMinLength && hasLetter && hasDigit && hasSpecialChar
}

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isPhoneValid by remember { mutableStateOf(true) }
    var loginMethod by remember { mutableStateOf(LoginMethod.Otp) }
    var passwordVisible by remember { mutableStateOf(false) }
    var otpVisible by remember { mutableStateOf(false) }
    var verificationId by remember { mutableStateOf<String?>(null) }

    val bgGradient = Brush.verticalGradient(
        listOf(Color(0xFFF3E5F5), Color(0xFFF8EAF6), Color(0xFFFDE7F3))
    )
    val gradientText = Brush.linearGradient(
        listOf(Color(0xFF8E24AA), Color(0xFF5E35B1), Color(0xFF7E57C2))
    )
    val labelTextColor = Color(0xFF6A1B9A)

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color(0xFFF3E5F5),
        unfocusedContainerColor = Color(0xFFF3E5F5),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        cursorColor = PurpleLavender,
        focusedTextColor = DarkTextColor,
        unfocusedTextColor = DarkTextColor
    )

    fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }
            } else {
                Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun sendOtp() {
        val activity = context as? Activity ?: return
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Toast.makeText(context, "Verification Failed: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                }

                override fun onCodeSent(verifId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    verificationId = verifId
                    Toast.makeText(context, "Verification code sent", Toast.LENGTH_SHORT).show()
                }
            })
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtpCode() {
        val code = otp.trim()
        val verifId = verificationId
        if (verifId != null && code.isNotEmpty()) {
            signInWithCredential(PhoneAuthProvider.getCredential(verifId, code))
        } else {
            Toast.makeText(context, "Enter valid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginWithPassword() {
        val trimmedEmail = email.trim()
        when {
            !android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() -> {
                Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show()
            }
            !isStrongPassword(password) -> {
                Toast.makeText(context, "Password must be 8+ chars with letters, numbers, and special chars", Toast.LENGTH_LONG).show()
            }
            else -> {
                auth.signInWithEmailAndPassword(trimmedEmail, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Login Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Welcome Back 👋",
                fontSize = 30.sp,
                style = TextStyle(brush = gradientText, textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.height(32.dp))

            when (loginMethod) {
                LoginMethod.Password -> {
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors,
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                    contentDescription = null,
                                    tint = PurpleLavender
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Use 8+ chars with letters, numbers, and special chars",
                        fontSize = 12.sp,
                        color = labelTextColor
                    )
                }

                LoginMethod.Otp, LoginMethod.Call -> {
                    TextField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            isPhoneValid = isValidPhone(it)
                        },
                        label = { Text("Phone Number") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        isError = !isPhoneValid,
                        modifier = Modifier.fillMaxWidth(),
                        colors = textFieldColors
                    )
                    if (!isPhoneValid) {
                        Text("Invalid phone number", color = Color.Red, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                RadioButton(selected = loginMethod == LoginMethod.Password, onClick = { loginMethod = LoginMethod.Password })
                Text("Password", modifier = Modifier.clickable { loginMethod = LoginMethod.Password })
                Spacer(modifier = Modifier.width(12.dp))
                RadioButton(selected = loginMethod == LoginMethod.Otp, onClick = { loginMethod = LoginMethod.Otp })
                Text("OTP", modifier = Modifier.clickable { loginMethod = LoginMethod.Otp })
                Spacer(modifier = Modifier.width(12.dp))
                RadioButton(selected = loginMethod == LoginMethod.Call, onClick = { loginMethod = LoginMethod.Call })
                Text("Call", modifier = Modifier.clickable { loginMethod = LoginMethod.Call })
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (loginMethod) {
                LoginMethod.Otp -> {
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        TextField(
                            value = otp,
                            onValueChange = { otp = it },
                            label = { Text("OTP") },
                            visualTransformation = if (otpVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = textFieldColors,
                            trailingIcon = {
                                IconButton(onClick = { otpVisible = !otpVisible }) {
                                    Icon(
                                        painter = painterResource(id = if (otpVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                                        contentDescription = null,
                                        tint = PurpleLavender
                                    )
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                if (isPhoneValid) sendOtp() else Toast.makeText(context, "Enter valid phone", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender)
                        ) {
                            Text("Get OTP", color = Color.White)
                        }
                    }
                }

                LoginMethod.Call -> {
                    Button(
                        onClick = {
                            if (isPhoneValid) sendOtp() else Toast.makeText(context, "Enter valid phone", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verify via Call", color = Color.White)
                    }
                }

                LoginMethod.Password -> Unit
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when (loginMethod) {
                        LoginMethod.Password -> loginWithPassword()
                        LoginMethod.Otp -> verifyOtpCode()
                        LoginMethod.Call -> Unit
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Login", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't have an account? Register",
                color = labelTextColor,
                modifier = Modifier.clickable { navController.navigate(Screen.Signup.route) }
            )
        }
    }
}
