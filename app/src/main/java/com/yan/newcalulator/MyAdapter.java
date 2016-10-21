package com.yan.newcalulator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Yan on 2016/10/18.
 */
public class MyAdapter extends BaseAdapter {
    private List<History> mList;
    private LayoutInflater mInflater;

    public MyAdapter(Context main_activity, List<History> mList) {
        this.mList = mList;
        mInflater = LayoutInflater.from(main_activity);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        History mItembean = mList.get(position);
        if (convertView == null) {
            //创建一个新视图
            convertView = mInflater.inflate(R.layout.historylistviewlayout, null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mTextView_historyques.setText(mItembean.getQues());
        mViewHolder.mTextView_historyresult.setHint("="+mItembean.getResult());
        return convertView;
    }

    class ViewHolder {
        public TextView mTextView_historyques;
        public TextView mTextView_historyresult;

        public ViewHolder(View view) {
            mTextView_historyques = (TextView) view.findViewById(R.id.text_historyques);
            mTextView_historyresult = (TextView) view.findViewById(R.id.text_historyresult);
        }
    }
}
