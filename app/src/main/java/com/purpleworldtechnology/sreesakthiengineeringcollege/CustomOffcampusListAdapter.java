package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;


public class CustomOffcampusListAdapter extends ArrayAdapter<Offcampus> {

    private int resourceLayout;
    private Context mContext;
    List<Offcampus> offcampus_data;

    public CustomOffcampusListAdapter(Context context, int resource, List<Offcampus> offcampus_data) {
        super(context, resource, offcampus_data);
        this.resourceLayout = resource;
        this.mContext = context;
        this.offcampus_data = offcampus_data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);

        }
        Offcampus r = offcampus_data.get(position);
        if (r != null) {

            TextView company_or_college_name_tv   = (TextView) v.findViewById(R.id.company_or_college_name_tv);
            TextView role_tv   = (TextView) v.findViewById(R.id.role_tv);

            TextView interview_date_tv   = (TextView) v.findViewById(R.id.interview_date_tv);
            TextView eligibility_tv   = (TextView) v.findViewById(R.id.eligibility_tv);
            TextView address_tv   = (TextView) v.findViewById(R.id.address_tv);

//            TextView company_or_college_name_tv   = (TextView) v.findViewById(R.id.company_or_college_name_tv);
            if (company_or_college_name_tv != null) {
                company_or_college_name_tv.setText( r.company_or_college_name);

                role_tv.setText( r.role);
                interview_date_tv.setText( r.interview_date);


                eligibility_tv.setText( r.eligibility);
                address_tv.setText( r.address);
            }
        }

        return v;
    }

}
