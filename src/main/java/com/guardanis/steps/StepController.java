package com.guardanis.steps;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.guardanis.steps.views.draggable.DraggableLinearLayout;
import com.guardanis.steps.views.nav.IndicatorController;

import java.util.List;

public class StepController {

    public interface StepEventListener {
        public void onStepLoaded(StepModule module, int index);
        public void onFinished();
        public void onSkipped();
    }

    private List<StepModule> modules;
    private int currentModuleIndex = 0;

    private ViewGroup parentView;
    private IndicatorController indicatorController;

    private StepEventListener eventListener;
    private boolean skipEnabled = false;
    private boolean finishable = true;

    private boolean animatingTransition = false;

    public StepController(View root, List<StepModule> modules, StepEventListener eventListener){
        this.parentView = (ViewGroup) root.findViewById(R.id.step__content_parent);
        this.indicatorController = new IndicatorController(this, root);
        this.modules = modules;
        this.eventListener = eventListener;

        loadFirstModule();
    }

    public void onNextStep(){
        if(animatingTransition)
            return;

        currentModuleIndex++;

        if(currentModuleIndex < modules.size())
            animateNextStepIn();
        else if(finishable)
            eventListener.onFinished();
        else{
            currentModuleIndex--;

            resetDraggableContent(true);
        }
    }

    public void onPreviousStep(){
        if(animatingTransition)
            return;

        currentModuleIndex--;

        if(currentModuleIndex < 0){
            currentModuleIndex = 0;

            resetDraggableContent(true);
        }
        else
            animatePreviousStepIn();
    }

    public void onSkip(){
        if(isSkipEnabled())
            eventListener.onSkipped();
    }

    private void loadFirstModule(){
        final View view = inflateModule();

        loadCurrentStepModule(view);
        addView(view);
    }

    private void animateNextStepIn() {
        final View view = prepareTransition();

        if(parentView.getChildAt(0) != null)
            parentView.getChildAt(0)
                    .startAnimation(AnimationUtils.loadAnimation(parentView.getContext(),
                            R.anim.left_out));

        view.startAnimation(AnimationUtils.loadAnimation(parentView.getContext(),
                R.anim.right_in));

        setDraggableTouchesEnabled(view, false);

        addView(view);
    }

    private void animatePreviousStepIn() {
        final View view = prepareTransition();

        if(parentView.getChildAt(0) != null)
            parentView.getChildAt(0)
                    .startAnimation(AnimationUtils.loadAnimation(parentView.getContext(),
                            R.anim.right_out));

        view.startAnimation(AnimationUtils.loadAnimation(parentView.getContext(),
                R.anim.left_in));

        setDraggableTouchesEnabled(view, false);

        addView(view);
    }

    private View prepareTransition(){
        setDraggableTouchesEnabled(false);
        animatingTransition = true;

        View view = inflateModule();
        loadCurrentStepModule(view);

        return view;
    }

    protected View inflateModule(){
        return LayoutInflater.from(parentView.getContext())
                .inflate(getActiveModule().getLayoutResId(), parentView, false);
    }

    protected void addView(final View view){
        parentView.post(new Runnable() {
            public void run() {
                parentView.removeAllViews();
                parentView.addView(view);

                enableTouchesDelayed();
            }
        });
    }

    protected void enableTouchesDelayed(){
        parentView.postDelayed(new Runnable(){
            public void run(){
                setDraggableTouchesEnabled(true);

                animatingTransition = false;
            }
        }, parentView.getResources().getInteger(R.integer.step__anim_duration));
    }

    protected void loadCurrentStepModule(View content){
        getActiveModule()
                .onViewLoaded(this, content);

        indicatorController.update(this,
                currentModuleIndex,
                modules.size());

        eventListener.onStepLoaded(getActiveModule(),
                currentModuleIndex);
    }

    public StepModule getActiveModule(){
        return modules.get(currentModuleIndex);
    }

    public int getCurrentModuleIndex(){
        return currentModuleIndex;
    }

    protected void setDraggableTouchesEnabled(boolean touchEnabled){
        if(parentView.getChildAt(0) != null)
            setDraggableTouchesEnabled(parentView.getChildAt(0),
                    touchEnabled);
    }

    protected void setDraggableTouchesEnabled(View view, boolean touchEnabled){
        if(view != null && view instanceof DraggableLinearLayout)
            ((DraggableLinearLayout)view)
                    .setDragEnabled(touchEnabled);
    }

    protected void resetDraggableContent(boolean animateSnap){
        if(parentView.getChildAt(0) != null && parentView.getChildAt(0) instanceof DraggableLinearLayout)
            ((DraggableLinearLayout)parentView.getChildAt(0))
                    .reset(animateSnap);
    }

    public StepController setSkipEnabled(boolean skipEnabled) {
        this.skipEnabled = skipEnabled;

        indicatorController.update(this,
                currentModuleIndex,
                modules.size());

        return this;
    }

    /**
     * Set whether or not the navigation action Views (previous, skip, and next) should be
     * completely hidden. If set to true, you must either manually call the StepController
     * navigation methods, or have your StepModule use the draggable layout.
     */
    public StepController setNavigationActionsHidden(boolean hidden){
        indicatorController.setHideAllActions(hidden);

        indicatorController.update(this,
                currentModuleIndex,
                modules.size());

        return this;
    }

    public boolean isSkipEnabled(){
        return skipEnabled && finishable;
    }

    /**
     * Set whether or not this StepController can ever finish. If false,
     * neither StepEventListener.onFinished() nor StepEventListener.onSkipped() will
     * be called, and isSkipEnabled() will return false
     */
    public StepController setFinishable(boolean finishable) {
        this.finishable = finishable;
        return this;
    }

    public Resources getResources(){
        return parentView.getResources();
    }
}
