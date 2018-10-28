package com.guardanis.steps.sample

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.guardanis.steps.BaseStepModule
import com.guardanis.steps.StepController
import com.guardanis.steps.StepDialogBuilder
import com.guardanis.steps.StepModule

class MainActivity: AppCompatActivity(), StepController.StepEventListener {

    private val stepModules: List<StepModule> = {
        val data: MutableList<StepModule> = mutableListOf()
        data.add(SampleLottieStep("example_lottie_asset.json", "This is the first title"))
        data.addAll(0.until(2).map({ BaseStepModule(R.drawable.abc_ic_star_black_48dp, "A title", "A description") }))
        data.add(SampleLottieStep("example_lottie_asset.json", "This is the last title"))

        data
    }()

    override fun onCreate(savedInstance: Bundle?) {
        super.onCreate(savedInstance)

        setContentView(R.layout.activity_main)
    }

    fun showSteps(view: View?) {
        StepDialogBuilder(stepModules)
                .setEventListener(this)
                .setCancelable(true)
                .show(this)
    }

    override fun onStepLoaded(module: StepModule, index: Int) {
        Log.i(tag, "onStepLoaded at index $index")
    }

    override fun onFinished() {
        Log.i(tag, "onFinished steps")
    }

    override fun onSkipped() {
        Log.i(tag, "onSkipped steps")
    }

    companion object {

        const val tag = "steps"
    }
}
