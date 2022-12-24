 val rememberLauncher = rememberLauncherForActivityResult(
        contract = LoginManager.getInstance().createLogInActivityResultContract()
    ){
        LoginManager.getInstance().onActivityResult(it.resultCode, it.data, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show()
            }
            override fun onCancel() {
                Toast.makeText(activity, "cancel", Toast.LENGTH_SHORT).show()
            }
            override fun onError(error: FacebookException) {
                Toast.makeText(activity, error.message, Toast.LENGTH_LONG).show()
            }
        })
    }
