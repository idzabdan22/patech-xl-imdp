package com.example.xl_imdp_patech.fragments;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.xl_imdp_patech.R;
import com.example.xl_imdp_patech.model.main.ReportModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogFragment extends android.app.DialogFragment {

    ImageView cancelButton;
    Button ya,tidak;
    private final String FIREBASE_URL = "https://patech-xl-imdp-default-rtdb.asia-southeast1.firebasedatabase.app/";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_layout, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_URL);
        databaseReference = firebaseDatabase.getReference("report");

        cancelButton = v.findViewById(R.id.cancel_dialog);
        ya = v.findViewById(R.id.ya_button);
        tidak = v.findViewById(R.id.tidak_button);

        ya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeReport("20-21-22", "ya", "mod", 80.0f, 90.0f);
                i = 1;
                getDialog().dismiss();
            }
        });

        tidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                getDialog().dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i != 1){

                }
                else{
                    getDialog().dismiss();
                }
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = 875;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    private void writeReport(String time_stamp, String input_petani, String prediction, Float leaf_wetness, Float temp){
        ReportModel reportModel = new ReportModel(time_stamp, input_petani, prediction, leaf_wetness, temp);
        databaseReference.child(time_stamp).setValue(reportModel);
    }
}
