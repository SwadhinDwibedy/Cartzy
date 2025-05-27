package com.example.cartzy.u_i.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cartzy.navigation.Screen
import com.example.cartzy.ui.theme.DarkTextColor
import com.example.cartzy.ui.theme.PurpleLavender
import com.example.cartzy.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isPhoneValid by remember { mutableStateOf(true) }
    var loginMethod by remember { mutableStateOf("OTP") }
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

    fun validatePhone(phone: String): Boolean {
        return phone.length == 10 && phone.all { it.isDigit() }
    }

    fun signInWithCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
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

    fun sendOtpOrCall() {
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
            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtpCode() {
        val verifId = verificationId
        val code = otp.trim()
        if (verifId != null && code.isNotEmpty()) {
            val credential = PhoneAuthProvider.getCredential(verifId, code)
            signInWithCredential(credential)
        } else {
            Toast.makeText(context, "Enter valid OTP", Toast.LENGTH_SHORT).show()
        }
    }

    fun loginWithPassword() {
        if (!email.contains("@")) {
            Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
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

            if (loginMethod == "Password") {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors
                )
            } else {
                TextField(
                    value = phoneNumber,
                    onValueChange = {
                        phoneNumber = it
                        isPhoneValid = validatePhone(it)
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

            Spacer(modifier = Modifier.height(16.dp))

            // Login method selector
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = loginMethod == "Password", onClick = { loginMethod = "Password" })
                Text("Password", modifier = Modifier.clickable { loginMethod = "Password" })
                Spacer(modifier = Modifier.width(12.dp))
                RadioButton(selected = loginMethod == "OTP", onClick = { loginMethod = "OTP" })
                Text("OTP", modifier = Modifier.clickable { loginMethod = "OTP" })
                Spacer(modifier = Modifier.width(12.dp))
                RadioButton(selected = loginMethod == "Call", onClick = { loginMethod = "Call" })
                Text("Call", modifier = Modifier.clickable { loginMethod = "Call" })
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic fields based on method
            when (loginMethod) {
                "OTP" -> {
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
                                if (isPhoneValid) sendOtpOrCall()
                                else Toast.makeText(context, "Enter valid phone", Toast.LENGTH_SHORT).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender)
                        ) {
                            Text("Get OTP", color = Color.White)
                        }
                    }
                }

                "Call" -> {
                    Button(
                        onClick = {
                            if (isPhoneValid) sendOtpOrCall()
                            else Toast.makeText(context, "Enter valid phone", Toast.LENGTH_SHORT).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Verify via Call", color = Color.White)
                    }
                }

                "Password" -> {
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
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    when (loginMethod) {
                        "Password" -> loginWithPassword()
                        "OTP" -> verifyOtpCode()
                        "Call" -> {} // Already triggered
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
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Signup.route)
                }
            )
        }
    }
}
