package com.guardanis.steps.views.nav;

import android.view.View;
import android.widget.TextView;

import com.guardanis.steps.R;
import com.guardanis.steps.StepController;

public class IndicatorController {

    private IndicatorView indicatorView;

    private TextView actionNext;
    private TextView actionPrevious;
    private TextView actionSkip;

    public IndicatorController(final StepController controller, View root){
        this.indicatorView = (IndicatorView) root.findViewById(R.id.step__nav_indicator);

        this.actionNext = (TextView) root.findViewById(R.id.step__nav_next);
        actionNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.onNextStep();
            }
        });

        this.actionPrevious = (TextView) root.findViewById(R.id.step__nav_previous);
        actionPrevious.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.onPreviousStep();
            }
        });

        this.actionSkip = (TextView) root.findViewById(R.id.step__nav_skip);
        actionSkip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                controller.onSkip();
            }
        });
    }

    public void update(StepController controller, int currentIndex, int itemsCount){
        indicatorView.update(currentIndex, itemsCount);

        updateActionNext(controller, currentIndex, itemsCount);
        updateActionPrevious(controller, currentIndex, itemsCount);
        updateActionSkip(controller, currentIndex, itemsCount);
    }

    private void updateActionNext(StepController controller, int currentIndex, int itemsCount){
        if(currentIndex == itemsCount - 1)
            actionNext.setText(controller.getResources().getString(R.string.step__nav_text_finish));
        else actionNext.setText(controller.getResources().getString(R.string.step__nav_text_next));
    }

    private void updateActionPrevious(StepController controller, int currentIndex, int itemsCount){
        if(currentIndex < 1)
            actionPrevious.setVisibility(View.GONE);
        else actionPrevious.setVisibility(View.VISIBLE);
    }

    private void updateActionSkip(StepController controller, int currentIndex, int itemsCount){
        if(currentIndex > 0)
            actionSkip.setVisibility(View.GONE);
        else if(controller.isSkipEnabled())
            actionSkip.setVisibility(View.VISIBLE);
    }

}
