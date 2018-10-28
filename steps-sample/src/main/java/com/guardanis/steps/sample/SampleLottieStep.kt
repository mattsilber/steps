package com.guardanis.steps.sample

import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.guardanis.steps.StepController
import com.guardanis.steps.StepModule

class SampleLottieStep(
        val lottieAsset: String,
        val title: String): StepModule(R.layout.lottie_step_module) {

    override fun setup(controller: StepController, content: View) {
        content.findViewById<TextView>(R.id.lottie_module_title).text = this.title
        content.findViewById<LottieAnimationView>(R.id.lottie_module_animation).setAnimation(lottieAsset)
    }
}