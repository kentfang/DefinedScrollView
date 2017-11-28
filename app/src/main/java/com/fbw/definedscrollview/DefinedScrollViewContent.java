package com.fbw.definedscrollview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class DefinedScrollViewContent extends LinearLayout {
    public DefinedScrollViewContent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
    }
    /**
     * Returns a new set of layout parameters based on the supplied attributes set.
     * 根据提供的属性，返回一个新的属性集合
     * @param attrs the attributes to build the layout parameters from
     *
     * @return an instance of {@link android.view.ViewGroup.LayoutParams} or one
     *         of its descendants
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new DefinedLayoutParams(getContext(),attrs);
    }

    /**
     * Adds a child view with the specified layout parameters.
     * 重载这个方法，主要是为了判断这个子view是否存在有自定义的属性，如果不存在，不做处理
     * 如果存在自定义的属性，给子view封装一层自定义的FrameLayout,并把这些自定义的属性传给FrameLayout
     * <p><strong>Note:</strong> do not invoke this method from
     * {@link #draw(android.graphics.Canvas)}, {@link #onDraw(android.graphics.Canvas)},
     * {@link #dispatchDraw(android.graphics.Canvas)} or any related method.</p>
     * 添加子view，需要带LayoutParams
     * @param child the child view to add
     * @param index the position at which to add the child or -1 to add last
     * @param params the layout parameters to set on the child
     *
     */
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        DefinedLayoutParams p = (DefinedLayoutParams) params;
        if(!isDiscrollvable(p)){
            //不存在自定义属性，直接调用父类的addview
            Log.d("fbw","没有自定义属性："+index);
            super.addView(child,index,params);
        }else{
            //存在自定义属性，给view 外出封装包裹一层FrameLayout,并把自定义的属性传递给FrameLayout
            //实际添加到父布局的子view 就是这个FrameLayout
            Log.d("fbw","有自定义属性："+index);
            DefinedFrameLayout frameLayout = new DefinedFrameLayout(getContext());
            frameLayout.mAlpha = p.mAlpha;
            frameLayout.mFromBgColor = p.mFromBgColor;
            frameLayout.mScaleX = p.mScaleX;
            frameLayout.mScaleY = p.mScaleY;
            frameLayout.mToBgColor = p.mToBgColor;
            frameLayout.mTranslation = p.mTranslation;
            frameLayout.addView(child);
            super.addView(frameLayout,index,params);
        }
    }

    /**
     * 判断这个view，是否存在自定义的属性
     * @param p
     * @return
     */
    private boolean isDiscrollvable(DefinedLayoutParams p) {
        // TODO Auto-generated method stub
        return p.mAlpha||
                p.mScaleX||
                p.mScaleY||
                p.mTranslation!=-1||
                (p.mFromBgColor!=-1&&
                        p.mToBgColor!=-1);
    }

    static class DefinedLayoutParams extends LinearLayout.LayoutParams{

        //自定义的属性值，在attrs 中有定义
        public int mFromBgColor;//背景颜色变化开始值
        public int mToBgColor;//背景颜色变化结束值
        public boolean mAlpha;//是否需要透明度动画
        public int mTranslation;//平移值  definedscrollve_translation 中有定义四个方向
        public boolean mScaleX;//是否需要x轴方向缩放
        public boolean mScaleY;//是否需要y轴方向缩放
        public DefinedLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            // 从child里面拿到我自定义的属性
            TypedArray a = c.obtainStyledAttributes(attrs,R.styleable.definedScrollView_LayoutParams);
            mFromBgColor = a.getColor(R.styleable.definedScrollView_LayoutParams_definedscrollve_fromBgColor,-1);
            mToBgColor = a.getColor(R.styleable.definedScrollView_LayoutParams_definedscrollve_toBgColor,-1);
            mAlpha = a.getBoolean(R.styleable.definedScrollView_LayoutParams_definedscrollve_alpha,false);
            mTranslation = a.getColor(R.styleable.definedScrollView_LayoutParams_definedscrollve_translation,-1);
            mScaleX = a.getBoolean(R.styleable.definedScrollView_LayoutParams_definedscrollve_scaleX,false);
            mScaleY = a.getBoolean(R.styleable.definedScrollView_LayoutParams_definedscrollve_scaleY,false);
            a.recycle();
        }
    }
}
