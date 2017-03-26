package com.melodyxxx.flowlayoutdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:      Melodyxxx
 * Email:       95hanjie@gmail.com
 * Created at:  16/10/09.
 * Description:
 */

public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int exactlyWidth = 0;
        int exactlyHeight = 0;

        // 记录行宽和行高
        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            // 子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            // 子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth <= widthSpecSize - getPaddingLeft() - getPaddingRight()) {   // 不换行
                // 叠加行宽
                lineWidth += childWidth;
                // 得到当前行高为此行最高的那个child的高度
                lineHeight = Math.max(lineHeight, childHeight);
            } else {    // 换行
                // 换行时需确定刚才的那一满行是否是所有行中最宽的
                exactlyWidth = Math.max(exactlyWidth, lineWidth);
                // 重置lineWidth为最新行的第一个child的宽度
                lineWidth = childWidth;
                // 记录行高
                exactlyHeight += lineHeight;
                // 重置lineHeight为最新行的第一个child的高度
                lineHeight = childHeight;
            }

            if (i == childCount - 1) {
                exactlyWidth = Math.max(exactlyWidth, childWidth);
                exactlyHeight += lineHeight;
            }
        }

        setMeasuredDimension(
                widthSpecMode == MeasureSpec.EXACTLY ? widthSpecSize : exactlyWidth + getPaddingLeft() + getPaddingRight(),
                heightSpecMode == MeasureSpec.EXACTLY ? heightSpecSize : exactlyHeight + getPaddingTop() + getPaddingBottom()
        );
    }

    private List<List<View>> mAllViews = new ArrayList<>();

    private List<Integer> mLineHeights = new ArrayList<>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllViews.clear();
        mLineHeights.clear();

        // 获取当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> lineViews = new ArrayList<>();

        int childCount = getChildCount();

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            // 如果需要换行
            if (lineWidth + childWidth + lp.leftMargin + lp.rightMargin > width - getPaddingLeft() - getPaddingRight()) {
                mLineHeights.add(lineHeight);
                mAllViews.add(lineViews);

                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

                lineViews = new ArrayList<>();
            }

            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);

            lineViews.add(child);
        }   // for end

        // 处理最后一行
        mLineHeights.add(lineHeight);
        mAllViews.add(lineViews);

        // 设置子View的位置

        int left = getPaddingLeft();
        int top = getPaddingTop();

        // 行数
        int lineNum = mAllViews.size();

        for (int i = 0; i < lineNum; i++) {

            lineViews = mAllViews.get(i);
            lineHeight = mLineHeights.get(i);

            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

                int _l = left + lp.leftMargin;
                int _t = top + lp.topMargin;
                int _r = _l + child.getMeasuredWidth();
                int _b = _t + child.getMeasuredHeight();

                // layout
                child.layout(_l, _t, _r, _b);

                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            left = getPaddingLeft();
            top += lineHeight;
        }

    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
