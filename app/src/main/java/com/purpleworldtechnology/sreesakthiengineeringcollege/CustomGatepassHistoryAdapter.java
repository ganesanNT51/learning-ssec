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

class CustomGatepassHistoryAdapter extends ArrayAdapter<GatePass> {

    private int resourceLayout;
    private Context mContext;
    List<GatePass> gatepass_data;
    Date date1;
    Date date;


    public CustomGatepassHistoryAdapter(Context context, int resource, List<GatePass> gatepass_data) {
        super(context, resource, gatepass_data);
        this.resourceLayout = resource;
        this.mContext = context;
        this.gatepass_data = gatepass_data;
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

        GatePass p = gatepass_data.get(position);

        if (p != null) {
            TextView gate_pass_id_tv = (TextView) v.findViewById(R.id.gate_pass_id_tv);
            TextView RequetDate_tv   = (TextView) v.findViewById(R.id.RequetDate_tv);
            TextView reason_tv       = (TextView) v.findViewById(R.id.reason_tv);

            TextView RequestTo_tv       = (TextView) v.findViewById(R.id.RequestTo_tv);
            TextView ReturnDate_tv       = (TextView) v.findViewById(R.id.ReturnDate_tv);
            TextView Request_status_tv = (TextView) v.findViewById(R.id.Request_status_tv);






            if (gate_pass_id_tv != null) {
                gate_pass_id_tv.setText("Gete Pass ID :  " + String.valueOf(p.gatepass_rq_id) );
                RequetDate_tv.setText(p.create_date);
                reason_tv.setText("Reason  : " + p.reason  );
                RequestTo_tv.setText("Request To : " + p.request_to);
                ReturnDate_tv.setText("Return Date : " + p.return_date);
                Request_status_tv.setText("Gate Pass Request Status : " + p.request_status);

            }




        }

        return v;
    }

}