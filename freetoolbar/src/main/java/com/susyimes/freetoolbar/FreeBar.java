package com.susyimes.freetoolbar;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by Susyimes on 2017/1/9 0009.
 */

public class FreeBar extends FrameLayout {

    public static final int MODE_DEFAULT = 0;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SHIFTING = 2;
    private RtlSpacingHelper mContentInsets;
    @IntDef({MODE_DEFAULT, MODE_FIXED, MODE_SHIFTING})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Mode {
    }

    public static final int BACKGROUND_STYLE_DEFAULT = 0;
    public static final int BACKGROUND_STYLE_STATIC = 1;
    public static final int BACKGROUND_STYLE_RIPPLE = 2;

    @IntDef({BACKGROUND_STYLE_DEFAULT, BACKGROUND_STYLE_STATIC, BACKGROUND_STYLE_RIPPLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface BackgroundStyle {
    }


    private static final int FAB_BEHAVIOUR_TRANSLATE_AND_STICK = 0;
    private static final int FAB_BEHAVIOUR_DISAPPEAR = 1;
    private static final int FAB_BEHAVIOUR_TRANSLATE_OUT = 2;

    @IntDef({FAB_BEHAVIOUR_TRANSLATE_AND_STICK, FAB_BEHAVIOUR_DISAPPEAR, FAB_BEHAVIOUR_TRANSLATE_OUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FabBehaviour {
    }

    @Mode
    private int mMode = MODE_DEFAULT;
    @BackgroundStyle
    private int mBackgroundStyle = BACKGROUND_STYLE_DEFAULT;

    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private ViewPropertyAnimatorCompat mTranslationAnimator;

    private boolean mScrollable = false;

    private static final int MIN_SIZE = 3;
    private static final int MAX_SIZE = 5;



    private static final int DEFAULT_SELECTED_POSITION = -1;
    private int mSelectedPosition = DEFAULT_SELECTED_POSITION;
    private int mFirstSelectedPosition = 0;


    private int mActiveColor;
    private int mInActiveColor;
    private int mBackgroundColor;

    private FrameLayout mBackgroundOverlay;
    private FrameLayout mContainer;
    private LinearLayout mTabContainer;

    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private int mAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private int mRippleAnimationDuration = (int) (DEFAULT_ANIMATION_DURATION * 2.5);

    private float mElevation;

    private boolean mAutoHideEnabled;
    private boolean mIsHidden = false;




    public FreeBar(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FreeBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FreeBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FreeBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void ensureContentInsets() {
        if (mContentInsets == null) {
            mContentInsets = new RtlSpacingHelper();
        }
    }
    /**
     * hide with animation
     */
    //can hide force
    public void move(int direnction,int offset, int duration){
        hide(true,direnction,offset,duration);
    }
    public void hide(int direction) {
        hide(true,direction);
    }
    public void hide() {
        hide(true);
    }


    /**
     * @param animate is animation enabled for hide
     */
    public void hide(boolean animate, int direction, int offset, int duration) {
        mIsHidden = true;


        if (direction==0){
            setTranslationY(-offset, animate,duration);}
        else if (direction==1){
            setTranslationY(offset, animate,duration);
        }else if (direction==3){
            setTranslationX(-offset,animate,duration);
        }else if (direction==4){
            setTranslationX(offset,animate,duration);
        }
    }

    public void hide(boolean animate,int direction) {
        mIsHidden = true;
        if (direction==0){
            setTranslationY(-this.getHeight(), animate);}
        else if (direction==1){
            setTranslationY(this.getHeight(), animate);
        }else if (direction==3){
            setTranslationX(-this.getWidth(),animate);
        }else if (direction==4){
            setTranslationX(this.getWidth(),animate);
        }
    }

    public void hide(boolean animate) {
        mIsHidden = true;

        setTranslationY(-this.getHeight(), animate);
    }

    /**
     * show with animation
     */
    public void show() {
        show(true);
    }

    public void show(int direction){
        show(true,direction);
    }

    /**
     * @param animate is animation enabled for show
     */
    public void show(boolean animate) {
        mIsHidden = false;
        setTranslationY(0, animate);
    }

    public void show(boolean animate,int direction) {
        mIsHidden = false;
        if (direction==0||direction==1){
            setTranslationY(0, animate);
        }else {
            setTranslationX(0, animate);
        }
    }


    /**
     * @param offset  offset needs to be set
     * @param animate is animation enabled for translation
     */
    private void setTranslationY(int offset, boolean animate) {
        if (animate) {
            animateOffset(offset);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }
            this.setTranslationY(offset);
        }
    }
    private void setTranslationX(int offset, boolean animate) {
        if (animate) {
            animateOffset(offset);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }
            this.setTranslationY(offset);
        }
    }
    private void setTranslationY(int offset, boolean animate,int duraion) {
        if (animate) {
            animateOffset(offset,duraion);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }
            this.setTranslationY(offset);
        }
    }
    private void setTranslationX(int offset, boolean animate,int duration) {
        if (animate) {
            animateOffset(offset,duration);
        } else {
            if (mTranslationAnimator != null) {
                mTranslationAnimator.cancel();
            }
            this.setTranslationY(offset);
        }
    }

    /**
     * Internal Method
     * <p>
     * used to set animation and
     * takes care of cancelling current animation
     * and sets duration and interpolator for animation
     *
     * @param offset translation offset that needs to set with animation
     */
    private void animateOffset(final int offset) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            mTranslationAnimator.setDuration(mRippleAnimationDuration);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }
        mTranslationAnimator.translationY(offset).start();
    }

    private void animateOffset(final int offset,final int duration) {
        if (mTranslationAnimator == null) {
            mTranslationAnimator = ViewCompat.animate(this);
            mTranslationAnimator.setDuration(duration);
            mTranslationAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mTranslationAnimator.cancel();
        }
        mTranslationAnimator.translationY(offset).start();
    }


    public boolean isHidden() {
        return mIsHidden;
    }


    public void turnState(){
        if (mTranslationAnimator!=null){
            mTranslationAnimator.cancel();}


    }
}
