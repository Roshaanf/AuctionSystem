package roshaan.auctionsystem.Auctioneer;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import roshaan.auctionsystem.AdaptersAndHolders.ArrayAdapter;
import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAds extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayAdapter adapter;
    ArrayList<AdStructure> allAds;
    SimpleDateFormat sdf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_ads, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        allAds = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.myAdsRecycler);


        adapter = new ArrayAdapter(getActivity(), allAds,"null");

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference().child("Ads");
        ref.orderByChild("userKey").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                allAds.clear();

                Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                for (DataSnapshot ch : child) {

                    AdStructure struct = ch.getValue(AdStructure.class);

                    allAds.add(struct);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static AdsCommmunication communicator;

    public interface AdsCommmunication{
        public  void sendAdId(String id,String startDate,String endDate,String initialBid ,String description/*,String auctionType*/);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        communicator= (AdsCommmunication) context;
    }

}

