package com.geecon.toc.adapters;

/**
 * Created by MI on 3/14/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geecon.toc.R;
import com.geecon.toc.models.SpeakerModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class SpeakerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SpeakerAdapter.class.getSimpleName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<SpeakerModel> speakerModel = new ArrayList<>();
    Context context;
    private OnItemClickListener mItemClickListener;

    public SpeakerAdapter( Context context, List<SpeakerModel> speakerModel, OnItemClickListener listener) {
        this.context = context;
        this.speakerModel.addAll(speakerModel);
        this.mItemClickListener = listener;
    }

    public void addSpeaker(List<SpeakerModel> eaSpeaker) {
        this.speakerModel.addAll(eaSpeaker);
        notifyDataSetChanged();
    }

    public void addSingleSpeaker(SpeakerModel eaSpeaker) {
        this.speakerModel.add(eaSpeaker);
        notifyItemInserted(speakerModel.size() - 1);
    }

    public void removeNullLastItem() {
        if(!speakerModel.isEmpty() && null == speakerModel.get(speakerModel.size() - 1)) {
            this.speakerModel.remove(speakerModel.size() - 1);
            notifyItemRemoved(speakerModel.size() - 1);
        }
    }

    public void clearList() {
        speakerModel.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.grid_element_speaker, parent, false);
            return new SpeakerAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (vholder instanceof SpeakerAdapter.ViewHolder) {
            final SpeakerModel eaSpeaker = speakerModel.get(position);
            SpeakerAdapter.ViewHolder holder = (SpeakerAdapter.ViewHolder) vholder;

            holder.bind(eaSpeaker, mItemClickListener);
            holder.txtName.setText(eaSpeaker.getSPEAKER_NAME());
            holder.txtTitles.setText(eaSpeaker.getDESIGNATION());
            holder.txtDivision.setText(eaSpeaker.getCOMPANY_NAME());
            Log.e(TAG, "onBindViewHolder: "+ eaSpeaker.getIMAGE());
            if(eaSpeaker.getIMAGE()!= null && eaSpeaker.getIMAGE()!= ""){
                Picasso.with(context).load(eaSpeaker.getIMAGE()).placeholder(R.drawable.pro).resize(200,200).into(holder.imgSpeaker);
            }
        }
        if (vholder instanceof SpeakerAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) vholder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }


    @Override
    public long getItemId(int position) {
        return speakerModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    /*@Override
    public int getItemViewType(int position) {
        return position;
    }*/

    @Override
    public int getItemViewType(int position) {
        return speakerModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return speakerModel == null ? 0 : speakerModel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtTitles, txtDivision;
        public ImageView imgSpeaker;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txt_name);
            txtTitles = (TextView)itemView.findViewById(R.id.txt_dep);
            txtDivision = (TextView)itemView.findViewById(R.id.txt_desc);
            imgSpeaker = (ImageView)itemView.findViewById(R.id.speaker_image);
        }

        public void bind(final SpeakerModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(v,item);
                }
            });
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
        void onItemClick(View v, SpeakerModel item);
    }
}

