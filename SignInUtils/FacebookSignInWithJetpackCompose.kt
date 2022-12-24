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
               onResultReceived(result.accessToken) // return the token to the lamda 
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

    
    //key is state from the viewModel to trigger the lancher when the key change .....when you want to trigger the activity lancher all you should do is to make this key
    //true like viewModel.setKey(true) and this will make the Launched effect trigger the activity launcher 
    // and give you the result on onResultREceived lamda where you call the composable
    LaunchedEffect(key1 = key){
        launcher(activityLauncher,context)
    }

}
