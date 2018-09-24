package ChessBase;

import java.awt.Point;

import DefaultSet.BoarderDef;

public class ChessRules {

	/**
	 * 判断棋子可否从src移动到des
	 * **/
	public boolean CanPieceMove(ChessBoarder NowBrd, Point src, Point des) {
		switch(NowBrd.NowPieces[src.y][src.x].name.charAt(1)) {
		case '车': return CanJuMove(NowBrd, src, des);
		case '马': return CanMaMove(NowBrd, src, des);
		case '象': return CanBlackXiangMove(NowBrd, src, des);
		case '士': return CanBlackShiMove(NowBrd, src, des);
		case '将': return CanJiangMove(NowBrd, src, des);
		case '炮': return CanPaoMove(NowBrd, src, des);
		case '卒': return CanZuMove(NowBrd, src, des);
		// 红方所特有的棋子
		case '相': return CanRedXiangMove(NowBrd, src, des);
		case '仕': return CanRedShiMove(NowBrd, src, des);
		case '帅': return CanShuaiMove(NowBrd, src, des);
		case '兵': return CanBingMove(NowBrd, src, des);
		}
		return true;
	}

	/**
	 * 判断src位置棋子可否吃des位置棋子
	 * **/
	public boolean CanPieceEat(ChessBoarder NowBrd, Point src, Point des) {
		// 只有炮需要特殊处理，其余棋子与走子判断一样
		switch(NowBrd.NowPieces[src.y][src.x].name.charAt(1)) {
		case '炮': return CanPaoEat(NowBrd, src, des);
		default: return CanPieceMove(NowBrd, src, des);
		}
	}

