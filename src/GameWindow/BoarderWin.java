package GameWindow;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import DefaultSet.BoarderDef;
import ChessBase.ChessMatch;
import ChessBase.ChessPiece;
import GameWindow.MainWindow;
import GameWindow.MyButton.TimerBtn;
import GameWindow.MyButton.PauseBtn;
import SomeListener.MatchWinnerListener;
import Sound.SoundResource;


/**
 * 棋盘窗体
 * **/
public class BoarderWin extends JPanel implements MatchWinnerListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 自行设定位置
	public BoarderWin(int xl, int yl, int xr, int yr) {
		this.bgLx = xl;
		this.bgLy = yl;
		this.bgRx = xr;
		this.bgRy = yr;
		this.winner = '无';
		this.NowMatch = new ChessMatch();
		this.SetClickEnabledFalse();
		this.srcp = null;
		this.desp = null;
		this.Reverse = false;
		this.setBounds(BoarderDef.CanvasPosX, BoarderDef.CanvasPosY, 
				BoarderDef.BoarderWidth, BoarderDef.BoarderHeight);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	// 默认使用如下位置
	public BoarderWin() {
		this(BoarderDef.CanvasPosX, BoarderDef.CanvasPosY, 
				BoarderDef.CanvasPosX+BoarderDef.BoarderWidth, 
				BoarderDef.CanvasPosY+BoarderDef.BoarderHeight);
	}

	public void BoarderWinInit() {
		this.winner = '无';
		if(this.NowMatch == null) {
			this.NowMatch = new ChessMatch();
		}
		else {
			this.NowMatch.MatchInit();
		}
		// 棋盘默认不响应鼠标事件
		this.SetClickEnabledFalse();
		// 选自框置空
		this.ChosePosInit();		
	}

	public void ChosePosInit() {
		// this.SetChosePos(null, null);
		this.srcp = null;
		this.desp = null;
	}	

	@Override
	public void OnWinnerChange(char winner) {
		if(winner != '无') {
			JOptionPane.showMessageDialog(this,
					winner+"方获得胜利", "中国象棋", JOptionPane.OK_OPTION);
			this.BoarderWinInit();
			this.pausebtn.Init();
		}
	}

	/***************************************************
	 * 设置各种提示图片
	 **************************************************/
	public void SetBackgroundImg(Image BgImg) {
		this.bgImage = BgImg;
	}

	public void SetBrdImg(Image bdImg) {
		this.boarderImg = bdImg;
	}

	public void SetChoseImg(Image cImg) {
		this.choseImg = cImg;
	}


	/***************************************************
	 * 棋盘自带走子与悔棋程序
	 * **************************************************/
	public Point BrdPieceMove() {
		// 走子也需要考虑是否翻转位置
		Point src, des;
		src = this.ReversePoint(this.Getsrcp());
		des = this.ReversePoint(this.Getdesp());

		ChessPiece despiece = this.NowMatch.NowBoarder.NowPieces[desp.y][des.x];

		Point res = this.NowMatch.PieceMove(src, des);

		if(res == null) {
			// 走子成功
			this.NowTimer.LastTimeInit();
			new Thread(()->{
				if(despiece == null) {
					SoundResource.PieceMoveSound.Start();
				}
				else {
					SoundResource.PieceEatSound.Start();
				}
				try{
					Thread.sleep(100);
				}catch(Exception e) {
					e.printStackTrace();
				}
				if(despiece == null) {
					SoundResource.PieceMoveSound.Close();
				}
				else {
					SoundResource.PieceEatSound.Close();
				}
			}).start();
			SwingUtilities.invokeLater(()->{
				new Thread(()->{
					this.myrepaint("BrdPieceMove");

					this.NowMatch.SwitchNowPlayer();
				}).start();
			});
		}
		else if(new Point(-1, -1).equals(res)){
			// 走子不符合规定导致走子失败
		}
		else {
			// 走子后己方被将军导致走子失败
		}
		return res;

	}
	public void BrdWithdraw() {
		this.NowTimer.LastTimeInit();
		this.NowMatch.WithDrawOneStep();
		this.myrepaint("[BrdWithdraw]");
	}


	/***************************************************
	 * 设置棋盘是否响应点击事件
	 * **************************************************/
	public void SetClickEnabledFalse() {
		this.ClickEnabled = false;
	}
	public void SetClickEnabledTrue() {
		this.ClickEnabled = true;
	}
	public void SwitchClickEnabledState() {
		this.ClickEnabled = !this.ClickEnabled;
	}



	/*
	 * 绘图时，翻转棋盘点位
	 * @param i: Point.y、棋盘的纵向
	 * @param j: Point.x、棋盘的横向
	 */
	private ChessPiece BrdWinReverse(int i, int j) {
		if(this.Reverse) { // 翻转棋盘
			return this.NowMatch.NowBoarder.NowPieces[9-i][8-j];
		}
		else {
			return this.NowMatch.NowBoarder.NowPieces[i][j];
		}
	}

	/*
	 * 走子时，翻转点位
	 */
	public Point ReversePoint(Point tmp) {
		if(this.Reverse) {
			return new Point(8-tmp.x, 9-tmp.y);
		}
		else {
			return tmp;
		}
	}

	public void SwitchReverseState() {
		this.Reverse = !this.Reverse;
		if(this.srcp != null) {
			this.srcp = this.ReversePoint(this.srcp);
		}
		if(this.desp != null) {
			this.desp = this.ReversePoint(this.desp);
		}
	}

	/***************************************************
	 * 获取各种棋盘状态
	 * 注意获取棋盘状态也是需要考虑是否翻转
	 **************************************************/
	public ChessPiece BrdGetPiece(int y, int x) {
		return this.BrdWinReverse(y, x);
	}
	public ChessPiece BrdGetPiece(Point tmp) {
		return this.BrdWinReverse(tmp.y, tmp.x);
	}
	public void SetPiece(int y, int x, ChessPiece tmp) {
		if(this.Reverse) {
			this.NowMatch.NowBoarder.NowPieces[9-y][8-x] = tmp;
		}
		else {
			this.NowMatch.NowBoarder.NowPieces[y][x] = tmp;
		}
	}
	public void SetPiece(Point p, ChessPiece tmp) {
		this.SetPiece(p.y, p.x, tmp);
	}


	/***************************************************
	 * 设置与获取srcp desp
	 * 更新srcp或者desp时需要重绘图像
	 **************************************************/
	public void Setdesp(Point desp) {
		this.desp = desp;
		this.myrepaint("[Setdesp]");
	}
	public void Setsrcp(Point srcp) {
		this.srcp = srcp;
		this.desp = null;
		this.myrepaint("[Setsrcp]");
	}
	public void SetChosePos(Point srcp, Point desp) {
		this.Setsrcp(srcp);
		this.Setdesp(desp);
	}
	public Point Getsrcp() {
		return this.srcp;
	}
	public Point Getdesp() {
		return this.desp;
	}
	public Point[] GetChosePos() {
		Point[] tmp = new Point[2];
		tmp[0] = this.Getsrcp();
		tmp[1] = this.Getdesp();
		return tmp;
	}

	/**
	 * 重写绘图函数
	 * **/
	@Override
	public void paintComponent(Graphics g) {

		// System.out.println("[BoarderWin]:paintComponent");

		// 背景图片，与游戏窗口背景一致，以免显得突兀
		g.drawImage(bgImage, 0, 0, this.getWidth(), this.getHeight(), 
				bgLx, bgLy, bgRx, bgRy, this);
		// 绘制棋盘
		g.drawImage(boarderImg,
				0, 0, boarderImg.getWidth(null), 
				boarderImg.getHeight(null), this);
		// 画棋子
		int xx = BoarderDef.ChessBoarderXX;
		int yy = BoarderDef.ChessBoarderYY;
		int pp = BoarderDef.ChessBoarderPP;
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 9; j++){
				if (this.BrdWinReverse(i, j) != null) {
					// 发现棋子就绘制
					g.drawImage(this.BrdWinReverse(i, j).Icon, 
							xx + j  * pp, yy + i * pp,
							this.BrdWinReverse(i, j).Icon.getWidth(null) , 
							this.BrdWinReverse(i, j).Icon.getHeight(null), this);

				}
			}
		}

		// 点击位置不需要翻转
		if (this.srcp != null){
			g.drawImage(choseImg, xx +  this.srcp.x * pp, 
					yy + this.srcp.y * pp, choseImg.getWidth(null) , 
					choseImg.getHeight(null), this);
		}
		if (this.desp != null){
			g.drawImage(choseImg, xx +  this.desp.x * pp, 
					yy + this.desp.y * pp, choseImg.getWidth(null) , 
					choseImg.getHeight(null), this);
		}

		//绘制胜利图片
		if (this.winner == '红'){
			g.drawImage(Toolkit.getDefaultToolkit().getImage(
					MainWindow.class.getResource(
							"/imageLibary/red-win.png")), 50, 270,559, 132, 
					this);
		}
		else if (this.winner == '黑'){
			g.drawImage(Toolkit.getDefaultToolkit().getImage(
					MainWindow.class.getResource(
							"/imageLibary/black-win.png")), 50, 270,559, 132, 
					this);
		}
	}

	public void myrepaint(String caller) {
		System.out.println("[BoarderWin]:" + caller + "调用了repaint");
		this.repaint();
	}


	/***************************************************
	 * 常量信息
	 **************************************************/
	// 默认棋盘图片
	Image boarderImg = Toolkit.getDefaultToolkit().getImage(
			MainWindow.class.getResource(
					"/imageLibary/chessboardafter.png"));

	// 默认选子用的方框
	Image choseImg = Toolkit.getDefaultToolkit().getImage(
			MainWindow.class.getResource(
					"/imageLibary/kuang.png"));

	// 默认背景图片
	// 因为棋盘图片周边有一些空白，为了不使得背景图片被遮盖掉
	// 此处在棋盘图片的窗体上补上相应的被掩盖的背景
	Image bgImage = Toolkit.getDefaultToolkit().getImage(
			MainWindow.class.getResource(
					"/imageLibary/background.png"));


	/***************************************************
	 * 变量信息
	 **************************************************/
	// 阻塞标志，是否阻塞鼠标点击事件
	// 用于机器方行走时使用
	public boolean ClickEnabled;

	// 现在的对局信息
	public ChessMatch NowMatch;

	// 现在的计时信息
	// 成功走子后NowTimer需要被翻转
	TimerBtn NowTimer;

	// 开始暂停按钮
	PauseBtn pausebtn;

	// 背景图片的左上角坐标
	int bgLx;
	int bgLy;

	// 背景图片的右下角坐标
	int bgRx;
	int bgRy;

	// 胜利方
	char winner = '无';

	// 被点击的棋盘格点
	private Point srcp = null;
	private Point desp = null;

	// 棋盘是否被翻转，默认为上黑下红
	public boolean Reverse = false;
	// 控制棋盘是否响应鼠标点击
	public boolean Unable = false;

}
