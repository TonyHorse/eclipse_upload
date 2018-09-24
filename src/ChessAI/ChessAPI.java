package ChessAI;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ChessBase.ChessMatch;
import ChessBase.ChessPiece;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChessAPI{


	/***************************************************
	 * ��API��������
	 **************************************************/
	/** 
	 * ����get����
	 * @param url 
	 * @return 
	 */  
	private String httpGet(String url) {  
		String result = null;  
		OkHttpClient client = new OkHttpClient();  
		Request request = new Request.Builder().url(url).build();  
		try {  
			Response response = client.newCall(request).execute();  
			result = response.body().string();  
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
		return result;
	}


	/***************************************************
	 * ��API�����л�ȡ���ӷ���,�Ȳ�ѯ����߷��������ҵ���ֱ����
	 * �����ѯ��ѡ�ŷ�
	 **************************************************/
	public String GetFenMove(String url, String data) {
		System.out.println("[ChessAPI]:Fen���Ϊ��"+data);
		String Fenmoves = this.httpGet(url + "querybest" + data);
		if(Fenmoves.contains("invalid board")) {
			System.out.println("[ChessAPI]:����ת��ΪFen��ʽ����");
			return null;
		}
		
		if(Fenmoves.contains("nobestmove") 
				|| Fenmoves.contains("unknown")) {
			Fenmoves = this.httpGet(url + "querysearch" + data);
			String[] posmove = Fenmoves.split("\\|");
			Random rand = new Random();
			// �ӱ�ѡ�ŷ������ѡ��һ��
			Fenmoves = posmove[Math.abs(rand.nextInt(posmove.length))];
		}
		if(Fenmoves.contains("nobestmove")
				|| Fenmoves.contains("unknown")) {
			Fenmoves = this.httpGet(url + "queryall&showall=1" + data);
			String[] posmove = Fenmoves.split("\\|");
			Random rand = new Random();
			// �ӱ�ѡ�ŷ������ѡ��һ��
			Fenmoves = posmove[Math.abs(rand.nextInt(posmove.length))];
			Fenmoves.substring(Fenmoves.indexOf(':') + 1, 
					Fenmoves.indexOf(':') + 5);
		}
		System.out.println("[ChessAPI]:ԭʼFenmoves2---"+Fenmoves);
		return Fenmoves;
	}

	/*
	 * �μ��й��������Ӧ�ù淶(��)
	 * http://www.xqbase.com/protocol/cchess_move.htm
	 * ��ά�����ʾ�й�����
	 *  a b c d e f g h i
	 * 9
	 * 8
	 * 7
	 * 6
	 * 5
	 * 4
	 * 3
	 * 2
	 * 1
	 * 0
	 * �����������̸��������ʾ
	 *  0 1 2 3 4 5 6 7 8
	 * 0
	 * 1
	 * 2
	 * 3
	 * 4
	 * 5
	 * 6
	 * 7
	 * 8
	 * 9
	 */
	/**
	 * @param FenMoves����ʽΪ move:c3c4 , ʵ�����ö�ά�����ʾ�����̸��
	 * @return ���BrdWin.NowMatch�����Ӳ���
	 * **/
	public Point[] GetMove(String Fenmoves) {
		if(Fenmoves.equals("unknown")) {
			// API��֪����������ˣ�����null������computer�Լ������
			return null;
		}

		System.out.println("[ChessAPI]:��ʼ����Fenmoves--"+Fenmoves);

		if(Fenmoves.indexOf(':') != -1) {
			Fenmoves = Fenmoves.substring(Fenmoves.indexOf(':')+1);
		}

		Point[] res = new Point[2];

		res[0] = new Point();
		res[1] = new Point();

		res[0].x = XYLinesLabel.get(Fenmoves.charAt(0));
		res[0].y = XYLinesLabel.get(Fenmoves.charAt(1));
		res[1].x = XYLinesLabel.get(Fenmoves.charAt(2));
		res[1].y = XYLinesLabel.get(Fenmoves.charAt(3));

		return res;
	}


	/***************************************************
	 * ������Ϣת����Fen��ʽ�ַ���
	 **************************************************/
	public String MyMatch2FenMatch(ChessMatch nowMatch) {
		String res = "&board=";
		if(nowMatch == null 
				|| nowMatch.NowBoarder == null
				|| nowMatch.NowBoarder.NowPieces == null) {
			return null;
		}

		int cnt = 0;
		for(int y = 0; y < 10; y++) {
			cnt = 0;
			for(int x = 0; x < 9; x++) {
				if(nowMatch.NowBoarder.NowPieces[y][x] != null) {
					// ���������ˣ����һ��cnt�Ƿ�Ϊ0
					if(cnt != 0) {
						// Ϊ0˵���м�û�п���
						res += String.valueOf(cnt);
						cnt = 0;
					}
					// ���ϵ�ǰλ������
					res += MyPiece2FenPiece.get(
							nowMatch.NowBoarder.NowPieces[y][x].name);
				}
				else {
					cnt++;
				}
			}
			if(cnt != 0) {
				// ˵����󼸸�ȫΪ�ף��ü���
				res += String.valueOf(cnt);
			}
			// ÿһ���á�/�������
			if(y != 9) {
				res += '/';
			}
		}

		res += ' ';
		res += nowMatch.NowPlayer == '��' ? 'b' : 'w';

		return res;
	}


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	// ��ʼ����Fen:
	// rnbakabnr/
	// 9/
	// 1c5c1/
	// p1p1p1p1p/
	// 9/
	// 9/
	// P1P1P1P1P/
	// 1C5C1/
	// 9/
	// RNBAKABNR 
	// w

	// �������̸�ʽ������������£�
	// rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w

	// ����Fen�������ϸ�ڲμ���http://www.xqbase.com/protocol/cchess_fen.htm

	public static final char[] FenPieceName = {
			'r', 'n', 'b', 'a', 'k', 'c', 'p',
			'R', 'N', 'B', 'A', 'K', 'C', 'P'};

	public static final String ChessDBHost = 
			"http://api.chessdb.cn:81/chessdb.php?action=";

	public static final Map<String, Character> MyPiece2FenPiece = 
			new HashMap<String, Character>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			for(int i = 0; i < ChessPiece.PiecesName.length; i++) {
				put(ChessPiece.PiecesName[i], FenPieceName[i]);
			}			
		}
	};

	public static final Map<Character, Integer> XYLinesLabel = 
			new HashMap<Character, Integer>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		{
			for(Character i = 'a'; i <= 'i'; i++) {
				put(i, i - 'a');
			}
			for(Character i = '9'; i >= '0'; i--) {
				put(i, '9'-i);
			}
		}
	};

}
