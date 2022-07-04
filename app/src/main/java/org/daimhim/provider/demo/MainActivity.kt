package org.daimhim.provider.demo

import android.app.Activity
import android.os.Bundle
import org.daimhim.provider.api.TestGroup

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TestGroup()
    }
}