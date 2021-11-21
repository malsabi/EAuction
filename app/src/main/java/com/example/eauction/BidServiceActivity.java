package com.example.eauction;

import static com.example.eauction.BidActivity.BID_LABEL_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eauction.Adapters.CommentAdapter;
import com.example.eauction.Adapters.TelemetryMyPropertiesAdapter;
import com.example.eauction.DummyData.DummyData;
import com.example.eauction.Helpers.TelemetryHelper;
import com.royrodriguez.transitionbutton.TransitionButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BidServiceActivity extends AppCompatActivity {

    @BindView(R.id.TableServiceContent)
    public TableLayout TableServiceContent;

    @BindView(R.id.TelServiceIV)
    public ImageView TelServiceIV;

//    @BindView(R.id.EtServiceCurrentBid)
//    public EditText EtServiceCurrentBid;

    @BindView(R.id.MakeBidServiceBtn)
    public TransitionButton MakeBidServiceBtn;

    @BindView(R.id.ServiceComments)
    public TextView ServiceComments;

    private CommentAdapter CommentsAdapter;
    private RecyclerView.LayoutManager LayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_service);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        for (String key : extras.keySet())
        {
            if (key.equals("ID") || key.equals("Picture") || key.equals("Details") || key.equals("AuctionOwnerUserId") || key.equals("UserId"))
                continue;
            AddTableRow(key, extras.get(key).toString(), TableServiceContent);
        }
        AddTableRow("Details", extras.get("Details").toString(), TableServiceContent);

        if(!extras.get("Picture").equals(""))
        {
            Bitmap img = TelemetryHelper.Base64ToImage(extras.get("Picture").toString());
            TelServiceIV.setImageBitmap(img);
        }

        ServiceComments.setOnClickListener((v) -> {
            //region Dialog Settings
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_comments);
            RecyclerView RvComments = dialog.findViewById(R.id.RvComments);
            LayoutManager = new LinearLayoutManager(this);
            CommentsAdapter = new CommentAdapter(DummyData.GetDummyComments()); //TODO replace DummyData.GetDummyComments() with actual comments from DB
            RvComments.setLayoutManager(LayoutManager);
            RvComments.setAdapter(CommentsAdapter);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);
            //endregion
        });

        MakeBidServiceBtn.setOnClickListener((view -> {
            //region Dialog Settings
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.dialog_makebid);
            //region MakeEvent
            EditText EtServiceBidDialog = dialog.findViewById(R.id.EtServiceBidDialog);
            EditText EtServiceCommentDialog = dialog.findViewById(R.id.EtServiceCommentDialog);
            TransitionButton BtnSubmitServiceBid = dialog.findViewById(R.id.BtnSubmitServiceBid);
            //endregion
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);
            //endregion
        }));
    }

    private void AddTableRow(String label, String content, TableLayout TableContent){
        String Label = "<b><u>" + label  + ":" + "</u></b> ";
        TextView labelTV = new TextView(TableContent.getContext());
        labelTV.setText(Html.fromHtml(Label));
        labelTV.setTextSize(20);
        labelTV.setPadding(8,0,0,8);
        labelTV.setTextColor(Color.GRAY);

        TextView ContentTV = new TextView(TableContent.getContext());
        ContentTV.setTextSize(20);
        ContentTV.setPadding(8,0,0,8);
        ContentTV.setTextColor(Color.BLACK);
        if(label.equals("Current Bid"))
        {
            ContentTV.setText(content + " AED");
            ContentTV.setId(BID_LABEL_ID);
        }else{
            ContentTV.setText(content);
        }
        TableRow tableRow = new TableRow(TableContent.getContext());
        tableRow.addView(labelTV);
        tableRow.addView(ContentTV);
        tableRow.setPadding(0,0,0,32);
        TableContent.addView(tableRow);
    }
}