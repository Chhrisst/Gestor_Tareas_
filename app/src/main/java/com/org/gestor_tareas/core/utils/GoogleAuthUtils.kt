package com.org.gestor_tareas.core.utils

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//Función para iniciar sesión con Google utilizando Credential Manager en Google Cloud Console
suspend fun iniciarSesionConGoogle(
    context: Context,
    webClientId: String,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val credentialManager = CredentialManager.create(context)

    // Opción para obtener el ID de Google
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false) // Permite elegir cuentas nuevas
        .setServerClientId(webClientId)
        .setAutoSelectEnabled(false) 
        .build()

    // Petición de credenciales
    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val result = withContext(Dispatchers.Main) {
            credentialManager.getCredential(
                context = context,
                request = request
            )
        }
        handleSignIn(result, onSuccess, onError)
    } catch (e: GetCredentialException) {
        Log.e("GoogleAuth", "Error al obtener credenciales: ${e.message}")
        onError("Error de Google (¿Falta registrar el SHA-1 en Firebase/Google Cloud?): ${e.message}")
    } catch (e: Exception) {
        Log.e("GoogleAuth", "Error inesperado: ${e.message}")
        onError("Error inesperado: ${e.message}")
    }
}

private fun handleSignIn(
    result: GetCredentialResponse,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    val credential = result.credential

    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            val idToken = googleIdTokenCredential.idToken
            onSuccess(idToken)
        } catch (e: Exception) {
            Log.e("GoogleAuth", "Error al procesar el token de Google: ${e.message}")
            onError("Error al procesar el token: ${e.message}")
        }
    } else {
        onError("Tipo de credencial no soportado")
    }
}
