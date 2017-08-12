package roshaan.auctionsystem.Bidder;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import roshaan.auctionsystem.AdaptersAndHolders.BidArrayAdapter;
import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.BidStructure;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSelectedAd extends Fragment {

    String adID;
    String adStartDate;
    String adEndDate;
    String adInitialBid;
    String adDesciption;
    String auctionType;

    TextView description;
    EditText bidText;
    Boolean flag=true;
    TextView initialBid;
    TextView startDate;
    TextView endDate;
    Button postBid;
    LinearLayout ll;
    DatabaseReference ref;
    RecyclerView recyclerView;
    ArrayList<BidStructure> allBids;
    BidArrayAdapter adapter;
    public long difference;
    Runnable runnable;
     Handler handler;
    TextView timer;
    LinearLayout timerLayout;

    public ShowSelectedAd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            adID=getArguments().getString(BiddersFeed.adIdKey);
        adStartDate=getArguments().getString(BiddersFeed.adStartDateKey);
        adEndDate=getArguments().getString(BiddersFeed.adEndDateKey);
        adInitialBid=getArguments().getString(BiddersFeed.adInitialBidey);
        adDesciption=getArguments().getString(BiddersFeed.adDescriptionKey);


        if(getActivity().getClass().getName().equals("roshaan.auctionsystem.Bidder.BiddersFeed")) {
            auctionType = getArguments().getString(BiddersFeed.adAuctionTypeKey);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(auctionType+ " auction");
        }
        else{
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My ads");
        }

        //using single java class to inflate two  layouts according to the auction type
        //Auction type is comming from BiddersFeed
        // Inflate the layout for this fragment
        if(getActivity().getClass().getName().equals("roshaan.auctionsystem.Auctioneer.AuctioneerFeed")||auctionType.equals("Live"))
            return inflater.inflate(R.layout.fragment_show_selected_live_ad, container, false);

        else {
        return inflater.inflate(R.layout.fragment_show_selected_future_ad, container, false);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        description=(TextView) getView().findViewById(R.id.showSelectedAdDescription);
        initialBid=(TextView) getView().findViewById(R.id.showSelectedAdInitialBid);
        startDate=(TextView) getView().findViewById(R.id.showSelectedAdStartTime);
        endDate=(TextView) getView().findViewById(R.id.showSelectedAdEndTime);
        bidText=(EditText) getView().findViewById(R.id.showSelectedAdBidText);
        postBid=(Button) getView().findViewById(R.id.showSelectedAdBid);

        ll=(LinearLayout) getView().findViewById(R.id.showSelectedAdLastLayout);
        allBids=new ArrayList<>();
        adapter=new BidArrayAdapter(getActivity(),allBids);


        //timer will only showed if the fragment is live auction
        if(getActivity().getClass().getName().equals("roshaan.auctionsystem.Bidder.BiddersFeed")&&
                auctionType.equals("Live")) {

            timerLayout=(LinearLayout) getView().findViewById(R.id.timerLinear);
            timerLayout.setVisibility(View.VISIBLE);
            timer = (TextView) getView().findViewById(R.id.timer);
        }

        if(getActivity().getClass().getName().equals("roshaan.auctionsystem.Auctioneer.AuctioneerFeed"))
            ll.setVisibility(View.GONE);

        //setting texts
        description.setText(adDesciption);
        startDate.setText(adStartDate);
        endDate.setText(adEndDate);
        initialBid.setText(adInitialBid);

        //setting on click listener on bid
        postBid();
        //checking if ad exists
        existanceCheck();

        //if auction type is future thn no recycler view will be initialized
        // function will be returned here
        //checking null also bcx auctonType will be null when comming from auctioneer feed
        if(auctionType!=null&&auctionType.equals("Future"))
            return;

        recyclerView=(RecyclerView) getView().findViewById(R.id.showSelectedAdRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);



        //filling recycler view by bids

        DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Bids");

        ref2.orderByChild("adId").equalTo(adID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                allBids.clear();

                Iterable<DataSnapshot> child=dataSnapshot.getChildren();

                for(DataSnapshot ch:child){

                    BidStructure struct=ch.getValue(BidStructure.class);

                    allBids.add(struct);

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





        if(getActivity().getClass().getName().equals("roshaan.auctionsystem.Bidder.BiddersFeed")&&
                auctionType.equals("Live")) {
            /////setting timer
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date endDate = null;
            try {
                endDate = sdf.parse(this.adEndDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            final Date current = getCurrentDate();

            //also subtracting 5 hours offset
            difference = endDate.getTime() - current.getTime() - (1000 * 60 * 60 * 5);


            final SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");


            handler = new Handler();

            final Date finalEndDate = endDate;

            runnable = new Runnable() {
                @Override
                public void run() {

                    difference = difference - 1000;
                    if (!sdf1.format(difference).equals("00:00:00")
                            && current.compareTo(finalEndDate) < 0) {

                        timer.setText(sdf1.format(difference));
                        //this will make iterations till the time timer is 0
                        handler.postDelayed(this, 1000);

                    } else{

                        timer.setText(sdf1.format(difference));
                        postBid.setEnabled(false);

                        try {

                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        Toast.makeText(getActivity(), "Auction closed", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                }
            };

            /////// this will let the runnable exexute for the first time
            handler.postDelayed(runnable, 1000);
        }
    }




    @Override
    public void onStop() {
        super.onStop();

        if(getActivity().getClass().getName().equals("roshaan.auctionsystem.Bidder.BiddersFeed")&&
                auctionType.equals("Live"))
        handler.removeCallbacks(runnable);
    }

    void postBid(){

        //adding bid

        postBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!TextUtils.isEmpty(bidText.getText())){

                    final DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Bids");
                    final String key=ref.push().getKey();
                    //making bid object
                    final BidStructure struct=new BidStructure(FirebaseAuth.getInstance().getCurrentUser().getUid()
                            ,adID,bidText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),adStartDate
                            ,adEndDate);

                    //check if input bid is greater thn initial bid
                    if(Long.parseLong(bidText.getText().toString())>Long.parseLong(adInitialBid)) {

                        String bid=bidText.getText().toString();
                        //checking if fragment is live auction only thn check also for remaining bids

                        if(auctionType.equals("Live")){

                            String bid1=bidText.toString();
                            DatabaseReference ref2=FirebaseDatabase.getInstance().getReference().child("Bids");
                            Query q1=ref2.orderByChild("adId").equalTo(adID);

                              flag=true;
                            final long bidMax=Long.parseLong(bidText.getText().toString());

                            q1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {



                                    if(dataSnapshot.exists()){

                                    Iterable<DataSnapshot> child=dataSnapshot.getChildren();
                                        for(DataSnapshot ch:child) {

                                            BidStructure struct = ch.getValue(BidStructure.class);

                                            if (Long.parseLong(struct.getBid()) >= bidMax) {

                                                flag = false;
                                                break;
                                            }
                                        }

                                        if(flag){

                                            ref.child(key).setValue(struct);
                                            Toast.makeText(getContext(), "Bid successfully posted", Toast.LENGTH_SHORT).show();
                                            bidText.setText("");
                                        }

                                    }
                                    else{

                                    //directly bid

                                    ref.child(key).setValue(struct);
                                    Toast.makeText(getContext(), "Bid successfully posted", Toast.LENGTH_SHORT).show();
                                    bidText.setText("");

                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                        else{

                            ref.child(key).setValue(struct);
                            Toast.makeText(getContext(), "Bid successfully posted", Toast.LENGTH_SHORT).show();
                            bidText.setText("");

                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "Bid should be greater thn initial bid", Toast.LENGTH_SHORT).show();
                    }

                }
                else

                    Toast.makeText(getContext(), "Fill bid field", Toast.LENGTH_SHORT).show();

            }
        });
    }

    void existanceCheck(){

        //if current add does not exist thn reset the text to null
        ref=FirebaseDatabase.getInstance().getReference().child("Ads").child(adID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){

                    description.setText("");
                    startDate.setText("");
                    endDate.setText("");
                    initialBid.setText("");

                    Toast.makeText(getContext(), "Selected post has been deleted", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }}


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public  Date getCurrentDate(){

        SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Calendar c=Calendar.getInstance();

        Date d=null;
        try {
            d=sdf.parse(sdf.format(c.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
