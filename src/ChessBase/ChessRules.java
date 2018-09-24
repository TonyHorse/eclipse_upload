package ChessBase;

import java.awt.Point;

import DefaultSet.BoarderDef;

public class ChessRules {

	/**
	 * �ж����ӿɷ��src�ƶ���des
	 * **/
	public boolean CanPieceMove(ChessBoarder NowBrd, Point src, Point des) {
		switch(NowBrd.NowPieces[src.y][src.x].name.charAt(1)) {
		case '��': return CanJuMove(NowBrd, src, des);
		case '��': return CanMaMove(NowBrd, src, des);
		case '��': return CanBlackXiangMove(NowBrd, src, des);
		case 'ʿ': return CanBlackShiMove(NowBrd, src, des);
		case '��': return CanJiangMove(NowBrd, src, des);
		case '��': return CanPaoMove(NowBrd, src, des);
		case '��': return CanZuMove(NowBrd, src, des);
		// �췽�����е�����
		case '��': return CanRedXiangMove(NowBrd, src, des);
		case '��': return CanRedShiMove(NowBrd, src, des);
		case '˧': return CanShuaiMove(NowBrd, src, des);
		case '��': return CanBingMove(NowBrd, src, des);
		}
		return true;
	}

	/**
	 * �ж�srcλ�����ӿɷ��desλ������
	 * **/
	public boolean CanPieceEat(ChessBoarder NowBrd, Point src, Point des) {
		// ֻ������Ҫ���⴦�����������������ж�һ��
		switch(NowBrd.NowPieces[src.y][src.x].name.charAt(1)) {
		case '��': return CanPaoEat(NowBrd, src, des);
		default: return CanPieceMove(NowBrd, src, des);
		}
	}

	/**
	 * �ж�ĳ����λ�õ������Ƿ�Ϊͬһ��
	 * **/
	public boolean IsSameSide(ChessBoarder NowBrd, Point src, Point des) {
		if(NowBrd.NowPieces[src.y][src.x] == null 
				|| NowBrd.NowPieces[des.y][des.x] == null) {
			return false;
		}
		return (NowBrd.NowPieces[src.y][src.x].name.charAt(1)) 
				== (NowBrd.NowPieces[des.y][des.x].name.charAt(1));
	}

