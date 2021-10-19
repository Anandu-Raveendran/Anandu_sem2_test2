package com.example.anandusem2test2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.anandusem2test2.Database.AppDatabase;
import com.example.anandusem2test2.Database.UserData;
import com.example.anandusem2test2.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {

    private ActivityEditBinding binding;
    private UserData selectedUserData = null;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = AppDatabase.getDbInstance(getApplicationContext());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedUserData = (UserData) bundle.getSerializable("SelectedItem");
            if (selectedUserData != null) {
                Log.i("anandu", "selected user is NOT null");
                binding.titleEditText.setText(selectedUserData.getTitle());
                binding.subTitleEditText.setText(selectedUserData.getSubtitle());
                binding.lattitudeEditText.setText(String.valueOf(selectedUserData.getLatitude()));
                binding.longitudeEditText.setText(String.valueOf(selectedUserData.getLongitude()));
                binding.deleteBtn.setVisibility(View.VISIBLE);
            } else {
                Log.i("anandu", "selected user is null");
                binding.deleteBtn.setVisibility(View.GONE);
            }
        }

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.userDao().delete(selectedUserData);
                Intent intent = new Intent();
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Save").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String message = "";
                if (binding.titleEditText.getText().toString().isEmpty()) {
                    message = "Title is empty";
                } else if (binding.subTitleEditText.getText().toString().isEmpty()) {
                    message += " Sub title is empty";
                } else if (binding.longitudeEditText.getText().toString().isEmpty()) {
                    message += " Longitude is empty";
                } else if (binding.lattitudeEditText.getText().toString().isEmpty()) {
                    message += " Latitude is empty";
                } else {

                    if(selectedUserData == null) {
                        selectedUserData = new UserData(0,
                                binding.titleEditText.getText().toString(),
                                binding.subTitleEditText.getText().toString(),
                                Double.valueOf(String.valueOf(binding.lattitudeEditText.getText())),
                                Double.valueOf(String.valueOf(binding.longitudeEditText.getText())));
                        db.userDao().inserData(selectedUserData);
                    } else {
                        selectedUserData.setTitle(binding.titleEditText.getText().toString());
                        selectedUserData.setSubtitle(binding.subTitleEditText.getText().toString());
                        selectedUserData.setLatitude(Double.valueOf(binding.lattitudeEditText.getText().toString()));
                        selectedUserData.setLatitude(Double.valueOf(binding.longitudeEditText.getText().toString()));
                        db.userDao().update(selectedUserData);
                    }

                    finish();
                }
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}