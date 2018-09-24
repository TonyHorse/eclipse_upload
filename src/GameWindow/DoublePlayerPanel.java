package GameWindow;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import DefaultSet.WindowDef;
import ChessBase.ChessMatch;
import GameWindow.MyButton.LoadOrSaveManualBtn;
import GameWindow.MyButton.PauseBtn;
import GameWindow.MyButton.PlayerInfoBtn;
import GameWindow.MyButton.ResetBtn;
import GameWindow.MyButton.ReverseBtn;
import GameWindow.MyButton.TimerBtn;
import GameWindow.MyButton.WithdrawBtn;

public class DoublePlayerPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoublePlayerPanel() {
		// JPanel 初始化
		this.setVisible(false);
		this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);

		this.BrdWinInit();
		this.add(this.BrdWin);

		// 添加计时器
		timer = new TimerBtn(LeftPos, UpPos + 6 * YDistance, this.BrdWin);
		this.add(timer);

		// 执子方提示
		PlayerInfoBtn playerInfo = new PlayerInfoBtn(LeftPos,
				UpPos + 0 * YDistance);
		// 为监听器指定监听方，监听执子方的变化
		this.BrdWin.NowMatch.addPlayerListner(playerInfo);
		this.add(playerInfo);

		// 翻转棋盘按钮
		RevBtn = new ReverseBtn(LeftPos, 
				UpPos + 1 * YDistance, BrdWin);
		this.add(RevBtn);

		// 开始/暂停按钮
		pausebtn = new PauseBtn(LeftPos, 
				UpPos + 2 * YDistance, this.BrdWin, timer);
		this.add(pausebtn);
		this.BrdWin.pausebtn = this.pausebtn;

		// 悔棋按钮
		BackButton =  new WithdrawBtn(LeftPos, 
				UpPos + 3 * YDistance, this, BrdWin, pausebtn);
		this.add(BackButton);

		//重新开始按钮
		AllResetBtn = new ResetBtn(LeftPos, 
				UpPos + 4 * YDistance, BrdWin, this, pausebtn);
		this.add(AllResetBtn);

		// 保存棋谱按钮
		SaveManualBtn = new LoadOrSaveManualBtn(
				LeftPos, UpPos + 5 * YDistance, this.BrdWin);
		SaveManualBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				LoadOrSaveManualBtn tmpBtn = 
						(LoadOrSaveManualBtn) arg0.getSource();
				if(tmpBtn.FileName != null && tmpBtn.FileRoot != null) {
					// 保存成功提示
					new Thread() {
						@Override
						public void run() {
							tmpBtn.setText("保存成功");
							tmpBtn.setForeground(Color.red);
							try{
								Thread.sleep(1500);
							}catch(Exception e) {
								e.printStackTrace();
							}
							tmpBtn.setText("保存棋谱");
							tmpBtn.setForeground(Color.black);
						}
					}.start();
				}
				else {
					// 不管什么原因保存失败，进行闪烁提示
					new Thread() {
						@Override
						public void run() {
							tmpBtn.setText("保存失败");
							for(int i = 0; i < 10; i++) {
								tmpBtn.setForeground(
										i % 2 == 0 ? Color.red : Color.green);
								try{
									Thread.sleep(100);
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
							tmpBtn.setText("保存棋谱");
							tmpBtn.setForeground(Color.black);
						}
					}.start();
				}
			}
		});
		this.add(SaveManualBtn);

		this.BrdWin.NowTimer = timer;
		
	}


	public void BrdWinInit() {
		if(this.BrdWin == null) {
			this.BrdWin = new BoarderWin();
		}
		this.BrdWin.BoarderWinInit();
		this.BrdWin.addMouseListener(new BrdWinMouseEvent());

		if(this.BrdWin.NowMatch == null) {
			this.BrdWin.NowMatch = new ChessMatch();
		}
		this.BrdWin.NowMatch.MatchInit();

	}


	/***************************************************
	 * 变量信息
	 **************************************************/
	// DoublePlayerPanel所需组件
	// 棋盘窗体
	public BoarderWin BrdWin;
	// 计时器
	TimerBtn timer;
	// 开始/暂停
	PauseBtn pausebtn;
	// 重新开始
	ResetBtn AllResetBtn;
	// 悔棋
	WithdrawBtn BackButton;
	// 翻转棋盘
	ReverseBtn RevBtn;
	// 保存棋谱
	public LoadOrSaveManualBtn SaveManualBtn;

	int LeftPos = 1011;
	int UpPos = 60;
	int YDistance = 80;
}
