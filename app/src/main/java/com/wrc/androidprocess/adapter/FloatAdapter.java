package com.wrc.androidprocess.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wrc.androidprocess.R;
import com.wrc.androidprocess.bean.ShowInfos;

import java.util.List;


/**
 * Created by wrc_urovo on 2017/12/29/029.
 */

public class FloatAdapter extends BaseAdapter {
    private  Context mContext;
    private List<ShowInfos> mData ;
    private LayoutInflater mInflater;//布局装载器对象
    // 通过构造方法将数据源与数据适配器关联起来
    // context:要使用当前的Adapter的界面对象
    public FloatAdapter(Context context, List<ShowInfos> list) {
        mContext = context;
        mData = list;
        mInflater = LayoutInflater.from(mContext);
    }
    
    public  void setData(List<ShowInfos> data){
        if (data !=null){
            mData = data;
           notifyDataSetChanged();
        }
        
    }
    
    @Override
    //ListView需要显示的数据数量
    public int getCount() {
        return mData.size();
    }
    
    @Override
    //指定的索引对应的数据项
    public Object getItem(int position) {
        return mData.get(position);
    }
    
    @Override
    //指定的索引对应的数据项ID
    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        //如果view未被实例化过，缓存池中没有对应的缓存
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
            convertView = mInflater.inflate(R.layout.list_item, null);
            
            //对viewHolder的属性进行赋值
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.float_list_image);
            viewHolder.title = (TextView) convertView.findViewById(R.id.float_list_text);
            
            //通过setTag将convertView与viewHolder关联
            convertView.setTag(viewHolder);
        }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 取出bean对象
        ShowInfos bean = mData.get(position);
        
        // 设置控件的数据
        viewHolder.imageView.setImageDrawable(bean.getDrawable());
        viewHolder.title.setText(bean.getAppName());
        
        return convertView;
    }
    // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
    class ViewHolder{
        public ImageView imageView;
        public TextView title;
    }
}
