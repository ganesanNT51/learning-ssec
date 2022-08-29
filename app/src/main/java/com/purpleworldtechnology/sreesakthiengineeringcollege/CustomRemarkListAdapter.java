package com.purpleworldtechnology.sreesakthiengineeringcollege;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CustomRemarkListAdapter extends ArrayAdapter<Remarks> {

    private int resourceLayout;
    private Context mContext;
    List<Remarks> remark_data;
    Date date1;
    Date date;
    String reformattedStr= "";

    public CustomRemarkListAdapter(Context context, int resource, List<Remarks> remark_data) {
        super(context, resource, remark_data);
        this.resourceLayout = resource;
        this.mContext = context;
        this.remark_data = remark_data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
//            v = vi.inflate(R.layout.items, null);

            v = vi.inflate(resourceLayout, null);

        }

        Remarks r = remark_data.get(position);

        if (r != null) {
            TextView remarkId   = (TextView) v.findViewById(R.id.remarkId);
            TextView remarkedBy = (TextView) v.findViewById(R.id.remarkedBy);
            TextView remarkDate = (TextView) v.findViewById(R.id.remarkDate);
            TextView remarkDisc = (TextView) v.findViewById(R.id.remarkDisc);

            if (remarkId != null) {
                String remark_create_time =  r.created_at;
                remark_create_time = remark_create_time.replace(":","").replace(" ","").replace("-","");
                remarkId.setText("REMARK ID : " + remark_create_time);
                remarkedBy.setText(r.staff_name);
                remarkDisc.setText(r.remark);

                String str_date = r.remark_date;

                SimpleDateFormat view_format = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat fromDB = new SimpleDateFormat("yyyy-MM-dd");

                try {

                     reformattedStr = view_format.format(fromDB.parse(r.remark_date));
                    remarkDate.setText(reformattedStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }





            }




        }

        return v;
    }

}
