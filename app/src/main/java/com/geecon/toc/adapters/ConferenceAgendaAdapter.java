package com.geecon.toc.adapters;

/**
 * Created by MI on 3/14/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bluejamesbond.text.DocumentView;
import com.geecon.toc.R;
import com.geecon.toc.async.SpeakerAsync;
import com.geecon.toc.models.SessionsModel;
import com.geecon.toc.models.SpeakerModel;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ConferenceAgendaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = ConferenceAgendaAdapter.class.getSimpleName();
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private int ratings = 0;
    List<SessionsModel> sessionsModel = new ArrayList<>();
    Context context;
    OnItemClickListener listener;

    public ConferenceAgendaAdapter( Context context, List<SessionsModel> sessionsModel, OnItemClickListener listener) {
        this.context = context;
        this.sessionsModel.addAll(sessionsModel);
        this.listener = listener;
    }

    public void addConferenceAgenda(List<SessionsModel> eaSession) {
        this.sessionsModel.addAll(eaSession);
        notifyDataSetChanged();
    }

    public void addSingleConferenceAgenda(SessionsModel eaSession) {
        this.sessionsModel.add(eaSession);
        notifyItemInserted(sessionsModel.size() - 1);
    }

    public void removeNullLastItem() {
        if(!sessionsModel.isEmpty() && null == sessionsModel.get(sessionsModel.size() - 1)) {
            this.sessionsModel.remove(sessionsModel.size() - 1);
            notifyItemRemoved(sessionsModel.size() - 1);
        }
    }

    public void clearList() {
        sessionsModel.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.ca_single, parent, false);
            return new ConferenceAgendaAdapter.ViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConferenceAgendaAdapter.ViewHolder) {
            ratings = 0;
            final SessionsModel eaSession = sessionsModel.get(position);
            final ConferenceAgendaAdapter.ViewHolder caHolder = (ConferenceAgendaAdapter.ViewHolder) holder;

            //caHolder.rate_label.setText(Html.fromHtml("<u>Rate this Session</u>"));
            caHolder.txtTitle.setText(eaSession.getAll().getHEADING());
            caHolder.txtDate.setText(eaSession.getAll().getSTART_DATE_TIME());
            String desc = eaSession.getAll().getDESCRIPTION();
            String short_desc = desc;
            if(desc.indexOf("\n") > 0)
                short_desc = desc.substring(0,desc.indexOf("\n"));
            caHolder.txtPara.setText(Html.fromHtml(short_desc));
            caHolder.txtPara.setVisibility(View.VISIBLE);
            caHolder.relReadMore.setVisibility(View.VISIBLE);
            if(eaSession.getAll().getDESCRIPTION().isEmpty()){
                caHolder.txtPara.setVisibility(View.GONE);
                caHolder.relReadMore.setVisibility(View.INVISIBLE);
            }
            caHolder.relReadMore.setTag(eaSession.getAll().getSESSION_ID());
            caHolder.relReadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onReadMoreClicked((String) v.getTag());
                }
            });

            caHolder.poll_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPollingClicked();
                }
            });

            caHolder.ask_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onAskClicked(eaSession.getAll().getSESSION_ID(),eaSession.getAll().getHEADING());
                }
            });

            //For Ratings
            caHolder.rel_submit_rating.setTag(ratings);
            caHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating,
                                            boolean fromUser) {
                    caHolder.rel_submit_rating.setTag(Math.round(rating));
                    caHolder.rel_submit_rating.setVisibility(View.VISIBLE);
                }
            });

            caHolder.rel_submit_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,String.valueOf(v.getTag()),Toast.LENGTH_SHORT).show();
                    listener.onSubmitRatingClicked(v,String.valueOf(eaSession.getAll().getSESSION_ID()),String.valueOf(v.getTag()));
                }
            });

            caHolder.rel_addtocalendar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(context,String.valueOf(v.getTag()),Toast.LENGTH_SHORT).show();
                    listener.onDateClicked(eaSession);
                }
            });

            if(eaSession.getSpeaker().size() == 0){
                caHolder.txtSpeakerTitle.setVisibility(View.GONE);
                caHolder.horizontalSV.setVisibility(View.GONE);
            } else {
                for (SpeakerModel single_speaker:
                        eaSession.getSpeaker()) {
                    View SpeakerView = LayoutInflater.from(context).inflate(R.layout.ca_single_speaker, null, false);
                    TextView txt_name = (TextView) SpeakerView.findViewById(R.id.speaker_name);
                    txt_name.setText(single_speaker.getSPEAKER_NAME());
                    ImageView imgSpeaker = (ImageView) SpeakerView.findViewById(R.id.speaker_imv);
                    LinearLayout lin_single_speaker = (LinearLayout)SpeakerView.findViewById(R.id.lin_single_speaker);
                    final String speaker_id = single_speaker.getSPEAKER_ID();
                    lin_single_speaker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onSpeakerClicked(speaker_id);
                        }
                    });
                    if(single_speaker.getIMAGE()!= null && single_speaker.getIMAGE()!= ""){
                        Picasso.with(context).load(single_speaker.getIMAGE()).placeholder(R.drawable.pro).resize(200,200).into(imgSpeaker);
                    }
                    caHolder.speakerList.addView(SpeakerView);
                }
            }

        }
        if (holder instanceof ConferenceAgendaAdapter.LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public long getItemId(int position) {
        return sessionsModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

/*    @Override
    public int getItemViewType(int position) {
        return position;
    }*/

    @Override
    public int getItemViewType(int position) {
        return sessionsModel.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return sessionsModel == null ? 0 : sessionsModel.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle,txtDate,txtSpeakerTitle,txtPara,rate_label;
        RelativeLayout relReadMore,rel_addtocalendar,rel_submit_rating;
        LinearLayout speakerList;
        HorizontalScrollView horizontalSV;
        Button poll_btn,ask_btn,submit_rating;
        RatingBar ratingBar;
        LinearLayout lin_date;
        //DocumentView txtPara;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txt_title);
            txtDate = (TextView) itemView.findViewById(R.id.txt_date);
            txtPara = (TextView) itemView.findViewById(R.id.txt_para);
            rate_label = (TextView)itemView.findViewById(R.id.rate_label);
            relReadMore = (RelativeLayout) itemView.findViewById(R.id.rel_readMore);
            speakerList = (LinearLayout) itemView.findViewById(R.id.speaker_list);
            txtSpeakerTitle = (TextView) itemView.findViewById(R.id.speaker_title);
            horizontalSV = (HorizontalScrollView) itemView.findViewById(R.id.horizontalScrollView);
            poll_btn = (Button)itemView.findViewById(R.id.poll_btn);
            ask_btn = (Button)itemView.findViewById(R.id.ask_btn);
            //submit_rating = (Button)itemView.findViewById(R.id.submit_rating);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            //lin_date = (LinearLayout)itemView.findViewById(R.id.lin_date);
            rel_addtocalendar = (RelativeLayout)itemView.findViewById(R.id.rel_addtocalendar);
            rel_submit_rating =(RelativeLayout)itemView.findViewById(R.id.rel_submit_rating);
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
        void onSpeakerClicked(String speakerID);
        void onReadMoreClicked(String confID);
        void onPollingClicked();
        void onAskClicked(String session_id,String session_name);
        void onSubmitRatingClicked(View v,String confID,String ratings);
        void onDateClicked(SessionsModel sessionsModel);
    }
}

