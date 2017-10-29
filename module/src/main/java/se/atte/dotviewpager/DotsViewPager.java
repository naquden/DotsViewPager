/*
 * Atte. Copyright (c) 2017.
 */

package se.atte.dotviewpager;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * {@link ViewPager} that displays dots under for each page.
 */
public class DotsViewPager extends LinearLayout implements ViewPager.OnPageChangeListener{

    private ViewPager mViewPager;
    private LinearLayout mDotsContainer;
    private LayoutParams mDotLayoutParams;
    private View mPreviousSelectedDot;

    /**
     * {@inheritDoc}.
     */
    public DotsViewPager(Context context) {
        super(context);
        initialize(context);
    }

    /**
     * {@inheritDoc}.
     */
    public DotsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    /**
     * {@inheritDoc}.
     */
    public DotsViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize(context);
    }

    /**
     * Returns the view pager's adapter.
     */
    public ViewPager getViewPager() {
        return mViewPager;
    }

    /**
     * Initializes the content.
     */
    private void initialize(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        final int genericMargin = getResources().getDimensionPixelSize(R.dimen.dots_view_pager_dot_margin_end);

        // Dots container
        final FrameLayout frameLayout = new FrameLayout(context);
        addView(frameLayout, new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mDotsContainer = new LinearLayout(context);
        mDotsContainer.setGravity(Gravity.CENTER);
        final FrameLayout.LayoutParams dotsContainerParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        dotsContainerParams.gravity = Gravity.CENTER;
        frameLayout.addView(mDotsContainer, dotsContainerParams);

        // Dot variables
        final int dotSize = getResources().getDimensionPixelSize(R.dimen.dots_view_pager_dot_size);
        mDotLayoutParams = new LayoutParams(dotSize, dotSize);
        mDotLayoutParams.setMarginEnd(genericMargin);

        // View pager
        mViewPager = new ViewPager(context) {
            @Override
            public void setAdapter(PagerAdapter adapter) {
                super.setAdapter(adapter);
                loadDots();
            }
        };
        final LayoutParams viewPagerLayoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1f);
        viewPagerLayoutParams.setMargins(0, 0, 0, genericMargin);
        mViewPager.addOnPageChangeListener(this);
        addView(mViewPager, 0, viewPagerLayoutParams);

        invalidate();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // Ignored
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void onPageSelected(int position) {
        setDotSelected(position);
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        // Ignored
    }

    /**
     * Loads the dots based on the current view pager's adapter.
     */
    private void loadDots() {
        final Context context = getContext();
        final PagerAdapter adapter = mViewPager.getAdapter();
        if (context == null || adapter == null) {
            return;
        }

        for (int i = 0; i < adapter.getCount(); i++) {
            final Drawable dotDrawable = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.dots_view_pager_dot_drawable, context.getTheme());
            final View dot = new View(context);
            dot.setBackground(dotDrawable);
            mDotsContainer.addView(dot, mDotLayoutParams);
        }

        // Set default dot as selected
        setDotSelected(0);
    }

    /**
     * Sets the dot at the given position selected, and unselect the previous selected dot.
     */
    private boolean setDotSelected(int position) {
        if (mPreviousSelectedDot != null) {
            mPreviousSelectedDot.setSelected(false);
        }

        final View dot = mDotsContainer.getChildAt(position);
        if (dot != null) {
            dot.setSelected(true);
            mPreviousSelectedDot = dot;
            return true;
        }

        return false;
    }
}
