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
	 * 向API发送请求
	 **************************************************/
	/** 
	 * 发起get请求
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
	 * 从API返回中获取走子方法,先查询最佳走法，可以找到则直接走
	 * 否则查询候选着法
	 **************************************************/
	public String GetFenMove(String url, String data) {
		System.out.println("[ChessAPI]:Fen格局为："+data);
		String Fenmoves = this.httpGet(url + "querybest" + data);
		if(Fenmoves.contains("invalid board")) {
			System.out.println("[ChessAPI]:棋盘转换为Fen格式出错");
			return null;
		}
		
		if(Fenmoves.contains("nobestmove") 
				|| Fenmoves.contains("unknown")) {
			Fenmoves = this.httpGet(url + "querysearch" + data);
			String[] posmove = Fenmoves.split("\\|");
			Random rand = new Random();
			// 从备选着法中随机选择一种
			Fenmoves = posmove[Math.abs(rand.nextInt(posmove.length))];
		}
		if(Fenmoves.contains("nobestmove")
				|| Fenmoves.contains("unknown")) {
			Fenmoves = this.httpGet(url + "queryall&showall=1" + data);
			String[] posmove = Fenmoves.split("\\|");
			Random rand = new Random();
			// 从备选着法中随机选择一种
			Fenmoves = posmove[Math.abs(rand.nextInt(posmove.length))];
			Fenmoves.substring(Fenmoves.indexOf(':') + 1, 
					Fenmoves.indexOf(':') + 5);
		}
		System.out.println("[ChessAPI]:原始Fenmoves2---"+Fenmoves);
		return Fenmoves;
	}

	/*
	 * 参见中国象棋电脑应用规范(二)
	 * http://www.xqbase.com/protocol/cchess_move.htm
	 * 二维坐标表示中国象棋
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
	 * 本程序中棋盘格点的坐标表示
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
	 * @param FenMoves，格式为 move:c3c4 , 实际是用二维坐标表示的棋盘格点
	 * @return 针对BrdWin.NowMatch的走子策略
	 * **/
	public Point[] GetMove(String Fenmoves) {
		if(Fenmoves.equals("unknown")) {
			// API不知道该如何走了，返回null，告诉computer自己随便走
			return null;
		}

		System.out.println("[ChessAPI]:开始解析Fenmoves--"+Fenmoves);

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
	 * 棋盘信息转换成Fen格式字符串
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
					// 遇到棋子了，检查一下cnt是否为0
					if(cnt != 0) {
						// 为0说明中间没有空子
						res += String.valueOf(cnt);
						cnt = 0;
					}
					// 加上当前位置棋子
					res += MyPiece2FenPiece.get(
							nowMatch.NowBoarder.NowPieces[y][x].name);
				}
				else {
					cnt++;
				}
			}
			if(cnt != 0) {
				// 说明最后几个全为孔，得加上
				res += String.valueOf(cnt);
			}
			// 每一行用’/‘间隔开
			if(y != 9) {
				res += '/';
			}
		}

		res += ' ';
		res += nowMatch.NowPlayer == '黑' ? 'b' : 'w';

		return res;
	}


	/***************************************************
	 * 常量信息
	 **************************************************/
	// 初始局面Fen:
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

	// 上述棋盘格式完整版情况如下：
	// rnbakabnr/9/1c5c1/p1p1p1p1p/9/9/P1P1P1P1P/1C5C1/9/RNBAKABNR w

	// 关于Fen规则具体细节参见：http://www.xqbase.com/protocol/cchess_fen.htm

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
