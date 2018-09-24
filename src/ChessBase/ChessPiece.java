package ChessBase;

import java.awt.Image;
import java.awt.Toolkit;

public class ChessPiece {
	public ChessPiece() {
		this.id = -1;
		this.Icon = null;
		this.name = null;
	}

	public ChessPiece(int pieceID) {
		int realID = -1;
		// 根据pieceID判断出是哪一类棋子
		if(pieceID < 5) {
			realID = pieceID;
		}
		else if(pieceID < 9) {
			realID = 8 - pieceID;
		}
		else if(pieceID < 11) {
			realID = 5;
		}
		else if(pieceID < 16) {
			realID = 6;
		}
		else if(pieceID < 21) {
			realID = pieceID - 9;
		}
		else if(pieceID < 25) {
			realID = 31 - pieceID;
		}
		else if(pieceID < 27) {
			realID = 12;
		}
		else if(pieceID < 32) {
			realID = 13;
		}
		if(realID != -1) {
			this.id = pieceID;
			this.name = new String(PiecesName[realID]);
			try {
				this.Icon = Toolkit.getDefaultToolkit().getImage(
						// 通过相对路径定位图片位置
						ChessPiece.class.getResource(
								"/imageLibary/" 
						+ PiecesImgName[realID] + ".png"));
			}
			catch(Exception e) {
				System.err.println("[ChessPiece]:图片加载失败");
			}
		}
		else {
			System.out.println("[ChessPiece]:pieceID("
					+ pieceID + ")不在0~32之间");
		}
	}


	/***************************************************
	 * 变量信息
	 **************************************************/
	/**
	 *  棋子的真实ID，取值范围0~31
	 *  0~15为黑方、16~31为红方
	 * **/
	public int id = -1;

	/**
	 * 棋子的图片
	 * **/
	public Image Icon = null;

	/**
	 * 通过name直接判断棋子的属性和判断可行的行为
	 * **/
	public String name = null;

	
	/***************************************************
	 * 常量信息
	 **************************************************/
	/**
	 * 0：黑车
	 * 1：黑马
	 * 2：黑象
	 * 3：黑士
	 * 4：黑将
	 * 5：黑炮
	 * 6：黑卒
	 * ---------
	 * 7：红车
	 * 8：红马
	 * 9：红相
	 * 10：红士
	 * 11：红帅
	 * 12：红炮
	 * 13：红兵
	 * 实际Id为32个,便于直接通过ID定位某个棋子的位置
	 * 而不是通过遍历棋盘得到
	 * black：0 1 2 3   4 5 6 7 8          9 10    11 12 13 14 15
	 * ju ma xiang shi  jiang shi xiang ma ju pao*2    zu*5
	 * red：16 17 18 19  20 21 22 23 24          25 26   27 28 29 30 31
	 * ju ma xiang shi   shuai shi xiang ma ju   pao*2   bing*5
	 */
	public static final String[] PiecesName = {
			"黑车", "黑马", "黑象", "黑士", "黑将", "黑炮", "黑卒", 
			"红车", "红马", "红相", "红仕", "红帅", "红炮", "红兵"};
	public static final String[] PiecesImgName = {
			"black-ju", "black-ma", "black-xiang", "black-shi",
			"black-jiang", "black-pao", "black-zu",
			"red-ju", "red-ma", "red-xiang", "red-shi",
			"red-shuai", "red-pao", "red-bing"};

}
