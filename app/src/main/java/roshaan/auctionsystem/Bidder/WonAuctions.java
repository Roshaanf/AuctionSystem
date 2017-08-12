package roshaan.auctionsystem.Bidder;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import roshaan.auctionsystem.AdaptersAndHolders.ArrayAdapter;
import roshaan.auctionsystem.AdaptersAndHolders.BidArrayAdapter;
import roshaan.auctionsystem.AdaptersAndHolders.WinnerArrayAdapter;
import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;
import roshaan.auctionsystem.StructuresForDb.BidStructure;
import roshaan.auctionsystem.StructuresForDb.WinnerUserStructure;

/**
 * A simple {@link Fragment} subclass.
 */
public class WonAuctions extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayList<BidStructure> allAds;
    ArrayList<AdStructure> winnerAd;
    ArrayList<WinnerUserStructure> winnerAllInfo;
  SimpleDateFormat sdf;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Won auctions");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_won_auctions, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

        recyclerView=(RecyclerView) getView().findViewById(R.id.wonAuctionsRecycler);

        sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        allAds=new ArrayList<>();
        winnerAd=new ArrayList<>();
        winnerAllInfo=new ArrayList<>();
        final WinnerArrayAdapter adapter=new WinnerArrayAdapter(getActivity(),winnerAllInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        //Fillin gUI
        //1) Retrieving all bids that contains current user id
        //2)Check if time of ad is of past (if yes go furthr)

        ref= FirebaseDatabase.getInstance().getReference().child("Bids");
        Query q=ref.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                allAds.clear();


                Boolean flag=false;
                if(dataSnapshot.exists()) {

                    //all bids here are done by the currennt user

                    Iterable<DataSnapshot> child=dataSnapshot.getChildren();

                    for(DataSnapshot ch:child) {
                        final BidStructure struct =ch.getValue(BidStructure.class);



                        //now checking if the ads auction time is completed or not

                        Date end=null;
                        try {
                            end=sdf.parse(struct.getEndDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Date current=ShowAds.getCurrentDate();


                        //comparing dates

                        if(current.compareTo(end)>0){

                            //here only those bids(that user have done) will come whose add auction time has been expired

                            //now getting allbids of the current ad to compare for winner

                            DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("Bids");
                            Query q1=ref1.orderByChild("adId").equalTo(struct.getAdId());

                            q1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    //yhn srf ek particular ad ki sari bids aengy ab unhy compare krky winner nikal lo

                                    Iterable<DataSnapshot> child1=dataSnapshot.getChildren();

                                   BidStructure  winner=new BidStructure();
                                    winner.setBid("0");
                                    for(DataSnapshot ch:child1){

                                        BidStructure struct1=ch.getValue(BidStructure.class);


                                        if(Math.max(Long.parseLong(winner.getBid()) ,Long.parseLong ( struct1.getBid()))==Long.parseLong(struct1.getBid())){

                                            winner=struct1;

                                        }


                                    }

                                    Boolean flag1=true;
                                    //checking if current ad winner has already been decided thn dont apply the process again
                                    for(int i=0;i<allAds.size();i++)
                                    {
                                        if(allAds.get(i).getAdId().equals(winner.getAdId())){
                                            flag1=false;
                                        }
                                    }


                                //now checking if a user who has won is our current user ornot
                                    if(winner.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            &&flag1){

                                        allAds.add(winner);


                                        //now getting the infor of the ad

                                        DatabaseReference ref6=FirebaseDatabase.getInstance().getReference().child("Ads")
                                                .child(winner.getAdId());

                                        final BidStructure finalWinner = winner;
                                        ref6.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                AdStructure structt=dataSnapshot.getValue(AdStructure.class);

                                                winnerAllInfo.add(new WinnerUserStructure(finalWinner.getBid()
                                                ,structt.getTitle(),structt.getImageUri(),structt.getDescription()));

                                                adapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }

                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

}
}
