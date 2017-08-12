package roshaan.auctionsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import roshaan.auctionsystem.Auctioneer.AuctioneerFeed;
import roshaan.auctionsystem.Bidder.BiddersFeed;

public class MainActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        progressDialog=new ProgressDialog(this);

        progressDialog.setMessage("Getting information please wait");
        progressDialog.show();

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            //no one is logged in

            progressDialog.dismiss();
            finish();
            startActivity(new Intent(this, Login.class));
        }

        else{

            DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Users");
                    ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("type")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String type=dataSnapshot.getValue(String.class);
                            if(type!=null&&type.equals("Bidder")){

                                progressDialog.dismiss();

                                //opening bidders feed
                                finish();
                                startActivity(new Intent(MainActivity.this,BiddersFeed.class ));
                            }
                            else if(type!=null){

                                progressDialog.dismiss();

                                //opening auctioneer feed
                                finish();

                                startActivity(new Intent(MainActivity.this,AuctioneerFeed.class));
                            }

                            else{

                                progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "No on is logged in", Toast.LENGTH_SHORT).show();


                                finish();

                                startActivity(new Intent(MainActivity.this,Login.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

        }
    }
}

