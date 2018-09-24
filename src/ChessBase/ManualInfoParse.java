package ChessBase;

import java.awt.Point;

import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.IOException;

import ChessBase.ChessBoarder;

/**
 * 对一句象棋术语，解析出他所对应的象棋走法
 * **/
public class ManualInfoParse {
	public ManualInfoParse() {
		// 1000个术语的容量
		// 相当于双放大战500回合
		this.InfoPos = 0;
		this.AllManuInfo = new String[1000];
	}


	/**
	 * 用于在重新载入一个棋谱时调用
	 * **/
	public void Init() {
		// 走棋位置归0
		this.InfoPos = 0;

		// 将棋谱初始化
		if(this.AllManuInfo == null) {
			this.AllManuInfo = new String[1000];
		}
		else {
			for(int i = 0; i < this.AllManuInfo.length; i++) {
				this.AllManuInfo[i] = null;
			}
		}
	}


	/***************************************************
	 * 设置棋谱信息
	 * 上一步与下一步操作模块
	 **************************************************/
	public boolean SetManual(BufferedReader fr) {
		// 1000步的走棋术语，相当于双方大战500回
		this.AllManuInfo = new String[1000];
		String tmpStr = null;
		int cnt = 0;
		do {
			try {
				tmpStr = fr.readLine();
				if(tmpStr != null) {
					this.AllManuInfo[cnt++] = tmpStr;
				}
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}while(tmpStr != null);

		try{
			fr.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}

		// 读入信息后要重置术语位置
		this.InfoPos = 0;
		// 读入信息不为0，说明读入棋谱成功
		return cnt > 0;

	}

	public void WithdrawParseOneStep() {
		this.InfoPos--;
	}


	/***************************************************
	 * 解析模块；由术语解析出走法信息
	 **************************************************/
	/**
	 * 每一步解析都是基于当前的棋盘状态进行
	 * 这样可以减小搜索量
	 * 而不是一次全都解析完
	 * **/
	public Point[] GetMove(ChessMatch DemoMatch) {
		String NowInfo = this.AllManuInfo[this.InfoPos++];
		if(NowInfo == null) {
			return null;
		}

		if(NowInfo.length() < 4 || NowInfo.length() > 5) {
			System.out.println("[ManualInfoParse]:棋谱读入出错");
			return null;
		}

		Point[] res = new Point[2];

		char[] ManuWord = new char[5];
		if(NowInfo.length() == 5) {
			// 五字术语
			for(int i = 0; i < 5; i++) {
				ManuWord[i] = NowInfo.charAt(i);
			}
		}
		else {
			// 四字术语
			// 将“前”“后”标记为0
			ManuWord[0] = '无';
			for(int i = 0; i < 4; i++) {
				// 没有前后之分，只需找到即可
				ManuWord[i+1] = NowInfo.charAt(i);
			}
		}

		char whichside = (ManuWord[2] >= '1' && ManuWord[2] <= '9') ? '黑' : '红';

		switch(ManuWord[1]) {
		case '车': 
			res = GetJuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '军': 
			res = GetJuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '马': 
			res = GetMaMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '象': 
			res = GetXiangMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '相': 
			res = GetXiangMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '士': 
			res = GetShiMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '仕': 
			res = GetShiMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '将': 
			res = GetBossMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '帅': 
			res = GetBossMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '炮': 
			res = GetPaoMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '兵': 
			res = GetBingOrZuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '卒': 
			res = GetBingOrZuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		default:
			System.out.println("[ManualInfoParse]:出现特殊符号("+ManuWord[1]+"), 解析失败");
			return null;
		}

		System.out.println("[ManualInfoParse]:from (" + res[0].y + "," + res[0].x + ") to (" 
				+ res[1].y + "," + res[1].x + ")");

		return res;
	}

	private Point[] GetJuMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des) {
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point tmp1, tmp2;

		if(whichside == '黑') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[0];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[8];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '前') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '后'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑车不存在");
				return null;
			}

			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - (des - '0')];
			}

		}
		else {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[16];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[24];

			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '后') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '前'){
					res[0] = tmp1.y < tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,红车不存在");
				return null;
			}

			if(dir == '退') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - (des - '0')];
			}

		}
		return res;
	}

	private Point[] GetMaMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des){
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point tmp1, tmp2;

		if(whichside == '黑') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[1];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[7];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '前') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '后'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑马不存在");
				return null;
			}

			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + (
						3 - Math.abs(Label2num.get(des) - srcx))];
			}
			else { // 马不可能平，只能进退
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y - (
						3 - Math.abs(Label2num.get(des) - srcx))];
			}

		}
		else {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[17];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[23];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '后') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '前'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,红马不存在");
				return null;
			}

			if(dir == '退') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + (
						3 - Math.abs(Label2num.get(des) - srcx))];
			}
			else { // 马不可能平，只能进退
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y - (
						3 - Math.abs(Label2num.get(des) - srcx))];
			}
		}
		return res;
	}

	private Point[] GetXiangMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des) {
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point tmp1, tmp2;

		if(whichside == '黑') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[2];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[6];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '前') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '后'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑象不存在");
				return null;
			}

			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 2];
			}
			else { // 相不可能平，只能进退
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y - 2];
			}

		}
		else {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[18];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[22];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '后') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '前'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,红相不存在");
				return null;
			}

			if(dir == '退') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 2];
			}
			else { // 相不可能平，只能进退
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y - 2];
			}
		}
		return res;
	}

	private Point[] GetShiMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des) {
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point tmp1, tmp2;

		if(whichside == '黑') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[3];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[5];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '前') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '后'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑象不存在");
				return null;
			}

			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 1];
			}
			else { // 士不可能平，只能进退
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y - 1];
			}

		}
		else {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[19];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[21];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '后') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '前'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑象不存在");
				return null;
			}

			if(dir == '退') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 1];
			}
			else { // 士不可能平，只能进退
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y - 1];
			}
		}
		return res;
	}

	private Point[] GetBossMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des) {
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point tmp1;

		if(whichside == '黑') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[4];
			if(tmp1 != null) {
				res[0] = tmp1;
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑将不存在");
				return null;
			}

			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + 1];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - 1];
			}

		}
		else {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[20];
			if(tmp1 != null) {
				res[0] = tmp1;
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,红相不存在");
				return null;
			}

			if(dir == '退') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + 1];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - 1];
			}
		}

		return res;
	}

	private Point[] GetPaoMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des) {
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point tmp1, tmp2;

		if(whichside == '黑') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[9];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[10];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '前') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '后'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,黑炮不存在");
				return null;
			}

			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - (des - '0')];
			}

		}
		else {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[25];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[26];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == '后') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '前'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:棋谱解析错误,红炮不存在");
				return null;
			}

			if(dir == '退') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - (des - '0')];
			}

		}
		return res;
	}

	private Point[] GetBingOrZuMove(ChessMatch DemoMatch, 
			char Pos, char whichside, 
			int srcx, char dir, char des){
		Point[] res = new Point[2];
		res[0] = null;
		res[1] = null;

		Point[] tmp = new Point[5];

		if(whichside == '黑') {
			for(int i = 0; i < 5; i++) {
				tmp[i] = DemoMatch.NowBoarder.PiecesPos[11 + i];
				// 将不在线上的卒剔除
				tmp[i] = (tmp[i] == null || tmp[i].x != srcx) ? null : tmp[i];
			}
			// 将srcx线上的卒按照从大到小排列
			int idx = 0;
			for(int i = 0; i < 5; i++) {
				res[0] = tmp[i];
				idx = i;
				for(int j = i+1; j < 5; j++) {
					if(tmp[j] != null) {
						if(res[0] == null || res[0].y < tmp[j].y) {
							res[0] = tmp[j];
							idx = j;
						}
					}
				}
				tmp[idx] = tmp[i];
				tmp[i] = res[0];
			}

			// 从前往后找出确切位置
			if(Pos == '前') {
				res[0] = tmp[0];
			}
			else if(Pos == '二'){
				res[0] = tmp[1];
			}
			else if(Pos == '三') {
				res[0] = tmp[2];
			}
			else if(Pos == '四') {
				res[0] = tmp[3];
			}
			else if(Pos == '后'){
				// 找到最后一个不为0的即可
				idx = 4;
				while(idx >= 0 && tmp[idx] == null) {
					idx--;
				}
				res[0] = tmp[idx];
			}
			else if(Pos == '无'){
				res[0] = tmp[0];
			}
			else {
				System.out.println("[ManualInfoParse]:出现特殊符号("+ Pos +"),无法解析");
			}

			if(res[0] == null) {
				System.out.println("[ManualInfoParse]:解析失败，"+srcx+"路上不存在黑卒");
				return null;
			}

			// 确定行走目地点坐标
			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + 1];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				System.out.println("[ManualInfoParse]:黑卒走法错误");
			}

		}
		else {
			for(int i = 0; i < 5; i++) {
				tmp[i] = DemoMatch.NowBoarder.PiecesPos[27 + i];
				// 将不在线上的卒剔除
				tmp[i] = (tmp[i] == null || tmp[i].x != srcx) ? null : tmp[i];
			}
			// 将srcx线上的卒按照从前到后排列
			int idx = 0;
			for(int i = 0; i < 5; i++) {
				res[0] = tmp[i];
				idx = i;
				for(int j = i+1; j < 5; j++) {
					if(tmp[j] != null) {
						if(res[0] == null || res[0].y > tmp[j].y) {
							res[0] = tmp[j];
							idx = j;
						}
					}
				}
				tmp[idx] = tmp[i];
				tmp[i] = res[0];
			}

			// 从前往后找出确切位置
			if(Pos == '前') {
				res[0] = tmp[0];
			}
			else if(Pos == '二'){
				res[0] = tmp[1];
			}
			else if(Pos == '三') {
				res[0] = tmp[2];
			}
			else if(Pos == '四') {
				res[0] = tmp[3];
			}
			else if(Pos == '后'){
				// 找到最后一个不为null的即可
				idx = 4;
				while(idx >= 0 && tmp[idx] == null) {
					idx--;
				}
				res[0] = tmp[idx];
			}
			else if(Pos == '无') {
				res[0] = tmp[0];
			}
			else {
				System.out.println("[ManualInfoParse]:出现特殊符号("+ Pos +"),无法解析");
			}

			if(res[0] == null) {
				System.out.println("[ManualInfoParse]:解析失败，"+srcx+"路上不存在红兵");
				return null;
			}

			// 确定行走目地点坐标
			if(dir == '进') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - 1];
			}
			else if(dir == '平') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				System.out.println("[ManualInfoParse]:红兵走法错误");
			}
		}
		return res;
	}


	/***************************************************
	 * 变量信息
	 **************************************************/
	public String[] AllManuInfo;

	// 待解析的信息所在的位置
	// 点击下一步则InfoPos++，点击上一步则InfoPos--
	public int InfoPos;


	/***************************************************
	 * 常量信息
	 **************************************************/
	private static final Map<Character, Integer> Label2num = 
			new HashMap<Character, Integer>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			// 红方路线标识与棋盘横向格点坐标的对应
			put('一', 8);
			put('二', 7);
			put('三', 6);
			put('四', 5);
			put('五', 4);
			put('六', 3);
			put('七', 2);
			put('八', 1);
			put('九', 0);

			// 黑方
			put('9', 8);
			put('8', 7);
			put('7', 6);
			put('6', 5);
			put('5', 4);
			put('4', 3);
			put('3', 2);
			put('2', 1);
			put('1', 0);

		}
	};

}