	/**
	 * ��������ʱ��������Ϣ�����������׼���������ӵ�����
	 * **/
	public String GenMoveInfo(ChessBoarder NowBrd, Point src, Point des) {
		String res = "";
		String nowPieceName = NowBrd.NowPieces[src.y][src.x].name;
		if(nowPieceName.charAt(0) == '��') {
			if(nowPieceName.charAt(1) != '��') {
				// ���˱�����������ͬһ���������ֻ������
				// ֻ��֡�ǰ������
				for(int i = 0; i < BoarderDef.HeightLines; i++) {
					if((NowBrd.NowPieces[i][src.x] != null)
							&& NowBrd.NowPieces[i][src.x].name.equals(nowPieceName)
							&& (i != src.y)) {
						if(i < src.y) {
							res += "��";
						}
						else {
							res += 'ǰ';
						}
						break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[1][src.x];
				// �ؼ����������һ�����ϣ�����������·û�б�ʾ���ʶ��޷�ʹ�ö�ά����ֱ�Ӷ�λ
				// ���ڲ�ͬ���ӡ�ƽ�������߶�Ӧ�ĺ���·��ʶ
				// ���������ˡ�����ݲ�ͬ�����в�ͬ��ʶ
				// ʿ�������ߵ���б�ߣ��ʶ���Ŀ�ĸ����·��ʶ��ʾ��һ����
				// �����ڡ�����˧���������ߵ���ֱ�ߣ��ʶ���Ҫָ�����������˼���
				if(src.y < des.y) {
					res += "��";
					if(nowPieceName.charAt(1) == 'ʿ' 
							|| nowPieceName.charAt(1) == '��'
							|| nowPieceName.charAt(1) == '��') {
						res += ChessBoarder.LinesLabel[1][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
				else if(src.y == des.y) {
					res += "ƽ";
					res += ChessBoarder.LinesLabel[1][des.x];
				}
				else {
					res += "��";
					if(nowPieceName.charAt(1) == 'ʿ' 
							|| nowPieceName.charAt(1) == '��'
							|| nowPieceName.charAt(1) == '��') {
						res += ChessBoarder.LinesLabel[1][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
			}
			else {
				// ���ͬһ������������������������
				// ��ǰ�������γ�Ϊǰ�����������������ı������
				int beforeNow = 0;
				int afterNow = 0;
				for(int i = 0; i < BoarderDef.HeightLines; i++) {
					if((NowBrd.NowPieces[i][src.x] != null)
							&& NowBrd.NowPieces[i][src.x].name.equals(nowPieceName)
							&& (i != src.y)) {
						if(i < src.y) {
							beforeNow++;
						}
						else {
							afterNow++;
						}
					}
				}
				if(beforeNow == 0 && afterNow != 0) {
					res += "ǰ";
				}
				else if(beforeNow != 0 && afterNow == 0) {
					res += "��";
				}
				else if(beforeNow != 0 && afterNow != 0){
					switch(beforeNow) {
					case 1: res += "��"; break;
					case 2: res += "��"; break;
					case 3: res += "��"; break;
					default: break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[1][src.x];
				if(src.y < des.y) {
					res += "��";
					// ��ֻ����һ�������ϱ������ˣ��˴����������ж�
					res += String.valueOf(1);
				}
				else if(src.y == des.y) {
					res += "ƽ";
					res += ChessBoarder.LinesLabel[1][des.x];
				}
				else {
					res += "��";
					res += String.valueOf(1);
				}
			}
		}
		else {
			// �ڷ�����
			if(nowPieceName.charAt(1) != '��') {
				// ���˱�����������ͬһ���������ֻ������
				// ֻ��֡�ǰ������
				for(int i = 0; i < BoarderDef.HeightLines; i++) {
					if((NowBrd.NowPieces[i][src.x] != null)
							&& NowBrd.NowPieces[i][src.x].name.equals(nowPieceName)
							&& (i != src.y)) {
						if(i > src.y) {
							res += "��";
						}
						else {
							res += 'ǰ';
						}
						break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[0][src.x];
				// �ؼ����������һ�����ϣ�����������·û�б�ʾ���ʶ��޷�ʹ�ö�ά����ֱ�Ӷ�λ
				// ���ڲ�ͬ���ӡ�ƽ�������߶�Ӧ�ĺ���·��ʶ
				// ���������ˡ�����ݲ�ͬ�����в�ͬ��ʶ
				// ʿ�������ߵ���б�ߣ��ʶ���Ŀ�ĸ����·��ʶ��ʾ��һ����
				// �����ڡ�����˧���������ߵ���ֱ�ߣ��ʶ���Ҫָ�����������˼���
				if(src.y > des.y) {
					res += "��";
					if(nowPieceName.charAt(1) == 'ʿ' 
							|| nowPieceName.charAt(1) == '��'
							|| nowPieceName.charAt(1) == '��') {
						res += ChessBoarder.LinesLabel[0][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
				else if(src.y == des.y) {
					res += "ƽ";
					res += ChessBoarder.LinesLabel[0][des.x];
				}
				else {
					res += "��";
					if(nowPieceName.charAt(1) == 'ʿ' 
							|| nowPieceName.charAt(1) == '��'
							|| nowPieceName.charAt(1) == '��') {
						res += ChessBoarder.LinesLabel[0][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
			}
			else {
				// ���ͬһ������������������������
				// ��ǰ�������γ�Ϊǰ�����������������ı������
				int beforeNow = 0;
				int afterNow = 0;
				for(int i = 0; i < BoarderDef.HeightLines; i++) {
					if((NowBrd.NowPieces[i][src.x] != null)
							&& NowBrd.NowPieces[i][src.x].name.equals(nowPieceName)
							&& (i != src.y)) {
						if(i > src.y) {
							beforeNow++;
						}
						else {
							afterNow++;
						}
					}
				}
				if(beforeNow == 0 && afterNow != 0) {
					res += "ǰ";
				}
				else if(beforeNow != 0 && afterNow == 0) {
					res += "��";
				}
				else if(beforeNow != 0 && afterNow != 0){
					switch(beforeNow) {
					case 1: res += "��"; break;
					case 2: res += "��"; break;
					case 3: res += "��"; break;
					default: break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[0][src.x];
				if(src.y > des.y) {
					res += "��";
					// ��ֻ����һ�������ϱ������ˣ��˴����������ж�
					res += String.valueOf(1);
				}
				else if(src.y == des.y) {
					res += "ƽ";
					res += ChessBoarder.LinesLabel[0][des.x];
				}
				else {
					res += "��";
					res += String.valueOf(1);
				}
			}
		}
		// System.out.println("[ChessRules]:"+res);
		return res;
	}

	/**
	 * Ŀ�ĸ��@param:des�Ƿ���������
	 * @return true:��������
	 * **/
	public boolean InBoarder(Point des) {
		return (des.y >= 0 && des.y <= 9 && des.x >= 0 && des.x <= 8);
	}

	/**
	 * '��'���ӹ���ֱ�����ߣ�����δ���������ӵ�ס
	 * **/
	private boolean CanJuMove(ChessBoarder NowBrd, Point src, Point des){
		// �ߵ���ֱ�� && �м�û����ס
		return (src.x == src.x || src.y == src.y) && (PieceCnt(NowBrd, src, des) == 0);
	}

	/**
	 * '��'���ӹ������գ�ͬʱ����ѹ���
	 * **/
	private boolean CanMaMove(ChessBoarder NowBrd, Point src, Point des) {
		// �ߵ�����(Distance(src, des) == sqrt(5)) && (���ռӲ���ѹ��� || ���ղ���ѹ���)
		return (Math.abs(Distance(src, des) - Math.sqrt(5.0f)) < 0.01) 
				&& (
						(Math.abs(src.x - des.x) == 1 && NowBrd.NowPieces[(src.y+des.y)/2][src.x] == null) 
						|| 
						(Math.abs(src.y - des.y) == 1 && NowBrd.NowPieces[src.y][(src.x+des.x)/2] == null));
	}

	/**
	 * '����'���ӹ������ͬʱ����ѹ���
	 * **/
	private boolean CanBlackXiangMove(ChessBoarder NowBrd, Point src, Point des) {
		// ���ܹ��� && ����(Distance(src, des) == sqrt(8)) && ����ѹ��� 
		return ( des.y <= 4 ) 
				&& (Math.abs(Distance(src, des) - Math.sqrt(8.0f)) < 0.01) 
				&& (NowBrd.NowPieces[(des.y + src.y)/2][(des.x + src.x)/2] == null);
	}

	/**
	 * '��ʿ'���ӹ�����б�ߣ��ڴ�Ӫ��
	 * **/
	private boolean CanBlackShiMove(ChessBoarder NowBrd, Point src, Point des) {
		// �ڴ�Ӫ�� && ��б��(Distance(src, des) == sqrt(2))
		return (des.x >= 3 && des.x <= 5 && des.y >= 0 && des.y <= 2)
				&& (Math.abs(Distance(src,des) - Math.sqrt(2.0f)) < 0.01);
	}

	/**
	 * '��'���ӹ����ڴ�Ӫ������
	 * **/
	private boolean CanJiangMove(ChessBoarder NowBrd, Point src, Point des) {
		// �ڴ�Ӫ�� && ��һ��ֱ��
		return (des.x >= 3 && des.x <= 5 && des.y >= 0 && des.y <= 2) 
				&& (Distance(src,des) == 1);
	}

	/**
	 * '��'���ӹ�����ֱ��
	 * **/
	private boolean CanPaoMove(ChessBoarder NowBrd, Point src, Point des) {
		// ��ֱ�� && ������ס
		return (src.y == des.y || src.x == des.x) && (PieceCnt(NowBrd, src,des) == 0);
	}

	/**
	 * �ڳ��ӹ���ֱ�߳��ӣ����м��һ��
	 * �������ӳ��ӹ���ʵ���������ӹ�����ͬ���ʿ���ֱ��ʹ����Ӧ��CanPieceMove����
	 * **/
	private boolean CanPaoEat(ChessBoarder NowBrd, Point src, Point des) {
		// ��ֱ�� && �м�ǡ�ü��һ����
		return (src.y == des.y || src.x == des.x) && (PieceCnt(NowBrd, src,des) == 1);
	}

	/**
	 * '��'���ӹ��򣺹���ǰֻ����ǰ�����Ӻ���Ժ��ƣ�ÿһ������һ��
	 * **/
	private boolean CanZuMove(ChessBoarder NowBrd, Point src, Point des) {
		if (des.y >= src.y){ // ֻ��ǰ��
			// ���Ѿ����� && ���߾���Ϊ1�� || ��δ���� && ����ǰ�� && �߶�����Ϊ1��
			return (des.y >= 5 && Distance(src,des) == 1) ||
					(des.y < 5 && des.x == src.x && Math.abs(des.y - src.y) == 1);
		}
		else{ //������Υ��
			return false;
		}
	}

	/**
	 * '����'���ӹ������ͬʱ����ѹ���
	 * **/
	private boolean CanRedXiangMove(ChessBoarder NowBrd, Point src, Point des) {
		// ���ܹ��� && ����(Distance(src, des) == sqrt(8)) && ����ѹ��� 
		return (des.y >= 5) 
				&& (Math.abs(Distance(src, des) - Math.sqrt(8.0f)) < 0.01) 
				&& (NowBrd.NowPieces[(des.y + src.y)/2][(des.x + src.x)/2] == null);
	}

	/**
	 * '����'���ӹ�����б�ߣ��ڴ�Ӫ��
	 * **/
	private boolean CanRedShiMove(ChessBoarder NowBrd, Point src, Point des) {
		// �ڴ�Ӫ�� && ��һ��б��
		return (des.x >= 3 && des.x <= 5 && des.y >= 7 && des.y <= 9)
				&& (Math.abs(Distance(src,des) - Math.sqrt(2.0f)) < 0.01);
	}

	/**
	 * '˧'���ӹ����ڴ�Ӫ�ڣ�ÿ��һ��
	 * **/
	private boolean CanShuaiMove(ChessBoarder NowBrd, Point src, Point des) {
		// �ڴ�Ӫ�� && ��һ��
		return (des.x >= 3 && des.x <= 5 && des.y >= 7 && des.y <= 9) 
				&& (Distance(src,des) == 1);
	}

	/**
	 * '��'���ӹ��򣺹���ǰֻ����ǰ�����Ӻ���Ժ��ƣ�ÿһ������һ��
	 * **/
	private boolean CanBingMove(ChessBoarder NowBrd, Point src, Point des) {
		if (des.y <= src.y){
			// �Ѿ����� && ���߾���Ϊ1 ||��δ���� && ��ǰ�� && �߶�����Ϊ1�� 
			return (des.y <= 4 && Distance(src,des) == 1) || 
					(des.y > 4 && des.x == src.x && Math.abs(des.y - src.y) == 1);
		}
		else{
			return false;
		}
	}


	/**
	 * ��������ϸ��Point a��b �����
	 * **/
	public double Distance(Point a,Point b){
		return a.distance(b);
	}

	/**
	 * ���������ͬһֱ���ϵ����������м�������
	 * �����ж������Ƿ��������
	 * �Լ����Ƿ���Գ���
	 * @param NowBrd:��������
	 * @param a:���̸��a
	 * @param b:���̸��b
	 * **/
	public int PieceCnt(ChessBoarder NowBrd, Point a, Point b) {
		int cnt = 0;
		int min = 0, max = 0;
		if(a.x == b.x) {
			min = a.y > b.y ? b.y : a.y;
			max = a.y > b.y ? a.y : b.y;
			for (int i = min + 1;i < max; i++){
				if (NowBrd.NowPieces[i][a.x] != null){
					cnt++;
				}
			}
		}
		else if(a.y == b.y){
			min = a.x > b.x ? b.x : a.x;
			max = a.x > b.x ? a.x : b.x;
			for (int i = min + 1;i < max; i++){
				if (NowBrd.NowPieces[a.y][i] != null){
					cnt++;
				}
			}
		}
		else {
			// ����һ�����ϣ�����һ������ֵ
			return Integer.MAX_VALUE;
		}
		return cnt;
	}


}
