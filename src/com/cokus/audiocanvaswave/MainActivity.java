package com.cokus.audiocanvaswave;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cokus.audiocanvaswave.util.MusicSimilarityUtil;
import com.cokus.audiocanvaswave.util.U;
import com.cokus.wavelibrary.draw.WaveCanvas;
import com.cokus.wavelibrary.utils.SamplePlayer;
import com.cokus.wavelibrary.utils.SoundFile;
import com.cokus.wavelibrary.view.WaveSurfaceView;
import com.cokus.wavelibrary.view.WaveformView;

public class MainActivity extends Activity
{

	private static final int FREQUENCY = 16000;// ������Ƶ�����ʣ�44100��Ŀǰ�ı�׼������ĳЩ�豸��Ȼ֧��22050��16000��11025
	private static final int CHANNELCONGIFIGURATION = AudioFormat.CHANNEL_IN_MONO;// ���õ���������
	private static final int AUDIOENCODING = AudioFormat.ENCODING_PCM_16BIT;// ��Ƶ���ݸ�ʽ��ÿ������16λ
	public final static int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;// ��Ƶ��ȡԴ
	private int recBufSize;// ¼����Сbuffer��С
	private AudioRecord audioRecord;
	private WaveCanvas waveCanvas;
	private String mFileName = "wgcwgc";// �ļ���

