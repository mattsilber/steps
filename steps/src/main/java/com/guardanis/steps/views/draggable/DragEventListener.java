package com.guardanis.steps.views.draggable;

public interface DragEventListener {

    public void onDragged(int distanceX);

    public void onDragThresholdReached(int distanceX);

}
