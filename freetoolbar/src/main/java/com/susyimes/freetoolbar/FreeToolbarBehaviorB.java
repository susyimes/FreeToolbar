package com.susyimes.freetoolbar;

import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by Susyimes on 2016/12/23 0023.
 */

public class FreeToolbarBehaviorB extends VerticalScrollingBehavior {
    private WeakReference<FreeToolbarBottom> mViewRef;
    private int mBottomNavHeight;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, final View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        if (child instanceof FreeToolbarBottom) {
            mViewRef = new WeakReference<>((FreeToolbarBottom) child);
        }

        child.post(new Runnable() {
            @Override
            public void run() {
                mBottomNavHeight = child.getHeight();
            }
        });
        //updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child));

        return super.onLayoutChild(parent, child, layoutDirection);
    }
    @Override
    public void onNestedVerticalScrollUnconsumed(CoordinatorLayout coordinatorLayout, View child, @ScrollDirection int scrollDirection, int currentOverScroll, int totalScroll) {

    }

    @Override
    public void onNestedVerticalScrollConsumed(CoordinatorLayout coordinatorLayout, View child, @ScrollDirection int scrollDirection, int currentOverScroll, int totalConsumedScroll) {
        handleDirection(coordinatorLayout, child, scrollDirection);
    }

    private void handleDirection(CoordinatorLayout coordinatorLayout, View child, int scrollDirection) {
        FreeToolbarBottom atobar = mViewRef.get();


        if (!atobar.isHidden()){
            atobar.hide(1);
        }
        //if (bottomNavigationBar != null && bottomNavigationBar.isAutoHideEnabled()) {
          /*  if (scrollDirection == com.ashokvarma.bottomnavigation.behaviour.VerticalScrollingBehavior.ScrollDirection.SCROLL_DIRECTION_DOWN && !atobar.isHidden()) {
                //updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child), -mBottomNavHeight);
                atobar.hide();
            } else if (scrollDirection == com.ashokvarma.bottomnavigation.behaviour.VerticalScrollingBehavior.ScrollDirection.SCROLL_DIRECTION_UP && !atobar.isHidden()) {
                //updateSnackBarPosition(parent, child, getSnackBarInstance(parent, child), 0);
                atobar.hide();
            }*/
        //}
    }

    @Override
    public void onNestedVerticalPreScroll(CoordinatorLayout coordinatorLayout, View child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {

    }

    @Override
    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX, float velocityY, boolean consumed, @ScrollDirection int scrollDirection) {
        return false;
    }


}
