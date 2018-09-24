package ChessAI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import DefaultSet.BoarderDef;
import GameWindow.BoarderWin;

public class ComputerPlayer extends ChessAPI{

	public ComputerPlayer() {
		this.ThinkingBtnBtnInit();
	}
	public ComputerPlayer(char piececolor, BoarderWin tmp) {
		this.MySide = piececolor;
		this.BrdWin = tmp;
		this.ThinkingBtnBtnInit();
	}
	public void SetMySide(char tmp) {
		this.MySide = tmp;
	}
	public void SetBrdWin(BoarderWin tmp) {
		this.BrdWin = tmp;
	}

	public void ThinkingBtnBtnInit() {
		if(this.ThinkingBtn == null) {
			this.ThinkingBtn =  new JButton();
			this.ThinkingBtn.setVisible(true);
		}
		else {
			this.ThinkingBtn.setVisible(true);
		}
		this.ThinkingBtn.setBounds(
				BoarderDef.CanvasPosX + 30,
				BoarderDef.CanvasPosY + BoarderDef.BoarderHeight + 10,
				300, 40);
		this.ThinkingBtn.setBackground(null);
		this.ThinkingBtn.setForeground(Color.black);
		this.ThinkingBtn.setFont(new Font("等线", Font.CENTER_BASELINE, 26));
		this.ThinkingBtn.setText("电脑玩家等待中...");
	}

	public void SetThinkingBtnComputerInfo(String str) {
		if(this.ThinkingBtn == null) {
			this.ThinkingBtnBtnInit();
		}
		this.ThinkingBtn.setText(str);
	}
	public void SetThinkingBtnComputerInfo() {
		this.SetThinkingBtnComputerInfo("电脑玩家思考中...");
	}

	public void SetThinkingBtnPlayerInfo(String str) {
		if(this.ThinkingBtn == null) {
			this.ThinkingBtnBtnInit();
		}
		this.ThinkingBtn.setText(str);
	}

	public void SetThinkingBtnPlayerInfo() {
		this.SetThinkingBtnPlayerInfo("到你了，愚蠢的人类");
	} 

	/***************************************************
	 * 提示信息:
	 * API请求的ACTION
	 * 具体参见：http://www.chessdb.cn/cloudbook_api.html
	 * querybest：
	 * 	返回格式为move:c3c4
	 * 	局面错误，返回invalid board；没有已知着法 返回nobestmove；
	 *  走棋方无子可走返回checkmate或者stalemate
	 * queryserach:
	 * 	返回格式为{search:c3c4}
	 * 	局面错误，返回invalid board；没有已知着法 返回nobestmove；
	 *  走棋方无子可走返回checkmate或者stalemate
	 *  
	 **************************************************/
	/***************************************************
	 * computer走棋
	 **************************************************/
	public void LetMeMove(char NowPalyer) {

		System.out.println("[ComputerPlayer]>>>我看看哈，现在执子方为" 
				+ this.BrdWin.NowMatch.NowPlayer);
		System.out.println("[ComputerPlayer]>>>我是" + this.MySide + "方");

		if(this.pauseBtn.getText().contains("开始")) {
			return;
		}

		if (this.BrdWin.NowMatch.NowPlayer == this.MySide) {
			System.out.println("[ComputerPlayer]>>>^v^轮到我了");

			computerThread = new Thread(() -> {
				// 修改棋盘状态为不可修改
				SwingUtilities.invokeLater(() -> {
					this.BrdWin.SetClickEnabledFalse();
				});

				this.SetThinkingBtnComputerInfo();

				// 该我这个电脑玩家走子了
				String FenMatch = this.MyMatch2FenMatch(this.BrdWin.NowMatch);
				String FenMove = this.GetFenMove(ChessAPI.ChessDBHost, FenMatch);
				if (FenMove.contains("checkmate") || FenMove.contains("stalemate")) {
					SwingUtilities.invokeLater(() -> {
						ComputerPlayer.this.BrdWin.NowMatch.SetWinner((char) (ComputerPlayer.this.MySide ^ ('红' ^ '黑')));
					});
					System.out.println("[ComputerPlayer]>>>好吧，你赢了 " + FenMove);
				} 
				else if (FenMove.contains("unknown")) {
					System.out.println("[ComputerPlayer]>>>unknown, 直接换人");
					SwingUtilities.invokeLater(() -> this.BrdWin.NowMatch.SwitchNowPlayer());
				} 
				else {
					System.out.println("[ComputerPlayer]>>>有大师告诉我这么走" + FenMove);
					Point[] tmp = this.GetMove(FenMove);
					System.out.println("[ComputerPlayer]>>>我走完了,该你了");
					try{
						Thread.sleep(1 * 1000);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(() -> {
						this.BrdWin.Setsrcp(this.BrdWin.ReversePoint(tmp[0]));
					});
					try{
						Thread.sleep(600);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(() -> {
						this.BrdWin.Setdesp(this.BrdWin.ReversePoint(tmp[1]));
					});
					try{
						Thread.sleep(300);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(() -> {
						this.BrdWin.BrdPieceMove();
					});
				}
				this.SetThinkingBtnPlayerInfo();
				// 修改棋盘状态为可以点击
				SwingUtilities.invokeLater(() -> {
					this.BrdWin.SetClickEnabledTrue();
				});
			});
			computerThread.start();

		} else {
			System.out.println("[ComputerPlayer]>>>TnT 不该我走");
			// 暂时do nothing
			// 可以用来后台计算，以后再进行考虑
		}
	}

	public void StopThinking() {
		if(this.computerThread != null) {
			this.computerThread.interrupt();
		}
		this.computerThread = null;
		this.SetThinkingBtnComputerInfo("电脑等待中...");
	}


	/***************************************************
	 * 变量信息
	 **************************************************/
	// 假设我是computer玩家
	// 我得知道自己执子的颜色
	public char MySide;
	// 我还得知道当前棋局
	public BoarderWin BrdWin;
	// 提示电脑正在思考
	public JButton ThinkingBtn;
	// 暂停按钮
	public JButton pauseBtn;
	// 电脑思考线程
	public Thread computerThread;
	// 还需要什么昵？后面再考虑吧
}
