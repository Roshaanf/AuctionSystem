package roshaan.auctionsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import roshaan.auctionsystem.Auctioneer.AuctioneerFeed;
import roshaan.auctionsystem.Bidder.BiddersFeed;

public class Login extends AppCompatActivity {

    EditText email;
    EditText password;
    Button login;
    TextView signup;
    Toolbar toolBar;
    DatabaseReference ref;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=(EditText) findViewById(R.id.loginEmail);
        password=(EditText) findViewById(R.id.loginPassword);
        login=(Button) findViewById(R.id.loginButton);
        signup=(TextView) findViewById(R.id.loginSignup);
        progressDialog=new ProgressDialog(this);
        toolBar=(Toolbar) findViewById(R.id.toolBar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Login");

    }

    public void login(View v){



        if(!TextUtils.isEmpty(email.getText())&&
                !TextUtils.isEmpty(password.getText())){


            String emailString =String.valueOf(email.getText());
            final String passwordString =String.valueOf(password.getText());

            progressDialog.setMessage("Logging in! please wait");
            progressDialog.show();

            FirebaseAuth.getInstance().signInWithEmailAndPassword(emailString,passwordString ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        //now checking login account is of which type and will show corresponding feed accordingly

                        ref= FirebaseDatabase.getInstance().getReference().child("Users");

                        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("type")
                                .addValueEventListener(new ValueEventListener() {

                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        String type=dataSnapshot.getValue(String.class);

                                        if(type!=null&&type.equals("Bidder")){


                                            progressDialog.dismiss();

                                            //opening bidders feed
                                            finish();
                                            startActivity(new Intent(Login.this, BiddersFeed.class));
                                        }
                                        else if(type!=null){

                                            progressDialog.dismiss();

                                            //opening auctioneer activity
                                            finish();
                                            startActivity(new Intent(Login.this,AuctioneerFeed.class));
                                        }
                                        else{

                                            progressDialog.dismiss();

                                            Toast.makeText(Login.this, "Incorrect username of password", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                    }
                    //some error occure during logging in
                    else{

                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();

                    }

                }
            });


        }
        else{

            progressDialog.dismiss();
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }

    }


    public void signup(View v){

        finish();

        startActivity(new Intent(this,Signup.class));
    }
}
