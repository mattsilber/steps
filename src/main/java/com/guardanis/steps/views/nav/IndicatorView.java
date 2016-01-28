package com.guardanis.steps.views.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.guardanis.steps.R;

import java.util.ArrayList;
import java.util.List;

public class IndicatorView extends View {

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

    protected void init(){
        setWillNotDraw(false);

        activePaint.setColor(getContext().getResources().getColor(R.color.step__nav_indicator_active));
        activePaint.setAntiAlias(true);

        inactivePaint.setColor(getContext().getResources().getColor(R.color.step__nav_indicator_inactive));
        inactivePaint.setAntiAlias(true);

        itemRadius = getContext().getResources().getDimension(R.dimen.step__nav_indicator_radius);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if(items == null || items.size() != itemsCount)
            setupPoints(canvas);

        for(int i = 0; i < items.size(); i++)
            canvas.drawCircle(items.get(i).x, items.get(i).y, itemRadius, activeIndex == i ? activePaint : inactivePaint);
    }

    private void setupPoints(Canvas canvas) {
        items = new ArrayList<Point>();

        float center = canvas.getWidth() / 2;
        float spacing = Math.min(canvas.getWidth() / itemsCount, getContext().getResources().getDimension(R.dimen.step__nav_indicator_spacing));

        float requiredWidth = (itemRadius * itemsCount) + (spacing * (itemsCount - 1));
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

}
