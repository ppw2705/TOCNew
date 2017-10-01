package com.geecon.toc.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geecon.toc.R;

/**
 * Created by MI on 12/30/2016.
 */

public class TeamAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] names,titles,phones,firstNames;
    private final Integer[] imageId;

    public TeamAdapter(Activity context,
                       String[] names,String[] titles,String[] phones,String[] firstNames, Integer[] imageId) {
        super(context,R.layout.team_single, names);
        this.context = context;
        this.names = names;
        this.titles = titles;
        this.firstNames = firstNames;
        this.phones = phones;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.team_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_title);
        TextView txtName = (TextView) rowView.findViewById(R.id.txt_name);
        TextView txtphones = (TextView) rowView.findViewById(R.id.txt_phone);
        TextView txtEmail =(TextView) rowView.findViewById(R.id.txt_email_name);
        TextView txtLinkedin =(TextView) rowView.findViewById(R.id.txt_linkedin_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.circular_img);
        txtTitle.setText(titles[position]);
        txtName.setText(names[position]);
        txtphones.setText(phones[position]);
        txtEmail.setText(" "+firstNames[position]);
        txtLinkedin.setText(" "+firstNames[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
