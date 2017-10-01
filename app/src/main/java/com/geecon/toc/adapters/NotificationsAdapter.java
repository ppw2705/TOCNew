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
import com.geecon.toc.models.NotificationModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MI on 3/23/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = NotificationsAdapter.class.getSimpleName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<NotificationModel> notificationModel = new ArrayList<>();
    Context context;
    private OnItemClickListener mItemClickListener;

    public NotificationsAdapter( Context context, List<NotificationModel> notificationModel, OnItemClickListener listener) {
        this.context = context;
        this.notificationModel.addAll(notificationModel);
        this.mItemClickListener = listener;
    }

    public void addNotifications(List<NotificationModel> notification) {
        this.notificationModel.addAll(notification);
        notifyDataSetChanged();
    }

    public void addSingleNotification(NotificationModel notification) {
        this.notificationModel.add(notification);
        notifyItemInserted(notificationModel.size() - 1);
    }

    public void removeNullLastItem() {
        if(!notificationModel.isEmpty() && null == notificationModel.get(notificationModel.size() - 1)) {
            this.notificationModel.remove(notificationModel.size() - 1);
            notifyItemRemoved(notificationModel.size() - 1);
        }
    }

    public void clearList() {
        notificationModel.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.notify_single, parent, false);
            return new NotificationsAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vholder, int position) {
        if (vholder instanceof NotificationsAdapter.ViewHolder) {
            final NotificationModel notification = notificationModel.get(position);
            NotificationsAdapter.ViewHolder holder = (NotificationsAdapter.ViewHolder) vholder;

            holder.bind(notification, mItemClickListener);
            holder.txtTime.setText(notification.getCREATED_ON());
            holder.txtTitle.setText(notification.getTITLE());
            holder.txtMessage.setText(notification.getMESSAGE());
            //holder.imgEx.setImageResource(notification.getIMAGE());
            /*if(notification.getIMAGE()!= null && notification.getIMAGE()!= ""){
                Picasso.with(context).load(notification.getIMAGE()).placeholder(R.drawable.pro).resize(200,200).into(holder.imgEx);
            }*/
        }
        if (vholder instanceof NotificationsAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) vholder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return notificationModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return notificationModel == null ? 0 : notificationModel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTime, txtTitle, txtMessage;
        public ImageView imgNotify;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView)itemView.findViewById(R.id.notify_time);
            txtTitle = (TextView)itemView.findViewById(R.id.notify_title);
            txtMessage = (TextView)itemView.findViewById(R.id.notify_message);
            imgNotify = (ImageView)itemView.findViewById(R.id.notify_img);
        }

        public void bind(final NotificationModel item, final OnItemClickListener listener) {
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
        void onItemClick(View v, NotificationModel item);
    }
}
