package com.example.eauction.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eauction.Helpers.TelemetryHelper;
import com.example.eauction.Models.Car;
import com.example.eauction.Models.CarPlate;
import com.example.eauction.Models.General;
import com.example.eauction.Models.Landmark;
import com.example.eauction.Models.Service;
import com.example.eauction.Models.ServiceComment;
import com.example.eauction.Models.Telemetry;
import com.example.eauction.Models.VipPhoneNumber;
import com.example.eauction.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    ArrayList<ServiceComment> Comments;
    @Override
    public int getItemCount()
    {
        return Comments.size();
    }

    public CommentAdapter(ArrayList<ServiceComment> Comments)
    {
        this.Comments = Comments;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        CommentViewHolder commentViewHolder = (CommentViewHolder)holder;

        String cvNameLabel = "<b><u>" + "Name:" + "</u></b> ";
        String cvCommentLabel = "<b><u>" + "Comment:" + "</u></b> ";
        String cvCostLabel = "<b><u>" + "Cost:" + "</u></b> ";
        ServiceComment serviceComment = Comments.get(position);

        commentViewHolder.cvCommentName.setText(Html.fromHtml(cvNameLabel+serviceComment.getName()));
        commentViewHolder.cvComment.setText(Html.fromHtml(cvCommentLabel+serviceComment.getComment()));
        commentViewHolder.cvCost.setText(Html.fromHtml(cvCostLabel+serviceComment.getCost()));
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        View v;
        Context context = parent.getContext();
        v = LayoutInflater.from(context).inflate(R.layout.card_comment, parent, false);
        holder = new CommentViewHolder(v);
        return holder;
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.cvCommentName)
        public TextView cvCommentName;

        @BindView(R.id.cvComment)
        public TextView cvComment;

        @BindView(R.id.cvCost)
        public TextView cvCost;

        public CommentViewHolder(@NonNull View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
