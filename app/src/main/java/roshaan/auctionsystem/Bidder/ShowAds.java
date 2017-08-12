package roshaan.auctionsystem.Bidder;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import roshaan.auctionsystem.AdaptersAndHolders.ArrayAdapter;
import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowAds extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference ref;
    ArrayAdapter adapter;
    ArrayList<AdStructure> allAds;
    String auctionType;
    static SimpleDateFormat sdf;
    Spinner category;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       //getting the type of auction either live or future
        auctionType=getArguments().getString(BiddersFeed.adAuctionTypeKey);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(auctionType+ " auction");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_ads, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        allAds = new ArrayList<>();
        recyclerView = (RecyclerView) getView().findViewById(R.id.showAdsRecycler);
        category=(Spinner) getView().findViewById(R.id.showAdCategory);

        //making adapter for spinner
        final android.widget.ArrayAdapter categoryAdapter = android.widget.ArrayAdapter.createFromResource(getActivity(),R.array.category1,android.R.layout.simple_spinner_item);
        category.setAdapter(categoryAdapter);

        adapter = new ArrayAdapter(getActivity(), allAds,auctionType);

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        System.out.println("HAn bhai");



        //setting on click listenere on spinner
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Query q;
                System.out.println("han bhai1");

                //means all is selected
                if(i==0) {
                    ref = FirebaseDatabase.getInstance().getReference().child("Ads");
                    System.out.println("han bhai3");
                    q=ref;
                }
                else {
                    ref = FirebaseDatabase.getInstance().getReference().child("Ads");
                    q=ref.orderByChild("category").equalTo(category.getSelectedItem().toString());
                }
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println("HAn bhai2");
                        allAds.clear();

                        Iterable<DataSnapshot> child = dataSnapshot.getChildren();

                        for (DataSnapshot ch : child) {

                            AdStructure struct = ch.getValue(AdStructure.class);

                            Date current=getCurrentDate();

                            //////// LIVe Auction is clicked
                            if(auctionType.equals("Live")){


                                //contains start and end date
                                Date startDate=null;
                                Date endDate=null;

                                try {
                                    startDate=sdf.parse(struct.getStartDate());
                                    endDate=sdf.parse(struct.getEndDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                //compare current date with start and end date
                                //if current date is >=start date and current date <=end date
                                if(current.compareTo(startDate)>=0&&
                                        current.compareTo(endDate)<=0
                                        ){

                                    allAds.add(struct);

                                }

                            }

                            else if(auctionType.equals("Future")){

                                //making start date
                                Date startDate=null;

                                try {
                                    startDate=sdf.parse(struct.getStartDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                /////if current date is small less thn start date

                                if(current.compareTo(startDate)<0){

                                    allAds.add(struct);
                                }

                            }


                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }




    public static AdsCommmunication communicator;

    public interface AdsCommmunication{
       public  void sendAdId(String id,String startDate,String endDate,String initialBid ,String description,String auctionType);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        communicator= (AdsCommmunication) context;
    }

   public static Date getCurrentDate(){

         sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
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
