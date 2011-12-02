package qdx.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class activity2 extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);             
        setContentView(R.layout.main3);
    }
    @Override
    public void onBackPressed() {
    	/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(activity2.this, activity0.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		activity2.this.finish();
    	super.onBackPressed();
    }
}