package com.hans.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
 
public class Game extends View {
 
    private Cell[][] singlesquare = null;
    int x = 3;
    int y = 3;
    private int l;
    private int a;
    private int playerwin = 3;
    private Paint caneta;
    private int mode = 1;//single player
    private boolean userPlaying = true;
    public XY computerMove;
    public XY[] winThree = new XY[3];
    boolean isFinished = false;
    public String level = "Medium";
    public int widthOfWindow;
	boolean androidStartedLast = true;
	boolean flag = true;
	boolean tripFinished = true;
	private MediaPlayer touchPlayer;
	private MediaPlayer finishPlayer;
	public TextView fireScore;
	public TextView ieScore;
	int fireScoreVal = 0;
	int ieScoreVal = 0;
	
    Handler handler = new Handler() {
        // @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 0:
                invalidate();
                break;
            case 1:
                Toast.makeText(getContext(), "You Win!", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(), "Computer Win!", Toast.LENGTH_SHORT).show();
                break;
            case 3:
            	showAlert(false);
                if(androidStartedLast)
	        		androidStartedLast = false;
	        	else
	        		androidStartedLast = true;
                break;
            case 4:
            {
            	(new Thread(new Runnable() {
					
					@Override
					public void run() {
						if(getMode() == 1)
		                {
		                	try {
		        				Thread.sleep(600);
		        			} catch (InterruptedException e) {
		        				// TODO Auto-generated catch block
		        				e.printStackTrace();
		        			}
		                	
		                	drawimage(computerMove.x, computerMove.y);
		                }
					}
				})).start();
            }
            break;
            case 5:
            {
	        	for(XY xy : winThree)
				{
					if(singlesquare[xy.y][xy.x] instanceof Ball)
					{
						singlesquare[xy.y][xy.x] = new Ball(xy.x, xy.y, false);
					}
					else
						singlesquare[xy.y][xy.x] = new Cross(xy.x, xy.y, false);
				}
	        	isFinished = true;
	        	if(androidStartedLast)
	        		androidStartedLast = false;
	        	else
	        		androidStartedLast = true;
	        	
				invalidate();
				showAlert(true);
            }
            break;
            default:
                break;
            }
 
