package roshaan.auctionsystem.AdaptersAndHolders;

import android.app.Activity;
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

/**
 * Created by Roshaann 2.7 gpa on 03/08/2017.
 */

public class ArrayAdapter extends RecyclerView.Adapter<ViewHolder> {


    ArrayList<AdStructure> allAds = new ArrayList<>();

    String auctionType;
    LayoutInflater lf;
    Activity activity;

    public ArrayAdapter(Activity activity, ArrayList<AdStructure> allAds, String auctionType) {

        this.activity = activity;

        this.allAds = allAds;
        this.auctionType = auctionType;
        lf = LayoutInflater.from(activity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = lf.inflate(R.layout.single_ad_item, parent, false);
        ViewHolder holder = new ViewHolder(itemView, allAds, activity, auctionType);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //filling ui

        holder.title.setText(allAds.get(position).getTitle());


        Log.d("tag", "onBindViewHolder: " + allAds.get(position).toString());
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(FirebaseStorage.getInstance().getReferenceFromUrl(allAds.get(position).getImageUri()))
                .error(R.drawable.big)
                .centerCrop()
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return allAds.size();
    }
}
