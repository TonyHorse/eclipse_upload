
package ChessBase;

import java.awt.Point;

public class ChessBoarder {
	/*
	 * 构造函数
	 */
	public ChessBoarder() {
		// 重新new一个棋盘，代表构建一个新的棋盘实例
		// 与原实例进行区分
		this.NowPieces = new ChessPiece[10][9];
		this.PiecesPos = new Point[32];
		this.ChessBoarderInit();
	}

	/**
	 * 重置棋盘为初始状态，用于构造函数以及重新开始游戏
	 * **/
	public void ChessBoarderInit() {
		if(this.NowPieces == null) {
			this.NowPieces = new ChessPiece[10][9];
		}
		if(this.PiecesPos == null) {
			this.PiecesPos = new Point[32];
		}
		// 棋盘重新置空
		// 棋子初始位置在后面初始化棋子标识的同时被重置
		// 此处不需要特殊处理棋子的初始位置
		for (int i = 0;i < 10;i ++){
			for (int j = 0;j < 9;j ++){
				this.NowPieces[i][j] = null;
			}
		}
		// 黑方底线棋子
		for(int i = 0; i < 9; i++) {
			this.NowPieces[0][i] = AllPieces[i];
			this.PiecesPos[i] = AllPos[i][0];
		}
		//设置黑炮
		this.NowPieces[2][1] = AllPieces[9];
		this.PiecesPos[9] = AllPos[1][2];
		this.NowPieces[2][7] = AllPieces[10];
		this.PiecesPos[10] = AllPos[7][2];
		//设置黑卒
		for (int i = 0;i < 9;i += 2){
			this.NowPieces[3][i] = AllPieces[11 + i/2];
			this.PiecesPos[11 + i/2] = AllPos[i][3];
		}

		// 红方底线棋子
		for(int i = 0; i < 9; i++) {
			this.NowPieces[9][i] = AllPieces[16 + i];
			this.PiecesPos[16 + i] = AllPos[i][9];
		}
		//设置红炮
		this.NowPieces[7][1] = AllPieces[25];
		this.PiecesPos[25] = AllPos[1][7];
		this.NowPieces[7][7] = AllPieces[26];
		this.PiecesPos[26] = AllPos[7][7];
		//设置红兵
		for (int i = 0;i < 9;i += 2){
			this.NowPieces[6][i] = AllPieces[27 + i/2];
			this.PiecesPos[27 + i/2] = AllPos[i][6];
		}
	}


	/***************************************************
	 *常量信息
	 **************************************************/
	/**
	 * 9行10列的固定Point[][]数组
	 * AllPos[i][j] = Pieces[NowPieces[j][i].name.charAt(0)]
	 * 即实际棋子的位置只需要引用这些位置
	 * 而不需要重新创建一个新的Point对象
	 * 若棋子位置为null,代表棋子已经被吃掉
	 * **/
	public static final Point[][] AllPos = {
			{new Point(0,0), new Point(0,1), new Point(0,2), new Point(0,3), new Point(0,4),
				new Point(0,5), new Point(0,6), new Point(0,7), new Point(0,8), new Point(0,9)},

			{new Point(1,0), new Point(1,1), new Point(1,2), new Point(1,3), new Point(1,4),
					new Point(1,5), new Point(1,6), new Point(1,7), new Point(1,8), new Point(1,9)},

			{new Point(2,0), new Point(2,1), new Point(2,2), new Point(2,3), new Point(2,4),
						new Point(2,5), new Point(2,6), new Point(2,7), new Point(2,8), new Point(2,9)},

			{new Point(3,0), new Point(3,1), new Point(3,2), new Point(3,3), new Point(3,4),
							new Point(3,5), new Point(3,6), new Point(3,7), new Point(3,8), new Point(3,9)},

			{new Point(4,0), new Point(4,1), new Point(4,2), new Point(4,3), new Point(4,4),
								new Point(4,5), new Point(4,6), new Point(4,7), new Point(4,8), new Point(4,9)},

			{new Point(5,0), new Point(5,1), new Point(5,2), new Point(5,3), new Point(5,4),
									new Point(5,5), new Point(5,6), new Point(5,7), new Point(5,8), new Point(5,9)},

			{new Point(6,0), new Point(6,1), new Point(6,2), new Point(6,3), new Point(6,4),
										new Point(6,5), new Point(6,6), new Point(6,7), new Point(6,8), new Point(6,9)},

			{new Point(7,0), new Point(7,1), new Point(7,2), new Point(7,3), new Point(7,4),
											new Point(7,5), new Point(7,6), new Point(7,7), new Point(7,8), new Point(7,9)},

			{new Point(8,0), new Point(8,1), new Point(8,2), new Point(8,3), new Point(8,4),
												new Point(8,5), new Point(8,6), new Point(8,7), new Point(8,8), new Point(8,9)},
	};

