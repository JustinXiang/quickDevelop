package com.zhenxinkeji.poly_agent.widgets;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zhenxinkeji.poly_agent.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @describe 错误视图负责显示的类
 */
public class LoadingLayout extends FrameLayout implements View.OnClickListener {
    private final Context mContext;
    private ImageView mImage;
    private TextView mText;
    private View mErrorView;
    private RelativeLayout mProgressBar;
//    private ImageView iv_loading;

    /**
     * 暴露ImageView 方便实列设置
     *
     * @return
     */
    public ImageView getImage() {
        return mImage;
    }

    /**
     * 暴露TextView 方便实列设置
     *
     * @return
     */
    public TextView getText() {
        return mText;
    }


    public LoadingLayout(Context context) {
        this(context, null);
    }

    public LoadingLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    /**
     * The succeed state
     */
    public final static int TYPE_GONE = 0;
    /**
     * The loading state
     */
    private int TYPE_LOADING = 1;
    /**
     * The error state
     */
    public final static int TYPE_CUSTOM = 2;
    /**
     * 记录当前视图的状态
     * 默认是0
     */
    public int currentState;
    /**
     * 存放子类视图
     */
    private List<View> childViews;
    /**
     * 回调出视图的点击事件
     */
    public CustomErrorViewLayoutClickListenler mCustomErrorViewLayoutClickListenler;

    public void setCustomErrorViewLayoutClickListenler(CustomErrorViewLayoutClickListenler customErrorViewLayoutClickListenler) {
        mCustomErrorViewLayoutClickListenler = customErrorViewLayoutClickListenler;
    }

    public interface CustomErrorViewLayoutClickListenler {
        void customErrorClick(View view);
    }


    /**
     * 初始化
     */
    private void init() {
        childViews = new ArrayList<>();
        mErrorView = View.inflate(mContext, R.layout.cel_layout, null);
        mImage = (ImageView) mErrorView.findViewById(R.id.image);
        mText = (TextView) mErrorView.findViewById(R.id.text);
        mProgressBar = (RelativeLayout) mErrorView.findViewById(R.id.progressbar);
//        iv_loading = (ImageView) mErrorView.findViewById(R.id.loading_iv);
//
//        iv_loading.setImageResource(R.drawable.sobot_loading_dialog_anim);
//        animationDrawable = (AnimationDrawable) iv_loading.getDrawable();
        mErrorView.setVisibility(GONE);
        mErrorView.setOnClickListener(this);
        addView(mErrorView);
        hide();
    }


    /**
     * 展示错误信息
     *
     * @param resId 图片资源id
     * @param text
     */
    public void showError(int resId, String text) {
        showAbnormalView(resId, text);
        currentState = TYPE_CUSTOM;
    }

    public void showError(int resId, Spanned text) {
        showAbnormalView(resId, text);
        currentState = TYPE_CUSTOM;
    }

    public void loading() {
        mErrorView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        mImage.setVisibility(GONE);
        mText.setText("");
        currentState = TYPE_LOADING;
        getChildViews();
        hideChildView();
    }

    /**
     * 显示异常视图
     */
    private void showAbnormalView(int resId, Spanned text) {
        mText.setText(text);
        mProgressBar.setVisibility(GONE);
        mImage.setImageResource(resId);
        mErrorView.setVisibility(VISIBLE);
        mImage.setVisibility(VISIBLE);
        getChildViews();
        hideChildView();
    }

    /**
     * 显示异常视图
     */
    private void showAbnormalView(int resId, String text) {
        mText.setText(text);
        mProgressBar.setVisibility(GONE);
        mImage.setImageResource(resId);
        mErrorView.setVisibility(VISIBLE);
        mImage.setVisibility(VISIBLE);
        getChildViews();
        hideChildView();
    }

    /**
     * 隐藏加入异常的视图,显示正常的子View
     * currentState默认是0的时候，会不执行内容，所以对性能无影响
     */
    public void hide() {
        if (TYPE_GONE != currentState) {
            mErrorView.setVisibility(GONE);
            showChildView();
            currentState = TYPE_GONE;
        }
    }


    /**
     * 判断view 对象是否是EmptyOrErrorView
     *
     * @param view
     * @return
     */
    private boolean isEmptyView(View view) {
        if ((view == null || mErrorView == view)) {
            return true;
        }
        return false;
    }


    /**
     * 隐藏所有的子View
     */
    private void hideChildView() {
        for (View view : childViews) {
            if (isEmptyView(view)) {
                continue;
            }
            view.setVisibility(GONE);
        }
    }

    /**
     * 获取除了对象是否是EmptyOrErrorView 的子View
     */
    private void getChildViews() {
        int childCount = getChildCount();
        View view;
        for (int i = 0; i < childCount; i++) {
            view = getChildAt(i);
            if (isEmptyView(view)) {
                continue;
            }
            childViews.add(view);
        }
    }

    /**
     * 显示除了EmptyOrErrorView 的子View
     */
    private void showChildView() {
        for (View view : childViews) {
            if (isEmptyView(view)) {
                continue;
            }
            view.setVisibility(VISIBLE);
        }
    }


    /**
     * 视图的点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (mCustomErrorViewLayoutClickListenler != null && currentState != TYPE_LOADING) {
            mCustomErrorViewLayoutClickListenler.customErrorClick(view);
        }
    }
}
