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

public class ConferenceAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] titles,content;
    private final Integer[] imageId;

    public ConferenceAdapter(Activity context,
                             String[] titles,String[] content, Integer[] imageId) {
        super(context,R.layout.conference_single, titles);
        this.context = context;
        this.titles = titles;
        this.content = content;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.conference_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_title_single);
        TextView txtcontent = (TextView) rowView.findViewById(R.id.txt_content_single);

        ImageView imageViewLeft = (ImageView) rowView.findViewById(R.id.circular_img_left);
        ImageView imageViewRight = (ImageView) rowView.findViewById(R.id.circular_img_right);
        txtTitle.setText(titles[position]);
        txtcontent.setText(content[position]);

        imageViewLeft.setImageResource(imageId[position]);
        imageViewRight.setImageResource(imageId[position]);
        if(position%2 != 0){
            imageViewLeft.setVisibility(View.GONE);
            imageViewRight.setVisibility(View.VISIBLE);
        }
        return rowView;
    }
}
