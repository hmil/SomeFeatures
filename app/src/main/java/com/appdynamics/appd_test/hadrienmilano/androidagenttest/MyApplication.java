package com.appdynamics.appd_test.hadrienmilano.androidagenttest;

import android.app.Application;

import com.appdynamics.eumagent.runtime.AgentConfiguration;
import com.appdynamics.eumagent.runtime.Instrumentation;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Start the AppDynamics Instrumentation
        Instrumentation.start(AgentConfiguration.builder()
            .withContext(getApplicationContext())
            .withAppKey("YOUR_APP_KEY")
            .withCollectorURL("YOUR_COLLECTOR_URL")
            .build());
    }
}