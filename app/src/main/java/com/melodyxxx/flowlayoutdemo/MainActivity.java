package com.melodyxxx.flowlayoutdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class MainActivity extends Activity {

    private FlowLayout mFlowLayout;

    private String[] mTags = new String[]{
            "Android", "iOS", "JavaScript",
            "HTML/CSS", "jQuery", "Node.js",
            "WebApp", "Python", "Java",
            "MySQL", "SQL", "Oracle",
            "Unity 3D", "Cocos2d-x", "AngularJS"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0; i < mTags.length; i++) {
            TextView tv = (TextView) inflater.inflate(R.layout.text, mFlowLayout, false);
            tv.setText(mTags[i]);
            mFlowLayout.addView(tv);
        }
    }
}
