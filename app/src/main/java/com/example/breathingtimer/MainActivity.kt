package com.example.breathingtimer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.breathingtimer.Home.BreevApp
import com.example.breathingtimer.Home.breevTorus
import com.example.breathingtimer.ui.theme.BreathingTimerTheme
import processing.android.CompatUtils
import processing.android.PFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Create a PFragment
            val sketch = breevTorus() // Replace with your Processing sketch
            val processingFragment = PFragment(sketch)
            val fm: FragmentManager = supportFragmentManager
            fm.beginTransaction().replace(R.id.fragment_one, processingFragment).commit()

            BreathingTimerTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Transparent
                ) {
                    //Box{Text("Hello World")}
                    BreevApp()
                }

            }
        }
    }
}
