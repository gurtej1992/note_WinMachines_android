package com.tej.note_winmachines_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Realm.init(this);
        {
            try{
                Realm.getDefaultInstance();

            }catch (Exception e){

                // Get a Realm instance for this thread
                RealmConfiguration config = new RealmConfiguration.Builder()
                        .deleteRealmIfMigrationNeeded()
                        .build();
                Realm.getInstance(config);
            }
        }
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        finish();
    }
    }

