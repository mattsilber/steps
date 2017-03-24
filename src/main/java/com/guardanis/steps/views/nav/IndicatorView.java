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

        float center = canvas.getWidth() / 2;
        float spacing = Math.min(canvas.getWidth() / itemsCount, getContext().getResources().getDimension(R.dimen.step__nav_indicator_spacing));

        float requiredWidth = (itemRadius * itemsCount)
                + (spacing * (itemsCount - 1));

        float halfWidth = requiredWidth / 2;

        for(int i = 0; i < itemsCount; i++)
            items.add(new Point((int)(center - halfWidth + itemRadius + ((itemRadius * 2) * i) + (spacing * i)),
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
