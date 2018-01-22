package com.example.sungem.sungempharma.Medrep.Profile;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.Payment.InvoiceItemActivity;
import com.example.sungem.sungempharma.Medrep.Payment.InvoicePaymentAdapter;
import com.example.sungem.sungempharma.Medrep.Payment.PaymentMainFragment;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileMainFragment extends Fragment {

    DBController dbController;
    GlobalFunctions globalFunctions;
    View view;

    String SHAREDPREF = "medrepInfo";
    SharedPreferences sharedpref;

    ImageView myImage;
    TextView txtChangePhoto;
    EditText etFirstname, etLastname, etMiddlename, etOldPassword, etNewPassword;
    Button btnSave;

    String firstname;
    String middlename;
    String lastname;
    String oldpassword;
    String newpassword;

    //picasso
    private Target mTarget;

    Boolean imageUpload=false; //check if may bitmap or wala

    final int IMG_REQUEST = 1;
    Bitmap bitmap;
    String userChoosenTask = "";
    private static final int REQUEST_CAMERA = 0;
    private static final int SELECT_FILE = 1;

    public ProfileMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_profile_main, container, false);
        myImage = (ImageView) view.findViewById(R.id.myImage);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile Information");

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        dbController = new DBController(getActivity().getApplicationContext());
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        txtChangePhoto = (TextView) view.findViewById(R.id.txtChangePhoto);
        etFirstname = (EditText) view.findViewById(R.id.etFirstname);
        etMiddlename = (EditText) view.findViewById(R.id.etMiddlename);
        etLastname = (EditText) view.findViewById(R.id.etLastname);
        etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);

        FloatingActionButton fab = ((MedrepMainActivity) getActivity()).getFloatingActionButton();
        fab.hide();


        btnSave = (Button) view.findViewById(R.id.btnSave);

        String userimg = sharedpref.getString("USERIMAGE","");
        if(!userimg.equals("")) {
            Picasso.with(getActivity()).load(globalFunctions.externalUrl()+userimg).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(myImage);
        }else{
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.logo_orig).into(myImage);
        }

        etFirstname.setText(sharedpref.getString("FIRSTNAME",""));
        etMiddlename.setText(sharedpref.getString("MIDDLENAME",""));
        etLastname.setText(sharedpref.getString("LASTNAME",""));

        txtChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = etFirstname.getText().toString();
                middlename = etMiddlename.getText().toString();
                lastname = etLastname.getText().toString();
                oldpassword = etOldPassword.getText().toString();
                newpassword = etNewPassword.getText().toString();
                if (!firstname.equals("") && !middlename.equals("") && !lastname.equals("")) {
                    if (!oldpassword.equals("") && !newpassword.equals("")) {
                        if (oldpassword.equals(sharedpref.getString("PASSWORD", ""))) {
                            //CONFIRMATION
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Save Changes?");

                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface confirmDialog, int which) {
                                    //IF I SAVE NA GD NI PRE I UPDATE SA ONLINE NA MAY PASSWORD
                                    savePersonalInfoWithPassword();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface confirmDialog, int which) {
                                    confirmDialog.dismiss();
                                }
                            });
                            final AlertDialog confirmDialog = builder.create();
                            confirmDialog.show();
                        } else {
                            etOldPassword.setError("Incorrect old password");
                        }
                    }else{
                        if((!oldpassword.equals("") || !newpassword.equals("")) && (oldpassword.equals("") || newpassword.equals(""))){
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Incomplete Password Details");
                            builder.setMessage("Please complete password details");
                            builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface confirmDialog, int which) {
                                    confirmDialog.dismiss();
                                }
                            });
                            final AlertDialog confirmDialog = builder.create();
                            confirmDialog.show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Save Changes?");

                            builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface confirmDialog, int which) {
                                    //SAVE DATA NGA WALA CHANGES SA PASSWORD
                                    savePersonalInfo();
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface confirmDialog, int which) {
                                    confirmDialog.dismiss();
                                }
                            });
                            final AlertDialog confirmDialog = builder.create();
                            confirmDialog.show();
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Cannot Proceed");
                    builder.setMessage("Please fill information needed");
                    builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface confirmDialog, int which) {
                            confirmDialog.dismiss();
                        }
                    });
                    final AlertDialog confirmDialog = builder.create();
                    confirmDialog.show();
                }
            }
        });
        return view;
    }

    //SAVE WITH BASIC INFO AND IMAGE ONLY
    public void savePersonalInfo() {
        sharedpref = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        if (globalFunctions.isNetworkAvailable()) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Updating Profile Information");
            progressDialog.show();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "edit_profile.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Log.i("STRING REQUEST: ", obj.toString());
                                    String code = obj.getString("code");
                                    String message = obj.getString("message");
                                    String new_image = obj.getString("image");
                                    //UPDPATE SHAREDPREF INFO
                                    SharedPreferences.Editor editor = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE).edit();
                                    editor.putString("USERNAME", firstname + " " + lastname);
                                    editor.putString("FIRSTNAME", firstname);
                                    editor.putString("MIDDLENAME", middlename);
                                    editor.putString("LASTNAME", lastname);
                                    editor.putString("USERIMAGE", new_image);
                                    editor.commit();


                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                                    if(!new_image.equals("")){
                                        File file = new File(new_image);
                                        Picasso picasso = Picasso.with((MedrepMainActivity)getActivity());
                                        picasso.invalidate(file);
                                    }

                                    ((MedrepMainActivity) getActivity()).updateHeaderName(firstname + " " + lastname);
                                    Intent intent = new Intent(getActivity(), MedrepMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                }
                                /*//REPLACE CURRENT DISPLAY :D
                                ((MedrepMainActivity) getActivity()).updateHeaderName(firstname + " " + lastname);
                                (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content_frame, new ProfileMainFragment()).commit();
                                */
                            } catch (JSONException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Update Error", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Update Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

                    params.put("firstname", firstname);
                    params.put("middlename", middlename);
                    params.put("lastname", lastname);
                    if(imageUpload.equals(true)){
                        params.put("image", imageToString(bitmap));
                    }
                    params.put("type", "basic");
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);
        }else{
            Toast.makeText(getContext(), "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }


    //SAVE WITH PASSWORD
    public void savePersonalInfoWithPassword() {
        sharedpref = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);


        if (globalFunctions.isNetworkAvailable()) {
            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Updating Profile Information");
            progressDialog.show();

            final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.mainUrl() + "edit_profile.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            //response sang request

                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject obj = (JSONObject) jsonArray.get(i);
                                    Log.i("STRING REQUEST: ", obj.toString());
                                    String code = obj.getString("code");
                                    String message = obj.getString("message");
                                    String new_image = obj.getString("image");
                                    //UPDPATE SHAREDPREF INFO
                                    SharedPreferences.Editor editor = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE).edit();
                                    editor.putString("USERNAME", firstname + " " + lastname);
                                    editor.putString("FIRSTNAME", firstname);
                                    editor.putString("MIDDLENAME", middlename);
                                    editor.putString("LASTNAME", lastname);
                                    editor.putString("PASSWORD", newpassword);
                                    editor.commit();
                                    //UPDATE IMAGE
                                    if(!new_image.equals("")){
                                        File file = new File(new_image);
                                        Picasso picasso = Picasso.with((MedrepMainActivity)getActivity());
                                        picasso.invalidate(file);
                                    }
                                    ((MedrepMainActivity) getActivity()).updateHeaderName(firstname + " " + lastname);
                                    Intent intent = new Intent(getActivity(), MedrepMainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                   Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                }
                                //REPLACE CURRENT DISPLAY :D
                                ((MedrepMainActivity) getActivity()).updateHeaderName(firstname + " " + lastname);
                                (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content_frame, new ProfileMainFragment()).commit();

                            } catch (JSONException e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Update Error", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //Display records after response
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "Update Error", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

                    params.put("firstname", firstname);
                    params.put("middlename", middlename);
                    params.put("lastname", lastname);
                    params.put("password", newpassword);
                    /*if(!bitmap.equals(null)){
                        params.put("image", imageToString(bitmap));
                    }*/
                    params.put("type", "password");
                    params.put("medrep_id", sharedpref.getString("USERID", ""));
                    return params;
                }
            };
            //Send request
            MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);
        }else{
            Toast.makeText(getContext(), "Please connect to the internet", Toast.LENGTH_SHORT).show();
        }
    }

    //CONVERT SA JPEG SO TANAN NA FILES IS JPEG AMO MANI SA SERVER
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Edit Profile Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getContext());
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask="Choose from Gallery";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                } else {
                    //code for deny
                    Toast.makeText(getContext(), "AHAY", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
                myImage.setImageBitmap(bitmap);
                imageUpload=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        myImage.setImageBitmap(bitmap);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        bitmap = thumbnail;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUpload=true;
        myImage.setImageBitmap(bitmap);
    }

    //UTILITY FOR IMAGE
    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.KITKAT)
        public static boolean checkPermission(final Context context)
        {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
            {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.KITKAT)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        android.app.AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

}
