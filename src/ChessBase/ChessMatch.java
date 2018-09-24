package ChessBase;

import java.util.Stack;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import java.awt.Point;

import SomeListener.MatchPlayerListener;
import SomeListener.MatchWinnerListener;

/**
 * ����Ծ���
 * ������ǰִ�ӷ�NowPlayer
 * ����״̬NowBoarder
 * ����PieceMove
 * ����PieceEat
 * ����WithdrawOneStep
 * �Ȳ���
 * **/
public class ChessMatch extends ChessRules{
	/*
	 * ���캯��
	 */
	public ChessMatch() {
		this.NowBoarder = new ChessBoarder();
		this.NowManual = new LinkedList<String>();
		this.StepsList =  new Stack<StepInfo>();
		this.PlayerListener = new ArrayList<MatchPlayerListener>();
		this.WinnerListener = new ArrayList<MatchWinnerListener>();
		this.MatchInit();
	}

	/**
	 * ��������Ϊ��ʼ״̬
	 * **/
	public void MatchInit() {
		// ��ʼ��֮�����̾�������¼����ᱻ�ı�
		this.NowPlayer = '��';		
		this.ListenPlayerChange(this.NowPlayer);


		this.Winner = '��';
		this.ListenWinnerChange(this.Winner);

		if(this.NowBoarder == null) {
			this.NowBoarder = new ChessBoarder();
		}
		else {
			this.NowBoarder.ChessBoarderInit();
			// System.out.println("[ChessMatch]:�������Ϊ��ʼ״̬");
		}

		if(this.StepsList == null) {
			this.StepsList =  new Stack<StepInfo>();
		}
		else {
			this.StepsList.clear();
		}

		if(this.NowManual == null) {
			this.NowManual = new LinkedList<String>();
		}
		else {
			this.NowManual.clear();
		}

	}


	/***************************************************
	 * ��������仯
	 **************************************************/
	/**
	 * ������ǰִ�ӷ��ı仯
	 * **/
	private List<MatchPlayerListener> PlayerListener;

	public void addPlayerListner(MatchPlayerListener tmp) {
		this.PlayerListener.add(tmp);
	}

	private void ListenPlayerChange(char tmp) {
		if(this.PlayerListener != null) {
			int cnt = this.PlayerListener.size();
			for(int i = 0; i < cnt; i++) {
				this.PlayerListener.get(i).OnPlayerChange(tmp);
				System.out.println("[ChessMatch]:["
						+ this.PlayerListener.get(i).getClass().getName()
						+ "]��⵽��Player�ı仯�����ڲ�ȡ�ж�");
			}
		}
		else {
			System.out.println("[ChessMatch]:δ��ʼ��PlayerListener");
		}
	}


	/*
	 * ����ʤ�ߵı仯
	 */
	private List<MatchWinnerListener> WinnerListener;

	public void addWinnerListner(MatchWinnerListener tmp) {
		this.WinnerListener.add(tmp);
	}

	private void ListenWinnerChange(char tmp) {
		if(this.WinnerListener != null) {
			int cnt = this.WinnerListener.size();
			for(int i = 0; i < cnt; i++) {
				this.WinnerListener.get(i).OnWinnerChange(tmp);
				System.out.println("[ChessMatch]:["
						+ this.WinnerListener.get(i).getClass().getName()
						+ "]��⵽��Winner�ı仯�����ڲ�ȡ�ж�");
			}
		}
		else {
			System.out.println("[ChessMatch]:δ��ʼ��WinnerListener");
		}
	}


	/*
	 * ����ִ�ӷ�
	 */
	public void SwitchNowPlayer() {
		if(this.NowPlayer == '��') {
			this.NowPlayer = '��';
		}
		else {
			this.NowPlayer = '��';
		}
		System.out.println("[ChessMatch]:����ִ�ӷ�Ϊ��"+this.NowPlayer);
		this.ListenPlayerChange(this.NowPlayer);

	}


	/***************************************************
	 * �����ƶ�ģ�飬����ֱ���ƶ��ͳ���
	 * (��Ϊ����ģ���ǿ��ǵ�����������ӵĲ�ͬ���)
	 **************************************************/
	/*
	 * ����
	 * @return:
	 * 	1�����ӳɹ�, ����null
	 * 	2������ʧ�ܣ��������Ӻ󷸣��淵�ص��½����ĶԷ�����λ��
	 * 	3������ʧ�ܣ������߷����Ϲ��򣬷���Point(-1, -1)
	 */
	public Point PieceMove(Point src, Point des) {
		Point tmp;
		if(this.NowBoarder.NowPieces[des.y][des.x] != null) {
			if(this.NowBoarder.NowPieces[src.y][src.x] != null) {
				tmp = this.PieceEat(src, des);
				if(tmp == null) {
					this.SetWinner(this.GetWinner());
				}
				return tmp;
			}
			else {
				return null;
			}
		}
		else {
			if(this.NowBoarder.NowPieces[src.y][src.x] != null) {
				tmp = this.PriPieceMove(src, des);
				if(tmp == null) {
					this.SetWinner(this.GetWinner());
				}
				return tmp;
			}
			else {
				return null;
			}
		}
	}

