package com.kieronquinn.app.simnumbersetter.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kieronquinn.app.simnumbersetter.R
import com.kieronquinn.monetcompat.app.MonetCompatActivity
import com.kieronquinn.monetcompat.core.MonetCompat
import kotlinx.coroutines.launch

class MainActivity : MonetCompatActivity() {

    override val applyBackgroundColorToMenu = true
    override val applyBackgroundColorToWindow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MonetCompat.enablePaletteCompat()
        enableEdgeToEdge()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                monet.awaitMonetReady()
                setContentView(R.layout.activity_main)
            }
        }
    }

}
