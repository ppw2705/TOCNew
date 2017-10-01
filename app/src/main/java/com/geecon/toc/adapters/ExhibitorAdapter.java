package com.geecon.toc.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geecon.toc.R;
import com.geecon.toc.models.ExhibitorModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI on 3/21/2017.
 */

public class ExhibitorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ExhibitorAdapter.class.getSimpleName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<ExhibitorModel> exhibitorModel = new ArrayList<>();
    Context context;
    private OnItemClickListener mItemClickListener;

    public ExhibitorAdapter( Context context, List<ExhibitorModel> exhibitorModel, OnItemClickListener listener) {
        this.context = context;
        this.exhibitorModel.addAll(exhibitorModel);
        this.mItemClickListener = listener;
    }

    public void addSpeaker(List<ExhibitorModel> exhibitor) {
        this.exhibitorModel.addAll(exhibitor);
        notifyDataSetChanged();
    }

    public void addSingleSpeaker(ExhibitorModel exhibitor) {
        this.exhibitorModel.add(exhibitor);
        notifyItemInserted(exhibitorModel.size() - 1);
    }

    public void removeNullLastItem() {
        if(!exhibitorModel.isEmpty() && null == exhibitorModel.get(exhibitorModel.size() - 1)) {
            this.exhibitorModel.remove(exhibitorModel.size() - 1);
            notifyItemRemoved(exhibitorModel.size() - 1);
        }
    }

    public void clearList() {
        exhibitorModel.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.exhibitor_single, parent, false);
            return new ExhibitorAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (vholder instanceof ExhibitorAdapter.ViewHolder) {
            final ExhibitorModel exhibitor = exhibitorModel.get(position);
            ExhibitorAdapter.ViewHolder holder = (ExhibitorAdapter.ViewHolder) vholder;

            holder.bind(exhibitor, mItemClickListener);
            holder.txtName.setText(exhibitor.getEXHIBITOR_NAME());
            holder.txtStand.setText(exhibitor.getBOOTH());
            //holder.imgEx.setImageResource(exhibitor.getIMAGE());
            if(exhibitor.getEPIC_FILENAME()!= null && exhibitor.getEPIC_FILENAME()!= ""){
                Picasso.with(context).load(exhibitor.getEPIC_FILENAME()).placeholder(R.drawable.pro).resize(200,200).into(holder.imgEx);
            }
        }
        if (vholder instanceof ExhibitorAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) vholder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return exhibitorModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return exhibitorModel == null ? 0 : exhibitorModel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtStand;
        public ImageView imgEx;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txt_name);
            txtStand = (TextView)itemView.findViewById(R.id.txt_stand);
            imgEx = (ImageView)itemView.findViewById(R.id.ex_image);
        }

        public void bind(final ExhibitorModel item, final OnItemClickListener listener) {
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
        void onItemClick(View v, ExhibitorModel item);
    }

}
