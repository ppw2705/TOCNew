package com.geecon.toc.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geecon.toc.R;
import com.geecon.toc.models.CalendarModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by grevin on 06/06/17.
 */

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = CalendarAdapter.class.getSimpleName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int ratings = 0;
    List<CalendarModel> calModelList = new ArrayList<>();
    Context context;
    OnItemClickListener listener;

    public CalendarAdapter(Context context, List<CalendarModel> calModel, OnItemClickListener listener) {
        this.context = context;
        this.calModelList.addAll(calModel);
        this.listener = listener;
    }

    public void addEvents(List<CalendarModel> eaEvent) {
        this.calModelList.addAll(eaEvent);
        notifyDataSetChanged();
    }

    public void addSingleEvent(CalendarModel eaEvent) {
        this.calModelList.add(eaEvent);
        notifyItemInserted(calModelList.size() - 1);
    }

    public void remove(CalendarModel eaEvent) {
        this.calModelList.remove(eaEvent);
        notifyDataSetChanged();
    }

    public void removeNullLastItem() {
        if(!calModelList.isEmpty() && null == calModelList.get(calModelList.size() - 1)) {
            this.calModelList.remove(calModelList.size() - 1);
            notifyItemRemoved(calModelList.size() - 1);
        }
    }

    public void clearList() {
        calModelList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.calendar_single, parent, false);
            return new CalendarAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new CalendarAdapter.LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalendarAdapter.ViewHolder) {
            final CalendarModel eaEvent = calModelList.get(position);
            final CalendarAdapter.ViewHolder caHolder = (CalendarAdapter.ViewHolder) holder;

            caHolder.txtTitle.setText(eaEvent.getTITLE());
            caHolder.txtDate.setText(eaEvent.getTIME());
            //caHolder.txtTime.setText(eaEvent.getTIME());
            caHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,String.valueOf(v.getTag()),Toast.LENGTH_SHORT).show();
                    listener.onDeleteClicked(v,eaEvent);
                }
            });

        }
        if (holder instanceof CalendarAdapter.LoadingViewHolder) {
            CalendarAdapter.LoadingViewHolder loadingViewHolder = (CalendarAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return calModelList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return calModelList == null ? 0 : calModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDate,txtTime;
        ImageView imgDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_event_name);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date_time);
            imgDelete = (ImageView)itemView.findViewById(R.id.delete_event);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
        }
    }

    public interface OnItemClickListener {
        void onDeleteClicked(View v,CalendarModel calendarModel);
    }
}
