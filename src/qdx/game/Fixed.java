package qdx.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Fixed{
	
	int startX = 0;
	int startY = 0;
	
	GameShape shape = null;
	
	boolean hasMass = true;
	boolean exists = true;
	boolean visible = true;
	boolean isTarget = false;
	
	float impactFactor = 0.25f;
	float degree = 0f;//�������ת�Ƕ�
	
	public Fixed(int x,int y, int shape, Bitmap bitmap, float degree){
		startX = x;
		startY = y;
		this.degree = degree;
		this.shape = new GameShape(shape, bitmap);
	}
	
	public void drawSelf(Canvas canvas){
		Paint p = new Paint();
		p.setAntiAlias(true);
		canvas.rotate(degree, startX, startY);
		canvas.drawBitmap(shape.bitmap, startX - shape.width/2, startY - shape.height/2, p);//�����ε�ʱ�������Ͻ�Ϊê��
		//canvas.drawCircle(startX, startY, 3, p);
		canvas.rotate(-degree, startX, startY);
	}
	
	public boolean contains(int x,int y){//�޸ĺ�ı�����Ӧ���ܹ�����ת��Ҳ������ȷ�������δ����
		posVector center = new posVector(startX,startY);
		posVector posc = center.posRotate(x, y, degree);
		switch(shape.getShape()){
		case(GameShape.RECT_SHAPE):{
			if(posc.x <= shape.width/2
				&& posc.x >= -shape.width/2
				&& posc.y <= shape.height/2
				&& posc.y >= -shape.height/2)
				return true;
			else 
				return false;
		}
		case(GameShape.CIRCLE_SHAPE):{
			int tmpx = Math.abs(x-startX)*Math.abs(x-startX);//�����Բ�ĵľ���
			int tmpy = Math.abs(y-startY)*Math.abs(y-startY);
			if(tmpx+tmpy <= shape.radius*shape.radius)
				return true;
			else
				return false;
		}
		default:{
			return false;
		}
		}		
	}
}