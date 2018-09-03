package com.example.smoothcardanimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;

/**
 * LinearLayout that can expand and collapse
 */
public class ExpandableLinearLayout extends LinearLayout{
    private boolean expanded;
    private int duration;
    private ExpandListener expandListener;

    public ExpandableLinearLayout(Context context) {
        super(context);
    }

    public ExpandableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ExpandableLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExpandableLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        TypedArray customValues = getContext().obtainStyledAttributes(attributeSet, R.styleable.ExpandableLinearLayout);
        duration = customValues.getInt(R.styleable.ExpandableLinearLayout_expandDuration, -1);
        customValues.recycle();
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        Log.e("layout", expanded + "");
        this.expanded = expanded;
    }

    public void toggle() {
        if (expanded)
            expandView(this);
        else
            hideView(this);
    }

    private void expandView(final View view) {
        view.measure(WindowManager.LayoutParams.MATCH_PARENT
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        final int targetHeight = view.getMeasuredHeight();
        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        view.getLayoutParams().height = 1;
        view.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                view.getLayoutParams().height = interpolatedTime == 1
                        ? targetHeight : (int) (targetHeight * interpolatedTime);
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expandListener.onExpandComplete();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (duration == -1)
            a.setDuration((int) (targetHeight / view.getContext().getResources().getDisplayMetrics().density * 1.5));
        else
            a.setDuration(duration);
        view.startAnimation(a);
    }

    private void hideView(final View view) {
        final int initialHeight = view.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    view.setVisibility(View.GONE);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    view.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (duration == -1)
            a.setDuration((int) (initialHeight / view.getContext().getResources().getDisplayMetrics().density * 1.5));
        else
            a.setDuration(duration);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expandListener.onCollapseComplete();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(a);
    }

    public void setExpandListener(ExpandListener expandListener) {
        this.expandListener = expandListener;
    }
}
