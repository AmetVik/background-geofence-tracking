package com.plcoding.backgroundlocationtracking

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import com.plcoding.backgroundlocationtracking.ui.screens.MainScreen
import com.plcoding.backgroundlocationtracking.ui.theme.BackgroundLocationTrackingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        setContent {
            BackgroundLocationTrackingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen(applicationContext)
                }

            }
        }
    }

//    enableGPSLauncher =
//    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
//        if (activityResult.resultCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
//            Toast.makeText(requireContext(), "Permit Access Granted!!", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }


//    private fun isNetworkAvailable(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val nw = connectivityManager.activeNetwork ?: return false
//            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
//            return when {
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
//                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
//                else -> false
//            }
//        } else {
//            return connectivityManager.activeNetworkInfo?.isConnected ?: false
//        }
//    }
}

//@Composable
//fun MainUI(){
//    Column(
//        Modifier
//            .fillMaxSize()
//            .background(Color.Blue)
//            .padding(bottom = 16.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(1f)
//                .background(Color.Green),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally){
//            Text(text = "No")
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(2f)
//                .background(Color.Red),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally){
//            Text(text = "No")
//        }
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .weight(1f)
//                .background(Color.Yellow),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally){
//            Text(text = "Hemlo")
//        }
//    }
//
//
//}
//@Preview(showSystemUi = true)
//@Composable
//fun DefaultPreview() {
////    MainUI()
//}