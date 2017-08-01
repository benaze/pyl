package com.example.zak.testfragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EditSocialFragment extends Fragment {

    private DatabaseReference mDatabaseRef;
    private FirebaseDatabase database;
    private DatabaseReference mDtbRef;
    private DatabaseReference myRef;
    private EditText editText;
    Fragment fragment = this;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        return inflater.inflate(R.layout.fragment_edit_social, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("EditSocial");


        ImageView socialNetworkImage = (ImageView) view.findViewById(R.id.socialNetworkImage);

        editText = (EditText) view.findViewById(R.id.editSocialNetworkText);

        database = FirebaseDatabase.getInstance();
        mDatabaseRef = database.getReference("users");
        //mDtbRef = mDatabaseRef.child(((MainActivity)getActivity()).getUser());

        mDtbRef = mDatabaseRef.child(((MainActivity)getActivity()).getUser());
        String socialNetwork = ProfilFragment.INSTANCE.getNetworkClicked();

        myRef = mDtbRef.child(socialNetwork).child("id");

        Button valider = (Button) view.findViewById(R.id.buttonValider);

        //Uri image= Uri.parse("R.drawable."+socialNetwork);


        int resID = getResources().getIdentifier(socialNetwork , "drawable", getContext().getPackageName());
        Drawable drawable = getResources().getDrawable(resID);


        socialNetworkImage.setBackground(drawable);




        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                myRef.setValue(editText.getText().toString());
                //InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                getActivity().getSupportFragmentManager().popBackStack();

                /*Bundle args = new Bundle();
                args.putString("socialNetwork","facebook");
                EditSocialFragment fragment = new EditSocialFragment();
                fragment.setArguments(args);
                ((MainActivity)getActivity()).launchFragment(new EditSocialFragment());*/

            }
        });







    }
}
