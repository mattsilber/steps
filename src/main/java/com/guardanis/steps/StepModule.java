package com.guardanis.steps;

import android.view.View;

import com.guardanis.steps.views.draggable.DragEventListener;
import com.guardanis.steps.views.draggable.DraggableLinearLayout;

public abstract class StepModule<T extends StepController> {

    protected int layoutResId;

    public StepModule(int layoutResId){
        this.layoutResId = layoutResId;
    }

    public void onViewLoaded(T controller, View content){
        if(content instanceof DraggableLinearLayout)
            setupDraggableContent(controller,
                    (DraggableLinearLayout) content);

        setup(controller, content);
    }

    protected abstract void setup(T controller, View content);

    protected void setupDraggableContent(final T controller, DraggableLinearLayout content){
        content.setDragThreshold(content.getResources()
                .getDisplayMetrics()
                .widthPixels / 4);

        content.setDisabledOnThresholdReached(true);
        content.setSnapBackOnReleaseBelowThreshold(true);

        content.setDragEventListener(new DragEventListener() {
            public void onDragged(int distanceX) { }

            public void onDragThresholdReached(int distanceX) {
                if(distanceX < 0)
                    controller.onNextStep();
                else
                    controller.onPreviousStep();
            }
        });
    }

    public int getLayoutResId(){
        return layoutResId;
    }

}