	WaveSurfaceView waveSfv;
	private Button switchBtn;
	TextView status;
	WaveformView waveView;
	Button playBtn;
	Button scoreBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState )
	{
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		setContentView(R.layout.activity_main);

		waveSfv = (WaveSurfaceView) findViewById(R.id.wavesfv);
		switchBtn = (Button) findViewById(R.id.switchbtn);
		status = (TextView) findViewById(R.id.status);
		waveView = (WaveformView) findViewById(R.id.waveview);
		playBtn = (Button) findViewById(R.id.play);
		scoreBtn = (Button) findViewById(R.id.socreaudio);

		U.createDirectory();
		if(waveSfv != null)
		{
			waveSfv.setLine_off(42);
			// ���surfaceView��ɫ����Ч��
			waveSfv.setZOrderOnTop(true);
			waveSfv.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		}
		waveView.setLine_offset(42);

		switchBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				if(waveCanvas == null || !waveCanvas.isRecording)
				{
					status.setText("¼����...");
					switchBtn.setText("ֹͣ¼��");
					waveSfv.setVisibility(View.VISIBLE);
					waveView.setVisibility(View.INVISIBLE);
					initAudio();
				}
				else
				{
					status.setText("ֹͣ¼��");
					switchBtn.setText("��ʼ¼��");
					waveCanvas.Stop();
					waveCanvas = null;
					initWaveView();
				}
			}
		});

		playBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				onPlay(0);
			}
		});

		scoreBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v )
			{
				float sim = 0;
				try
				{
					sim = MusicSimilarityUtil.getScoreByCompareFile(U.DATA_DIRECTORY + mFileName + ".wav" ,U.DATA_DIRECTORY + mFileName + ".wav");
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				Toast.makeText(MainActivity.this ,sim + "" ,Toast.LENGTH_LONG).show();
			}
		});
	}


	private void initWaveView()
	{
		loadFromFile();
	}

	File mFile;
	Thread mLoadSoundFileThread;
	SoundFile mSoundFile;
	boolean mLoadingKeepGoing;
	SamplePlayer mPlayer;

	/**
	 * ����wav�ļ���ʾ����
	 */
	private void loadFromFile()
	{
		try
		{
			Thread.sleep(300);// ���ļ�д����ɺ������벨�� �ʵ���������
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
		}
		mFile = new File(U.DATA_DIRECTORY + mFileName + ".wav");
		mLoadingKeepGoing = true;
		// Load the sound file in a background thread
		mLoadSoundFileThread = new Thread()
		{
			public void run()
			{
				try
				{
					mSoundFile = SoundFile.create(mFile.getAbsolutePath() ,null);
					if(mSoundFile == null)
					{
						return;
					}
					mPlayer = new SamplePlayer(mSoundFile);
				}
				catch(final Exception e)
				{
					e.printStackTrace();
					return;
				}
				if(mLoadingKeepGoing)
				{
					Runnable runnable = new Runnable()
					{
						public void run()
						{
							finishOpeningSoundFile();
							waveSfv.setVisibility(View.INVISIBLE);
							waveView.setVisibility(View.VISIBLE);
						}
					};
					MainActivity.this.runOnUiThread(runnable);
				}
			}
		};
		mLoadSoundFileThread.start();
	}

	float mDensity;

	/**
	 * waveview���벨�����
	 */
	private void finishOpeningSoundFile()
	{
		waveView.setSoundFile(mSoundFile);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mDensity = metrics.density;
		waveView.recomputeHeights(mDensity);
	}

	private void initAudio()
	{
		recBufSize = AudioRecord.getMinBufferSize(FREQUENCY ,CHANNELCONGIFIGURATION ,AUDIOENCODING);// ¼�����
		audioRecord = new AudioRecord(AUDIO_SOURCE ,// ָ����Ƶ��Դ������Ϊ��˷�
		FREQUENCY , // 16000HZ����Ƶ��
		CHANNELCONGIFIGURATION ,// ¼��ͨ��
		AUDIO_SOURCE ,// ¼�Ʊ����ʽ
		recBufSize);// ¼�ƻ�������С //���޸�
		waveCanvas = new WaveCanvas();
		waveCanvas.baseLine = waveSfv.getHeight() / 2;
		waveCanvas.Start(audioRecord ,recBufSize ,waveSfv ,mFileName ,U.DATA_DIRECTORY ,new Handler.Callback()
		{
			@Override
			public boolean handleMessage(Message msg )
			{
				return true;
			}
		});

	}

	private int mPlayStartMsec;
	private int mPlayEndMsec;
	private final int UPDATE_WAV = 100;

	/**
	 * ������Ƶ��@param startPosition ��ʼ���ŵ�ʱ��
	 */
	private synchronized void onPlay(int startPosition )
	{
		if(mPlayer == null)
			return;
		if(mPlayer != null && mPlayer.isPlaying())
		{
			mPlayer.pause();
			updateTime.removeMessages(UPDATE_WAV);
		}
		mPlayStartMsec = waveView.pixelsToMillisecs(startPosition);
		mPlayEndMsec = waveView.pixelsToMillisecsTotal();
		mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener()
		{
			@Override
			public void onCompletion()
			{
				waveView.setPlayback( -1);
				updateDisplay();
				updateTime.removeMessages(UPDATE_WAV);
				Toast.makeText(getApplicationContext() ,"�������" ,Toast.LENGTH_LONG).show();
			}
		});
		mPlayer.seekTo(mPlayStartMsec);
		mPlayer.start();
		Message msg = new Message();
		msg.what = UPDATE_WAV;
		updateTime.sendMessage(msg);
	}

	Handler updateTime = new Handler()
	{
		public void handleMessage(Message msg )
		{
			updateDisplay();
			updateTime.sendMessageDelayed(new Message() ,10);
		}

		;
	};

	/**
	 * ����upd ateview �еĲ��Ž���
	 */
	private void updateDisplay()
	{
		int now = mPlayer.getCurrentPosition();// nullpointer
		int frames = waveView.millisecsToPixels(now);
		waveView.setPlayback(frames);// ͨ��������µ�ǰ���ŵ�λ��
		if(now >= mPlayEndMsec)
		{
			waveView.setPlayFinish(1);
			if(mPlayer != null && mPlayer.isPlaying())
			{
				mPlayer.pause();
				updateTime.removeMessages(UPDATE_WAV);
			}
		}
		else
		{
			waveView.setPlayFinish(0);
		}
		waveView.invalidate();// ˢ�������ͼ
	}

}
