package roshaan.auctionsystem.AdaptersAndHolders;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import roshaan.auctionsystem.Auctioneer.MyAds;
import roshaan.auctionsystem.Bidder.ShowAds;
import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;

/**
 * Created by Roshaann 2.7 gpa on 03/08/2017.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    CardView car;
    ArrayList<AdStructure> allAds;
    ImageView img;
    TextView title;
    String auctionType;
    Activity activity;

    public ViewHolder(View itemView, final ArrayList<AdStructure> allAds, final Activity activity, final String auctionType) {
        super(itemView);

        img = (ImageView) itemView.findViewById(R.id.singleAdImage);
        title = (TextView) itemView.findViewById(R.id.singleAdTitle);
        car = (CardView) itemView.findViewById(R.id.cardView);
        this.activity = activity;
        this.auctionType = auctionType;

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activity.getClass().getName().equals("roshaan.auctionsystem.Bidder.BiddersFeed") && auctionType != null) {
                    ShowAds.communicator.sendAdId(allAds.get(getAdapterPosition()).getPushId()
                            , allAds.get(getAdapterPosition()).getStartDate()
                            , allAds.get(getAdapterPosition()).getEndDate()
                            , allAds.get(getAdapterPosition()).getInitialBid()
                            , allAds.get(getAdapterPosition()).getDescription()
                            , auctionType);
                } else if (activity.getClass().getName().equals("roshaan.auctionsystem.Auctioneer.AuctioneerFeed")) {

                    MyAds.communicator.sendAdId(allAds.get(getAdapterPosition()).getPushId()
                            , allAds.get(getAdapterPosition()).getStartDate()
                            , allAds.get(getAdapterPosition()).getEndDate()
                            , allAds.get(getAdapterPosition()).getInitialBid()
                            , allAds.get(getAdapterPosition()).getDescription()
               /* ,auctionType*/);
                } else {

                    Toast.makeText(activity, "Hey ITs me", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

