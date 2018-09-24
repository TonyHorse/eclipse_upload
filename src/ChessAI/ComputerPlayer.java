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
		this.ThinkingBtn.setFont(new Font("����", Font.CENTER_BASELINE, 26));
		this.ThinkingBtn.setText("������ҵȴ���...");
	}

	public void SetThinkingBtnComputerInfo(String str) {
		if(this.ThinkingBtn == null) {
			this.ThinkingBtnBtnInit();
		}
		this.ThinkingBtn.setText(str);
	}
	public void SetThinkingBtnComputerInfo() {
		this.SetThinkingBtnComputerInfo("�������˼����...");
	}

	public void SetThinkingBtnPlayerInfo(String str) {
		if(this.ThinkingBtn == null) {
			this.ThinkingBtnBtnInit();
		}
		this.ThinkingBtn.setText(str);
	}

	public void SetThinkingBtnPlayerInfo() {
		this.SetThinkingBtnPlayerInfo("�����ˣ��޴�������");
	} 

	/***************************************************
	 * ��ʾ��Ϣ:
	 * API�����ACTION
	 * ����μ���http://www.chessdb.cn/cloudbook_api.html
	 * querybest��
	 * 	���ظ�ʽΪmove:c3c4
	 * 	������󣬷���invalid board��û����֪�ŷ� ����nobestmove��
	 *  ���巽���ӿ��߷���checkmate����stalemate
	 * queryserach:
	 * 	���ظ�ʽΪ{search:c3c4}
	 * 	������󣬷���invalid board��û����֪�ŷ� ����nobestmove��
	 *  ���巽���ӿ��߷���checkmate����stalemate
	 *  
	 **************************************************/
	/***************************************************
	 * computer����
	 **************************************************/
	public void LetMeMove(char NowPalyer) {

		System.out.println("[ComputerPlayer]>>>�ҿ�����������ִ�ӷ�Ϊ" 
				+ this.BrdWin.NowMatch.NowPlayer);
		System.out.println("[ComputerPlayer]>>>����" + this.MySide + "��");

		if(this.pauseBtn.getText().contains("��ʼ")) {
			return;
		}

		if (this.BrdWin.NowMatch.NowPlayer == this.MySide) {
			System.out.println("[ComputerPlayer]>>>^v^�ֵ�����");

			computerThread = new Thread(() -> {
				// �޸�����״̬Ϊ�����޸�
				SwingUtilities.invokeLater(() -> {
					this.BrdWin.SetClickEnabledFalse();
				});

				this.SetThinkingBtnComputerInfo();

				// ��������������������
				String FenMatch = this.MyMatch2FenMatch(this.BrdWin.NowMatch);
				String FenMove = this.GetFenMove(ChessAPI.ChessDBHost, FenMatch);
				if (FenMove.contains("checkmate") || FenMove.contains("stalemate")) {
					SwingUtilities.invokeLater(() -> {
						ComputerPlayer.this.BrdWin.NowMatch.SetWinner((char) (ComputerPlayer.this.MySide ^ ('��' ^ '��')));
					});
					System.out.println("[ComputerPlayer]>>>�ðɣ���Ӯ�� " + FenMove);
				} 
				else if (FenMove.contains("unknown")) {
					System.out.println("[ComputerPlayer]>>>unknown, ֱ�ӻ���");
					SwingUtilities.invokeLater(() -> this.BrdWin.NowMatch.SwitchNowPlayer());
				} 
				else {
					System.out.println("[ComputerPlayer]>>>�д�ʦ��������ô��" + FenMove);
					Point[] tmp = this.GetMove(FenMove);
					System.out.println("[ComputerPlayer]>>>��������,������");
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
				// �޸�����״̬Ϊ���Ե��
				SwingUtilities.invokeLater(() -> {
					this.BrdWin.SetClickEnabledTrue();
				});
			});
			computerThread.start();

		} else {
			System.out.println("[ComputerPlayer]>>>TnT ��������");
			// ��ʱdo nothing
			// ����������̨���㣬�Ժ��ٽ��п���
		}
	}

	public void StopThinking() {
		if(this.computerThread != null) {
			this.computerThread.interrupt();
		}
		this.computerThread = null;
		this.SetThinkingBtnComputerInfo("���Եȴ���...");
	}


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	// ��������computer���
	// �ҵ�֪���Լ�ִ�ӵ���ɫ
	public char MySide;
	// �һ���֪����ǰ���
	public BoarderWin BrdWin;
	// ��ʾ��������˼��
	public JButton ThinkingBtn;
	// ��ͣ��ť
	public JButton pauseBtn;
	// ����˼���߳�
	public Thread computerThread;
	// ����Ҫʲô�ǣ������ٿ��ǰ�
}
