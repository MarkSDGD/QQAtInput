package com.mark.customatfriends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mark.customatfriends.R;
import com.mark.customatfriends.bean.Member;

import java.util.ArrayList;

/**
 * Created by Mark on 2020/11/27.
 * <p>Copyright 2020 ZTZQ.</p>
 */

public class MemberAdapter extends BaseAdapter {
    Context mContext;
    private ArrayList<Member> mMembers = new ArrayList<>();
    public MemberAdapter(Context mContext,ArrayList<Member> mMembers) {
        this.mContext = mContext;
        this.mMembers=mMembers;
    }

    @Override
    public int getCount() {
        if (mMembers != null) {
            return mMembers.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mMembers != null) {
            return mMembers.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if (mMembers != null) {
            return position;
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_member, null);
            holder = new ViewHolder();
            holder.mTvName = convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Member member = mMembers.get(position);
        holder.mTvName.setText(member.getName());
        return convertView;
    }
}

class ViewHolder {
    TextView mTvName;
}