package roshaan.auctionsystem.Auctioneer;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import roshaan.auctionsystem.Bidder.BiddersFeed;
import roshaan.auctionsystem.Bidder.ShowSelectedAd;
import roshaan.auctionsystem.Login;
import roshaan.auctionsystem.R;

public class AuctioneerFeed extends AppCompatActivity implements AdapterView.OnItemClickListener , MyAds.AdsCommmunication{

    Toolbar toolBar;
    ListView lv;
    DrawerLayout dl;
    TextView email;
    ArrayList<String> data;
    ActionBarDrawerToggle actionBarDrawableToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auctioneer_feed);


        toolBar=(Toolbar) findViewById(R.id.toolBar);
        email=(TextView) findViewById(R.id.auctioneerFeedEmail);
        lv=(ListView) findViewById(R.id.listViewAuctioneer);
        dl=(DrawerLayout) findViewById(R.id.drawerLayout);
        setSupportActionBar(toolBar);

        //setting data source

        data=new ArrayList<>();
       data.add("Post Ad"); data.add("My ads");data.add("Logout");


        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());



        ArrayAdapter<String> adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,data);
        lv.setOnItemClickListener(this);
        lv.setAdapter(adapter);


        //for close and open events of drawer
        //also to show three lines of menu to indicate drawer
        actionBarDrawableToggle=new ActionBarDrawerToggle(this,dl,toolBar,R.string.open_drawable,
                                                      R.string.close_drawable){


            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //to give a slide animation look when drawer opens it will reduce the alpha value of appbar
                //it will onyl lower the alpha until offset is less thn 0.6 otherwise it will make whole backgoround dark

                if(slideOffset<0.6){

                    //alpha value increases as drawer opens and decreases as it close max value is 1
                    toolBar.setAlpha(1-slideOffset);

                }

            }


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

              //  Toast.makeText(AuctioneerFeed.this, "Drawer opened", Toast.LENGTH_SHORT).show();

                //this will redraw appbar
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
             //   Toast.makeText(AuctioneerFeed.this, "Drawer closed", Toast.LENGTH_SHORT).show();

                //this will redraw appbar
                invalidateOptionsMenu();
            }


            @Override
            public void onDrawerStateChanged(int newState) {


            }

    };

 //on default myads will be shown
        FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
        ftr.replace(R.id.forAuctioneersFragment, new MyAds(), "A");
        ftr.commit();


        //for close and opening state of drawer
        dl.addDrawerListener(actionBarDrawableToggle);


        //setting home button enabled
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

}

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


        //selectItem(i);
        //will set the action bar title
        getSupportActionBar().setTitle(data.get(i));

        //means post new ad
        if(i==0) {
            dl.closeDrawer(Gravity.LEFT);

            FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
            ftr.replace(R.id.forAuctioneersFragment, new PostAd(), "B");
            ftr.addToBackStack("B");
            ftr.commit();


        }

        if(i==1) {
            dl.closeDrawer(Gravity.LEFT);

            FragmentTransaction ftr = getSupportFragmentManager().beginTransaction();
            ftr.replace(R.id.forAuctioneersFragment, new MyAds(), "C");
            ftr.addToBackStack("C");
            ftr.commit();


        }

        if(i==2){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(this,Login.class));
        }

    }

    @Override
    public void onBackPressed() {

        if(dl.isDrawerVisible(Gravity.LEFT)){
            dl.closeDrawer(Gravity.LEFT);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //this is compulsory to show the hamburger icon for drawer
        actionBarDrawableToggle.syncState();

    }

    @Override
    public void sendAdId(String id, String startDate, String endDate, String initialBid, String description) {

        Bundle b=new Bundle();
        b.putString(BiddersFeed.adIdKey,id);
        b.putString(BiddersFeed.adStartDateKey,startDate);
        b.putString(BiddersFeed.adEndDateKey,endDate);
        b.putString(BiddersFeed.adInitialBidey,initialBid);
        b.putString(BiddersFeed.adDescriptionKey,description);
        ShowSelectedAd ssa=new ShowSelectedAd();
        ssa.setArguments(b);
        FragmentTransaction ftr=getSupportFragmentManager().beginTransaction();
        ftr.replace(R.id.forAuctioneersFragment,ssa,"D");
        ftr.addToBackStack("D");
        ftr.commit();
    }
}
