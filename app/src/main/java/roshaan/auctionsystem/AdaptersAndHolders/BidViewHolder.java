package roshaan.auctionsystem.AdaptersAndHolders;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import roshaan.auctionsystem.Bidder.ShowAds;
import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;
import roshaan.auctionsystem.StructuresForDb.BidStructure;

/**
 * Created by Roshaann 2.7 gpa on 05/08/2017.
 */

public class BidViewHolder extends RecyclerView.ViewHolder{

    ArrayList<BidStructure> allBids;
    TextView userName;
    TextView bid;
    Context c;

    public BidViewHolder(View itemView, final ArrayList<BidStructure> allBids, final Context c) {
        super(itemView);

        userName=(TextView) itemView.findViewById(R.id.bidUserName);
        bid=(TextView) itemView.findViewById(R.id.bidBid);
        this.c=c;

    }
}
