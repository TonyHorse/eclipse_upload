package ChessBase;

import java.awt.Point;

import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.IOException;

import ChessBase.ChessBoarder;

/**
 * ��һ���������������������Ӧ�������߷�
 * **/
public class ManualInfoParse {
	public ManualInfoParse() {
		// 1000�����������
		// �൱��˫�Ŵ�ս500�غ�
		this.InfoPos = 0;
		this.AllManuInfo = new String[1000];
	}


	/**
	 * ��������������һ������ʱ����
	 * **/
	public void Init() {
		// ����λ�ù�0
		this.InfoPos = 0;

		// �����׳�ʼ��
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
	 * ����������Ϣ
	 * ��һ������һ������ģ��
	 **************************************************/
	public boolean SetManual(BufferedReader fr) {
		// 1000������������൱��˫����ս500��
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

		// ������Ϣ��Ҫ��������λ��
		this.InfoPos = 0;
		// ������Ϣ��Ϊ0��˵���������׳ɹ�
		return cnt > 0;

	}

	public void WithdrawParseOneStep() {
		this.InfoPos--;
	}


	/***************************************************
	 * ����ģ�飻������������߷���Ϣ
	 **************************************************/
	/**
	 * ÿһ���������ǻ��ڵ�ǰ������״̬����
	 * �������Լ�С������
	 * ������һ��ȫ��������
	 * **/
	public Point[] GetMove(ChessMatch DemoMatch) {
		String NowInfo = this.AllManuInfo[this.InfoPos++];
		if(NowInfo == null) {
			return null;
		}

		if(NowInfo.length() < 4 || NowInfo.length() > 5) {
			System.out.println("[ManualInfoParse]:���׶������");
			return null;
		}

		Point[] res = new Point[2];

		char[] ManuWord = new char[5];
		if(NowInfo.length() == 5) {
			// ��������
			for(int i = 0; i < 5; i++) {
				ManuWord[i] = NowInfo.charAt(i);
			}
		}
		else {
			// ��������
			// ����ǰ�����󡱱��Ϊ0
			ManuWord[0] = '��';
			for(int i = 0; i < 4; i++) {
				// û��ǰ��֮�֣�ֻ���ҵ�����
				ManuWord[i+1] = NowInfo.charAt(i);
			}
		}

		char whichside = (ManuWord[2] >= '1' && ManuWord[2] <= '9') ? '��' : '��';

		switch(ManuWord[1]) {
		case '��': 
			res = GetJuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetJuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetMaMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetXiangMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetXiangMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case 'ʿ': 
			res = GetShiMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetShiMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetBossMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '˧': 
			res = GetBossMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetPaoMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetBingOrZuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		case '��': 
			res = GetBingOrZuMove(DemoMatch, ManuWord[0], whichside, Label2num.get(ManuWord[2]), ManuWord[3], ManuWord[4]);
			break;
		default:
			System.out.println("[ManualInfoParse]:�����������("+ManuWord[1]+"), ����ʧ��");
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

		if(whichside == '��') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[0];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[8];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == 'ǰ') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '��'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,�ڳ�������");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == 'ƽ') {
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
				if(Pos == '��') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == 'ǰ'){
					res[0] = tmp1.y < tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,�쳵������");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == 'ƽ') {
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

		if(whichside == '��') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[1];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[7];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == 'ǰ') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '��'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,��������");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + (
						3 - Math.abs(Label2num.get(des) - srcx))];
			}
			else { // ������ƽ��ֻ�ܽ���
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
				if(Pos == '��') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == 'ǰ'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,��������");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + (
						3 - Math.abs(Label2num.get(des) - srcx))];
			}
			else { // ������ƽ��ֻ�ܽ���
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

		if(whichside == '��') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[2];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[6];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == 'ǰ') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '��'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,���󲻴���");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 2];
			}
			else { // �಻����ƽ��ֻ�ܽ���
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
				if(Pos == '��') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == 'ǰ'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,���಻����");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 2];
			}
			else { // �಻����ƽ��ֻ�ܽ���
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

		if(whichside == '��') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[3];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[5];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == 'ǰ') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '��'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,���󲻴���");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 1];
			}
			else { // ʿ������ƽ��ֻ�ܽ���
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
				if(Pos == '��') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == 'ǰ'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,���󲻴���");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y + 1];
			}
			else { // ʿ������ƽ��ֻ�ܽ���
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

		if(whichside == '��') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[4];
			if(tmp1 != null) {
				res[0] = tmp1;
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,�ڽ�������");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + 1];
			}
			else if(dir == 'ƽ') {
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
				System.out.println("[ManualInfoParse]:���׽�������,���಻����");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + 1];
			}
			else if(dir == 'ƽ') {
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

		if(whichside == '��') {
			tmp1 = DemoMatch.NowBoarder.PiecesPos[9];
			tmp2 = DemoMatch.NowBoarder.PiecesPos[10];
			if(tmp1 != null && tmp2 == null) {
				res[0] = tmp1;
			}
			else if(tmp1 == null && tmp2 != null) {
				res[0] = tmp2;
			}
			else if(tmp1 != null && tmp2 != null) {
				if(Pos == 'ǰ') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == '��'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,���ڲ�����");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == 'ƽ') {
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
				if(Pos == '��') {
					res[0] = tmp1.y > tmp2.y ? tmp1 : tmp2;
				}
				else if(Pos == 'ǰ'){
					res[0] = tmp1.y > tmp2.y ? tmp2 : tmp1;
				}
				else {
					res[0] = tmp1.x == srcx ? tmp1 : tmp2;
				}
			}
			else {
				System.out.println("[ManualInfoParse]:���׽�������,���ڲ�����");
				return null;
			}

			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + (des - '0')];
			}
			else if(dir == 'ƽ') {
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

		if(whichside == '��') {
			for(int i = 0; i < 5; i++) {
				tmp[i] = DemoMatch.NowBoarder.PiecesPos[11 + i];
				// ���������ϵ����޳�
				tmp[i] = (tmp[i] == null || tmp[i].x != srcx) ? null : tmp[i];
			}
			// ��srcx���ϵ��䰴�մӴ�С����
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

			// ��ǰ�����ҳ�ȷ��λ��
			if(Pos == 'ǰ') {
				res[0] = tmp[0];
			}
			else if(Pos == '��'){
				res[0] = tmp[1];
			}
			else if(Pos == '��') {
				res[0] = tmp[2];
			}
			else if(Pos == '��') {
				res[0] = tmp[3];
			}
			else if(Pos == '��'){
				// �ҵ����һ����Ϊ0�ļ���
				idx = 4;
				while(idx >= 0 && tmp[idx] == null) {
					idx--;
				}
				res[0] = tmp[idx];
			}
			else if(Pos == '��'){
				res[0] = tmp[0];
			}
			else {
				System.out.println("[ManualInfoParse]:�����������("+ Pos +"),�޷�����");
			}

			if(res[0] == null) {
				System.out.println("[ManualInfoParse]:����ʧ�ܣ�"+srcx+"·�ϲ����ں���");
				return null;
			}

			// ȷ������Ŀ�ص�����
			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y + 1];
			}
			else if(dir == 'ƽ') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				System.out.println("[ManualInfoParse]:�����߷�����");
			}

		}
		else {
			for(int i = 0; i < 5; i++) {
				tmp[i] = DemoMatch.NowBoarder.PiecesPos[27 + i];
				// ���������ϵ����޳�
				tmp[i] = (tmp[i] == null || tmp[i].x != srcx) ? null : tmp[i];
			}
			// ��srcx���ϵ��䰴�մ�ǰ��������
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

			// ��ǰ�����ҳ�ȷ��λ��
			if(Pos == 'ǰ') {
				res[0] = tmp[0];
			}
			else if(Pos == '��'){
				res[0] = tmp[1];
			}
			else if(Pos == '��') {
				res[0] = tmp[2];
			}
			else if(Pos == '��') {
				res[0] = tmp[3];
			}
			else if(Pos == '��'){
				// �ҵ����һ����Ϊnull�ļ���
				idx = 4;
				while(idx >= 0 && tmp[idx] == null) {
					idx--;
				}
				res[0] = tmp[idx];
			}
			else if(Pos == '��') {
				res[0] = tmp[0];
			}
			else {
				System.out.println("[ManualInfoParse]:�����������("+ Pos +"),�޷�����");
			}

			if(res[0] == null) {
				System.out.println("[ManualInfoParse]:����ʧ�ܣ�"+srcx+"·�ϲ����ں��");
				return null;
			}

			// ȷ������Ŀ�ص�����
			if(dir == '��') {
				res[1] = ChessBoarder.AllPos[res[0].x][res[0].y - 1];
			}
			else if(dir == 'ƽ') {
				res[1] = ChessBoarder.AllPos[Label2num.get(des)][res[0].y];
			}
			else {
				System.out.println("[ManualInfoParse]:����߷�����");
			}
		}
		return res;
	}


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	public String[] AllManuInfo;

	// ����������Ϣ���ڵ�λ��
	// �����һ����InfoPos++�������һ����InfoPos--
	public int InfoPos;


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	private static final Map<Character, Integer> Label2num = 
			new HashMap<Character, Integer>(){
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			// �췽·�߱�ʶ�����̺���������Ķ�Ӧ
			put('һ', 8);
			put('��', 7);
			put('��', 6);
			put('��', 5);
			put('��', 4);
			put('��', 3);
			put('��', 2);
			put('��', 1);
			put('��', 0);

			// �ڷ�
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
