package qdx.game;

import java.util.concurrent.Semaphore;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class MainMovable extends Movable{

	double timeX;
	double timeY;
	
	boolean moving = false;
	boolean menuOpen = false;

		
	float touchX = 0;
	float touchY = 0;
	
	Bitmap arrowUp = null;
	Bitmap arrowDown = null;
	
	public MainMovable(int x,int y, int shape, Bitmap bitmap, int mass, GameView father){
		super(x, y, shape, bitmap, mass, father);
		
	}
	
	@Override
	public void drawSelf(Canvas canvas){
		try{
			semaphore.acquire();
			//mt.isRunning = false;
			if(menuOpen == true){//���Ƶ����ٶȴ�С�ͷ���Ĳ˵�				
				Paint p = new Paint();			
				/* ����һ�δ����У�t,k,q��Ϊ�м���ʱ����
				 * ��δ���������Ǽ�������ָ��Բ�ĵ�����
				 * �ķ����ϣ�����Բ�Ķ����ĵ�����ꡣ����
				 * ���������������Ƶ����ٶȷ�����ٶ���
				 */
				float t = 0;
				float k = 0;
				float L = 100;
				if(touchY == currentY){
					k = 0;
					t = L*L;
				}
				else{
					float q = Math.abs(touchX - currentX)/Math.abs(touchY-currentY);
					t = (q*q*L*L)/(q*q+1);
					k = (L*L)/(q*q+1);
				}
				float m = 0;
				float n = 0;
				if(touchX - currentX >= 0 && touchY - currentY >= 0){
					m = (float) (Math.sqrt(t) + currentX);
					n = (float) (Math.sqrt(k) + currentY);
				}
				else if(touchX - currentX < 0 && touchY - currentY < 0){
					m = (float) (currentX - Math.sqrt(t));
					n = (float) (currentY - Math.sqrt(k));
				}
				else if(touchX - currentX < 0 && touchY - currentY >= 0){
					m = (float) (currentX - Math.sqrt(t));
					n = (float) (Math.sqrt(k) + currentY);
				}
				else if(touchX - currentX >= 0 && touchY - currentY < 0){
					m = (float) (Math.sqrt(t) + currentX);
					n = (float) (currentY - Math.sqrt(k));
				}
				else{
					canvas.drawText("error: " + m +" " + n, 150, 20, p);
				}
				
				p.setColor(Color.GREEN);
				p.setAntiAlias(true);
				p.setStrokeWidth(2);
				canvas.drawLine(currentX, currentY, m, n, p);//����������	
				
				p.setColor(Color.BLUE);
				p.setStrokeWidth(3);
				p.setTextSize(30);
				
				p.setAntiAlias(true);
				canvas.drawBitmap(arrowUp, currentX + 15, currentY - 45 , null);//���������ٶȵ������ͷ
				canvas.drawText(""+speedLv, currentX + 25, currentY + 10, p);//������ǰ���ٶ�Lv
				canvas.drawBitmap(arrowDown, currentX + 15, currentY + 15 , null);//���������ٶȵļ�С��ͷ
			}
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			if(shape.getShape() == GameShape.CIRCLE_SHAPE){//�����������
				canvas.drawBitmap(shape.bitmap, currentX - shape.radius, currentY - shape.radius, null);
				//canvas.drawCircle(currentX, currentY, 1, paint);
				/*paint.setColor(Color.WHITE);
				canvas.drawRect(400, 0, 480, 80, paint);
				paint.setColor(Color.BLUE);
				canvas.drawText("fvx: "+firstVX, 400, 20, paint);
				canvas.drawText("fvy: "+firstVY, 400, 30, paint);
				canvas.drawText("cvx: "+currentVX, 400, 40, paint);
				canvas.drawText("cvy: "+currentVY, 400, 50, paint);	*/			
			}
			semaphore.release();
		}
		catch(Exception e){
			Log.println(0, "MainMovable", "Outer Exception");
			e.printStackTrace();
		}
		
	}
	
	public void getMenuBitmap(Bitmap up, Bitmap down){//�õ��˵��ļ�ͷͼƬ��������
		arrowUp = up;
		arrowDown = down;
	}
	
	@Override
	public boolean contains(float x,float y){//�жϸ����������Ƿ��ڱ�����ķ�Χ��
		switch(shape.getShape()){
		case(GameShape.RECT_SHAPE):{
			if(x >= currentX && x <= currentX + shape.width 
					&& y <= currentY && y >= currentY + shape.height)
				return true;
			else 
				return false;
		}
		case(GameShape.CIRCLE_SHAPE):{
			float tmpx = Math.abs(x-currentX)*Math.abs(x-currentX);//�����Բ�ĵľ���
			float tmpy = Math.abs(y-currentY)*Math.abs(y-currentY);
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