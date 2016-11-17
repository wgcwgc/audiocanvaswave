package com.cokus.wavelibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * ����ֻ��һ����ʼ��surfaceview�ķ�װ
 * 
 * @author cokus
 */
public class WaveSurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
	private SurfaceHolder holder;
	private int line_off;// ���±߾����

	public int getLine_off()
	{
		return line_off;
	}

	public void setLine_off(int line_off )
	{
		this.line_off = line_off;
	}

	public WaveSurfaceView(Context context , AttributeSet attrs)
	{
		super(context , attrs);
		// TODO Auto-generated constructor stub
		this.holder = getHolder();
		holder.addCallback(this);

	}

	/**
	 * @author cokus init surfaceview
	 */
	public void initSurfaceView(final SurfaceView sfv )
	{
		new Thread()
		{
			public void run()
			{
				Canvas canvas = sfv.getHolder().lockCanvas(new Rect(0 , 0 , sfv.getWidth() , sfv.getHeight()));// �ؼ�:��ȡ����
				if(canvas == null)
				{
					return;
				}
				// canvas.drawColor(Color.rgb(241, 241, 241));// �������
				canvas.drawARGB(255 ,239 ,239 ,239);

				int height = sfv.getHeight() - line_off;
				Paint paintLine = new Paint();
				Paint centerLine = new Paint();
				Paint circlePaint = new Paint();
				circlePaint.setColor(Color.rgb(246 ,131 ,126));
				circlePaint.setAntiAlias(true);

				canvas.drawCircle(0 ,line_off / 4 ,line_off / 4 ,circlePaint);// ����СԲ
				canvas.drawCircle(0 ,sfv.getHeight() - line_off / 4 ,line_off / 4 ,circlePaint);// ����СԲ
				canvas.drawLine(0 ,0 ,0 ,sfv.getHeight() ,circlePaint);// ��ֱ����
				paintLine.setColor(Color.rgb(169 ,169 ,169));
				centerLine.setColor(Color.rgb(39 ,199 ,175));
				canvas.drawLine(0 ,line_off / 2 ,sfv.getWidth() ,line_off / 2 ,paintLine);// ��������Ǹ���
				canvas.drawLine(0 ,sfv.getHeight() - line_off / 2 - 1 ,sfv.getWidth() ,sfv.getHeight() - line_off / 2 - 1 ,paintLine);// ��������Ǹ���
				// canvas.drawLine(0, height*0.25f+20,
				// sfv.getWidth(),height*0.25f+20, paintLine);//�ڶ�����
				// canvas.drawLine(0, height*0.75f+20,
				// sfv.getWidth(),height*0.75f+20, paintLine);//��3����
				canvas.drawLine(0 ,height * 0.5f + line_off / 2 ,sfv.getWidth() ,height * 0.5f + line_off / 2 ,centerLine);// ������
				sfv.getHolder().unlockCanvasAndPost(canvas);// �����������ύ���õ�ͼ��
			}

			;
		}.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder , int format , int width , int height )
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder )
	{
		// TODO Auto-generated method stub
		initSurfaceView(this);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder )
	{

	}

}
