package com.geecon.toc.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.geecon.toc.R;
import com.geecon.toc.models.ExhibitorModel;
import com.geecon.toc.utils.AppPreferences;
import com.squareup.picasso.Picasso;
import com.viethoa.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by grevin on 30/05/17.
 */

public class ExhibitorNewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerViewFastScroller.BubbleTextGetter {

    private static final String TAG = ExhibitorNewAdapter.class.getSimpleName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<ExhibitorModel> exhibitorModel = new ArrayList<>();
    List<ExhibitorModel> favExhibitorModel = new ArrayList<>();
    Context context;
    private OnItemClickListener mItemClickListener;

    public ExhibitorNewAdapter( Context context, List<ExhibitorModel> exhibitorModel, OnItemClickListener listener) {
        this.context = context;
        this.exhibitorModel.addAll(exhibitorModel);
        this.mItemClickListener = listener;
    }

    public void addSpeaker(List<ExhibitorModel> exhibitor) {
        this.exhibitorModel.addAll(exhibitor);
        notifyDataSetChanged();
    }

    public void remove(ExhibitorModel exhibitor) {
        this.exhibitorModel.remove(exhibitor);
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
            View view = LayoutInflater.from(context).inflate(R.layout.exhibitor_single_new, parent, false);
            return new ExhibitorNewAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (vholder instanceof ExhibitorNewAdapter.ViewHolder) {
            favExhibitorModel = AppPreferences.getInstance().getExhibitorData();
            final ExhibitorModel exhibitor = exhibitorModel.get(position);
            ExhibitorNewAdapter.ViewHolder holder = (ExhibitorNewAdapter.ViewHolder) vholder;
            holder.setIsRecyclable(false);
            holder.bind(exhibitor, mItemClickListener);
            holder.txtName.setText(Html.fromHtml(exhibitor.getEXHIBITOR_NAME().replace("&amp;","&")));
            holder.txtStand.setText(exhibitor.getBOOTH());
            if(Integer.parseInt(exhibitor.getIS_SPONSORED()) == 0){
                holder.relEx.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.relSponsor.setVisibility(View.GONE);
                holder.relStand.setVisibility(View.VISIBLE);
            }

            if(exhibitor.getBOOTH().equals("") || exhibitor.getBOOTH() == null){
                holder.relStand.setVisibility(View.GONE);
            }
            if(!exhibitor.getBOOTH().isEmpty()){
                holder.relStand.setVisibility(View.VISIBLE);
            }

            if(Integer.parseInt(exhibitor.getIS_SPONSORED()) == 1){
                holder.txtName.setTextColor(Color.WHITE);
                Animation RightSwipe = AnimationUtils.loadAnimation(context, R.anim.left_slide);
                holder.txtName.startAnimation(RightSwipe);
                holder.relEx.setBackgroundColor(Color.RED);
                holder.relSponsor.setVisibility(View.VISIBLE);
                holder.relStand.setVisibility(View.GONE);
            }
            //holder.imgEx.setImageResource(exhibitor.getIMAGE());
            /*if(exhibitor.getEPIC_FILENAME()!= null && exhibitor.getEPIC_FILENAME()!= ""){
                Picasso.with(context).load(exhibitor.getEPIC_FILENAME()).placeholder(R.drawable.pro).resize(200,200).into(holder.imgEx);
            }*/
            if(favExhibitorModel!=null) {
                if(favExhibitorModel.size() != 0) {
                    if (favExhibitorModel.contains(exhibitor)) {
                        holder.imgEx.setImageResource(R.drawable.star_checked);
                    }
                    if (!favExhibitorModel.contains(exhibitor)) {
                        holder.imgEx.setImageResource(R.drawable.star);
                    }

                }
            }

            holder.imgEx.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onImageClick((ImageView)v,exhibitor);
                }
            });

            holder.imgRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onRemoveClick(v,exhibitor);
                }
            });

            if(context.getClass().getSimpleName().equals("FavExhibitorActivity")){
                holder.imgRemove.setVisibility(View.VISIBLE);
            } else {
                holder.imgEx.setVisibility(View.VISIBLE);
            }
        }
        if (vholder instanceof ExhibitorNewAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) vholder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return exhibitorModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        if (pos < 0 || pos >= exhibitorModel.size())
            return null;

        String name = exhibitorModel.get(pos).getEXHIBITOR_NAME();
        if (name == null || name.length() < 1)
            return null;

        return exhibitorModel.get(pos).getEXHIBITOR_NAME().substring(0, 1);
    }

    @Override
    public int getItemCount() {
        return exhibitorModel == null ? 0 : exhibitorModel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtName, txtStand;
        public ImageView imgEx,imgRemove;
        public RelativeLayout relEx,relStand,relSponsor;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView)itemView.findViewById(R.id.txt_exhibitor_name);
            txtStand = (TextView)itemView.findViewById(R.id.txt_ex_stand);
            relEx = (RelativeLayout)itemView.findViewById(R.id.rel_exhibitor);
            imgEx = (ImageView)itemView.findViewById(R.id.exhibitor_imgView);
            imgRemove = (ImageView)itemView.findViewById(R.id.remove_imgView);
            relStand = (RelativeLayout)itemView.findViewById(R.id.rel_stand);
            relSponsor = (RelativeLayout)itemView.findViewById(R.id.rel_sponsor);
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
        void onImageClick(ImageView v,ExhibitorModel item);
        void onRemoveClick(View v,ExhibitorModel item);
    }

}