	/*
	 * �ƶ�
	 * @return:
	 * 	1���ƶ��ɹ�, ����null
	 * 	2���ƶ�ʧ�ܣ��������Ӻ󷸣��淵�ص��½����ĶԷ�����λ��
	 * 	3���ƶ�ʧ�ܣ������߷����Ϲ��򣬷���Point(-1, -1)
	 */
	private Point PriPieceMove(Point src, Point des) {
		if(this.CanPieceMove(this.NowBoarder, src, des)) {
			// �������Ӻ󲻻ᱻ����
			// �ȼ�¼��������Ϣ
			RecordStep(src, des);

			// �ٸ���PiecePos, ʹsrc������ָ�����λ��des
			this.NowBoarder.PiecesPos[this.NowBoarder.NowPieces[src.y][src.x].id] = des;

			// ������
			this.NowBoarder.NowPieces[des.y][des.x] = 
					this.NowBoarder.NowPieces[src.y][src.x];
			this.NowBoarder.NowPieces[src.y][src.x] = null;


			// �������ӽ������ж������Ƿ��н�������
			// ��������������˵������Υ�棬�������Ӳ�������Ӧ��ʾ
			Point tmp = this.IsChecked(this.NowPlayer);
			if(tmp != null) {
				this.WithDrawOneStep();
				System.out.println("[ChessMatch]:"+this.NowBoarder.NowPieces[src.y][src.x].name+"���Ӻ󱻽��������ӷ���");
				return tmp;
			}

			// �ж϶Է��Ƿ񱻽�����Ȼ�������ʾ
			tmp = this.IsChecked((char) (this.NowPlayer ^ ('��' ^ '��')));
			if(tmp != null) {
				System.out.println("[ChessMatch]:" + ((char) (this.NowPlayer ^ ('��' ^ '��'))) + "��������");
			}

			return null;
		}
		else {
			return new Point(-1, -1);
		}
	}

	/*
	 * ����
	 * @return:
	 * 	1�����ӳɹ�, ����null
	 * 	2������ʧ�ܣ��������Ӻ󷸣��淵�ص��½����ĶԷ�����λ��
	 * 	3������ʧ�ܣ������߷����Ϲ��򣬷���Point(-1, -1)
	 */
	private Point PieceEat(Point src, Point des) {
		if(this.CanPieceEat(this.NowBoarder, src, des)) {
			// �ȼ�¼��������Ϣ
			RecordStep(src, des);

			// �ٸ���PiecePos��des�����ӱ�Ϊnull,src�����ӱ�Ϊdes
			this.NowBoarder.PiecesPos[this.NowBoarder.NowPieces[src.y][src.x].id] = des;
			this.NowBoarder.PiecesPos[this.NowBoarder.NowPieces[des.y][des.x].id] = null;

			// ������
			this.NowBoarder.NowPieces[des.y][des.x] = 
					this.NowBoarder.NowPieces[src.y][src.x];
			this.NowBoarder.NowPieces[src.y][src.x] = null;

			// �������ӽ������ж������Ƿ��н�������
			// ��������������˵������Υ�棬�������Ӳ�������Ӧ��ʾ
			Point tmp = this.IsChecked(this.NowPlayer);
			if(tmp != null) {
				System.out.println("[ChessMatch]:���Ӻ󱻽��������ӷ���");
				this.WithDrawOneStep();
				return tmp;
			}

			// �ж϶Է��Ƿ񱻽�����������ʾ
			tmp = this.IsChecked((char) (this.NowPlayer ^ ('��' ^ '��')));
			if(tmp != null) {
				System.out.println("[ChessMatch]:" + ((char) (this.NowPlayer ^ ('��' ^ '��'))) + "��������");
			}

			return null;
		}
		else {
			return new Point(-1, -1);
		}
	}


