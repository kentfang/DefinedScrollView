package com.fbw.definedscrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * 这个scrollview 需要监听滚动事件，并且根据滚动的距离转换成百分比，然后将百分比传递给子view（DefinedFrameLayout）去重置view的属性
 */
public class DefinedScrollView extends ScrollView {

    private DefinedScrollViewContent mContent;

    public DefinedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //当加载view完成时，获取到DefinedScrollView的第一个view，其实就是DefinedScrollViewContent
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = (DefinedScrollViewContent) this.getChildAt(0);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);
        View first = mContent.getChildAt(0);
        first.getLayoutParams().height = getHeight();
    }


    @Override
    protected void onScrollChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int scrollViewHeight = getHeight();
        //监听滑动----接口---->控制DiscrollViewContent的属性
        for(int i=0;i<mContent.getChildCount();i++){//遍历MyLinearLayout里面所有子控件(MyViewGroup)
            View child = mContent.getChildAt(i);
            if(!(child instanceof DefinedInterface)){
                continue;
            }

            //ratio:0~1
            DefinedInterface discrollvableInterface =  (DefinedInterface) child;
            //1.child离scrollview顶部的高度  包括滑出去的部分
            int discrollvableTop = child.getTop();
            int discrollvableHeight = child.getHeight();

            //2.得到scrollview滑出去的高度
            //3.得到child离屏幕顶部的高度
            int discrollvableAbsoluteTop = discrollvableTop - h;
            //什么时候执行动画？当child滑进屏幕的时候
            if(discrollvableAbsoluteTop<=scrollViewHeight){
                int visibleGap = scrollViewHeight - discrollvableAbsoluteTop;
                //确保ratio是在0~1，超过了1 也设置为1
                discrollvableInterface.onScroll(clamp(visibleGap/(float)discrollvableHeight, 1f,0f));
            }else{//否则，就恢复到原来的位置
                discrollvableInterface.onResetScrollve();
            }
        }
    }
    public static float clamp(float value, float max, float min){
        return Math.max(Math.min(value, max), min);
    }
}
