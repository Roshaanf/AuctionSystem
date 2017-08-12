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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import roshaan.auctionsystem.Auctioneer.AuctioneerFeed;
import roshaan.auctionsystem.Bidder.BiddersFeed;

public class Signup extends AppCompatActivity {

    EditText email;
    EditText password;
    Button signup;
    EditText fullName;
    TextView login;
    EditText mobileNo;
    ProgressDialog progressDialog;
    RadioGroup rGroup;
    Toolbar toolBar;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email=(EditText) findViewById(R.id.signupEmail);
        password=(EditText) findViewById(R.id.signupPassword);
        signup=(Button) findViewById(R.id.signup);
        login=(TextView) findViewById(R.id.signupLogin);
        fullName=(EditText) findViewById(R.id.signupFullName);
        rGroup=(RadioGroup) findViewById(R.id.rGroup);
        mobileNo=(EditText) findViewById(R.id.signupCellNo);
        progressDialog=new ProgressDialog(this);
        toolBar=(Toolbar) findViewById(R.id.toolBar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("Signup");

    }

    public void singup(View v){

        progressDialog.setMessage("Signing up! please wait");
        progressDialog.show();



        if(!TextUtils.isEmpty(email.getText())&&
                !TextUtils.isEmpty(password.getText())&&
                !TextUtils.isEmpty(mobileNo.getText())&&
                        !TextUtils.isEmpty(fullName.getText())){


            String emailString=String.valueOf(email.getText());
            String passwordString=String.valueOf(password.getText());
            final String mobileNoString=String.valueOf(mobileNo.getText());
            final String fullNameString=String.valueOf(fullName.getText());


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString,passwordString).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if(task.isSuccessful()){

                        int id=rGroup.getCheckedRadioButtonId();
                        RadioButton radio=(RadioButton) findViewById(id);

                        ref= FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        ref.child("fullName").setValue(fullNameString);
                        ref.child("type").setValue(radio.getText().toString());
                        ref.child("uId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        ref.child("mobileNo").setValue(mobileNoString);

                        //if user is bidder thn open bidder's feed
                        if(radio.getText().toString().equals("Bidder")) {

                            progressDialog.dismiss();

                            finish();
                            startActivity(new Intent(Signup.this, BiddersFeed.class));

                        }
                        //else open auctioneer's feed
                        else{

                            progressDialog.dismiss();

                            finish();
                            startActivity(new Intent(Signup.this, AuctioneerFeed.class));

                        }

                    }
                    else{
                        //error occured while signing up

                        try {
                            throw task.getException();
                        } catch(FirebaseAuthWeakPasswordException e) {

                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Signup failed password must be 6 characters long",Toast.LENGTH_LONG).show();

                        } catch(FirebaseAuthInvalidCredentialsException e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Signup failed! Bad structure of email",Toast.LENGTH_LONG).show();
                        } catch(FirebaseAuthUserCollisionException e) {

                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Signup failed id already in use choose another one",Toast.LENGTH_LONG).show();
                        } catch(Exception e) {

                        }
                    }

                }

            });

        }

        else{
            progressDialog.dismiss();
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
        }

    }

    public void login(View v){

        finish();

        startActivity(new Intent(this,Login.class));
    }




}
