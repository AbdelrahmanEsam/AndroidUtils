@Composable
fun startActivityForResult(
    key : Any,
   onResultReceived : (String) -> Unit,
    onDialogDismiss : () -> Unit,
    launcher: (ManagedActivityResultLauncher<IntentSenderRequest,ActivityResult>) -> Unit
) {
    val activity = LocalContext.current as Activity
    val activityLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult()){ result ->
        try {
            if (result.resultCode == Activity.RESULT_OK){
                val onTapClient = Identity.getSignInClient(activity)
                val credentials = onTapClient.getSignInCredentialFromIntent(result.data)
                val tokenId = credentials.googleIdToken

                if (tokenId != null)
                {
                    onResultReceived(tokenId)
                }
            }else{
                onDialogDismiss()
            }


        }catch (e:java.lang.Exception){
            Log.d("LOG", e.message.toString())
            onDialogDismiss()
        }
    }

    LaunchedEffect(key1 = key){
        launcher(activityLauncher)
    }

}







suspend fun signInWithGoogle(
    context: Context,
    launcher:  (IntentSenderRequest) -> Unit,
    acceptNotFound : () -> Unit
) {
    val oneTapClient = Identity.getSignInClient(context)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                // Your server's client ID, not your Android client ID.
                .setServerClientId(Google_Client_Id)
                // Only show accounts previously used to sign in.
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        // Automatically sign in when exactly one credential is retrieved.
        .setAutoSelectEnabled(true)
        .build()

    try {
        // Use await() from https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-play-services
        // Instead of listeners that aren't cleaned up automatically
        val result = oneTapClient.beginSignIn(signInRequest).await()

        // Now construct the IntentSenderRequest the launcher requires
        val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent).build()
        launcher(intentSenderRequest)
    } catch (e: Exception) {
        // No saved credentials found. Launch the One Tap sign-up flow, or
        // do nothing and continue presenting the signed-out UI.
        Log.d("LOG", e.message.toString() +"sign catch")
     
    }
}