            super.handleMessage(msg);
        }
    }; 
    
    public int getGameSize() {
        return x;
    }

    Dialog dialog;
    public void showAlert(boolean noDraw)
    {
    	if(!noDraw)
    		touchPlayer = MediaPlayer.create(this.getContext(), R.raw.draw);
    	else
    		touchPlayer = MediaPlayer.create(this.getContext(), R.raw.bbm);

        touchPlayer.start();
        
    	dialog = new Dialog(this.getContext()){
		  @Override
		  public boolean onTouchEvent(MotionEvent event) {
		    // Tap anywhere to close dialog.
			  reset();
			  this.dismiss();
			  return true;
		  }
		};

    	if(!noDraw)
    			dialog.setTitle(Html.fromHtml("<small><tt>Draw!</tt> &nbsp;&nbsp;&nbsp;<u><font color='red'>New Game!</font></u></small>"));
    	else
    	{
	    	if(flag)
	    	{
	    		dialog.setTitle(Html.fromHtml("<small><tt>Firfox Won!</tt> &nbsp;&nbsp;&nbsp;<u><font color='red'>New Game!</font></u></small>"));
	    		fireScore.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>Firefox</font></u><br/><br/>"+(++fireScoreVal)+"</small></p>"), TextView.BufferType.SPANNABLE);
	    	}
	    	else
	    	{
	    		dialog.setTitle(Html.fromHtml("<small><tt>IE Won!</tt> &nbsp;&nbsp;&nbsp;<u><font color='red'>New Game!</font></u></small>"));
	    		ieScore.setText(Html.fromHtml("<p align=center><small><u><font color='blue'>IE</font></u><br/><br/>"+(++ieScoreVal)+"</small></p>"), TextView.BufferType.SPANNABLE);
	    	}
    	}

    	dialog.setCancelable(false);
    	
    	WindowManager.LayoutParams params = dialog.getWindow().getAttributes(); // change this to your dialog.
		params.y = 200; // Here is the param to set your dialog position. Same with params.x
		dialog.getWindow().setAttributes(params);
		
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.show();
		if(!noDraw)
    	{
	    	dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
    	}
    	else
    	{
	    	if(flag)
	        	dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.fire1);
	    	else
	        	dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ie1);
    	}
    }
    
    public Game(Context context, AttributeSet attrs)
    {
    	super(context, attrs);
    	init1();
    }
    
    public Game(Context context) {
        super(context);
        init1();
    }

   public Game(Context context, AttributeSet attrs,
            int defStyle)
    {
        super(context, attrs, defStyle);
        init1();
    }
    
    public void init1(){
        caneta = new Paint();
 
        this.caneta.setARGB(255,0,115,0);
        this.caneta.setAntiAlias(true);
        this.caneta.setStyle(Style.STROKE);
        this.caneta.setStrokeWidth(2);
 
        l = this.getWidth();
        a = this.getHeight();
 
        singlesquare = new Cell[x][y];
 
        int xss = l / x;
        int yss = a / y;
 
        for (int z = 0; z < y; z++) {
            for (int i = 0; i < x; i++) {
                singlesquare[z][i] = new Empty(xss * i, z * yss);
            }
        }
        
        computerMove = new XY();
        winThree[0] = new XY();
        winThree[1] = new XY();
        winThree[2] = new XY();
    }
 
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < singlesquare.length; i++) {
            for (int j = 0; j < singlesquare[0].length; j++) {
                singlesquare[i][j].draw(canvas, getResources(), j, i, (this
                        .getWidth() + 3)
                        / singlesquare.length, this.getHeight()
                        / singlesquare[0].length);
            }
        }
 
        int xs = this.getWidth() / x;
        int ys = this.getHeight() / y;
        for (int i = 0; i <= x; i++) {
            canvas.drawLine(xs * i, 0, xs * i, this.getHeight(), caneta);
        }
        for (int i = 0; i <= y; i++) {
            canvas.drawLine(0, ys * i, this.getWidth(), ys * i, caneta);
        }
 
        super.onDraw(canvas);
    }
 
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x_aux = (int) (event.getX() / (this.getWidth() / x));
        int y_aux = (int) (event.getY() / (this.getHeight() / y));
       	
        if(!isFinished && mode == 1 && tripFinished)
        {
        	userPlaying = true;
        	drawimage(x_aux, y_aux);	
        }
        	
        if(!isFinished && mode == 2)
        	drawimage(x_aux, y_aux);
        
        return super.onTouchEvent(event);
    }
 
    public String getPiece(int player) {
        switch (player) {
        case 1:
            return "x";
        case -1:
            return "o";
        }
        return null;
    }
 
    public synchronized void drawimage(int x_aux, int y_aux) {
        Cell cel = singlesquare[y_aux][x_aux];
        boolean isCross = true;
        if(cel instanceof Ball || cel instanceof Cross)
        	return;
        	
        touchPlayer = MediaPlayer.create(this.getContext(), R.raw.switch1);
        touchPlayer.start();
        if ((!userPlaying && mode == 1) || flag && mode == 2) 
        {
            cel = new Cross(singlesquare[x_aux][y_aux].x,singlesquare[x_aux][y_aux].y);
            flag = false;
        }
        else if((userPlaying && mode == 1) || !flag && mode == 2)
        {
        	isCross = false;
            cel = new Ball(singlesquare[x_aux][y_aux].x, singlesquare[x_aux][y_aux].y);
            flag = true;
            tripFinished = false;
        }
 
        singlesquare[y_aux][x_aux] = cel;
 
        handler.sendMessage(Message.obtain(handler, 0));
 
        boolean done = false;
        boolean isFull = false;
        if(level.equals("Simple"))
        	done = validate_game1(isCross);
        else if(level.equals("Medium"))
        	done = validate_game2(isCross);
        else
        	done = validate_game3(isCross);
        
        if (!done){
        	isFull = isFull();
        	if(isFull)
        		handler.sendMessage(Message.obtain(handler, 3));
       	}
        
	    if(!done && !isFull)
	    {
        	if(getMode() == 1)
        	{
	        	if(userPlaying)
	        	{
	        		userPlaying = false;
	        		handler.sendMessage(Message.obtain(handler,4));
	        	}
	        	else
	        	{
	        		userPlaying = true;
	        		tripFinished = true;
	        	}
        	}
        }
    }

    private boolean validate_game1(boolean isCross) {
        int countCross = 0;
        int countBall = 0;

        Cell cell = null;
        List<XY> defendList = new ArrayList<XY>();
        
        for (int i = 0; i < singlesquare.length; i++) {
        	countCross = 0;
            countBall = 0;
            for (int j = 0; j < singlesquare[0].length; j++) {
            	cell = singlesquare[i][j];
            	if(cell instanceof Empty)
            		defendList.add(new XY(j, i));
            	else if(cell instanceof Ball)
            		countBall++;
            	else if(cell instanceof Cross)
            		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = 0;winThree[0].y = i;
            	winThree[1].x = 1;winThree[1].y = i;
            	winThree[2].x = 2;winThree[2].y = i;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0))
            {
            	XY p = defendList.get(defendList.size() - 1);
            	boostPoint(p, defendList);
            }
        }
        
        for (int j = 0; j < singlesquare[0].length; j++) {
        	countCross = 0;
            countBall = 0;
            for (int i = 0; i < singlesquare.length; i++) {
            	cell = singlesquare[i][j];
            	if(cell instanceof Empty)
            		defendList.add(new XY(j, i));
            	else if(cell instanceof Ball)
            		countBall++;
            	else if(cell instanceof Cross)
            		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = j;winThree[0].y = 0;
            	winThree[1].x = j;winThree[1].y = 1;
            	winThree[2].x = j;winThree[2].y = 2;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0))
            {
            	XY p = defendList.get(defendList.size() - 1);
            	boostPoint(p, defendList);
            }
        }
        
        countCross = 0;
        countBall = 0;
        for (int j = singlesquare[0].length - 1; j >= 0; j--) {
            cell = singlesquare[j][j];
        	if(cell instanceof Empty)
        		defendList.add(new XY(j, j));
        	else if(cell instanceof Ball)
        		countBall++;
        	else if(cell instanceof Cross)
        		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = 0;winThree[0].y = 0;
            	winThree[1].x = 1;winThree[1].y = 1;
            	winThree[2].x = 2;winThree[2].y = 2;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0))
            {
            	XY p = defendList.get(defendList.size() - 1);
            	boostPoint(p, defendList);
            }
        
    	countCross = 0;
        countBall = 0;
        int yau = singlesquare.length - 1;
    	for (int j = 0; j < singlesquare[0].length; j++) {
        	cell = singlesquare[yau][j];
        	if(cell instanceof Empty)
        		defendList.add(new XY(j, yau));
        	else if(cell instanceof Ball)
        		countBall++;
        	else if(cell instanceof Cross)
        		countCross++;
            yau--;
        }
        if(countBall == 3 || countCross == 3)
        {
        	winThree[0].x = 0;winThree[0].y = 2;
        	winThree[1].x = 1;winThree[1].y = 1;
        	winThree[2].x = 2;winThree[2].y = 0;
        	handler.sendMessage(Message.obtain(handler, 5));
        	return true;
        }
        if((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0))
        {
        	XY p = defendList.get(defendList.size() - 1);
        	boostPoint(p, defendList);
        }
    	if(defendList.size() > 0)
		{
			Random randomGenerator = new Random();
    		if(defendList.size() >0){
		        int indx = randomGenerator.nextInt(defendList.size());
		        computerMove.x = defendList.get(indx).x;
	    		computerMove.y = defendList.get(indx).y;
    		}
		}
    	
        return false;
    }
 
    private void boostPoint(XY p, List<XY> defendList) {
    	for(int i = 0 ; i < 10 ; i ++)
    	{
    		XY tmp = new XY(p.x, p.y);
    		defendList.add(tmp);
    	}
	}

	private boolean validate_game2(boolean isCross) {
        int countCross = 0;
        int countBall = 0;
        
        int attackX = -1;
        int attackY = -1;
        
        Cell cell = null;
        boolean foundBetterDefence = false;
        List<XY> defendList = new ArrayList<XY>();
        
        for (int i = 0; i < singlesquare.length; i++) {
        	countCross = 0;
            countBall = 0;
            for (int j = 0; j < singlesquare[0].length; j++) {
            	cell = singlesquare[i][j];
            	if(cell instanceof Empty)
            		defendList.add(new XY(j, i));
            	else if(cell instanceof Ball)
            		countBall++;
            	else if(cell instanceof Cross)
            		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = 0;winThree[0].y = i;
            	winThree[1].x = 1;winThree[1].y = i;
            	winThree[2].x = 2;winThree[2].y = i;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if((countBall == 2 && countCross == 0 && !isCross) || (countCross == 2 && countBall == 0 && isCross))
            {
            	attackX = defendList.get(defendList.size() - 1).x;
        		attackY = defendList.get(defendList.size() - 1).y;
        		foundBetterDefence = true;
            }
        }
        
        for (int j = 0; j < singlesquare[0].length; j++) {
        	countCross = 0;
            countBall = 0;
            for (int i = 0; i < singlesquare.length; i++) {
            	cell = singlesquare[i][j];
            	if(cell instanceof Empty)
            		defendList.add(new XY(j, i));
            	else if(cell instanceof Ball)
            		countBall++;
            	else if(cell instanceof Cross)
            		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = j;winThree[0].y = 0;
            	winThree[1].x = j;winThree[1].y = 1;
            	winThree[2].x = j;winThree[2].y = 2;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if(!foundBetterDefence && ((countBall == 2 && countCross == 0 && !isCross) || (countCross == 2 && countBall == 0 && isCross)))
            {
            	attackX = defendList.get(defendList.size() - 1).x;
        		attackY = defendList.get(defendList.size() - 1).y;
        		foundBetterDefence = true;
            }
        }
        
        countCross = 0;
        countBall = 0;
        for (int j = singlesquare[0].length - 1; j >= 0; j--) {
            cell = singlesquare[j][j];
        	if(cell instanceof Empty)
        		defendList.add(new XY(j, j));
        	else if(cell instanceof Ball)
        		countBall++;
        	else if(cell instanceof Cross)
        		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = 0;winThree[0].y = 0;
            	winThree[1].x = 1;winThree[1].y = 1;
            	winThree[2].x = 2;winThree[2].y = 2;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if(!foundBetterDefence && ((countBall == 2 && countCross == 0 && !isCross) || (countCross == 2 && countBall == 0 && isCross)))
            {
            	attackX = defendList.get(defendList.size() - 1).x;
        		attackY = defendList.get(defendList.size() - 1).y;
        		foundBetterDefence = true;
            }
        
    	countCross = 0;
        countBall = 0;
        int yau = singlesquare.length - 1;
    	for (int j = 0; j < singlesquare[0].length; j++) {
        	cell = singlesquare[yau][j];
        	if(cell instanceof Empty)
        		defendList.add(new XY(j, yau));
        	else if(cell instanceof Ball)
        		countBall++;
        	else if(cell instanceof Cross)
        		countCross++;
            yau--;
        }
        if(countBall == 3 || countCross == 3)
        {
        	winThree[0].x = 0;winThree[0].y = 2;
        	winThree[1].x = 1;winThree[1].y = 1;
        	winThree[2].x = 2;winThree[2].y = 0;
        	handler.sendMessage(Message.obtain(handler, 5));
        	return true;
        }
        if(!foundBetterDefence && ((countBall == 2 && countCross == 0 && !isCross) || (countCross == 2 && countBall == 0 && isCross)))
        {
        	attackX = defendList.get(defendList.size() - 1).x;
    		attackY = defendList.get(defendList.size() - 1).y;
    		foundBetterDefence = true;
        }
        
    	if(attackX != -1)
    	{
    		computerMove.x = attackX;
    		computerMove.y = attackY;
    	}
    	else
    	{
    		if(defendList.size() > 0)
    		{
    			Random randomGenerator = new Random();
	    		if(defendList.size() >0){
			        int indx = randomGenerator.nextInt(defendList.size());
			        computerMove.x = defendList.get(indx).x;
		    		computerMove.y = defendList.get(indx).y;
	    		}
    		}
    	}
    	
        return false;
    }
 
    private boolean validate_game3(boolean isCross) {
        int countCross = 0;
        int countBall = 0;
        
        int attackX = -1;
        int attackY = -1;
        
        Cell cell = null;
        boolean foundAttck = false;
        List<XY> defendList = new ArrayList<XY>();
        int listIndex = -1;
        int finalIndex = -1;

        countCross = 0;
        countBall = 0;
        for (int j = singlesquare[0].length - 1; j >= 0; j--) {
            cell = singlesquare[j][j];
        	if(cell instanceof Empty)
        	{
        		defendList.add(new XY(j, j));
        		listIndex++;
        	}
        	else if(cell instanceof Ball)
        		countBall++;
        	else if(cell instanceof Cross)
        		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = 0;winThree[0].y = 0;
            	winThree[1].x = 1;winThree[1].y = 1;
            	winThree[2].x = 2;winThree[2].y = 2;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if(!foundAttck && ((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0)))
            {
            	attackX = defendList.get(defendList.size() - 1).x;
        		attackY = defendList.get(defendList.size() - 1).y;
            }
            
        	if((isCross && countBall == 2 && countCross == 0) || (!isCross && countCross == 2 && countBall == 0))
        		foundAttck = true;
        	if((!isCross && countBall == 0 && countCross == 1) || (isCross && countCross == 0 && countBall == 1))
        		finalIndex = listIndex;
        
    	countCross = 0;
        countBall = 0;
        int yau = singlesquare.length - 1;
    	for (int j = 0; j < singlesquare[0].length; j++) {
        	cell = singlesquare[yau][j];
        	if(cell instanceof Empty)
        	{
        		defendList.add(new XY(j, yau));
        		listIndex ++;
        	}
        	else if(cell instanceof Ball)
        		countBall++;
        	else if(cell instanceof Cross)
        		countCross++;
            yau--;
        }
        if(countBall == 3 || countCross == 3)
        {
        	winThree[0].x = 0;winThree[0].y = 2;
        	winThree[1].x = 1;winThree[1].y = 1;
        	winThree[2].x = 2;winThree[2].y = 0;
        	handler.sendMessage(Message.obtain(handler, 5));
        	return true;
        }
        if(!foundAttck && ((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0)))
        {
        	attackX = defendList.get(defendList.size() - 1).x;
    		attackY = defendList.get(defendList.size() - 1).y;
        }
        
    	if((isCross && countBall == 2 && countCross == 0) || (!isCross && countCross == 2 && countBall == 0))
    		foundAttck = true;
    	if((!isCross && countBall == 0 && countCross == 1) || (isCross && countCross == 0 && countBall == 1))
    		finalIndex = listIndex;
        
        for (int i = 0; i < singlesquare.length; i++) {
        	countCross = 0;
            countBall = 0;
            for (int j = 0; j < singlesquare[0].length; j++) {
            	cell = singlesquare[i][j];
            	if(cell instanceof Empty)
            	{
            		defendList.add(new XY(j, i));
            		listIndex++;
            	}
            	else if(cell instanceof Ball)
            		countBall++;
            	else if(cell instanceof Cross)
            		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = 0;winThree[0].y = i;
            	winThree[1].x = 1;winThree[1].y = i;
            	winThree[2].x = 2;winThree[2].y = i;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if(!foundAttck && ((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0)))
            {
            	attackX = defendList.get(defendList.size() - 1).x;
        		attackY = defendList.get(defendList.size() - 1).y;
            }
            
        	if((isCross && countBall == 2 && countCross == 0) || (!isCross && countCross == 2 && countBall == 0))
        		foundAttck = true;
        	if((!isCross && countBall == 0 && countCross == 1) || (isCross && countCross == 0 && countBall == 1))
        		finalIndex = listIndex;
        }
        
        for (int j = 0; j < singlesquare[0].length; j++) {
        	countCross = 0;
            countBall = 0;
            for (int i = 0; i < singlesquare.length; i++) {
            	cell = singlesquare[i][j];
            	if(cell instanceof Empty)
            	{
            		defendList.add(new XY(j, i));
            		listIndex ++;
            	}
            	else if(cell instanceof Ball)
            		countBall++;
            	else if(cell instanceof Cross)
            		countCross++;
            }
            if(countBall == 3 || countCross == 3)
            {
            	winThree[0].x = j;winThree[0].y = 0;
            	winThree[1].x = j;winThree[1].y = 1;
            	winThree[2].x = j;winThree[2].y = 2;
            	handler.sendMessage(Message.obtain(handler, 5));
            	return true;
            }
            if(!foundAttck && ((countBall == 2 && countCross == 0) || (countCross == 2 && countBall == 0)))
            {
            	attackX = defendList.get(defendList.size() - 1).x;
        		attackY = defendList.get(defendList.size() - 1).y;
            }
            
        	if((isCross && countBall == 2 && countCross == 0) || (!isCross && countCross == 2 && countBall == 0))
        		foundAttck = true;
        	if((!isCross && countBall == 0 && countCross == 1) || (isCross && countCross == 0 && countBall == 1))
        		finalIndex = listIndex;
        }
        
    	if(attackX != -1)
    	{
    		computerMove.x = attackX;
    		computerMove.y = attackY;
    	}
    	else
    	{
    		if(finalIndex != -1)
    		{
    			computerMove.x = defendList.get(finalIndex).x;
 	    		computerMove.y = defendList.get(finalIndex).y;
    		}
    		else 
    		{
	    		Random randomGenerator = new Random();
	    		if(defendList.size() >0){
			        int indx = randomGenerator.nextInt(defendList.size());
			        computerMove.x = defendList.get(indx).x;
		    		computerMove.y = defendList.get(indx).y;
	    		}
    		}
    	}
    	
        return false;
    }
    public boolean isFull() {
        for (int i = 0; i < singlesquare.length; i++) {
            for (int j = 0; j < singlesquare[0].length; j++) {
                if (singlesquare[i][j] instanceof Empty) {
                    return false;
                }
            }
        }
        return true;
    }
 
    public void resizegame(int s) {
        x = s;
        y = s;
 
        singlesquare = new Cell[x][y];
 
        int xss = l / x;
        int yss = a / y;
 
        for (int z = 0; z < y; z++) {
            for (int i = 0; i < x; i++) {
                singlesquare[z][i] = new Empty(xss * i, z * yss);
            }
        }
    	handler.sendMessage(Message.obtain(handler, 0));

        if(userPlaying && getMode() == 1)
        {
        	userPlaying = false;
			Random randomGenerator = new Random();		        
	        computerMove.x = randomGenerator.nextInt(3);
	        computerMove.y = randomGenerator.nextInt(3);
	        handler.sendMessage(Message.obtain(handler, 4));
        }
    }
     
    public void reset() {
        for (int z = 0; z < singlesquare.length; z++) {
            for (int i = 0; i < singlesquare[0].length; i++) {
                singlesquare[z][i] = new Empty(i, z);
            }
        }
        
        isFinished = false;
        tripFinished = true;
        handler.sendMessage(Message.obtain(handler, 0));
        if(!androidStartedLast)
    	{
            tripFinished = false;
    		userPlaying = false;
    		if(level.equals("Hard"))
    		{
    			Random random = new Random();
    			if(random.nextBoolean())
    			{
    				computerMove.x = 1;
    				computerMove.y = 1;
    			}
    			else
    			{
    				computerMove.x = 0;
    				computerMove.y = 0;
    			}
    		}
    		
    		handler.sendMessage(Message.obtain(handler,4));
    	}
    }
    
    public int getPlayerwin() {
        return playerwin;
    }
    
    public void setMode(int mode)
    {
    	this.mode = mode;
    }
    
    public int getMode()
    {
    	return mode;
    }
}

class XY
{
	int x;
	int y;
	XY()
	{}
	XY(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
}