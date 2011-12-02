package qdx.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class gravityField extends Fixed{
	
	public float gravityX = 20;
	public float gravityY = 0;
	public float degreeP = 0;
	public float impactFactor = 0.25f;
	
	public boolean inField =false;
	public boolean canTouch = false;
	public boolean canDrag = false;
	public boolean dragable = false;
	public boolean selected = false;
	public boolean menuOpen = false;

	Bitmap arrowUp = null;
	Bitmap arrowDown = null;
	
	int gravityLv = 1;
	int[] gravity = new int[9];
	
	
	public gravityField(int x, int y, int shape, Bitmap bitmap, float degree) {
		super(x, y, shape, bitmap, degree);
		gravityLv = 1;
		hasMass = false;
		impactFactor = 0;
		for(int i = 0 ; i < 9 ; i ++){
			gravity[i] = (i+1)*20;
		}
	}
	
	public void getMenuBitmap(Bitmap up, Bitmap down){//�õ��˵��ļ�ͷͼƬ��������
		arrowUp = up;
		arrowDown = down;
	}
	
	@Override
	public void drawSelf(Canvas canvas){
		degree = (float) ((180*degreeP)/Math.PI);
		super.drawSelf(canvas);
		if(menuOpen == true){//���Ƶ����ٶȴ�С�ͷ���Ĳ˵�
			Paint p = new Paint();
			p.setColor(Color.BLUE);
			p.setStrokeWidth(3);
			p.setTextSize(30);			
			p.setAntiAlias(true);
			canvas.drawBitmap(arrowUp, startX + 15, startY - 45 , null);//���������ٶȵ������ͷ
			canvas.drawText(""+gravityLv, startX + 25, startY + 10, p);//������ǰ���ٶ�Lv
			canvas.drawBitmap(arrowDown, startX + 15, startY + 15 , null);//���������ٶȵļ�С��ͷ
			//canvas.drawText(""+degree, 400, 200, p);
		}		
	}
	

	
}