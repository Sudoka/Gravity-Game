package qdx.game;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	
	//final String[] stage = null;
	//stage[0] = "400,160,100,160,1,250,160,1,wood01,90,2,250,80,1,gsquaresmall,90,250,280,1,gsquaresmall,-90,end" ;
	//final String stage1 = "400,160,100,160,1,250,160,1,wood01,90,2,250,80,1,gsquaresmall,90,250,280,1,gsquaresmall,-90,end";
	//final String stage2 ="400,160,100,160,1,250,160,1,wood01,90,2,250,80,1,gsquaresmall,90,250,280,1,gsquaresmall,90";
	
	private static final String	TAG	= "GameView";
	
	int screenWidth = 0;
	int screenHeight = 0;
	int gameStage = 1;
	int thislevel = 1;
	final int STAGES = 10;
	
	String[] stage = new String[STAGES];	
	
	Fixed[] fixedObjects = new Fixed[20];
	//ArrayList<Movable> movableObjects = new ArrayList<Movable>();
	Movable[] movableObjects = new Movable[10];
	
	final String woodBlock1str = "woodBlock1";
	final String woodBlockBigstr = "woodBlockBig";
	final String wood01str = "wood01";
	final String grectstr = "grect";
	final String gsquarestr = "gsquare";
	final String menustr = "menu";
	final String startbuttonstr = "startbutton";
	final String gsquaresmallstr = "gsquaresmall";
	
	
	Bitmap arrowUp = null;
	Bitmap arrowDown = null;
	Bitmap movingBall = null;//30*30
	Bitmap woodBlock1 = null;//100*21
	Bitmap woodBlockBig = null;//200*43
	Bitmap wood01 = null;//160*23
	Bitmap target = null;//40*32
	Bitmap grect = null;//322*119
	Bitmap gsquare = null;//144*119
	Bitmap menu = null;//85*600
	Bitmap startbutton = null;//171*68
	Bitmap gamebackground = null;//600*375
	Bitmap gsquaresmall = null;//72*60
	Bitmap winimage = null;//200*192
	SurfaceHolder surface;
	DrawThread dt = null;
	DrawThread dt2 = null;
	Canvas canvas = null;
	
	double timeDown = 0f;
	double directionVX = 0;
	double directionVY = 0;
	double directionaX = 0;
	double directionaY = 0;
	
	float touchX = 0f;
	float touchY = 0f;
	float touchX_UP = 0f;
	float touchY_UP = 0f;
	float firstTouchX = 0f;
	float firstTouchY = 0f;
	
	boolean startButton = false;
	boolean win = false;
	boolean lose = false;
	
	public GameView(Context activity){
		super(activity);
		for(int i = 0 ; i < STAGES ; i ++){
			stage[i] = new String();
		}
		stage[0] = "400,160,100,160,1,250,160,1,wood01,90,2,250,80,1,gsquaresmall,90,250,280,1,gsquaresmall,90,end";
		stage[1] = "450,300,100,40,3,160,300,1,woodBlockBig,0,310,300,1,woodBlock1,0,360,180,1,woodBlockBig,90,2,180,30,1,gsquaresmall,90,250,30,1,gsquaresmall,0,end";
		stage[2] = "400,160,150,160,2,100,160,1,wood01,90,200,160,1,wood01,90,3,300,50,1,gsquaresmall,0,300,100,1,gsquaresmall,0,300,150,1,gsquaresmall,0,end";
		
		getHolder().addCallback(this);
		initBitmaps(getResources());
		directionVX = 1;
		directionVY = 0;
		directionaX = 1;
		directionaY = 0;
		dt = new DrawThread(this,getHolder());		
	}
	
	public void loadstage(int level){
		
	}
	
	public void initial(int level){
		//initBitmaps(getResources());
		thislevel = level;
		initMovable(level);
		initFixed(level);
	}
	
	public void initBitmaps(Resources r){
		movingBall = BitmapFactory.decodeResource(r, R.drawable.main_role);
		arrowUp = BitmapFactory.decodeResource(r, R.drawable.arrow_up);
		arrowDown = BitmapFactory.decodeResource(r, R.drawable.arrow_down);
		woodBlock1 = BitmapFactory.decodeResource(r, R.drawable.woodblock);
		woodBlockBig = BitmapFactory.decodeResource(r, R.drawable.woodblockbig);
		wood01 = BitmapFactory.decodeResource(r, R.drawable.wood01);
		target = BitmapFactory.decodeResource(r, R.drawable.target);
		grect = BitmapFactory.decodeResource(r, R.drawable.grect);
		gsquare = BitmapFactory.decodeResource(r, R.drawable.gsquare);
		menu = BitmapFactory.decodeResource(r, R.drawable.menu);
		startbutton = BitmapFactory.decodeResource(r, R.drawable.startbutton);
		gamebackground = BitmapFactory.decodeResource(r, R.drawable.gamebackground);
		gsquaresmall = BitmapFactory.decodeResource(r, R.drawable.gsquaresmall);
		winimage = BitmapFactory.decodeResource(r, R.drawable.win);
	}
	
	public void initMovable(int level){
		for(int i = 0 ; i < movableObjects.length; i ++){
			movableObjects[i] = null;
		}
		directionVX = 1;
		directionVY = 0;
		directionaX = 1;
		directionaY = 0;
		String toinitx = getWord(stage[level-1],1);
		String toinity = getWord(stage[level-1],2);
		MainMovable main_role = null;
		main_role = new MainMovable(Integer.parseInt(toinitx), Integer.parseInt(toinity),
									GameShape.CIRCLE_SHAPE, movingBall, 5, this);
		main_role.canTouch = true;
		main_role.touchAble = true;
		main_role.canDrag = false;
		main_role.dragable = false;
		main_role.mt.a_Y = 0;
		main_role.getMenuBitmap(arrowUp, arrowDown);
		movableObjects[0] = main_role;
	}
	
	public static String getWord(String str,int num){
		String result = null;
		String tmp = null;
		tmp = str;
		if(num == 1){
			result = str.substring(0,str.indexOf(','));
			return result;
		}
		for(int i = 1; i < num ; i ++){
			tmp = tmp.substring(tmp.indexOf(',')+1, tmp.length());
		}
		result = tmp.substring(0, tmp.indexOf(','));
		return result;		
	}
	
	public void initFixed(int level){
		for(int i = 0 ; i < fixedObjects.length; i ++){
			fixedObjects[i] = null;
		}
		int targetx = Integer.parseInt(getWord(stage[level-1],3));
		int targety = Integer.parseInt(getWord(stage[level-1],4));
		Fixed targetob = new Fixed(targetx,targety,GameShape.CIRCLE_SHAPE,target,0);
		targetob.isTarget = true;
		fixedObjects[0] = targetob;
		int fixednum = Integer.parseInt(getWord(stage[level-1],5));
		Log.v("fixed:", ""+fixednum);
		int fx = 0;
		int fy = 0;
		int gs = 0;
		String bitmapstr = null;
		float degree = 0;
		for(int i = 0; i < fixednum ; i++){
			fx = Integer.parseInt(getWord(stage[level-1],5+i*5+1));
			fy = Integer.parseInt(getWord(stage[level-1],5+i*5+2));
			gs = Integer.parseInt(getWord(stage[level-1],5+i*5+3));
			bitmapstr = getWord(stage[level-1],5+i*5+4);
			degree = Float.parseFloat(getWord(stage[level-1],5+i*5+5));
			Bitmap param = null;
			if(bitmapstr.equals(woodBlock1str)) param = woodBlock1; 
			else if(bitmapstr.equals(woodBlockBigstr)) param = woodBlockBig;
			else if(bitmapstr.equals(wood01str)) param = wood01;
			else if(bitmapstr.equals(grectstr)) param = grect;
			else if(bitmapstr.equals(gsquarestr)) param = gsquare;
			else if(bitmapstr.equals(gsquaresmallstr)) param = gsquaresmall;
			Log.v(TAG,""+i+","+fixednum+","+param.toString());
			fixedObjects[i+1] = new Fixed(fx,fy,gs,param,degree);			
		}
		int gravitynum = Integer.parseInt(getWord(stage[level-1],5+fixednum*5+1));
		for(int i = 0 ; i < gravitynum ; i++){
			fx = Integer.parseInt(getWord(stage[level-1],6+5*fixednum+i*5+1));
			fy = Integer.parseInt(getWord(stage[level-1],6+5*fixednum+i*5+2));
			gs = Integer.parseInt(getWord(stage[level-1],6+5*fixednum+i*5+3));
			bitmapstr = getWord(stage[level-1],6+5*fixednum+i*5+4);
			degree = Float.parseFloat(getWord(stage[level-1],6+5*fixednum+i*5+5));
			Bitmap param = null;
			if(bitmapstr.equals(woodBlock1str)) param = woodBlock1; 
			else if(bitmapstr.equals(woodBlockBigstr)) param = woodBlockBig;
			else if(bitmapstr.equals(wood01str)) param = wood01;
			else if(bitmapstr.equals(grectstr)) param = grect;
			else if(bitmapstr.equals(gsquarestr)) param = gsquare;
			else if(bitmapstr.equals(gsquaresmallstr)) param = gsquaresmall;
			fixedObjects[i+fixednum+1] = new gravityField(fx,fy,gs,param,degree);
			((gravityField)fixedObjects[i+fixednum+1]).canDrag = true;
			((gravityField)fixedObjects[i+fixednum+1]).canTouch = true;
			((gravityField)fixedObjects[i+fixednum+1]).dragable = true;
			((gravityField)fixedObjects[i+fixednum+1]).getMenuBitmap(arrowUp, arrowDown);
		}
		//Fixed block1 = null;
		//block1 = new Fixed(200,80,GameShape.RECT_SHAPE,woodBlock1, -60);
		/*Fixed block2 = new Fixed(250, 220, GameShape.RECT_SHAPE, woodBlock1, 30);
		Fixed block3 = new Fixed(400,160,GameShape.RECT_SHAPE,woodBlock1,90);
		gravityField  gF = new gravityField(80,100,GameShape.RECT_SHAPE,gsquaresmall,0);
		gF.canTouch = true;
		gF.dragable = true;
		gF.canDrag = true;
		gF.getMenuBitmap(arrowUp, arrowDown);
		gF.gravityX = gF.gravity[0];*/
		//gF.isTarget = true;
		//fixedObjects.add(block1);

	}
	
	public void restart(){
		//initFixed();
		//initMovable();
	}
	
	public void doDraw(Canvas canvas){
		if(win == false && lose == false){
			Rect screen = new Rect(0,0,screenWidth,screenHeight);
			canvas.drawBitmap(gamebackground, null, screen, null);
			for(Fixed f:fixedObjects){
				if(f != null)
					f.drawSelf(canvas);
			}
			for(int i = 0; i < movableObjects.length ; i++){
				if(movableObjects[i] != null)
					movableObjects[i].drawSelf(canvas);
			}
			Rect menuarea = new Rect(0,0,screenWidth/8,screenHeight);
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			paint.setAntiAlias(true);
			paint.setStrokeWidth(2);
			paint.setTextSize(30);
			canvas.drawBitmap(menu, null, menuarea, null);
			Rect startarea = new Rect(0,screenHeight-50,screenWidth/8,screenHeight);
			canvas.drawBitmap(startbutton, null, startarea, paint);
			canvas.drawText("GO!", 5, screenHeight-18, paint);
		}
		else if(win == false && lose == true){			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setTextSize(30);
			paint.setColor(Color.RED);
			canvas.drawText("�ڰ�����Ľ�����ʿ���������ˡ���", 20, screenHeight/4, paint);
			canvas.drawText("�����ؼ���������", 20, screenHeight/2, paint);
			canvas.drawText("������Ļ�Ŵ���", 20, 3*screenHeight/4, paint);
			dt.run = false;
		}
		else if(win == true && lose == false){			
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStrokeWidth(3);
			paint.setTextSize(30);
			paint.setColor(Color.BLUE);
			canvas.drawBitmap(winimage,0,0,paint);
			canvas.drawText("��ϲ�㣡��ɹ������ˣ�", 20, screenHeight/4, paint);
			canvas.drawText("�����ؼ���������", 20, screenHeight/2, paint);
			canvas.drawText("������Ļ��Űһ��", 20, 3*screenHeight/4, paint);
			dt.run = false;
		}
		else{
			
		}
	}
	
	public boolean onTouchEvent (MotionEvent event){
		if(win == true || lose == true){
			initial(thislevel);
			win = false;
			lose = false;
			dt.run = true;
			return true;
		}
		touchX = event.getX();
		touchY = event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			firstTouchX = event.getX();//��¼DOWN�¼��еĴ���λ�ã���������UP���ж�ʹ��
			firstTouchY = event.getY();
			timeDown = System.currentTimeMillis();
			for(int i = 0; i < movableObjects.length ; i++){
				if(movableObjects[i] == null) continue;
				if(movableObjects[i].canTouch){//����Ƿ���Ӧ����
					if(movableObjects[i].contains(event.getX(), event.getY())
						&& movableObjects[i].mt.selected == false){//�жϴ�������Ļ�ϵ������Ƿ�����Ӧ�����
						movableObjects[i].mt.selected = true;//��������ñ�ѡ��λΪtrue
					}
					else{
						/* TODO:������У���û�Դ˴����������������
						 * ��ʱ���գ�δ�����������ز���
						 */
					}
				}
			}
			for(Fixed f:fixedObjects){
				if(f == null) continue;
				if(f instanceof gravityField){
					if(((gravityField)f).canTouch){//����Ƿ���Ӧ����
						if(((gravityField)f).contains((int)event.getX(), (int)event.getY())
							&& ((gravityField)f).selected == false){//�жϴ�������Ļ�ϵ������Ƿ�����Ӧ�����
							((gravityField)f).selected = true;//��������ñ�ѡ��λΪtrue
						}
						else{
							/* TODO:������У���û�Դ˴����������������
							 * ��ʱ���գ�δ�����������ز���
							 */
						}
					}
				}
			}
			if(event.getX() > 0//����ڿ�ʼ��ť�� 
				&& event.getX() < screenWidth /8 
				&& event.getY() < screenHeight 
				&& event.getY() > screenHeight - 50){
				if(startButton == false)
					startButton = true;
				else
					startButton = false;
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_MOVE){	//Ӧ�ÿ��ǵ����϶�����С���ʱ�򣬲��ܴ����ϰ��
			for(int i = 0; i < movableObjects.length ; i++){	//������Ϊ��ʵ���У�û����������С����Ա�����϶�
				if(movableObjects[i] == null) continue;			//������ʱ�Ͳ�ʵ�����������
				if(movableObjects[i] instanceof MainMovable//�Ƿ�ΪMainMovableʵ��
					&& System.currentTimeMillis() - timeDown > 150//�϶���ʱ���ٽ�ֵ
					&& ((MainMovable)movableObjects[i]).menuOpen){//�˵����ڴ�״̬
					
					((MainMovable)movableObjects[i]).touchX = touchX;//����MainMovable��������˵��У�
					((MainMovable)movableObjects[i]).touchY = touchY;//�ٶȷ����ߵĽ����㣬���ٶȷ������Ƕ������߶�
					
					double distance = Math.sqrt((touchX-movableObjects[i].currentX)*(touchX-movableObjects[i].currentX)
												+(touchY-movableObjects[i].currentY)*(touchY-movableObjects[i].currentY));
					
					directionVX = (touchX-movableObjects[i].currentX)/distance;//����X�����ٶ�����ָ��������ٶ�֮�ȣ����Լ���X�����ٶȷ���
					directionVY = (touchY-movableObjects[i].currentY)/distance;//ͬ�ϼ���Y
					
					movableObjects[i].firstVX= (float) (directionVX //����X�����ϵ��ٶȷ���
							* movableObjects[i].speed[movableObjects[i].speedLv-1]);
					movableObjects[i].firstVY = (float) (directionVY //����Y�����ϵ��ٶȷ���
							* movableObjects[i].speed[movableObjects[i].speedLv-1]);
				}
				if(movableObjects[i].canTouch){
					if(movableObjects[i].mt.selected
						&& movableObjects[i].dragable
						&& System.currentTimeMillis() - timeDown > 100){//�õ��϶�ʱ���λ��
						if(movableObjects[i].canDrag){//�϶�����ƶ�
							if(touchX > screenWidth/8){
								movableObjects[i].currentX = (int)event.getX();
								movableObjects[i].currentY = (int)event.getY();
							}
						}
						else{
							
						}
					}
				}
			}
			for(Fixed f:fixedObjects){
				if(f == null) continue;
				if(f instanceof gravityField){
					if(((gravityField)f).canTouch){//����Ƿ���Ӧ����
						if(System.currentTimeMillis() - timeDown > 150){//�϶���ʱ���ٽ�ֵ
							if(((gravityField)f).menuOpen){
								
								double distance = Math.sqrt((touchX-((gravityField)f).startX)*(touchX-((gravityField)f).startX)
															+(touchY-((gravityField)f).startY)*(touchY-((gravityField)f).startY));
								
								directionaX = (touchX-((gravityField)f).startX)/distance;//����X������ٶ�����ָ��������ٶ�֮��
								directionaY = (touchY-((gravityField)f).startY)/distance;//ͬ�ϼ���Y
								
								((gravityField)f).gravityX = (float) (directionaX //����X�����ϵļ��ٶȷ���
										* ((gravityField)f).gravity[((gravityField)f).gravityLv-1]);
								((gravityField)f).gravityY = (float) (directionaY //����Y�����ϵļ��ٶȷ���
										* ((gravityField)f).gravity[((gravityField)f).gravityLv-1]);
								
								if(touchX - f.startX < 0){
									((gravityField)f).degreeP = (float) (Math.PI-Math.asin((touchY - f.startY) / distance));
								}
								else{								
									((gravityField)f).degreeP = (float) Math.asin((touchY - f.startY) / distance);
								}
							}
							else{
								if(((gravityField)f).canTouch){
									if(((gravityField)f).selected
										&& ((gravityField)f).dragable
										&& System.currentTimeMillis() - timeDown > 100){//�õ��϶�ʱ���λ��
										if(((gravityField)f).canDrag){//�϶�����ƶ�
											if(touchX > screenWidth/8){
												((gravityField)f).startX = (int)event.getX();
												((gravityField)f).startY = (int)event.getY();
											}
										}
										else{
											
										}
									}
								}
							}
						}
					}
				}
			}
		}
		else if(event.getAction() == MotionEvent.ACTION_UP){
			double timeUp = System.currentTimeMillis();
			if(timeUp - timeDown < 200){//�����click�¼�
				if(event.getX() > 0//�����¼��е���ڿ�ʼ�ƶ��˵��� 
						&& event.getX() < screenWidth /8 
						&& event.getY() < screenHeight 
						&& event.getY() > screenHeight - 50){
					
					if(startButton){//�ڰ����¼���Ӧʱ�������start button
						for(int i = 0; i < movableObjects.length ; i++){
							if(movableObjects[i] == null) continue;
							if(movableObjects[i] instanceof MainMovable)//�Զ��ر�menu
								((MainMovable)movableObjects[i]).menuOpen = false;
							movableObjects[i].mt.isRunning = true;//��������������е������Ϊrun״̬
							movableObjects[i].startX = movableObjects[i].currentX;
							movableObjects[i].startY = movableObjects[i].currentY;
							movableObjects[i].lastMoveTimeX = System.nanoTime();
							movableObjects[i].lastMoveTimeY = System.nanoTime();
							movableObjects[i].isMoving = true;
							movableObjects[i].canDrag = false;
							movableObjects[i].mt.selected = false;
							movableObjects[i].canTouch = false;	
							movableObjects[i].mt.collisonFlag = true;
							movableObjects[i].touchAble = false;
						}
						for(Fixed f:fixedObjects){
							if(f == null) continue;
							if(f instanceof gravityField){
								((gravityField)f).menuOpen = false;
								((gravityField)f).canDrag = false;
								((gravityField)f).selected = false;
								((gravityField)f).canTouch = false;
							}
						}
						
					}
					else{//start button û�б�click��
						for(int i = 0; i < movableObjects.length ; i++){//!!����ע�⣬���ܻὫ��touchable�Ķ������ó�touchable
							if(movableObjects[i] == null) continue;
								movableObjects[i].isMoving = false;
								movableObjects[i].mt.isRunning = false;//��Ϊ��run״̬
							if(movableObjects[i].touchAble){
								movableObjects[i].canDrag = true;
								movableObjects[i].canTouch = true;
							}
						}
						for(Fixed f:fixedObjects){
							if(f == null) continue;
							if(f instanceof gravityField){
								((gravityField)f).canDrag = true;
								((gravityField)f).canTouch = true;
							}
						}
					}
				}
			}
			for(int i = 0; i < movableObjects.length ; i++){
				if(movableObjects[i] == null) continue;
				//������һ��if�ǲ������㿴���е�ͷƤ���飿�𼤶�~�������к��������дע�͵ġ����ĵ�ɹ���~
				/*����һ��ע�ʹ������߷�ɵ�ƣ������ӵ�������һ��if�������Լ�����ǰ����Ƶ�ʱ���鲻�㵼�µġ�
				* ��Ϊ��һ��д������΢����һ�����Ϸ�����Զ�����Ϸ��״̬û�и���������õĴ���Ӧ���Ǹ���
				* һ�µ���һ��if��Ϊûһ��ʵ�ʵ�״̬���б��룬Ȼ��ʹ��case����ֱ���д�����������������⣬
				* �����˵��Եĸ��������ô�����߼��Եø���������ͬʱ�����Ժ���չ���������δ���Ĵ�����ԭ��
				* ����Ĳ��������ڴ˵�Ǹ�ˣ�ûʱ����˰���
				*/
				if(movableObjects[i].canTouch){//����Ƿ���Ӧ����
					if(movableObjects[i].mt.selected){//����Ƿ��Ѿ���ѡ��
						if(timeUp - timeDown < 200){//���δ���������̧���ʱ����С��200΢������Ϊclick
							if(movableObjects[i] instanceof MainMovable){//�жϵ�ǰ��Movableʵ���Ƿ���MainMovableʵ��
								if(movableObjects[i].contains(event.getX(), event.getY())){//����up���¼����������������									
									if(((MainMovable)movableObjects[i]).menuOpen == false){//�˵�δ�򿪣���򿪲˵�
										((MainMovable)movableObjects[i]).menuOpen = true;//���Ʋ˵�
										movableObjects[i].canDrag = false;//ֹͣ�϶�
									}
									else{//�˵��Ѿ��򿪣���رղ˵�
										((MainMovable)movableObjects[i]).menuOpen = false;//��ֹ���Ʋ˵�
										movableObjects[i].canDrag = true;//�����϶�
										movableObjects[i].mt.selected = false;//���δ����¼���up��������ʱ������ٱ�ѡ��										
									}
								}
								else if(event.getX() >= movableObjects[i].currentX + 15 &&//����up���¼������ڲ˵�������
										event.getX() <= movableObjects[i].currentX + 50 &&
										event.getY() >= movableObjects[i].currentY - 45 &&
										event.getY() <= movableObjects[i].currentY + 45){									
									if(event.getY() <= movableObjects[i].currentY - 15){//�������Up��ť
										if(((MainMovable)movableObjects[i]).speedLv < 9){
											((MainMovable)movableObjects[i]).speedLv ++;
										}										
									}
									else if(event.getY() >= movableObjects[i].currentY + 15){//�������down��ť
										if(((MainMovable)movableObjects[i]).speedLv > 1){
											((MainMovable)movableObjects[i]).speedLv --;
										}
									}
									movableObjects[i].firstVX = (float) (directionVX //����X�����ϵ��ٶȷ�������ǰACTION_MOVE��Ӧ����
											* movableObjects[i].speed[movableObjects[i].speedLv-1]);
									movableObjects[i].firstVY = (float) (directionVY //����Y�����ϵ��ٶȷ���
											* movableObjects[i].speed[movableObjects[i].speedLv-1]);
								}
							}
							else{	/*�����ǰ��Movableʵ������MainMovableʵ��
									*TODO:��ǰ������У����ڷ�MainMovableʵ���Ķ���
									*��û����Ӧclick����ƣ���˴˴����ա����Ժ���
									*��ص���ƶ��������ڴ˴���Ӵ���
									*/
							}
						}
						else{//up�¼��ķ�������down�¼��ķ�������200΢���Ϊ��click����
							if(movableObjects[i] instanceof MainMovable){
								if(((MainMovable)movableObjects[i]).menuOpen){
									/* TODO:������У���û�Դ˴����������������
									 * ��ʱ���գ�δ�����������ز���
									 */									
								}
								else{
									movableObjects[i].mt.selected = false;
								}
							}
							else{
								movableObjects[i].mt.selected = false;
							}
						}
					}
				}
			}
			for(Fixed f:fixedObjects){
				if(f == null) continue;
				if(f instanceof gravityField){
					if(((gravityField)f).canTouch
						&& ((gravityField)f).selected){
						if( timeUp - timeDown < 200){
							if(((gravityField)f).contains((int)event.getX(), (int)event.getY())){//����up���¼����������������									
								if(((gravityField)f).menuOpen == false){//�˵�δ�򿪣���򿪲˵�
									((gravityField)f).menuOpen = true;//���Ʋ˵�
									((gravityField)f).canDrag = false;//ֹͣ�϶�
								}
								else{//�˵��Ѿ��򿪣���رղ˵�
									((gravityField)f).menuOpen = false;//��ֹ���Ʋ˵�
									((gravityField)f).canDrag = true;//�����϶�				
									((gravityField)f).selected = false;//���δ����¼���up��������ʱ������ٱ�ѡ��	
								}												
							}
							else if(event.getX() >= ((gravityField)f).startX + 15 &&//����up���¼������ڲ˵�������
									event.getX() <= ((gravityField)f).startX + 50 &&
									event.getY() >= ((gravityField)f).startY - 45 &&
									event.getY() <= ((gravityField)f).startY + 45){									
								if(event.getY() <= ((gravityField)f).startY - 15){//�������Up��ť
									if(((gravityField)f).gravityLv < 9){
										((gravityField)f).gravityLv ++;
									}										
								}
								else if(event.getY() >= ((gravityField)f).startY + 15){//�������down��ť
									if(((gravityField)f).gravityLv > 1){
										((gravityField)f).gravityLv --;
									}
								}
								((gravityField)f).gravityX = (float) (directionaX //����X�����ϵļ��ٶȷ���
										* ((gravityField)f).gravity[((gravityField)f).gravityLv-1]);
								((gravityField)f).gravityY = (float) (directionaY //����Y�����ϵļ��ٶȷ���
										* ((gravityField)f).gravity[((gravityField)f).gravityLv-1]);
							}
						}
						else{//up�¼��ķ�������down�¼��ķ�������200΢���Ϊ��click����
							if(f instanceof gravityField){
								if(((gravityField)f).menuOpen){
									/* TODO:������У���û�Դ˴����������������
									 * ��ʱ���գ�δ�����������ز���
									 */									
								}
								else{
									((gravityField)f).selected = false;
								}
							}
						}
					}
				}
			}
		}
		return true;
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if(!dt.isAlive()){
			dt.start();			
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		dt.run = false;
		dt = null;
	}
}