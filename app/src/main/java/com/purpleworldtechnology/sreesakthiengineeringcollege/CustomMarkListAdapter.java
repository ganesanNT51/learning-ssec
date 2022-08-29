package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

class CustomMarkListAdapter extends ArrayAdapter<Subjects> {

    private int resourceLayout;
    private Context mContext;
    List<Subjects> mark_data;
    Date date1;
    Date date;


    public CustomMarkListAdapter(Context context, int resource, List<Subjects> mark_data) {
        super(context, resource, mark_data);
        this.resourceLayout = resource;
        this.mContext = context;
        this.mark_data = mark_data;
    }
//    public CustomAdapter(Context context, int resource, List<PdfContent> items) {
//        super(context, resource, items);
//        this.resourceLayout = resource;
//        this.mContext = context;
//        this.items = items;
//    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
//            v = vi.inflate(R.layout.items, null);

            v = vi.inflate(resourceLayout, null);

        }

        Subjects p = mark_data.get(position);

        if (p != null) {
            TextView subject_name_tv = (TextView) v.findViewById(R.id.subject_name_tv);
            TextView mark_tv         = (TextView) v.findViewById(R.id.mark_tv);

            if (subject_name_tv != null) {
                subject_name_tv.setText(p.subject_name);
                mark_tv.setText(String.valueOf(p.mark));

            }




        }

        return v;
    }

}
