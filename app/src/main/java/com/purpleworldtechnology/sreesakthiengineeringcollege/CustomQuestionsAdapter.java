package com.purpleworldtechnology.sreesakthiengineeringcollege;


import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Date;
import java.util.List;


public class CustomQuestionsAdapter extends ArrayAdapter<Questions> {

    private int resourceLayout;
    private Context mContext;
    List<Questions> question_data;

    public CustomQuestionsAdapter(Context context, int resource, List<Questions> question_data) {
        super(context, resource, question_data);
        this.resourceLayout = resource;
        this.mContext = context;
        this.question_data = question_data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);

        }
        Questions r = question_data.get(position);
        if (r != null) {

            TextView subjec_name_tv   = (TextView) v.findViewById(R.id.subjec_name);
            TextView subjec_code      = (TextView) v.findViewById(R.id.subjec_code);
            TextView month_year   = (TextView) v.findViewById(R.id.month_year);
//            TextView semester   = (TextView) v.findViewById(R.id.semester);
            TextView file_name_tv = (TextView) v.findViewById(R.id.file_name_tv);
            ImageView download_icon = (ImageView) v.findViewById(R.id.download_icon);

//            TextView uploaded_by = (TextView) v.findViewById(R.id.uploaded_by);
//            TextView uploaded_on = (TextView) v.findViewById(R.id.uploaded_on);

            if (subjec_name_tv != null) {
                subjec_name_tv.setText( "Subject Name : " + r.subject_name);
                month_year.setText("Exam conducted month : " + r.exam_month_year);
//                semester.setText(String.valueOf("Semester " +  r.semester_id));
                subjec_code.setText("Subject Code : " + r.subject_code);
                file_name_tv.setText("Subject Code : " + r.file_name);
                file_name_tv.setVisibility(View.GONE);

//                uploaded_by.setText(String.valueOf("Uploaded By : " +  r.staff_name));
//                uploaded_on.setText(String.valueOf("Uploaded On : " +  r.created_at));


                String directory_path = Environment.getExternalStorageDirectory().getPath() + "/SSEC/QB/"+r.file_name;
                File file = new File(directory_path);
                if (!file.exists())
                {
                    download_icon.setVisibility(View.VISIBLE);
                }
                else
                {
                    download_icon.setVisibility(View.GONE);
                }
            }
        }

        return v;
    }

}
