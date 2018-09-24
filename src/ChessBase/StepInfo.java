package ChessBase;

import java.awt.Point;

public class StepInfo {
	public StepInfo(){ // 默认什么都不是
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
	 * 变量信息
	 **************************************************/
	/**
	 * pieceID:范围0~32
	 * 默认为-1
	 * **/
	public int pieceID;

	/*
	 * 棋子源位置与当前位置，这么设计的原因有两点
	 * 	1）便于获取需要改变的棋子在棋盘中的位置而不是去遍历
	 * 	2）更重要的原因，这样在悔棋时便于直接确定该如何操作
	 */
	/**
	 * pieceOriPos：棋子走子之前的位置
	 * 默认为Point(-1, -1);
	 * **/
	public Point OriPos;

	/**
	 * pieceNowPos：棋子走子之后的位置
	 * 默认为Point(-1, -1)
	 * **/
	public Point NowPos;
}
