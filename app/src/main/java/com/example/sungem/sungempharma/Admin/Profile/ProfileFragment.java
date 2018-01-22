package com.example.sungem.sungempharma.Admin.Profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.example.sungem.sungempharma.Admin.AdminMainActivity;
import com.example.sungem.sungempharma.Medrep.MedrepMainActivity;
import com.example.sungem.sungempharma.Medrep.Profile.ProfileMainFragment;
import com.example.sungem.sungempharma.Others.DBController;
import com.example.sungem.sungempharma.Others.GlobalFunctions;
import com.example.sungem.sungempharma.Others.MainSelectionActivity;
import com.example.sungem.sungempharma.Others.MySingleton;
import com.example.sungem.sungempharma.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sungem.sungempharma.Admin.AdminMainActivity.SHAREDPREF;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedpref;
    GlobalFunctions globalFunctions;
    //EDBERT
    ImageView myImage;
    TextView txtChangePhoto;
    EditText etFirstname, etLastname, etOldPassword, etNewPassword;
    Button btnSave;

    String firstname;
    String lastname;
    String oldpassword;
    String newpassword;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_admin, container, false);
        // Set title
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.title_profile);

        myImage = (ImageView) view.findViewById(R.id.myImage);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile Information");

        sharedpref = this.getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
        globalFunctions = new GlobalFunctions(getActivity().getApplicationContext());

        txtChangePhoto = (TextView) view.findViewById(R.id.txtChangePhoto);
        etFirstname = (EditText) view.findViewById(R.id.etFirstname);
        etLastname = (EditText) view.findViewById(R.id.etLastname);
        etOldPassword = (EditText) view.findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) view.findViewById(R.id.etNewPassword);
        btnSave = (Button) view.findViewById(R.id.btnSave);

        etFirstname.setText(sharedpref.getString("FIRSTNAME",""));
        etLastname.setText(sharedpref.getString("LASTNAME",""));

        String userimg = sharedpref.getString("USERIMAGE","");
        if(!userimg.equals("")) {
            Picasso.with(getActivity().getApplicationContext()).load(globalFunctions.externalUrl() + userimg).into(myImage);
        }else{
            Picasso.with(getActivity().getApplicationContext()).load(R.drawable.logo_orig).into(myImage);
        }

        setHasOptionsMenu(true);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstname = etFirstname.getText().toString();
                lastname = etLastname.getText().toString();
                oldpassword = etOldPassword.getText().toString();
                newpassword = etNewPassword.getText().toString();
                if (!firstname.equals("") && !lastname.equals("")) {
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

    //BUTTONS SA MENU
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.profile_main_actions, menu);
        super.onCreateOptionsMenu(menu, menuInflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout: {
                sharedpref = getActivity().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpref.edit();
                editor.putString("USERNAME", null);
                editor.putString("USERID", null);
                editor.putBoolean("LOGGED", false);
                editor.putString("ACCESS", null);
                editor.commit();
                Intent intent = new Intent(getActivity(), MainSelectionActivity.class);
                startActivity(intent);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //METHOD TO chANGE INFORMATION
    public void savePersonalInfo() {
        sharedpref = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating Profile Information");
        progressDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.adminUrl() + "edit_profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        //response sang request

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                Log.i("STRING REQUEST: ",obj.toString());
                                String code = obj.getString("code");
                                String message = obj.getString("message");
                                //UPDPATE SHAREDPREF INFO
                                SharedPreferences.Editor editor = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE).edit();
                                editor.putString("USERNAME", firstname+" "+lastname);
                                editor.putString("FIRSTNAME", firstname);
                                editor.putString("LASTNAME", lastname);

                                editor.commit();
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                            //REPLACE CURRENT DISPLAY :D
                            (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content, new ProfileFragment()).commit();

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
                params.put("lastname", lastname);
                params.put("type","basic");
                params.put("usr_id", sharedpref.getString("USERID", ""));
                return params;
            }
        };
        //Send request
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);
    }

    public void savePersonalInfoWithPassword() {
        sharedpref = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE);

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Updating Profile Information");
        progressDialog.show();

        final StringRequest stringRequest = new StringRequest(Request.Method.POST, globalFunctions.adminUrl() + "edit_profile.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        //response sang request

                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = (JSONObject) jsonArray.get(i);
                                Log.i("STRING REQUEST: ",obj.toString());
                                String code = obj.getString("code");
                                String message = obj.getString("message");
                                //UPDPATE SHAREDPREF INFO
                                SharedPreferences.Editor editor = getContext().getSharedPreferences(SHAREDPREF, MODE_PRIVATE).edit();
                                editor.putString("USERNAME", firstname+" "+lastname);
                                editor.putString("FIRSTNAME", firstname);
                                editor.putString("LASTNAME", lastname);
                                editor.putString("PASSWORD", newpassword);

                                editor.commit();
                                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                            }
                            //REPLACE CURRENT DISPLAY :D
                            (getActivity().getSupportFragmentManager()).beginTransaction().replace(R.id.content, new ProfileFragment()).commit();

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
                params.put("lastname", lastname);
                params.put("password", newpassword);
                params.put("type","password");
                params.put("usr_id", sharedpref.getString("USERID", ""));
                return params;
            }
        };
        //Send request
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestque(stringRequest);
    }
}
