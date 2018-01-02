package com.wrc.androidprocess.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**自适应宽度（wrap-content）的Listview
 * Created by wrc on 2018/1/2/002.
 */

public class WrapContentListView extends ListView {
    
    public WrapContentListView (Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public WrapContentListView (Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public WrapContentListView (Context context) {
        super(context);
    }
    
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
