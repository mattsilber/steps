package com.guardanis.steps;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseStepModule extends StepModule {

    private int logoResId;
    private String title;
    private String description;

    public BaseStepModule(Context context, int titleResId, int descriptionResId) {
        this(0, context.getString(titleResId), context.getString(descriptionResId));
    }

    public BaseStepModule(String title, String description) {
        this(0, title, description);
    }

    public BaseStepModule(int logoResId, String title, String description) {
        super(R.layout.step__base_module);

        this.logoResId = logoResId;
        this.title = title;
        this.description = description;
    }

    @Override
    protected void setup(StepController controller, View content) {
        ((ImageView) content.findViewById(R.id.step__base_model_image)).setImageResource(logoResId);
        ((TextView) content.findViewById(R.id.step__base_model_title)).setText(title);
        ((TextView) content.findViewById(R.id.step__base_model_description)).setText(description);
    }
}
