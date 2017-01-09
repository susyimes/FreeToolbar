package com.susyimes.freetoolbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Susyimes on 2016/12/23 0023.
 */
@CoordinatorLayout.DefaultBehavior(FreeToolbarBehaviorB.class)
public class FreeToolbarBottom extends Toolbar {
    private boolean mAutoHideEnabled;
    private boolean mIsHidden = false;
    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
    private ViewPropertyAnimatorCompat mTranslationAnimator;
    private static final int DEFAULT_ANIMATION_DURATION = 200;
    private int mAnimationDuration = DEFAULT_ANIMATION_DURATION;
    private int mRippleAnimationDuration = (int) (DEFAULT_ANIMATION_DURATION * 1);


    private static final String TAG = "Toolbar";

    private ActionMenuView mMenuView;
    private TextView mTitleTextView;
    private TextView mSubtitleTextView;
    private ImageButton mNavButtonView;
    private ImageView mLogoView;

    private Drawable mCollapseIcon;
    private CharSequence mCollapseDescription;
    ImageButton mCollapseButtonView;
    View mExpandedActionView;

    /** Context against which to inflate popup menus. */
    private Context mPopupContext;

    /** Theme resource against which to inflate popup menus. */
    private int mPopupTheme;

    private int mTitleTextAppearance;
    private int mSubtitleTextAppearance;

    int mButtonGravity;

    private int mMaxButtonHeight;

    private int mTitleMarginStart;
    private int mTitleMarginEnd;
    private int mTitleMarginTop;
    private int mTitleMarginBottom;

    private RtlSpacingHelper mContentInsets;
    private int mContentInsetStartWithNavigation;
    private int mContentInsetEndWithActions;

    private int mGravity = GravityCompat.START | Gravity.CENTER_VERTICAL;

    private CharSequence mTitleText;
    private CharSequence mSubtitleText;

    private int mTitleTextColor;
    private int mSubtitleTextColor;

    private boolean mEatingTouch;
    private boolean mEatingHover;

    // Clear me after use.
    private final ArrayList<View> mTempViews = new ArrayList<View>();

    // Used to hold views that will be removed while we have an expanded action view.
    private final ArrayList<View> mHiddenViews = new ArrayList<>();

    private final int[] mTempMargins = new int[2];

    OnMenuItemClickListener mOnMenuItemClickListener;

