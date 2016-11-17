package com.cokus.audiocanvaswave.util;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

import java.io.InputStream;

/**
 * Created by cokus on 2016/9/1.
 */
public class MusicSimilarityUtil
{

	/**
	 * �Ƚ����ļ��Ļ�ȡ���ƶ�
	 * 
	 * @param oriFilePath
	 *            ԭʼ�ļ�·��
	 * @param comFilePath
	 *            �Ƚϵ��ļ�·��
	 * @return ��ʶ��
	 */
	public static float getSimByCompareFile(String oriFilePath , String comFilePath )
	{
		Wave oriWave = new Wave(oriFilePath);
		Wave comWave = new Wave(comFilePath);
		FingerprintSimilarity fps = oriWave.getFingerprintSimilarity(comWave);
		float sim = fps.getSimilarity();
		return sim;
	}

	/**
	 * �Ƚ����ļ��Ļ�ȡ���ƶ�
	 * 
	 * @param oriInputStream
	 *            ԭʼ�ļ�·��
	 * @param oriInputStream
	 *            �Ƚϵ��ļ�·��
	 * @return ��ʶ��
	 */
	public static float getSimByCompareFile(InputStream oriInputStream , InputStream comInputStream )
	{
		Wave oriWave = new Wave(oriInputStream);
		Wave comWave = new Wave(comInputStream);
		FingerprintSimilarity fps = oriWave.getFingerprintSimilarity(comWave);
		float sim = fps.getSimilarity();
		return sim;
	}

	/**
	 * ��ȡ��Ƶ�ͱ�׼�ļ��ĵ÷�
	 * 
	 * @param oriFilePath
	 *            ԭʼ�ļ�·��
	 * @param comFilePath
	 *            �Ƚϵ��ļ�·��
	 * @return �÷�
	 */
	public static float getScoreByCompareFile(String oriFilePath , String comFilePath )
	{
		Wave oriWave = new Wave(oriFilePath);
		Wave comWave = new Wave(comFilePath);
		FingerprintSimilarity fps = oriWave.getFingerprintSimilarity(comWave);
		float score = fps.getScore();
		return score;
	}

	/**
	 * ��ȡ��Ƶ�ͱ�׼�ļ��ĵ÷�
	 * 
	 * @param oriInputStream
	 *            ԭʼ�ļ�·��
	 * @param comInputStream
	 *            �Ƚϵ��ļ�·��
	 * @return �÷�
	 */
	public static float getScoreByCompareFile(InputStream oriInputStream , InputStream comInputStream )
	{
		Wave oriWave = new Wave(oriInputStream);
		Wave comWave = new Wave(comInputStream);
		FingerprintSimilarity fps = oriWave.getFingerprintSimilarity(comWave);
		float score = fps.getScore();
		return score;
	}

}
