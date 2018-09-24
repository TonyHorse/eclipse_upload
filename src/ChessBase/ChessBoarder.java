
package ChessBase;

import java.awt.Point;

public class ChessBoarder {
	/*
	 * ���캯��
	 */
	public ChessBoarder() {
		// ����newһ�����̣�������һ���µ�����ʵ��
		// ��ԭʵ����������
		this.NowPieces = new ChessPiece[10][9];
		this.PiecesPos = new Point[32];
		this.ChessBoarderInit();
	}

	/**
	 * ��������Ϊ��ʼ״̬�����ڹ��캯���Լ����¿�ʼ��Ϸ
	 * **/
	public void ChessBoarderInit() {
		if(this.NowPieces == null) {
			this.NowPieces = new ChessPiece[10][9];
		}
		if(this.PiecesPos == null) {
			this.PiecesPos = new Point[32];
		}
		// ���������ÿ�
		// ���ӳ�ʼλ���ں����ʼ�����ӱ�ʶ��ͬʱ������
		// �˴�����Ҫ���⴦�����ӵĳ�ʼλ��
		for (int i = 0;i < 10;i ++){
			for (int j = 0;j < 9;j ++){
				this.NowPieces[i][j] = null;
			}
		}
		// �ڷ���������
		for(int i = 0; i < 9; i++) {
			this.NowPieces[0][i] = AllPieces[i];
			this.PiecesPos[i] = AllPos[i][0];
		}
		//���ú���
		this.NowPieces[2][1] = AllPieces[9];
		this.PiecesPos[9] = AllPos[1][2];
		this.NowPieces[2][7] = AllPieces[10];
		this.PiecesPos[10] = AllPos[7][2];
		//���ú���
		for (int i = 0;i < 9;i += 2){
			this.NowPieces[3][i] = AllPieces[11 + i/2];
			this.PiecesPos[11 + i/2] = AllPos[i][3];
		}

		// �췽��������
		for(int i = 0; i < 9; i++) {
			this.NowPieces[9][i] = AllPieces[16 + i];
			this.PiecesPos[16 + i] = AllPos[i][9];
		}
		//���ú���
		this.NowPieces[7][1] = AllPieces[25];
		this.PiecesPos[25] = AllPos[1][7];
		this.NowPieces[7][7] = AllPieces[26];
		this.PiecesPos[26] = AllPos[7][7];
		//���ú��
		for (int i = 0;i < 9;i += 2){
			this.NowPieces[6][i] = AllPieces[27 + i/2];
			this.PiecesPos[27 + i/2] = AllPos[i][6];
		}
	}


	/***************************************************
	 *������Ϣ
	 **************************************************/
	/**
	 * 9��10�еĹ̶�Point[][]����
	 * AllPos[i][j] = Pieces[NowPieces[j][i].name.charAt(0)]
	 * ��ʵ�����ӵ�λ��ֻ��Ҫ������Щλ��
	 * ������Ҫ���´���һ���µ�Point����
	 * ������λ��Ϊnull,���������Ѿ����Ե�
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
	 * �������ӵ�ʵ����������λ��ͬ��
	 * �ı�����ֻ��Ҫ�ı��AllPieces[][]�����ü���
	 * ������Ҫ����ȥ��������ʵ��
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
	 * ����Ĭ�ϰ��մ���������ȷ���������Ͻ�Ϊλ��(0��0)
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
	 * ����������򣬼�¼����ʱ���˫����·�ı�ʶ
	 * �ڷ�ΪСд���֣��췽Ϊ��д����
	 * ��ֵ���ǴӸ��ԽǶȴ���������������
	 * **/
	public static final String[][] LinesLabel = {
			{"1","2","3","4","5","6","7","8","9"},
			{"��","��","��","��","��","��","��","��","һ"}
	};


	/***************************************************
	 *������Ϣ
	 **************************************************/
	/**
	 * 10��9�е��������飬null����ǰû������
	 * ��ʼʱ���ڷ�ռ��0~4�У��췽ռ��5~9��
	 * **/
	public ChessPiece[][] NowPieces;

	/**
	 * length=32��λ�����飬���ڼ�¼32�����ӵ�λ��
	 * ��Ϊnull���������Ѿ����Ե�
	 * �ڷ���0 1 2 3 4 5 6 7 8   9 10   11 12 13 14 15
	 *      �� ����ʿ��ʿ �����    �� ��     ��*5
	 * �췽��16 17 18 19 20 21 22 23 24    25 26   27 28 29 30 31
	 * 	         �� ��   ��  ʿ  ˧  ʿ ��  ��   ��       �� ��       ��*5
	 * **/
	public Point[] PiecesPos;
}
