package io.microshow.retrofitgo.sample.appbar;

import android.support.design.widget.AppBarLayout;

/**
 * AppBarLayout 滚动状态监听
 * Created by Super on 2018/12/23.
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    /**
     * 滚动到中央的误差值
     */
    private int middleRangeValue = 10;

    public enum State {
        /**
         * 展开状态
         */
        EXPANDED,
        /**
         * 折叠状态
         */
        COLLAPSED,

        /**
         * 中间状态
         */
        MIDDLE,

        /**
         * 空闲其它
         */
        IDLE
    }

    private State mCurrentState = State.IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //当前滚动了多少高度
        int mVerticalOffset = Math.abs(verticalOffset);
        //AppBarLayout总共高度
        int totalScrollRange = appBarLayout.getTotalScrollRange();

        if (verticalOffset == 0) {
            if (mCurrentState != State.EXPANDED) {
                onStateChanged(appBarLayout, State.EXPANDED, verticalOffset);
            }
            mCurrentState = State.EXPANDED;

        } else if (mVerticalOffset >= totalScrollRange) {
            if (mCurrentState != State.COLLAPSED) {
                onStateChanged(appBarLayout, State.COLLAPSED, verticalOffset);
            }
            mCurrentState = State.COLLAPSED;

        } else if (mVerticalOffset > (totalScrollRange/2 - middleRangeValue)
                && mVerticalOffset < (totalScrollRange/2 + middleRangeValue)){
            if (mCurrentState != State.MIDDLE) {
                onStateChanged(appBarLayout, State.MIDDLE, verticalOffset);
            }
            mCurrentState = State.MIDDLE;

        } else {
            if (mCurrentState != State.IDLE) {
                onStateChanged(appBarLayout, State.IDLE, verticalOffset);
            }
            mCurrentState = State.IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset);

}
