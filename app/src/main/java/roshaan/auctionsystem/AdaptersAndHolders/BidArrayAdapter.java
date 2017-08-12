package roshaan.auctionsystem.AdaptersAndHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;
import roshaan.auctionsystem.StructuresForDb.BidStructure;

/**
 * Created by Roshaann 2.7 gpa on 05/08/2017.
 */

public class BidArrayAdapter extends RecyclerView.Adapter<BidViewHolder> {


    ArrayList<BidStructure> allBid=new ArrayList<>();
    Context context;
    LayoutInflater lf;

    public BidArrayAdapter(Context context,ArrayList<BidStructure> allBid){

        this.context=context;
        this.allBid=allBid;

        lf=LayoutInflater.from(context);
    }

    @Override
    public BidViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView=lf.inflate(R.layout.single_bid_item,parent,false);
        BidViewHolder holder=new BidViewHolder(itemView,allBid,context);

        return holder;
    }

    @Override
    public void onBindViewHolder(BidViewHolder holder, int position) {

        //filling ui

        holder.userName.setText(allBid.get(position).getUserEmail());
        holder.bid.setText(allBid.get(position).getBid());


    }

    @Override
    public int getItemCount() {
        return allBid.size();
    }
}
