package com.fbw.definedscrollview;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

public class DefinedFrameLayout extends FrameLayout implements DefinedInterface{

    public int mFromBgColor;//背景颜色变化开始值
    public int mToBgColor;//背景颜色变化结束值
    public boolean mAlpha;//是否需要透明度动画
    public int mTranslation;//平移值  definedscrollve_translation 中有定义四个方向
    public boolean mScaleX;//是否需要x轴方向缩放
    public boolean mScaleY;//是否需要y轴方向缩放

    private int mHeight;//本view的高度
    private int mWidth;//宽度

    private static final int TRANSLATION_FROM_TOP = 0x01;
    private static final int TRANSLATION_FROM_BOTTOM = 0x02;
    private static final int TRANSLATION_FROM_LEFT = 0x04;
    private static final int TRANSLATION_FROM_RIGHT = 0x08;


    //颜色估值器
    private static ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();

    public DefinedFrameLayout(@NonNull Context context) {
        super(context);
    }

    public DefinedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DefinedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DefinedFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * 这个是监听view的大小变化，
     * 因为这个DefinedFrameLayout 在scrollview中滚动的时候，存在大小变化
     * 大小发生改变，，动画需要执行改变view的属性，或者恢复属性
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mHeight = w;
//        onResetScrollve();
    }

    @Override
    public void onScroll(float ratio) {
// ratio:0~1
        //控制自身的动画属性
        if(mAlpha){
            setAlpha(ratio);
        }
        if(mScaleX){
            setScaleX(ratio);
        }
        if(mScaleY){
            setScaleY(ratio);
        }

//		int mDisCrollveTranslation 有很多种枚举的情况

        //判断到底是哪一种值：fromTop,fromBottom,fromLeft,fromRight
        //fromBottom
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight*(1-ratio));//mHeight-->0(代表原来的位置)
        }
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight*(1-ratio));//-mHeight-->0(代表原来的位置)
        }
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth*(1-ratio));//-width-->0(代表原来的位置)
        }
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth*(1-ratio));//width-->0(代表原来的位置)
        }

        //颜色渐变动画
        if(mFromBgColor!=-1&&mToBgColor!=-1){
            //ratio=0.5 color=中间颜色
            setBackgroundColor((Integer) sArgbEvaluator.evaluate(ratio, mFromBgColor, mToBgColor));
        }
    }
    private boolean isDiscrollTranslationFrom(int translationMask) {
        if(mTranslation==-1){
            return false;
        }
        //fromLeft|fromBottom & fromBottom = fromBottom
        return (mTranslation & translationMask)==translationMask;
    }

    @Override
    public void onResetScrollve() {

        //控制自身的动画属性
        if(mAlpha){
            setAlpha(0);
        }
        if(mScaleX){
            setScaleX(0);
        }
        if(mScaleY){
            setScaleY(0);
        }
//		int mDisCrollveTranslation 有很多种枚举的情况
        //判断到底是哪一种值：fromTop,fromBottom,fromLeft,fromRight
        //fromBottom
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_BOTTOM)){
            setTranslationY(mHeight);//mHeight-->0(代表原来的位置)
        }
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_TOP)){
            setTranslationY(-mHeight);//-mHeight-->0(代表原来的位置)
        }
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_LEFT)){
            setTranslationX(-mWidth);//-width-->0(代表原来的位置)
        }
        if(isDiscrollTranslationFrom(TRANSLATION_FROM_RIGHT)){
            setTranslationX(mWidth);//width-->0(代表原来的位置)
        }
    }
}
