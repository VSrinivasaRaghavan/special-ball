package com.example.raghavanpc.surfaceview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Random;

public class surface extends AppCompatActivity {
    DisplayMetrics dm=new DisplayMetrics();
    int x=0,y=0;
    int flag=0;
//    ProgressBar pb=(ProgressBar)findViewById(R.id.pb);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        x=dm.widthPixels;
        y=dm.heightPixels;
        surfaceview s=new surfaceview(this,x,y);
        s.setBackgroundColor(Color.WHITE);
        setContentView(s);
        }
    }
class surfaceview extends SurfaceView implements SurfaceHolder.Callback {
    int xpos=10,ypos=10;
    SurfaceHolder sf;
    int x,y;
    int a,b;
    Canvas c;
    Context contx;
    Thread t;
    thread_running tr;
    burst b1;
    int score=0;
    int thread;
    int color[]={Color.BLACK,Color.WHITE,Color.BLUE,Color.GREEN,Color.YELLOW};
    public surfaceview(Context context,int x,int y) {
        super(context);
        contx=context;
        sf=getHolder();
        thread=0;
        a=x;
        b=y;
//        this.x=50;
//        this.y=200;
//        draw(sf.lockCanvas());
        sf.addCallback(this);
        tr=new thread_running(sf,this);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p=new Paint();
        Paint text=new Paint();
        text.setTextSize(48f);
        int col=tr.rand.nextInt(4);
        p.setColor(color[col]);
//        int x1=xpos,y1=ypos;
        double ang;
        if(b1==null&&tr!=null)
        {
            c=canvas;
//            tr.thread_flag=true;
//        canvas.drawText(String.valueOf(xpos),100,100,p);
//        canvas.drawText(String.valueOf(y),100,100,p);
//        canvas.drawText(String.valueOf(ypos),300,300,p);
            canvas.drawText("Score : "+String.valueOf(score),a-300,100,text);
            canvas.drawCircle(tr.xpos,tr.ypos,50,p);
        }
        else if(tr!=null&&!burst.flag)
        {
            c=canvas;
            text.setTextSize(48f);
            //            tr.thread_flag=true;
//        canvas.drawText(String.valueOf(xpos),100,100,p);
//        canvas.drawText(String.valueOf(y),100,100,p);
//        canvas.drawText(String.valueOf(ypos),300,300,p);
            canvas.drawText("Score : "+String.valueOf(score),a-300,100,text);
            canvas.drawCircle(tr.xpos,tr.ypos,50,p);
        }
        if(b1!=null&&burst.flag)
        {
//            canvas.drawText(""+tr.xpos,300,300,p);
//            canvas.drawText(""+x,400,400,p);
            ang=45;
            for(int i=0;i<4;++i) {
                b1.xpos += (int) (b1.dif * Math.cos((22 / 7) / 180.0 * ang));
                b1.ypos+= (int) (b1.dif*Math.sin((22/7)/180.0*ang));
//                canvas.drawText(""+b1.xpos,300,300,p);
                canvas.drawText("Score : "+String.valueOf(score),a-300,100,text);
                canvas.drawCircle(b1.xpos,b1.ypos, b1.cur_rad, p);
                ang+=90;
            }
        }

//        canvas.drawText(""+burst.flag,400,400,p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tr.thread_flag=false;
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int)event.getX();
                y = (int)event.getY();
                if (x>(tr.xpos-50)&&x<(tr.xpos+50))
                    if(y>(tr.ypos-50)&&y<(tr.ypos+50))
                        if(Math.sqrt(Math.pow((tr.xpos-x),2)+Math.pow((tr.ypos-y),2))<=50d) {
                            score += 10;
//                            this.postInvalidate();
                                b1 = new burst(sf, this,tr.xpos,tr.ypos);
                                b1.start();
                            }
                break;
        }
        return true;
//        return super.onTouchEvent(event);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        tr.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
class thread_running extends Thread
{
    SurfaceHolder sh;
    Canvas c;
    surfaceview sv;
    int xpos,ypos;
    Random rand;
    boolean thread_flag;
    thread_running(SurfaceHolder s,surfaceview v)
    {
        sv=v;
        sh=s;
        thread_flag=true;
    }
    public void run()
    {
        while(true)
        {
            c=null;
            try {
                rand=new Random();
                xpos=rand.nextInt(sv.a-50)+50;
                ypos=rand.nextInt(sv.b-50)+50;
                c = sh.lockCanvas(null);
                    sv.draw(c);
//                    sv.postInvalidate();
            }
            catch(Exception e){}
            finally {
                sh.unlockCanvasAndPost(c);
                sv.postInvalidate();
                try {
                        sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
class burst extends Thread
{
    surfaceview sv;
    int xpos,ypos;
    static boolean flag;
    int cur_rad;
    int dif;
    int x1,x2;
    SurfaceHolder surfaceHolder;
    burst(SurfaceHolder h,surfaceview s,int x,int y)
    {
        sv=s;
        surfaceHolder=h;
        xpos=x;
        ypos=y;
        dif=10;
        cur_rad=10;
        flag=true;
        sv.postInvalidate();
    }
    @Override
    public void run() {
//        super.run();
        for(int i=0;i<5;++i)
        {
//            x1+=xpos;
//            x2=(int)((i+1)*10*Math.sin((22/7)/180.0*45))+ypos;
            sv.postInvalidate();
            cur_rad-=2;
            dif+=10;
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        flag=false;
    }
}