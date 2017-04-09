package com.guardanis.steps.views.nav;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.guardanis.steps.R;

import java.util.ArrayList;
import java.util.List;

public class IndicatorView extends View {

    public interface IndicatorItemDrawer {
        public void drawIndicatorItem(Canvas canvas, Point location, float maxRadius, Paint paint);
    }

    protected IndicatorItemDrawer itemDrawer = new CircleIndicatorItemDrawer();

    protected Paint activePaint = new Paint();
    protected Paint inactivePaint = new Paint();

    protected List<Point> items;
    protected int activeIndex = 0;
    protected int itemsCount = 0;

    protected float itemRadius;
    protected float itemSpacing;

    public IndicatorView(Context context) {
        super(context);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setWillNotDraw(false);

        activePaint.setColor(getResources()
                .getColor(R.color.step__nav_indicator_active));
        activePaint.setAntiAlias(true);

        inactivePaint.setColor(getResources()
                .getColor(R.color.step__nav_indicator_inactive));
        inactivePaint.setAntiAlias(true);

        itemRadius = getResources()
                .getDimension(R.dimen.step__nav_indicator_radius);

        itemSpacing = getResources()
                .getDimension(R.dimen.step__nav_indicator_spacing);
    }

    /**
     * Override the default color for the active items
     */
    public IndicatorView setActiveColor(int color){
        activePaint.setColor(color);
        return this;
    }

    /**
     * Override the default color for the inactive items
     */
    public IndicatorView setInactiveColor(int color){
        inactivePaint.setColor(color);
        return this;
    }

    /**
     * Set the spacing, in pixels, between each item
     */
    public IndicatorView setItemSpacingPx(int spacingPx){
        this.itemSpacing = spacingPx;
        return this;
    }

    /**
     * Draw the indicators with the default CircleIndicatorItemDrawer
     */
    public IndicatorView setCircleItemDrawer(){
        return setItemDrawer(new CircleIndicatorItemDrawer());
    }

    /**
     * Draw the indicators with the default RectIndicatorItemDrawer
     */
    public IndicatorView setRectItemDrawer(){
        return setItemDrawer(new RectIndicatorItemDrawer());
    }

    /**
     * Set an IndicatorItemDrawer for the individual items
     */
    public IndicatorView setItemDrawer(IndicatorItemDrawer itemDrawer){
        this.itemDrawer = itemDrawer;
        return this;
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(itemsCount < 1)
            return;

        if(items == null || items.size() != itemsCount)
            setupPoints(canvas);

        for(int i = 0; i < items.size(); i++)
            itemDrawer.drawIndicatorItem(canvas,
                    items.get(i),
                    itemRadius,
                    activeIndex == i
                            ? activePaint
                            : inactivePaint);
    }

    protected void setupPoints(Canvas canvas) {
        items = new ArrayList<Point>();

        float drawWidth = ((itemRadius * 2) * itemsCount)
                + (itemSpacing * (itemsCount - 1));

        float drawStartX = (canvas.getWidth() / 2) - (drawWidth / 2) + itemRadius;

        for(int i = 0; i < itemsCount; i++)
            items.add(new Point((int) (drawStartX + (i * (itemRadius * 2)) + (i * itemSpacing)),
                    canvas.getHeight() / 2));
    }

    public void update(int activeIndex, int itemsCount){
        this.activeIndex = activeIndex;
        this.itemsCount = itemsCount;

        invalidate();
    }

    protected static class CircleIndicatorItemDrawer implements IndicatorItemDrawer {

        @Override
        public void drawIndicatorItem(Canvas canvas, Point location, float maxRadius, Paint paint) {
            canvas.drawCircle(location.x,
                    location.y,
                    maxRadius,
                    paint);
        }

    }

    protected static class RectIndicatorItemDrawer implements IndicatorItemDrawer {

        @Override
        public void drawIndicatorItem(Canvas canvas, Point location, float maxRadius, Paint paint) {
            canvas.drawRect(location.x - maxRadius,
                    location.y - maxRadius,
                    location.x + maxRadius,
                    location.y + maxRadius,
                    paint);
        }

    }

}