	/**
	 * 所有棋子的实例，与棋子位置同理
	 * 改变棋子只需要改变对AllPieces[][]的引用即可
	 * 而不需要反复去创建棋子实例
	 * **/
	public static final ChessPiece[] AllPieces = {
			new ChessPiece(0),
			new ChessPiece(1),
			new ChessPiece(2),
			new ChessPiece(3),
			new ChessPiece(4),
			new ChessPiece(5),
			new ChessPiece(6),
			new ChessPiece(7),
			new ChessPiece(8),
			new ChessPiece(9),
			new ChessPiece(10),
			new ChessPiece(11),
			new ChessPiece(12),
			new ChessPiece(13),
			new ChessPiece(14),
			new ChessPiece(15),
			new ChessPiece(16),
			new ChessPiece(17),
			new ChessPiece(18),
			new ChessPiece(19),
			new ChessPiece(20),
			new ChessPiece(21),
			new ChessPiece(22),
			new ChessPiece(23),
			new ChessPiece(24),
			new ChessPiece(25),
			new ChessPiece(26),
			new ChessPiece(27),
			new ChessPiece(28),
			new ChessPiece(29),
			new ChessPiece(30),
			new ChessPiece(31)	
	};

	/**
	 * 棋盘默认按照窗口坐标来确定，即左上角为位置(0，0)
	 * **/
	public static final Point[] TopOriPos = {
			AllPos[0][0],
			AllPos[1][0],
			AllPos[2][0],
			AllPos[3][0],
			AllPos[4][0],
			AllPos[5][0],
			AllPos[6][0],
			AllPos[7][0],
			AllPos[8][0],
			AllPos[1][2],
			AllPos[7][2],
			AllPos[0][3],
			AllPos[2][3],
			AllPos[4][3],
			AllPos[6][3],
			AllPos[8][3]
	};
	public static final Point[] DownOriPos = {
			AllPos[0][9],
			AllPos[1][9],
			AllPos[2][9],
			AllPos[3][9],
			AllPos[4][9],
			AllPos[5][9],
			AllPos[6][9],
			AllPos[7][9],
			AllPos[8][9],
			AllPos[1][7],
			AllPos[7][7],
			AllPos[0][6],
			AllPos[2][6],
			AllPos[4][6],
			AllPos[6][6],
			AllPos[8][6]
	};
	/**
	 * 根据象棋规则，记录棋谱时红黑双方棋路的标识
	 * 黑方为小写数字，红方为大写数字
	 * 数值均是从各自角度从右往左依次增大
	 * **/
	public static final String[][] LinesLabel = {
			{"1","2","3","4","5","6","7","8","9"},
			{"九","八","七","六","五","四","三","二","一"}
	};


	/***************************************************
	 *变量信息
	 **************************************************/
	/**
	 * 10行9列的棋盘数组，null代表当前没有棋子
	 * 初始时，黑方占据0~4行，红方占据5~9行
	 * **/
	public ChessPiece[][] NowPieces;

	/**
	 * length=32的位置数组，用于记录32个棋子的位置
	 * 若为null代表棋子已经被吃掉
	 * 黑方：0 1 2 3 4 5 6 7 8   9 10   11 12 13 14 15
	 *      军 马象士将士 象马军    炮 炮     兵*5
	 * 红方：16 17 18 19 20 21 22 23 24    25 26   27 28 29 30 31
	 * 	         军 马   相  士  帅  士 相  马   军       炮 炮       卒*5
	 * **/
	public Point[] PiecesPos;
}
