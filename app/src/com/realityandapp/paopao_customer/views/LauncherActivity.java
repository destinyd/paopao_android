package com.realityandapp.paopao_customer.views;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.easemob.chat.EMChat;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import roboguice.activity.RoboActivity;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauncherActivity extends RoboActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        new RoboAsyncTask<Void>(this) {

            @Override
            public Void call() throws Exception {
                // todo init here
                EMChat.getInstance().setAppInited();
                PaopaoCustomerApplication application = (PaopaoCustomerApplication) getApplication();
                application.init_image_config();

                // todo get user info to login im
                // application.login();
                Thread.sleep(1000);//

                return null;
            }

            @Override
            protected void onSuccess(Void aVoid) throws Exception {
                go_to_main();
            }
        }.execute();
    }

    private void go_to_main() {
        startActivity(new Intent(this, RealMainActivity.class));
        finish();
    }
}