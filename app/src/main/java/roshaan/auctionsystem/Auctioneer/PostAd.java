package roshaan.auctionsystem.Auctioneer;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.microedition.khronos.opengles.GL;

import roshaan.auctionsystem.R;
import roshaan.auctionsystem.StructuresForDb.AdStructure;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostAd extends Fragment {

    Spinner day;
    Spinner month;
    Spinner year;
    Spinner startingHour;
    Spinner startingMinute;
    Spinner noOfHours;
    Spinner category;

    EditText title;
    EditText initialBid;
    EditText description;
    Button postAd;
    Button btn;
    ImageView img;
    Uri uriFinal=null;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post_ad, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        title=(EditText) getView().findViewById(R.id.adTitle);
        description=(EditText) getView().findViewById(R.id.adDescription);
        postAd=(Button) getView().findViewById(R.id.adPost);
        btn=(Button) getView().findViewById(R.id.btn);
        initialBid=(EditText)getView().findViewById(R.id.adInitialBid);
        img=(ImageView) getView().findViewById(R.id.image);
        progressDialog=new ProgressDialog(getContext());


        //getting references for date
        day=(Spinner) getView().findViewById(R.id.adDateDay);
        month=(Spinner) getView().findViewById(R.id.adDateMonth);
        year=(Spinner) getView().findViewById(R.id.adDayYear);

        //getting references for start time and no of hours
        startingHour=(Spinner) getView().findViewById(R.id.adStartingTimeHours);
        startingMinute=(Spinner) getView().findViewById(R.id.adStartingTimeMinutes);
        noOfHours=(Spinner) getView().findViewById(R.id.noOfHours);
        category=(Spinner) getView().findViewById(R.id.adCategory);

        //setting adapter for date
        ArrayAdapter adapterDays = ArrayAdapter.createFromResource(getActivity(),R.array.Days,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterMonths = ArrayAdapter.createFromResource(getActivity(),R.array.Months,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterYears = ArrayAdapter.createFromResource(getActivity(),R.array.year,android.R.layout.simple_spinner_item);

        //setting adapter for  starting time and hours
        ArrayAdapter adapterStartingHour = ArrayAdapter.createFromResource(getActivity(),R.array.startingHours,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterStartingMinute = ArrayAdapter.createFromResource(getActivity(),R.array.startingMinutes,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterNoOfHours = ArrayAdapter.createFromResource(getActivity(),R.array.hours,android.R.layout.simple_spinner_item);
        ArrayAdapter adapterCategory=ArrayAdapter.createFromResource(getActivity(),R.array.category,android.R.layout.simple_spinner_item);


        //setting adapter for date
        day.setAdapter(adapterDays); month.setAdapter(adapterMonths); year.setAdapter(adapterYears);

        //setting adapter for start time
        startingHour.setAdapter(adapterStartingHour); startingMinute.setAdapter(adapterStartingMinute); noOfHours.setAdapter(adapterNoOfHours);
        category.setAdapter(adapterCategory);


        // posting add
        postAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(day.getSelectedItemId()!=0&&
                        month.getSelectedItemId()!=0&&
                        year.getSelectedItemId()!=0&&
                        noOfHours.getSelectedItemId()!=0&&
                        startingHour.getSelectedItemId()!=0&&
                        startingMinute.getSelectedItemId()!=0&&
                        category.getSelectedItemId()!=0&&
                        !TextUtils.isEmpty(title.getText())&&
                        !TextUtils.isEmpty(description.getText())&&
                        !TextUtils.isEmpty(initialBid.getText())&&
                        uriFinal!=null){


                    String startDateString =day.getSelectedItem()+"/"+month.getSelectedItemId()+"/"+year.getSelectedItem()+
                            " "+startingHour.getSelectedItem()+":"+startingMinute.getSelectedItem()+":00";

                    SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    //1) checking if date is valid

                    sdf.setLenient(false);

                    Date startDate;
                    try {
                         startDate=sdf.parse(startDateString);
                    } catch (ParseException e) {

                        Toast.makeText(getActivity(), "Invalid date", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    }

                    //setting it to default value true
                    sdf.setLenient(true);

                    // 2) getting current date
                    Date currentDate=null;

                    try {
                        currentDate=sdf.parse(sdf.format(Calendar.getInstance().getTime()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    // 3) checking date should not be less thn current date
                    if(currentDate.compareTo(startDate)<=0){

                       // Toast.makeText(getActivity(), "Everything is correct", Toast.LENGTH_SHORT).show();

                        // 4) Now making end date

                        Calendar c=Calendar.getInstance();
                        c.setTime(startDate);
                        //adding no of hours
                        c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY)+Integer.parseInt(String.valueOf(noOfHours.getSelectedItem())));

                        String endDateString=sdf.format(c.getTime());




                        //setting refernce
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                                .child("Ads");
                        String key=ref.push().getKey();


                        //making object
                        AdStructure struct=new AdStructure(startDateString,endDateString,String.valueOf(uriFinal),category.getSelectedItem().toString(),
                                title.getText().toString(),description.getText().toString(),initialBid.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getUid(),key);


                        ref.child(key).setValue(struct);

                        Toast.makeText(getActivity(), "Ad posted successfully", Toast.LENGTH_SHORT).show();
                        //setting it to null so that only right image will be added when two or more ads are posted one after one
                        uriFinal=null;


                        //resetting
                        day.setSelection(0);month.setSelection(0);year.setSelection(0);startingHour.setSelection(0);
                        startingMinute.setSelection(0); noOfHours.setSelection(0); category.setSelection(0);
                        title.setText(""); description.setText("");


                    }else{

                        Toast.makeText(getActivity(), "Start date from past is not allowed", Toast.LENGTH_SHORT).show();
                        return;
                    }



                }

                else{
                    Toast.makeText(getActivity(), "Fill all fields and select all dropdowns", Toast.LENGTH_SHORT).show();
                }

            }
        });




        //for setting image
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openImageGallery();

            }
        });
    }

    public void openImageGallery(){

        //invoke the image gallery for an implicit intent
        Intent imageIntent=new Intent(Intent.ACTION_PICK);

        //where do we find to look data
        File pictureDirectory= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath=pictureDirectory.getPath();

        //finally get a URI representation
        Uri data=Uri.parse(pictureDirectoryPath);

        //set data and type , get images of all tyoe
        imageIntent.setDataAndType(data,"image/*");

        //we will invoke this activity and get something from it , like gallery invoke hojaegi or phr jo select krengy wo result
        startActivityForResult(imageIntent,20);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       if(resultCode==RESULT_OK){
           //if we are here everything proceed successfully
           if(requestCode==20){
               //if we are here we are hearing from image gallery

               //it contains the uri of selected image
               Uri imageUri=data.getData();


               //below codeis to open an image into image view from directory but it can be reduced by using glide
//               //declare a stream to read a image from sd card
//               InputStream inputStream;
//
//               //we are getting an input stream based on the URI of image
//
//               try {
//                   inputStream=getActivity().getContentResolver().openInputStream(imageUri);
//
//                   //get a bitmap from stream
//                   Bitmap image= BitmapFactory.decodeStream(inputStream);
//
//                   //show the image
//                   img.setImageBitmap(image);
//
//               } catch (FileNotFoundException e) {
//                   e.printStackTrace();
//               }


               //loading image into image view

               progressDialog.setMessage("Uploading! Please wait");
               progressDialog.show();

               Glide.with(getActivity())
                       .load(imageUri)
                       .centerCrop()
                       .into(img);

               StorageReference ref= FirebaseStorage.getInstance().getReference().child("Photos");

                ref.child(imageUri.getLastPathSegment())
               .putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                      // Toast.makeText(getActivity(), "Ad successfully posted", Toast.LENGTH_SHORT).show();

                       //getting download uri of uploaded image from storage
                       @SuppressWarnings("VisibleForTests") Uri uri=taskSnapshot.getDownloadUrl();

                       //saving uri into global variable to get access of it every where
                        uriFinal=uri;



                       progressDialog.dismiss();
//                       Glide.with(getActivity())
//                               .load(uri)
//                               .centerCrop()
//                               .into(img);



                     //////////  //delete krny k lioye ye code use kro
//                       StorageReference ref1=FirebaseStorage.getInstance().getReferenceFromUrl(String .valueOf(uri));
//                            ref1.delete();

                     ////////  // Open krny ke liye image storage say
//                       StorageReference ref=FirebaseStorage.getInstance().getReferenceFromUrl(String.valueOf(uri));
//
//                       Glide.with(getActivity())
//                                .using(new FirebaseImageLoader())
//                               .load(ref)
//                               .into(img);
//
                   }
               });


//               Glide.with(getActivity())
//
//                       .load(imageUri).centerCrop()
//
//                       .into(img);

           }
       }
    }
}
