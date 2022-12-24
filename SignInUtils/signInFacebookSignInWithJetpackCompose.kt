@Composable
fun startActivityForFacebookResult(
    key : Any,
    onResultReceived : (AccessToken) -> Unit,
    onDialogDismiss : () -> Unit,
    launcher: (ManagedActivityResultLauncher<Collection<String>, CallbackManager.ActivityResultParameters>,context:Context) -> Unit
) {

    val context = LocalContext.current
    val activityLauncher = rememberLauncherForActivityResult(contract = LoginManager.getInstance().createLogInActivityResultContract()){ result ->
        LoginManager.getInstance().onActivityResult(result.resultCode, result.data, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
//                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show()
               onResultReceived(result.accessToken)
            }
            override fun onCancel() {
                Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show()
               onDialogDismiss()
            }
            override fun onError(error: FacebookException) {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
               onDialogDismiss()
            }
        })

    }

    LaunchedEffect(key1 = key){
        Log.d("facebookuser","$key")
        launcher(activityLauncher,context)
    }

}
