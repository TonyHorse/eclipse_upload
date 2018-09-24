package ChessBase;

import java.awt.Point;

public class StepInfo {
	public StepInfo(){ // Ĭ��ʲô������
		this.pieceID = -1;
		this.OriPos = new Point(-1, -1);
		this.NowPos = new Point(-1, -1);
	};
	// 
	public StepInfo(int id, Point oriPos, Point nowPos){
		this.pieceID = id;
		this.OriPos = oriPos;
		this.NowPos = nowPos;
	};


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	/**
	 * pieceID:��Χ0~32
	 * Ĭ��Ϊ-1
	 * **/
	public int pieceID;

	/*
	 * ����Դλ���뵱ǰλ�ã���ô��Ƶ�ԭ��������
	 * 	1�����ڻ�ȡ��Ҫ�ı�������������е�λ�ö�����ȥ����
	 * 	2������Ҫ��ԭ�������ڻ���ʱ����ֱ��ȷ������β���
	 */
	/**
	 * pieceOriPos����������֮ǰ��λ��
	 * Ĭ��ΪPoint(-1, -1);
	 * **/
	public Point OriPos;

	/**
	 * pieceNowPos����������֮���λ��
	 * Ĭ��ΪPoint(-1, -1)
	 * **/
	public Point NowPos;
}
