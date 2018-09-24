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
 * ���̴���
 * **/
public class BoarderWin extends JPanel implements MatchWinnerListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// �����趨λ��
	public BoarderWin(int xl, int yl, int xr, int yr) {
		this.bgLx = xl;
		this.bgLy = yl;
		this.bgRx = xr;
		this.bgRy = yr;
		this.winner = '��';
		this.NowMatch = new ChessMatch();
		this.SetClickEnabledFalse();
		this.srcp = null;
		this.desp = null;
		this.Reverse = false;
		this.setBounds(BoarderDef.CanvasPosX, BoarderDef.CanvasPosY, 
				BoarderDef.BoarderWidth, BoarderDef.BoarderHeight);
		this.setBorder(new EmptyBorder(0, 0, 0, 0));
	}

	// Ĭ��ʹ������λ��
	public BoarderWin() {
		this(BoarderDef.CanvasPosX, BoarderDef.CanvasPosY, 
				BoarderDef.CanvasPosX+BoarderDef.BoarderWidth, 
				BoarderDef.CanvasPosY+BoarderDef.BoarderHeight);
	}

	public void BoarderWinInit() {
		this.winner = '��';
		if(this.NowMatch == null) {
			this.NowMatch = new ChessMatch();
		}
		else {
			this.NowMatch.MatchInit();
		}
		// ����Ĭ�ϲ���Ӧ����¼�
		this.SetClickEnabledFalse();
		// ѡ�Կ��ÿ�
		this.ChosePosInit();		
	}

	public void ChosePosInit() {
		// this.SetChosePos(null, null);
		this.srcp = null;
		this.desp = null;
	}	

	@Override
	public void OnWinnerChange(char winner) {
		if(winner != '��') {
			JOptionPane.showMessageDialog(this,
					winner+"�����ʤ��", "�й�����", JOptionPane.OK_OPTION);
			this.BoarderWinInit();
			this.pausebtn.Init();
		}
	}

	/***************************************************
	 * ���ø�����ʾͼƬ
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
	 * �����Դ�������������
	 * **************************************************/
	public Point BrdPieceMove() {
		// ����Ҳ��Ҫ�����Ƿ�תλ��
		Point src, des;
		src = this.ReversePoint(this.Getsrcp());
		des = this.ReversePoint(this.Getdesp());

		ChessPiece despiece = this.NowMatch.NowBoarder.NowPieces[desp.y][des.x];

		Point res = this.NowMatch.PieceMove(src, des);

		if(res == null) {
			// ���ӳɹ�
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
			// ���Ӳ����Ϲ涨��������ʧ��
		}
		else {
			// ���Ӻ󼺷���������������ʧ��
		}
		return res;

	}
	public void BrdWithdraw() {
		this.NowTimer.LastTimeInit();
		this.NowMatch.WithDrawOneStep();
		this.myrepaint("[BrdWithdraw]");
	}


	/***************************************************
	 * ���������Ƿ���Ӧ����¼�
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
	 * ��ͼʱ����ת���̵�λ
	 * @param i: Point.y�����̵�����
	 * @param j: Point.x�����̵ĺ���
	 */
	private ChessPiece BrdWinReverse(int i, int j) {
		if(this.Reverse) { // ��ת����
			return this.NowMatch.NowBoarder.NowPieces[9-i][8-j];
		}
		else {
			return this.NowMatch.NowBoarder.NowPieces[i][j];
		}
	}

	/*
	 * ����ʱ����ת��λ
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
	 * ��ȡ��������״̬
	 * ע���ȡ����״̬Ҳ����Ҫ�����Ƿ�ת
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
	 * �������ȡsrcp desp
	 * ����srcp����despʱ��Ҫ�ػ�ͼ��
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
	 * ��д��ͼ����
	 * **/
	@Override
	public void paintComponent(Graphics g) {

		// System.out.println("[BoarderWin]:paintComponent");

		// ����ͼƬ������Ϸ���ڱ���һ�£������Ե�ͻأ
		g.drawImage(bgImage, 0, 0, this.getWidth(), this.getHeight(), 
				bgLx, bgLy, bgRx, bgRy, this);
		// ��������
		g.drawImage(boarderImg,
				0, 0, boarderImg.getWidth(null), 
				boarderImg.getHeight(null), this);
		// ������
		int xx = BoarderDef.ChessBoarderXX;
		int yy = BoarderDef.ChessBoarderYY;
		int pp = BoarderDef.ChessBoarderPP;
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 9; j++){
				if (this.BrdWinReverse(i, j) != null) {
					// �������Ӿͻ���
					g.drawImage(this.BrdWinReverse(i, j).Icon, 
							xx + j  * pp, yy + i * pp,
							this.BrdWinReverse(i, j).Icon.getWidth(null) , 
							this.BrdWinReverse(i, j).Icon.getHeight(null), this);

				}
			}
		}

		// ���λ�ò���Ҫ��ת
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

		//����ʤ��ͼƬ
		if (this.winner == '��'){
			g.drawImage(Toolkit.getDefaultToolkit().getImage(
					MainWindow.class.getResource(
							"/imageLibary/red-win.png")), 50, 270,559, 132, 
					this);
		}
		else if (this.winner == '��'){
			g.drawImage(Toolkit.getDefaultToolkit().getImage(
					MainWindow.class.getResource(
							"/imageLibary/black-win.png")), 50, 270,559, 132, 
					this);
		}
	}

	public void myrepaint(String caller) {
		System.out.println("[BoarderWin]:" + caller + "������repaint");
		this.repaint();
	}


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	// Ĭ������ͼƬ
	Image boarderImg = Toolkit.getDefaultToolkit().getImage(
			MainWindow.class.getResource(
					"/imageLibary/chessboardafter.png"));

	// Ĭ��ѡ���õķ���
	Image choseImg = Toolkit.getDefaultToolkit().getImage(
			MainWindow.class.getResource(
					"/imageLibary/kuang.png"));

	// Ĭ�ϱ���ͼƬ
	// ��Ϊ����ͼƬ�ܱ���һЩ�հף�Ϊ�˲�ʹ�ñ���ͼƬ���ڸǵ�
	// �˴�������ͼƬ�Ĵ����ϲ�����Ӧ�ı��ڸǵı���
	Image bgImage = Toolkit.getDefaultToolkit().getImage(
			MainWindow.class.getResource(
					"/imageLibary/background.png"));


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	// ������־���Ƿ�����������¼�
	// ���ڻ���������ʱʹ��
	public boolean ClickEnabled;

	// ���ڵĶԾ���Ϣ
	public ChessMatch NowMatch;

	// ���ڵļ�ʱ��Ϣ
	// �ɹ����Ӻ�NowTimer��Ҫ����ת
	TimerBtn NowTimer;

	// ��ʼ��ͣ��ť
	PauseBtn pausebtn;

	// ����ͼƬ�����Ͻ�����
	int bgLx;
	int bgLy;

	// ����ͼƬ�����½�����
	int bgRx;
	int bgRy;

	// ʤ����
	char winner = '��';

	// ����������̸��
	private Point srcp = null;
	private Point desp = null;

	// �����Ƿ񱻷�ת��Ĭ��Ϊ�Ϻ��º�
	public boolean Reverse = false;
	// ���������Ƿ���Ӧ�����
	public boolean Unable = false;

}
