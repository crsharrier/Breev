package com.example.breev

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.dataStore
import androidx.fragment.app.FragmentManager
import com.example.breev.datastore.BreevSettingsSerializer
import com.example.breev.home.BreevApp
import com.example.breev.home.breevTorus
import com.example.breev.ui.theme.BreathingTimerTheme
import processing.android.PFragment

val Context.dataStore by dataStore("breev-settings-and-data.json", BreevSettingsSerializer)
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
                    BreevApp(dataStore)
                }

            }
        }
    }
}
