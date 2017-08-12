package roshaan.auctionsystem.AdaptersAndHolders;

import android.app.Activity;
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
import roshaan.auctionsystem.StructuresForDb.WinnerUserStructure;

/**
 * Created by Roshaann 2.7 gpa on 09/08/2017.
 */

public class WinnerViewHolder extends RecyclerView.ViewHolder {


    ArrayList<WinnerUserStructure> allAds;
    ImageView img;
    TextView title;
    TextView bid;
    TextView description;
    Activity activity;

    public WinnerViewHolder(View itemView, final ArrayList<WinnerUserStructure> allAds, final Activity activity, final String auctionType) {
        super(itemView);

        img= (ImageView) itemView.findViewById(R.id.winnerAdImage);
        title=(TextView) itemView.findViewById(R.id.winnerAdTitle);
        bid=(TextView) itemView.findViewById(R.id.winnerAdBid);
        description=(TextView) itemView.findViewById(R.id.winnerAdDescription);

        this.activity=activity;

    }
}