    private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener =
            new ActionMenuView.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (mOnMenuItemClickListener != null) {
                        return mOnMenuItemClickListener.onMenuItemClick(item);
                    }
                    return false;
                }
            };

    private ToolbarWidgetWrapper mWrapper;

    private MenuPresenter.Callback mActionMenuPresenterCallback;
    private MenuBuilder.Callback mMenuBuilderCallback;

    private boolean mCollapsible;

    private final Runnable mShowOverflowMenuRunnable = new Runnable() {
        @Override public void run() {
            showOverflowMenu();
        }
    };


    public FreeToolbarBottom(Context context) {
        this(context, null);
    }

    public FreeToolbarBottom(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.toolbarStyle);
    }

    public FreeToolbarBottom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Need to use getContext() here so that we use the themed context
        final TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                android.support.v7.appcompat.R.styleable.Toolbar, defStyleAttr, 0);

        mTitleTextAppearance = a.getResourceId(android.support.v7.appcompat.R.styleable.Toolbar_titleTextAppearance, 0);
        mSubtitleTextAppearance = a.getResourceId(android.support.v7.appcompat.R.styleable.Toolbar_subtitleTextAppearance, 0);
        mGravity = a.getInteger(android.support.v7.appcompat.R.styleable.Toolbar_android_gravity, mGravity);
        mButtonGravity = a.getInteger(android.support.v7.appcompat.R.styleable.Toolbar_buttonGravity, Gravity.TOP);

        // First read the correct attribute
        int titleMargin = a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_titleMargin, 0);
        if (a.hasValue(android.support.v7.appcompat.R.styleable.Toolbar_titleMargins)) {
            // Now read the deprecated attribute, if it has a value
            titleMargin = a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_titleMargins, titleMargin);
        }
        mTitleMarginStart = mTitleMarginEnd = mTitleMarginTop = mTitleMarginBottom = titleMargin;

        final int marginStart = a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_titleMarginStart, -1);
        if (marginStart >= 0) {
            mTitleMarginStart = marginStart;
        }

        final int marginEnd = a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_titleMarginEnd, -1);
        if (marginEnd >= 0) {
            mTitleMarginEnd = marginEnd;
        }

        final int marginTop = a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_titleMarginTop, -1);
        if (marginTop >= 0) {
            mTitleMarginTop = marginTop;
        }

        final int marginBottom = a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_titleMarginBottom,
                -1);
        if (marginBottom >= 0) {
            mTitleMarginBottom = marginBottom;
        }

        mMaxButtonHeight = a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.Toolbar_maxButtonHeight, -1);

        final int contentInsetStart =
                a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_contentInsetStart,
                        RtlSpacingHelper.UNDEFINED);
        final int contentInsetEnd =
                a.getDimensionPixelOffset(android.support.v7.appcompat.R.styleable.Toolbar_contentInsetEnd,
                        RtlSpacingHelper.UNDEFINED);
        final int contentInsetLeft =
                a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.Toolbar_contentInsetLeft, 0);
        final int contentInsetRight =
                a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.Toolbar_contentInsetRight, 0);

        ensureContentInsets();
        mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);

        if (contentInsetStart != RtlSpacingHelper.UNDEFINED ||
                contentInsetEnd != RtlSpacingHelper.UNDEFINED) {
            mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
        }

        mContentInsetStartWithNavigation = a.getDimensionPixelOffset(
                android.support.v7.appcompat.R.styleable.Toolbar_contentInsetStartWithNavigation, RtlSpacingHelper.UNDEFINED);
        mContentInsetEndWithActions = a.getDimensionPixelOffset(
                android.support.v7.appcompat.R.styleable.Toolbar_contentInsetEndWithActions, RtlSpacingHelper.UNDEFINED);

        mCollapseIcon = a.getDrawable(android.support.v7.appcompat.R.styleable.Toolbar_collapseIcon);
        mCollapseDescription = a.getText(android.support.v7.appcompat.R.styleable.Toolbar_collapseContentDescription);

        final CharSequence title = a.getText(android.support.v7.appcompat.R.styleable.Toolbar_title);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }

        final CharSequence subtitle = a.getText(android.support.v7.appcompat.R.styleable.Toolbar_subtitle);
        if (!TextUtils.isEmpty(subtitle)) {
            setSubtitle(subtitle);
        }

        // Set the default context, since setPopupTheme() may be a no-op.
        mPopupContext = getContext();
        setPopupTheme(a.getResourceId(android.support.v7.appcompat.R.styleable.Toolbar_popupTheme, 0));

        final Drawable navIcon = a.getDrawable(android.support.v7.appcompat.R.styleable.Toolbar_navigationIcon);
        if (navIcon != null) {
            setNavigationIcon(navIcon);
        }
        final CharSequence navDesc = a.getText(android.support.v7.appcompat.R.styleable.Toolbar_navigationContentDescription);
        if (!TextUtils.isEmpty(navDesc)) {
            setNavigationContentDescription(navDesc);
        }

        final Drawable logo = a.getDrawable(android.support.v7.appcompat.R.styleable.Toolbar_logo);
        if (logo != null) {
            setLogo(logo);
        }

        final CharSequence logoDesc = a.getText(android.support.v7.appcompat.R.styleable.Toolbar_logoDescription);
        if (!TextUtils.isEmpty(logoDesc)) {
            setLogoDescription(logoDesc);
        }

        if (a.hasValue(android.support.v7.appcompat.R.styleable.Toolbar_titleTextColor)) {
            setTitleTextColor(a.getColor(android.support.v7.appcompat.R.styleable.Toolbar_titleTextColor, 0xffffffff));
        }

        if (a.hasValue(android.support.v7.appcompat.R.styleable.Toolbar_subtitleTextColor)) {
            setSubtitleTextColor(a.getColor(android.support.v7.appcompat.R.styleable.Toolbar_subtitleTextColor, 0xffffffff));
        }
        a.recycle();
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
            setTranslationY(-offset, animate);}
        else if (direction==1){
            setTranslationY(offset, animate);
        }else if (direction==3){
            setTranslationX(-offset,animate);
        }else if (direction==4){
            setTranslationX(offset,animate);
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
