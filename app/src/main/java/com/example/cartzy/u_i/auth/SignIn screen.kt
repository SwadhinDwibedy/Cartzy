package com.example.cartzy.u_i.auth

import android.app.Activity
import android.widget.Toast
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cartzy.R
import com.example.cartzy.navigation.Screen
import com.example.cartzy.ui.theme.DarkTextColor
import com.example.cartzy.ui.theme.PurpleLavender
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

@Composable
fun AnimatedCartzyTitle() {
    val infiniteTransition = rememberInfiniteTransition(label = "titleAnim")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 600f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offset"
    )

    val animatedGradient = Brush.linearGradient(
        colors = listOf(Color(0xFF8E24AA), Color(0xFF5E35B1), Color(0xFF7E57C2)),
        start = Offset(animatedOffset, 0f),
        end = Offset(animatedOffset + 300f, 300f)
    )

    Text(
        text = "Welcome to Cartzy 🛒",
        fontSize = 30.sp,
        style = TextStyle(
            brush = animatedGradient,
            textAlign = TextAlign.Center
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    val context = LocalContext.current
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }  // Added email field
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var isPhoneValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var verificationIdHolder by remember { mutableStateOf("") }

    fun validatePhone(phone: String) = phone.length == 10 && phone.all { it.isDigit() }
    fun validateEmail(email: String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

    val bgGradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFF3E5F5), Color(0xFFF8EAF6), Color(0xFFFDE7F3))
    )

    val labelTextColor = Color(0xFF6A1B9A)

    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color(0xFFF3E5F5),
        unfocusedContainerColor = Color(0xFFF3E5F5),
        disabledContainerColor = Color(0xFFF3E5F5),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        focusedLabelColor = labelTextColor,
        unfocusedLabelColor = labelTextColor,
        errorLabelColor = Color.Red,
        cursorColor = PurpleLavender,
        focusedTextColor = DarkTextColor,
        unfocusedTextColor = DarkTextColor
    )

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
            AnimatedCartzyTitle()
            Spacer(modifier = Modifier.height(32.dp))

            // Full Name Field
            TextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Email Field (NEW)
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = email.isNotBlank() && !validateEmail(email),
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )
            if (email.isNotBlank() && !validateEmail(email)) {
                Text("Invalid email address", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone Field
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

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            TextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            painter = painterResource(id = if (confirmPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                            contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password"
                        )
                    }
                },
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(16.dp))

            // OTP Field and Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("OTP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        if (!validatePhone(phoneNumber)) {
                            Toast.makeText(context, "Enter valid phone number first!", Toast.LENGTH_SHORT).show()
                        } else {
                            val activity = context as Activity
                            val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                                .setPhoneNumber("+91$phoneNumber")
                                .setTimeout(60L, TimeUnit.SECONDS)
                                .setActivity(activity)
                                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                                        Toast.makeText(context, "Auto-verification complete", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onVerificationFailed(e: FirebaseException) {
                                        Toast.makeText(context, "Verification failed: ${e.message}", Toast.LENGTH_LONG).show()
                                    }

                                    override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                                        verificationIdHolder = verificationId
                                        Toast.makeText(context, "OTP sent to $phoneNumber", Toast.LENGTH_SHORT).show()
                                    }
                                })
                                .build()

                            PhoneAuthProvider.verifyPhoneNumber(options)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender),
                    elevation = ButtonDefaults.buttonElevation(4.dp)
                ) {
                    Text("Get OTP", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register Button
            Button(
                onClick = {
                    when {
                        fullName.isBlank() || phoneNumber.isBlank() || password.isBlank() || confirmPassword.isBlank() || otp.isBlank() || email.isBlank() -> {
                            Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
                        }
                        !validatePhone(phoneNumber) -> {
                            Toast.makeText(context, "Invalid phone number!", Toast.LENGTH_SHORT).show()
                        }
                        !validateEmail(email) -> {
                            Toast.makeText(context, "Invalid email address!", Toast.LENGTH_SHORT).show()
                        }
                        password != confirmPassword -> {
                            Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                        }
                        verificationIdHolder.isBlank() -> {
                            Toast.makeText(context, "Please get OTP first!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val credential = PhoneAuthProvider.getCredential(verificationIdHolder, otp)
                            FirebaseAuth.getInstance().signInWithCredential(credential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        val uid = FirebaseAuth.getInstance().currentUser?.uid
                                        val db = Firebase.firestore

                                        val user = hashMapOf(
                                            "uid" to uid,
                                            "fullName" to fullName,
                                            "email" to email,
                                            "phoneNumber" to phoneNumber
                                        )

                                        uid?.let {
                                            db.collection("users").document(it).set(user)
                                                .addOnSuccessListener {
                                                    Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                                    navController.navigate(Screen.Home.route)
                                                }
                                                .addOnFailureListener {
                                                    Toast.makeText(context, "Failed to save user data!", Toast.LENGTH_SHORT).show()
                                                }
                                        }
                                    } else {
                                        Toast.makeText(context, "OTP verification failed!", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleLavender),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                elevation = ButtonDefaults.buttonElevation(6.dp)
            ) {
                Text("Register", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Already have an account? Login",
                color = labelTextColor,
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Login.route)
                }
            )
        }
    }
}