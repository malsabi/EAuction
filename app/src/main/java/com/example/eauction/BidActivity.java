package com.example.eauction;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.eauction.Helpers.TelemetryHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BidActivity extends AppCompatActivity {


   @BindView(R.id.TableContent)
   TableLayout TableContent;

    @BindView(R.id.TelIV)
    ImageView TelIV;

    @BindView(R.id.EtCurrentBid)
    EditText EtCurrentBid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        for (String key : extras.keySet()) {
            if (key.equals("ID") || key.equals("Picture") || key.equals("Details"))
                continue;
            AddTableRow(key,extras.get(key).toString(),TableContent);
        }
        AddTableRow("Details",extras.get("Details").toString(),TableContent);
        if(!extras.get("Picture").equals("")){

            Bitmap img = TelemetryHelper.Base64ToImage(extras.get("Picture").toString());
            TelIV.setImageBitmap(img);
        }
        EtCurrentBid.setText(extras.get("Current Bid").toString());
    }

    private void AddTableRow(String label, String content, TableLayout TableContent){
        String Label = "<b><u>" + label+":" + "</u></b> ";
        TextView labelTV = new TextView(TableContent.getContext());
        labelTV.setText(Html.fromHtml(Label));
        labelTV.setTextSize(20);
        labelTV.setPadding(8,0,0,8);
        labelTV.setTextColor(Color.GRAY);

        TextView ContentTV = new TextView(TableContent.getContext());
        ContentTV.setTextSize(20);
        ContentTV.setPadding(8,0,0,8);
        ContentTV.setTextColor(Color.BLACK);
        ContentTV.setText(content);

        TableRow tableRow = new TableRow(TableContent.getContext());
        tableRow.addView(labelTV);
        tableRow.addView(ContentTV);
        tableRow.setPadding(0,0,0,32);

        TableContent.addView(tableRow);
    }
}