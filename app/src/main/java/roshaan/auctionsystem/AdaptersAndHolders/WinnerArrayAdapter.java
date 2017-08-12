package roshaan.auctionsystem.AdaptersAndHolders;

import android.app.Activity;
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
import roshaan.auctionsystem.StructuresForDb.WinnerUserStructure;

/**
 * Created by Roshaann 2.7 gpa on 09/08/2017.
 */

public class WinnerArrayAdapter extends RecyclerView.Adapter<WinnerViewHolder> {


    ArrayList<WinnerUserStructure> wins = new ArrayList<>();

    String auctionType;
    LayoutInflater lf;
    Activity activity;

    public WinnerArrayAdapter(Activity activity, ArrayList<WinnerUserStructure> wins) {

        this.activity = activity;

        this.wins = wins;
        this.auctionType = auctionType;
        lf = LayoutInflater.from(activity);
    }

    @Override
    public WinnerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = lf.inflate(R.layout.single_winner_item, parent, false);
        WinnerViewHolder holder = new WinnerViewHolder(itemView, wins, activity, auctionType);

        return holder;
    }

    @Override
    public void onBindViewHolder(WinnerViewHolder holder, int position) {

        //filling ui

        holder.title.setText(wins.get(position).getTitle());
        holder.description.setText(wins.get(position).getDescription());
        holder.bid.setText(wins.get(position).getBidAmount());


        Log.d("tag", "onBindViewHolder: " + wins.get(position).toString());
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReferenceFromUrl(wins.get(position).getImage()))
                .error(R.drawable.big)
                .centerCrop()
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return wins.size();
    }
}

