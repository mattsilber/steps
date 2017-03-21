package com.guardanis.steps.views.draggable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

public class DraggableLinearLayout extends LinearLayout implements OnTouchListener {

    private static final int SNAP_ANIM_CYCLE = 5;
    private static final int SNAP_ANIM_LENGTH = 40;

    private DragEventListener eventListener;
    private int draggedDistance = 0;
    private int lastDraggedDistance = 0;

    private int dragThreshold = 0;
    private boolean disabledOnThresholdReached = false;
    private boolean snapBackOnReleaseBelowThreshold = false;

    private boolean dragEnabled = true;

    private float touchStart = 0;

    private Thread snapAnimationThread;
    private boolean snapAnimationRunning = false;

    public DraggableLinearLayout(Context context) {
        super(context);
        setup();
    }

    public DraggableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    @SuppressLint("NewApi")
    public DraggableLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    protected void setup() {
        setWillNotDraw(false);
        setOnTouchListener(this);
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.translate(draggedDistance, 0);

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if(!dragEnabled)
            return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            killSnapAnimation();

            touchStart = event.getX();

            lastDraggedDistance = Integer.valueOf(draggedDistance);

            draggedDistance = 0;
        }
        else
            onDragged((int) (event.getX() - touchStart));

        if(event.getAction() == MotionEvent.ACTION_UP)
            onTouchActionUp((int) event.getX());

        return true;
    }

    public void onDragged(int distanceX) {
        if(dragEnabled) {
            draggedDistance = distanceX + lastDraggedDistance;

            invalidate();

            if(eventListener != null)
                eventListener.onDragged(draggedDistance);
        }
    }

    private void onTouchActionUp(int touchX) {
        if(eventListener != null && isDragThresholdSet()){
            if(isHorizontalThresholdReached()){
                dragEnabled = !disabledOnThresholdReached;

                eventListener.onDragThresholdReached(draggedDistance);
            }
            else if(snapBackOnReleaseBelowThreshold)
                snapBackToZero();
        }
    }

    private boolean isDragThresholdSet() {
        return 0 < dragThreshold;
    }

    private boolean isHorizontalThresholdReached() {
        return dragThreshold < Math.abs(draggedDistance);
    }

    public void setDragEventListener(DragEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void setDragThreshold(int dragThresholdX) {
        this.dragThreshold = dragThresholdX;
    }

    public void setDisabledOnThresholdReached(boolean disabledOnThresholdReached) {
        this.disabledOnThresholdReached = disabledOnThresholdReached;
    }

    public void setSnapBackOnReleaseBelowThreshold(boolean snapBackOnReleaseBelowThreshold) {
        this.snapBackOnReleaseBelowThreshold = snapBackOnReleaseBelowThreshold;
    }

    private void snapBackToZero() {
        killSnapAnimation();

        snapAnimationRunning = true;
        snapAnimationThread = new Thread(new Runnable() {
            public void run() {
                try{
                    float origX = Float.valueOf(draggedDistance);
                    float dX = -origX / SNAP_ANIM_LENGTH;

                    int count = 0;
                    while(snapAnimationRunning && count < SNAP_ANIM_LENGTH){
                        onSnapAnimationUpdate((int) (origX + (dX * count)));

                        Thread.sleep(SNAP_ANIM_CYCLE);
                        count++;
                    }
                }
                catch(Exception e){ e.printStackTrace(); }

                onSnapAnimationFinished();
            }
        });
        snapAnimationThread.start();
    }

    private void onSnapAnimationUpdate(final int x) {
        post(new Runnable() {
            public void run() {
                draggedDistance = x;
                invalidate();
            }
        });
    }

    private void onSnapAnimationFinished() {
        post(new Runnable() {
            public void run() {
                if(snapAnimationThread != null){
                    killSnapAnimation();
                    invalidate();
                }
            }
        });
    }

    private void killSnapAnimation() {
        if(snapAnimationThread != null){
            try{
                snapAnimationThread.stop();
            }
            catch(Exception e){ }

            snapAnimationRunning = false;
            snapAnimationThread = null;
        }
    }

    public void reset(boolean animateSnap) {
        if(animateSnap)
            snapBackToZero();
        else{
            zeroOutDistances();
            invalidate();
        }

        dragEnabled = true;
    }

    private void zeroOutDistances() {
        draggedDistance = 0;
        lastDraggedDistance = 0;
    }

    public void setDragEnabled(boolean enabled) {
        this.dragEnabled = enabled;
    }

    public boolean isDragEnabled() {
        return dragEnabled;
    }
}