	/***************************************************
	 * �жϾ����Ƿ��н�������
	 * �Ƿ��н���(ĳһ���Ѿ����������)
	 **************************************************/
	/*
	 * ��Ե�ǰ��֣�����ִ�ӷ���˭
	 * ֻ���ж�@param:whichPlayerһ���Ƿ��б���������
	 * @return Point,���ص���ĳһ�����±�����������
	 * ��������ڱ������򷵻�null
	 */
	public Point IsChecked(char whichPlayer) {
		if(whichPlayer == '��') {
			// �췽�Ƿ񱻽���     PiecesPos[20]Ϊ��˧λ��
			for(int i = 0; i < 16; i++) {
				if(i == 2 || i == 3 || i == 5 || i == 6) {
					// ʿ�����Ȼ���Ὣ������
					continue;
				}
				else if(i == 4) {
					// ��˧�Ƿ�����
					if(this.NowBoarder.PiecesPos[4] != null
							&& this.NowBoarder.PiecesPos[20] != null
							&& this.PieceCnt(this.NowBoarder, 
									this.NowBoarder.PiecesPos[4], 
									this.NowBoarder.PiecesPos[20]) == 0) {
						return this.NowBoarder.PiecesPos[4];
					}
				}
				else if(this.NowBoarder.PiecesPos[i] != null 
						&& this.NowBoarder.PiecesPos[20] != null
						&& this.CanPieceEat(this.NowBoarder, 
								this.NowBoarder.PiecesPos[i],
								this.NowBoarder.PiecesPos[20])) {
					return this.NowBoarder.PiecesPos[i];
				}
			}
			return null;
		}
		else {
			// �ڷ��Ƿ񱻽���     PiecesPos[4]Ϊ�ڽ�λ��
			for(int i = 16; i < 32; i++) {
				if(i == 18 || i == 19 || i == 21 || i == 22) {
					// ʿ�����Ȼ���Ὣ������
					continue;
				}
				else if(i == 20) {
					// ��˧�Ƿ�����
					if(this.NowBoarder.PiecesPos[4] != null
							&& this.NowBoarder.PiecesPos[20] != null
							&& this.PieceCnt(this.NowBoarder, 
									this.NowBoarder.PiecesPos[4], 
									this.NowBoarder.PiecesPos[20]) == 0) {
						return this.NowBoarder.PiecesPos[20];
					}
				}
				else if(this.NowBoarder.PiecesPos[i] != null 
						&& this.NowBoarder.PiecesPos[4] != null
						&& this.CanPieceEat(this.NowBoarder, 
								this.NowBoarder.PiecesPos[i], 
								this.NowBoarder.PiecesPos[4])) {
					return this.NowBoarder.PiecesPos[i];
				}
			}
			return null;
		}
	}


	/*
	 * �жϲ���ȡʤ����
	 * @return '��':�췽ʤ����'��':�ڷ�ʤ����'��':����ʤ��
	 * ������Boss���ӱ��Ե����ж��Ƿ����˱��Ե�
	 */
	public char GetWinner() {
		if(this.NowBoarder.PiecesPos[4] == null) {
			// ���Ѿ�����
			return '��';
		}
		else if(this.NowBoarder.PiecesPos[20] == null) {
			// ˧�Ѿ�����
			return '��';
		}
		else {
			// ��˧���ڣ��Ƿ�����������
			if(this.PieceCnt(this.NowBoarder, this.NowBoarder.PiecesPos[4],
					this.NowBoarder.PiecesPos[20]) == 0) {
				// ǰһ��������֮��ύ��ִ�ӷ����̴�ʱ��ִ�ӷ��൱���������
				return this.NowPlayer;
			}
			else {
				return '��';
			}
		}
	}

	public void SetWinner(char tmp) {
		this.Winner = tmp;
		if(tmp == '��') {
			return;
		}
		this.ListenWinnerChange(this.Winner);
	}

	/***************************************************
	 * ��¼������Ϣģ��
	 **************************************************/
	/**
	 * ������¼������Ϣ�Լ�����������������Ϣ
	 * ���ڿ��ܻ������ӱ���
	 * ������Ҫ��¼����
	 * **/
	public void RecordStep(Point src, Point des) {
		this.RecordStepMove(src, des);
		this.RecordStepInfo(src, des);
	}

	/**
	 * ��¼�����̸���ʾ���߲���Ϣ
	 * **/
	private void RecordStepMove(Point src, Point des) {
		if(this.NowBoarder.NowPieces[des.y][des.x] != null) {
			// Ŀ�ĵ������ӣ�˵���Ǳ��Ե�������
			this.StepsList.push(new StepInfo(this.NowBoarder.NowPieces[des.y][des.x].id,
					des, null));
			System.out.println("[ChessMatch]:"+this.NowBoarder.NowPieces[des.y][des.x].name+
					"��" + this.NowBoarder.NowPieces[src.y][src.x].name + "�Ե�");
		}
		else {
			System.out.println("[ChessMatch]:"+this.NowBoarder.NowPieces[src.y][src.x].name+"����");
		}
		// src���Ǳض��������ӵģ����������ж�
		this.StepsList.push(new StepInfo(this.NowBoarder.NowPieces[src.y][src.x].id, 
				src, des));
	}

