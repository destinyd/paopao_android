package com.realityandapp.paopao_customer.views;

import android.content.Intent;
import android.os.Bundle;
import com.easemob.chat.EMChat;
import com.realityandapp.paopao_customer.PaopaoCustomerApplication;
import com.realityandapp.paopao_customer.R;
import com.realityandapp.paopao_customer.models.User;
import com.realityandapp.paopao_customer.networks.DataProvider;
import com.realityandapp.paopao_customer.networks.HttpApi;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import roboguice.activity.RoboActivity;
import roboguice.util.RoboAsyncTask;

public class LauncherActivity extends RoboActivity {
    PaopaoCustomerApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher);
        UpdateConfig.setDebug(true);
        // 静默下载更新 wifi
        UmengUpdateAgent.silentUpdate(this);

        application = (PaopaoCustomerApplication) getApplication();

        EMChat.getInstance().setAppInited();
        // todo init here
        feedback_sync();
        application.init_image_config();
        go_to_main();
    }

    private void feedback_sync() {
        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();
    }

    private void go_to_main() {
        User user = User.current();
        if(user != null) {
            PaopaoCustomerApplication.getInstance().im_login();
            startActivity(new Intent(this, RealMainActivity.class));
        }else{
            startActivity(new Intent(LauncherActivity.this, SignInActivity.class));
        }
        finish();
    }
}