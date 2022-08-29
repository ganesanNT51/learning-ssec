package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


public class CustomLatecomeListAdapter extends ArrayAdapter<Latecome> {

    private int resourceLayout;
    private Context mContext;
    List<Latecome> latecome_data;

    public CustomLatecomeListAdapter(Context context, int resource, List<Latecome> latecome_data) {
        super(context, resource, latecome_data);
        this.resourceLayout = resource;
        this.mContext = context;
        this.latecome_data = latecome_data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);

        }
        Latecome r = latecome_data.get(position);
        if (r != null) {

            TextView arrival_time_tv   = (TextView) v.findViewById(R.id.arrival_time_tv);
            if (arrival_time_tv != null) {
                arrival_time_tv.setText( "Arrival Time  " + r.arrival_time);
            }
        }

        return v;
    }

}