	/**
	 * ����רҵ�������ﲢ��¼��������
	 * **/
	private void RecordStepInfo(Point src, Point des) {
		// ������������������֤src�ض���Ϊnull,�˴����������ж�
		this.NowManual.addLast(this.GenMoveInfo(this.NowBoarder, src, des));
	}



	/***************************************************
	 * ����ģ��
	 **************************************************/
	/**
	 * ��һ���壬����ʱ��Ҫ�޸ĵĶ����ڴ˴������޸�
	 * **/
	public boolean WithDrawOneStep() {
		this.WithdrawOneStepInfo();
		return this.WithdrawOneStepMove();
	}

	/**
	 * ����һ��������Ϣ
	 * **/
	private boolean WithdrawOneStepMove() {
		if(this.StepsList.empty()) {
			System.out.println("[ChessMatch]:�����Ѿ�Ϊ��ʼ״̬");
			this.NowPlayer = '��';
			this.ListenPlayerChange(this.NowPlayer);
			return false;
		}
		// �ָ���һ������
		StepInfo lastStep = (StepInfo) this.StepsList.pop();
		this.NowBoarder.NowPieces[lastStep.OriPos.y][lastStep.OriPos.x] = 
				this.NowBoarder.NowPieces[lastStep.NowPos.y][lastStep.NowPos.x];
		this.NowBoarder.PiecesPos[lastStep.pieceID] = lastStep.OriPos;
		this.NowBoarder.NowPieces[lastStep.NowPos.y][lastStep.NowPos.x] = null;

		if(this.StepsList.empty()) {
			System.out.println("[ChessMatch].WithdrawOneStepMove:1)���ָ̻�����ʼ״̬");
			this.NowPlayer = '��';
			this.ListenPlayerChange(this.NowPlayer);
			return true;
		}

		// �ж���һ�������Ƿ�Ϊ���Ե�������
		lastStep = (StepInfo) this.StepsList.peek();
		if(lastStep.NowPos == null) {
			// ˵���Ǳ��Ե������ӣ����䵯�����ָ�
			lastStep = (StepInfo) this.StepsList.pop();
			this.NowBoarder.NowPieces[lastStep.OriPos.y][lastStep.OriPos.x] = 
					ChessBoarder.AllPieces[lastStep.pieceID];
			this.NowBoarder.PiecesPos[lastStep.pieceID] = lastStep.OriPos;

			// �޸�NowPlayer
			if(this.StepsList.empty()) {
				System.out.println("[ChessMatch].WithdrawOneStepMove:2)���ָ̻�����ʼ״̬");
				this.NowPlayer = '��';
				this.ListenPlayerChange(this.NowPlayer);
				return true;
			}
			else {
				lastStep = (StepInfo) this.StepsList.peek();
				this.NowPlayer = new ChessPiece(lastStep.pieceID).name.charAt(0) == '��' ? 
						'��' : '��';
				this.ListenPlayerChange(this.NowPlayer);
				return true;
			}
		}
		else {
			this.NowPlayer = new ChessPiece(lastStep.pieceID).name.charAt(0) == '��' ? 
					'��' : '��';
			this.ListenPlayerChange(this.NowPlayer);
			return true;
		}
	}

	/**
	 * ����һ����������
	 * **/
	private boolean WithdrawOneStepInfo() {
		if(this.NowManual == null) {
			System.out.println("[ChessMatch]:�����б�����");
		}
		if(this.NowManual.isEmpty()) {
			return false;
		}
		// �������һ������
		// ��WithdrawOneStepMove������������
		this.NowManual.removeLast();
		return true;
	}


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	/**
	 * ��ǰִ�ӷ�
	 * **/
	public char NowPlayer = '��';

	/**
	 * ������Ϣ��˫�˶��У����ڻ���ʱ��β��ɾ��
	 * ����ʱ���׶ο�ʼд���ļ�(Ҳ��������˳��)
	 * **/
	public Deque<String> NowManual = null;

	/**
	 * ��ǰ��ʤ��
	 * **/
	public char Winner = '��';

	/**
	 * ��ǰ����״̬
	 * **/
	public ChessBoarder NowBoarder = null;

	/**
	 * ������̲�����������Ϣ�б�
	 * **/
	public Stack<StepInfo> StepsList = null;

}
