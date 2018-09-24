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
 * 象棋对局类
 * 包括当前执子方NowPlayer
 * 棋盘状态NowBoarder
 * 走子PieceMove
 * 吃子PieceEat
 * 悔棋WithdrawOneStep
 * 等操作
 * **/
public class ChessMatch extends ChessRules{
	/*
	 * 构造函数
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
	 * 棋盘重置为初始状态
	 * **/
	public void MatchInit() {
		// 初始化之后棋盘局面监听事件不会被改变
		this.NowPlayer = '红';		
		this.ListenPlayerChange(this.NowPlayer);


		this.Winner = '无';
		this.ListenWinnerChange(this.Winner);

		if(this.NowBoarder == null) {
			this.NowBoarder = new ChessBoarder();
		}
		else {
			this.NowBoarder.ChessBoarderInit();
			// System.out.println("[ChessMatch]:重置棋局为初始状态");
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
	 * 监听局面变化
	 **************************************************/
	/**
	 * 监听当前执子方的变化
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
						+ "]监测到了Player的变化，正在采取行动");
			}
		}
		else {
			System.out.println("[ChessMatch]:未初始化PlayerListener");
		}
	}


	/*
	 * 监听胜者的变化
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
						+ "]监测到了Winner的变化，正在采取行动");
			}
		}
		else {
			System.out.println("[ChessMatch]:未初始化WinnerListener");
		}
	}


	/*
	 * 交换执子方
	 */
	public void SwitchNowPlayer() {
		if(this.NowPlayer == '黑') {
			this.NowPlayer = '红';
		}
		else {
			this.NowPlayer = '黑';
		}
		System.out.println("[ChessMatch]:更换执子方为："+this.NowPlayer);
		this.ListenPlayerChange(this.NowPlayer);

	}


	/***************************************************
	 * 棋子移动模块，包括直接移动和吃子
	 * (分为两个模块是考虑到炮走子与吃子的不同情况)
	 **************************************************/
	/*
	 * 走子
	 * @return:
	 * 	1）走子成功, 返回null
	 * 	2）走子失败，己方走子后犯，规返回导致将军的对方棋子位置
	 * 	3）走子失败，由于走法不合规则，返回Point(-1, -1)
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
	 * 移动
	 * @return:
	 * 	1）移动成功, 返回null
	 * 	2）移动失败，己方走子后犯，规返回导致将军的对方棋子位置
	 * 	3）移动失败，由于走法不合规则，返回Point(-1, -1)
	 */
	private Point PriPieceMove(Point src, Point des) {
		if(this.CanPieceMove(this.NowBoarder, src, des)) {
			// 假设走子后不会被将军
			// 先记录下走子信息
			RecordStep(src, des);

			// 再更改PiecePos, 使src点棋子指向的新位置des
			this.NowBoarder.PiecesPos[this.NowBoarder.NowPieces[src.y][src.x].id] = des;

			// 再走子
			this.NowBoarder.NowPieces[des.y][des.x] = 
					this.NowBoarder.NowPieces[src.y][src.x];
			this.NowBoarder.NowPieces[src.y][src.x] = null;


			// 假设走子结束，判断棋盘是否有将军现象
			// 若己方被将军，说明走子违规，撤销走子并进行相应提示
			Point tmp = this.IsChecked(this.NowPlayer);
			if(tmp != null) {
				this.WithDrawOneStep();
				System.out.println("[ChessMatch]:"+this.NowBoarder.NowPieces[src.y][src.x].name+"走子后被将军，走子犯规");
				return tmp;
			}

			// 判断对方是否被将军、然后进行提示
			tmp = this.IsChecked((char) (this.NowPlayer ^ ('红' ^ '黑')));
			if(tmp != null) {
				System.out.println("[ChessMatch]:" + ((char) (this.NowPlayer ^ ('红' ^ '黑'))) + "方被将军");
			}

			return null;
		}
		else {
			return new Point(-1, -1);
		}
	}

	/*
	 * 吃子
	 * @return:
	 * 	1）吃子成功, 返回null
	 * 	2）吃子失败，己方走子后犯，规返回导致将军的对方棋子位置
	 * 	3）吃子失败，由于走法不合规则，返回Point(-1, -1)
	 */
	private Point PieceEat(Point src, Point des) {
		if(this.CanPieceEat(this.NowBoarder, src, des)) {
			// 先记录下走子信息
			RecordStep(src, des);

			// 再更改PiecePos，des点棋子变为null,src点棋子变为des
			this.NowBoarder.PiecesPos[this.NowBoarder.NowPieces[src.y][src.x].id] = des;
			this.NowBoarder.PiecesPos[this.NowBoarder.NowPieces[des.y][des.x].id] = null;

			// 再走子
			this.NowBoarder.NowPieces[des.y][des.x] = 
					this.NowBoarder.NowPieces[src.y][src.x];
			this.NowBoarder.NowPieces[src.y][src.x] = null;

			// 假设走子结束，判断棋盘是否有将军现象
			// 若己方被将军，说明走子违规，撤销走子并进行相应提示
			Point tmp = this.IsChecked(this.NowPlayer);
			if(tmp != null) {
				System.out.println("[ChessMatch]:走子后被将军，走子犯规");
				this.WithDrawOneStep();
				return tmp;
			}

			// 判断对方是否被将军、进行提示
			tmp = this.IsChecked((char) (this.NowPlayer ^ ('红' ^ '黑')));
			if(tmp != null) {
				System.out.println("[ChessMatch]:" + ((char) (this.NowPlayer ^ ('红' ^ '黑'))) + "方被将军");
			}

			return null;
		}
		else {
			return new Point(-1, -1);
		}
	}


	/***************************************************
	 * 判断局面是否有将军现象
	 * 是否有将死(某一方已经输棋的现象)
	 **************************************************/
	/*
	 * 针对当前棋局，不管执子方是谁
	 * 只是判断@param:whichPlayer一方是否有被将军现象
	 * @return Point,返回的是某一个导致被将军的棋子
	 * 如果不存在被将军则返回null
	 */
	public Point IsChecked(char whichPlayer) {
		if(whichPlayer == '红') {
			// 红方是否被将军     PiecesPos[20]为红帅位置
			for(int i = 0; i < 16; i++) {
				if(i == 2 || i == 3 || i == 5 || i == 6) {
					// 士、象必然不会将军对面
					continue;
				}
				else if(i == 4) {
					// 将帅是否碰面
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
			// 黑方是否被将军     PiecesPos[4]为黑将位置
			for(int i = 16; i < 32; i++) {
				if(i == 18 || i == 19 || i == 21 || i == 22) {
					// 士、象必然不会将军对面
					continue;
				}
				else if(i == 20) {
					// 将帅是否碰面
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
	 * 判断并获取胜利方
	 * @return '红':红方胜利、'黑':黑方胜利、'无':无人胜利
	 * 暂且以Boss棋子被吃掉来判断是否有人被吃掉
	 */
	public char GetWinner() {
		if(this.NowBoarder.PiecesPos[4] == null) {
			// 将已经不在
			return '红';
		}
		else if(this.NowBoarder.PiecesPos[20] == null) {
			// 帅已经还在
			return '黑';
		}
		else {
			// 将帅都在，是否有碰面现象
			if(this.PieceCnt(this.NowBoarder, this.NowBoarder.PiecesPos[4],
					this.NowBoarder.PiecesPos[20]) == 0) {
				// 前一方走完子之后会交换执子方，固此时的执子方相当于守株待兔
				return this.NowPlayer;
			}
			else {
				return '无';
			}
		}
	}

	public void SetWinner(char tmp) {
		this.Winner = tmp;
		if(tmp == '无') {
			return;
		}
		this.ListenWinnerChange(this.Winner);
	}

	/***************************************************
	 * 记录走棋信息模块
	 **************************************************/
	/**
	 * 走棋后记录走子信息以及产生的棋谱术语信息
	 * 由于可能会有棋子被吃
	 * 可能需要记录两次
	 * **/
	public void RecordStep(Point src, Point des) {
		this.RecordStepMove(src, des);
		this.RecordStepInfo(src, des);
	}

	/**
	 * 记录用棋盘格点表示的走步信息
	 * **/
	private void RecordStepMove(Point src, Point des) {
		if(this.NowBoarder.NowPieces[des.y][des.x] != null) {
			// 目的点有棋子，说明是被吃掉的棋子
			this.StepsList.push(new StepInfo(this.NowBoarder.NowPieces[des.y][des.x].id,
					des, null));
			System.out.println("[ChessMatch]:"+this.NowBoarder.NowPieces[des.y][des.x].name+
					"被" + this.NowBoarder.NowPieces[src.y][src.x].name + "吃掉");
		}
		else {
			System.out.println("[ChessMatch]:"+this.NowBoarder.NowPieces[src.y][src.x].name+"走子");
		}
		// src点是必定存在棋子的，不用特殊判断
		this.StepsList.push(new StepInfo(this.NowBoarder.NowPieces[src.y][src.x].id, 
				src, des));
	}

	/**
	 * 产生专业象棋术语并记录如棋谱中
	 * **/
	private void RecordStepInfo(Point src, Point des) {
		// 由走棋规则与走棋程序保证src必定不为null,此处不做特殊判断
		this.NowManual.addLast(this.GenMoveInfo(this.NowBoarder, src, des));
	}



	/***************************************************
	 * 悔棋模块
	 **************************************************/
	/**
	 * 悔一步棋，撤销时需要修改的东西在此处进行修改
	 * **/
	public boolean WithDrawOneStep() {
		this.WithdrawOneStepInfo();
		return this.WithdrawOneStepMove();
	}

	/**
	 * 撤销一步走子信息
	 * **/
	private boolean WithdrawOneStepMove() {
		if(this.StepsList.empty()) {
			System.out.println("[ChessMatch]:棋盘已经为初始状态");
			this.NowPlayer = '红';
			this.ListenPlayerChange(this.NowPlayer);
			return false;
		}
		// 恢复第一个棋子
		StepInfo lastStep = (StepInfo) this.StepsList.pop();
		this.NowBoarder.NowPieces[lastStep.OriPos.y][lastStep.OriPos.x] = 
				this.NowBoarder.NowPieces[lastStep.NowPos.y][lastStep.NowPos.x];
		this.NowBoarder.PiecesPos[lastStep.pieceID] = lastStep.OriPos;
		this.NowBoarder.NowPieces[lastStep.NowPos.y][lastStep.NowPos.x] = null;

		if(this.StepsList.empty()) {
			System.out.println("[ChessMatch].WithdrawOneStepMove:1)棋盘恢复到初始状态");
			this.NowPlayer = '红';
			this.ListenPlayerChange(this.NowPlayer);
			return true;
		}

		// 判断下一个棋子是否为被吃掉的棋子
		lastStep = (StepInfo) this.StepsList.peek();
		if(lastStep.NowPos == null) {
			// 说明是被吃掉的棋子，将其弹出并恢复
			lastStep = (StepInfo) this.StepsList.pop();
			this.NowBoarder.NowPieces[lastStep.OriPos.y][lastStep.OriPos.x] = 
					ChessBoarder.AllPieces[lastStep.pieceID];
			this.NowBoarder.PiecesPos[lastStep.pieceID] = lastStep.OriPos;

			// 修改NowPlayer
			if(this.StepsList.empty()) {
				System.out.println("[ChessMatch].WithdrawOneStepMove:2)棋盘恢复到初始状态");
				this.NowPlayer = '红';
				this.ListenPlayerChange(this.NowPlayer);
				return true;
			}
			else {
				lastStep = (StepInfo) this.StepsList.peek();
				this.NowPlayer = new ChessPiece(lastStep.pieceID).name.charAt(0) == '红' ? 
						'黑' : '红';
				this.ListenPlayerChange(this.NowPlayer);
				return true;
			}
		}
		else {
			this.NowPlayer = new ChessPiece(lastStep.pieceID).name.charAt(0) == '红' ? 
					'黑' : '红';
			this.ListenPlayerChange(this.NowPlayer);
			return true;
		}
	}

	/**
	 * 撤销一句象棋术语
	 * **/
	private boolean WithdrawOneStepInfo() {
		if(this.NowManual == null) {
			System.out.println("[ChessMatch]:棋谱列表不存在");
		}
		if(this.NowManual.isEmpty()) {
			return false;
		}
		// 撤销最后一步术语
		// 由WithdrawOneStepMove决定撤销几次
		this.NowManual.removeLast();
		return true;
	}


	/***************************************************
	 * 变量信息
	 **************************************************/
	/**
	 * 当前执子方
	 * **/
	public char NowPlayer = '红';

	/**
	 * 棋谱信息，双端队列，便于悔棋时从尾端删除
	 * 保存时从首段开始写入文件(也就是走棋顺序)
	 * **/
	public Deque<String> NowManual = null;

	/**
	 * 当前获胜方
	 * **/
	public char Winner = '无';

	/**
	 * 当前棋盘状态
	 * **/
	public ChessBoarder NowBoarder = null;

	/**
	 * 下棋过程产生的走子信息列表
	 * **/
	public Stack<StepInfo> StepsList = null;

}
