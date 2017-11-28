package com.fbw.definedscrollview;

/**
 * Created by Administrator on 2017/11/24.
 */

interface DefinedInterface {
    /**
     * 当滑动的时候调用该方法，用来控制里面的控件执行相应的动画
     * @param ratio 0 到 1 之间的一个百分比值
     */
     void onScroll(float ratio);

    /**
     * 重置view的属性----恢复view的原来属性
     */
     void onResetScrollve();
}
