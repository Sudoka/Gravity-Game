package qdx.game;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class activity1 extends Activity {
	public static String Level = "";
	public int level = 1;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        level = getIntent().getIntExtra(qdx.game.activity1.Level, level);
        if(level == 0)level = 1;
        GameView gm = new GameView(this);
        gm.initial(level);        
        
        WindowManager manage=getWindowManager();
        Display display=manage.getDefaultDisplay();
        gm.screenWidth = display.getWidth();
        gm.screenHeight = display.getHeight();        
        setContentView(gm);
    }
    @Override
    public void onBackPressed() {
    	/* �½�һ��Intent���� */
		Intent intent = new Intent();
		/* ָ��intentҪ�������� */
		intent.setClass(activity1.this, activity0.class);
		/* ����һ���µ�Activity */
		startActivity(intent);
		/* �رյ�ǰ��Activity */
		activity1.this.finish();
    	super.onBackPressed();
    }
}