	/**
	 * 判断某两个位置的棋子是否为同一方
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
	 * 产生走子时的棋谱信息，按照象棋标准来产生走子的术语
	 * **/
	public String GenMoveInfo(ChessBoarder NowBrd, Point src, Point des) {
		String res = "";
		String nowPieceName = NowBrd.NowPieces[src.y][src.x].name;
		if(nowPieceName.charAt(0) == '红') {
			if(nowPieceName.charAt(1) != '兵') {
				// 除了兵，其余棋子同一竖线上最多只有两个
				// 只需分”前“”后“
				for(int i = 0; i < BoarderDef.HeightLines; i++) {
					if((NowBrd.NowPieces[i][src.x] != null)
							&& NowBrd.NowPieces[i][src.x].name.equals(nowPieceName)
							&& (i != src.y)) {
						if(i < src.y) {
							res += "后";
						}
						else {
							res += '前';
						}
						break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[1][src.x];
				// 关键点在于最后一个字上，象棋纵向线路没有表示，故而无法使用二维坐标直接定位
				// 对于不同棋子”平“都是走对应的横线路标识
				// ”进“”退“则根据不同棋子有不同标识
				// 士、马、象走的是斜线，故而用目的格点线路标识表示后一个字
				// 车、炮、将、帅、兵、卒走的是直线，故而需要指出进或者退了几步
				if(src.y < des.y) {
					res += "退";
					if(nowPieceName.charAt(1) == '士' 
							|| nowPieceName.charAt(1) == '马'
							|| nowPieceName.charAt(1) == '相') {
						res += ChessBoarder.LinesLabel[1][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
				else if(src.y == des.y) {
					res += "平";
					res += ChessBoarder.LinesLabel[1][des.x];
				}
				else {
					res += "进";
					if(nowPieceName.charAt(1) == '士' 
							|| nowPieceName.charAt(1) == '马'
							|| nowPieceName.charAt(1) == '相') {
						res += ChessBoarder.LinesLabel[1][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
			}
			else {
				// 红兵同一竖线上最多有五个，单独考虑
				// 从前往后依次称为前兵、二兵、三兵、四兵、后兵
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
					res += "前";
				}
				else if(beforeNow != 0 && afterNow == 0) {
					res += "后";
				}
				else if(beforeNow != 0 && afterNow != 0){
					switch(beforeNow) {
					case 1: res += "二"; break;
					case 2: res += "三"; break;
					case 3: res += "四"; break;
					default: break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[1][src.x];
				if(src.y < des.y) {
					res += "退";
					// 并只能走一格，理论上兵不能退，此处不做特殊判断
					res += String.valueOf(1);
				}
				else if(src.y == des.y) {
					res += "平";
					res += ChessBoarder.LinesLabel[1][des.x];
				}
				else {
					res += "进";
					res += String.valueOf(1);
				}
			}
		}
		else {
			// 黑发棋子
			if(nowPieceName.charAt(1) != '卒') {
				// 除了兵，其余棋子同一竖线上最多只有两个
				// 只需分”前“”后“
				for(int i = 0; i < BoarderDef.HeightLines; i++) {
					if((NowBrd.NowPieces[i][src.x] != null)
							&& NowBrd.NowPieces[i][src.x].name.equals(nowPieceName)
							&& (i != src.y)) {
						if(i > src.y) {
							res += "后";
						}
						else {
							res += '前';
						}
						break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[0][src.x];
				// 关键点在于最后一个字上，象棋纵向线路没有表示，故而无法使用二维坐标直接定位
				// 对于不同棋子”平“都是走对应的横线路标识
				// ”进“”退“则根据不同棋子有不同标识
				// 士、马、象走的是斜线，故而用目的格点线路标识表示后一个字
				// 车、炮、将、帅、兵、卒走的是直线，故而需要指出进或者退了几步
				if(src.y > des.y) {
					res += "退";
					if(nowPieceName.charAt(1) == '士' 
							|| nowPieceName.charAt(1) == '马'
							|| nowPieceName.charAt(1) == '象') {
						res += ChessBoarder.LinesLabel[0][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
				else if(src.y == des.y) {
					res += "平";
					res += ChessBoarder.LinesLabel[0][des.x];
				}
				else {
					res += "进";
					if(nowPieceName.charAt(1) == '士' 
							|| nowPieceName.charAt(1) == '马'
							|| nowPieceName.charAt(1) == '象') {
						res += ChessBoarder.LinesLabel[0][des.x];
					}
					else {
						res += String.valueOf(Math.abs(src.y-des.y));
					}
				}
			}
			else {
				// 红兵同一竖线上最多有五个，单独考虑
				// 从前往后依次称为前兵、二兵、三兵、四兵、后兵
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
					res += "前";
				}
				else if(beforeNow != 0 && afterNow == 0) {
					res += "后";
				}
				else if(beforeNow != 0 && afterNow != 0){
					switch(beforeNow) {
					case 1: res += "二"; break;
					case 2: res += "三"; break;
					case 3: res += "四"; break;
					default: break;
					}
				}
				res += nowPieceName.substring(1);
				res += ChessBoarder.LinesLabel[0][src.x];
				if(src.y > des.y) {
					res += "退";
					// 并只能走一格，理论上兵不能退，此处不做特殊判断
					res += String.valueOf(1);
				}
				else if(src.y == des.y) {
					res += "平";
					res += ChessBoarder.LinesLabel[0][des.x];
				}
				else {
					res += "进";
					res += String.valueOf(1);
				}
			}
		}
		// System.out.println("[ChessRules]:"+res);
		return res;
	}

	/**
	 * 目的格点@param:des是否在棋盘中
	 * @return true:在棋盘中
	 * **/
	public boolean InBoarder(Point des) {
		return (des.y >= 0 && des.y <= 9 && des.x >= 0 && des.x <= 8);
	}

	/**
	 * '军'走子规则：直线行走，而且未被其他棋子挡住
	 * **/
	private boolean CanJuMove(ChessBoarder NowBrd, Point src, Point des){
		// 走的是直线 && 中间没被挡住
		return (src.x == src.x || src.y == src.y) && (PieceCnt(NowBrd, src, des) == 0);
	}

	/**
	 * '马'走子规则：走日，同时不被压马脚
	 * **/
	private boolean CanMaMove(ChessBoarder NowBrd, Point src, Point des) {
		// 走的是日(Distance(src, des) == sqrt(5)) && (长日加不被压马脚 || 短日不被压马脚)
		return (Math.abs(Distance(src, des) - Math.sqrt(5.0f)) < 0.01) 
				&& (
						(Math.abs(src.x - des.x) == 1 && NowBrd.NowPieces[(src.y+des.y)/2][src.x] == null) 
						|| 
						(Math.abs(src.y - des.y) == 1 && NowBrd.NowPieces[src.y][(src.x+des.x)/2] == null));
	}

	/**
	 * '黑象'走子规则：走田，同时不被压象脚
	 * **/
	private boolean CanBlackXiangMove(ChessBoarder NowBrd, Point src, Point des) {
		// 不能过河 && 走田(Distance(src, des) == sqrt(8)) && 不能压象脚 
		return ( des.y <= 4 ) 
				&& (Math.abs(Distance(src, des) - Math.sqrt(8.0f)) < 0.01) 
				&& (NowBrd.NowPieces[(des.y + src.y)/2][(des.x + src.x)/2] == null);
	}

	/**
	 * '黑士'走子规则：走斜线，在大本营内
	 * **/
	private boolean CanBlackShiMove(ChessBoarder NowBrd, Point src, Point des) {
		// 在大本营内 && 走斜线(Distance(src, des) == sqrt(2))
		return (des.x >= 3 && des.x <= 5 && des.y >= 0 && des.y <= 2)
				&& (Math.abs(Distance(src,des) - Math.sqrt(2.0f)) < 0.01);
	}

	/**
	 * '将'走子规则：在大本营内行走
	 * **/
	private boolean CanJiangMove(ChessBoarder NowBrd, Point src, Point des) {
		// 在大本营内 && 走一步直线
		return (des.x >= 3 && des.x <= 5 && des.y >= 0 && des.y <= 2) 
				&& (Distance(src,des) == 1);
	}

	/**
	 * '炮'走子规则：走直线
	 * **/
	private boolean CanPaoMove(ChessBoarder NowBrd, Point src, Point des) {
		// 走直线 && 不被挡住
		return (src.y == des.y || src.x == des.x) && (PieceCnt(NowBrd, src,des) == 0);
	}

	/**
	 * 炮吃子规则：直线吃子，且中间隔一子
	 * 其余棋子吃子规则实际上与走子规则相同，故可以直接使用相应得CanPieceMove方法
	 * **/
	private boolean CanPaoEat(ChessBoarder NowBrd, Point src, Point des) {
		// 走直线 && 中间恰好间隔一个子
		return (src.y == des.y || src.x == des.x) && (PieceCnt(NowBrd, src,des) == 1);
	}

	/**
	 * '卒'走子规则：过河前只可向前，过河后可以横移，每一至多走一步
	 * **/
	private boolean CanZuMove(ChessBoarder NowBrd, Point src, Point des) {
		if (des.y >= src.y){ // 只可前进
			// （已经过河 && 行走距离为1） || （未过河 && 得往前走 && 走动距离为1）
			return (des.y >= 5 && Distance(src,des) == 1) ||
					(des.y < 5 && des.x == src.x && Math.abs(des.y - src.y) == 1);
		}
		else{ //往后退违规
			return false;
		}
	}

	/**
	 * '红相'走子规则：走田，同时不被压象脚
	 * **/
	private boolean CanRedXiangMove(ChessBoarder NowBrd, Point src, Point des) {
		// 不能过河 && 走田(Distance(src, des) == sqrt(8)) && 不能压象脚 
		return (des.y >= 5) 
				&& (Math.abs(Distance(src, des) - Math.sqrt(8.0f)) < 0.01) 
				&& (NowBrd.NowPieces[(des.y + src.y)/2][(des.x + src.x)/2] == null);
	}

	/**
	 * '红仕'走子规则：走斜线，在大本营内
	 * **/
	private boolean CanRedShiMove(ChessBoarder NowBrd, Point src, Point des) {
		// 在大本营内 && 走一格斜线
		return (des.x >= 3 && des.x <= 5 && des.y >= 7 && des.y <= 9)
				&& (Math.abs(Distance(src,des) - Math.sqrt(2.0f)) < 0.01);
	}

	/**
	 * '帅'走子规则：在大本营内，每次一步
	 * **/
	private boolean CanShuaiMove(ChessBoarder NowBrd, Point src, Point des) {
		// 在大本营内 && 走一格
		return (des.x >= 3 && des.x <= 5 && des.y >= 7 && des.y <= 9) 
				&& (Distance(src,des) == 1);
	}

	/**
	 * '兵'走子规则：过河前只可向前，过河后可以横移，每一至多走一步
	 * **/
	private boolean CanBingMove(ChessBoarder NowBrd, Point src, Point des) {
		if (des.y <= src.y){
			// 已经过河 && 行走距离为1 ||（未过河 && 往前走 && 走动距离为1） 
			return (des.y <= 4 && Distance(src,des) == 1) || 
					(des.y > 4 && des.x == src.x && Math.abs(des.y - src.y) == 1);
		}
		else{
			return false;
		}
	}


	/**
	 * 求解棋盘上格点Point a、b 间距离
	 * **/
	public double Distance(Point a,Point b){
		return a.distance(b);
	}

	/**
	 * 求解棋盘上同一直线上的两个格点间有几个棋子
	 * 用于判断棋子是否可以行走
	 * 以及炮是否可以吃子
	 * @param NowBrd:待求棋盘
	 * @param a:棋盘格点a
	 * @param b:棋盘格点b
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
			// 不在一条线上，返回一个极大值
			return Integer.MAX_VALUE;
		}
		return cnt;
	}


}
