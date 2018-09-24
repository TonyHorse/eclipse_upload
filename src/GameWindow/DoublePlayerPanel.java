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
		// JPanel ��ʼ��
		this.setVisible(false);
		this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);

		this.BrdWinInit();
		this.add(this.BrdWin);

		// ��Ӽ�ʱ��
		timer = new TimerBtn(LeftPos, UpPos + 6 * YDistance, this.BrdWin);
		this.add(timer);

		// ִ�ӷ���ʾ
		PlayerInfoBtn playerInfo = new PlayerInfoBtn(LeftPos,
				UpPos + 0 * YDistance);
		// Ϊ������ָ��������������ִ�ӷ��ı仯
		this.BrdWin.NowMatch.addPlayerListner(playerInfo);
		this.add(playerInfo);

		// ��ת���̰�ť
		RevBtn = new ReverseBtn(LeftPos, 
				UpPos + 1 * YDistance, BrdWin);
		this.add(RevBtn);

		// ��ʼ/��ͣ��ť
		pausebtn = new PauseBtn(LeftPos, 
				UpPos + 2 * YDistance, this.BrdWin, timer);
		this.add(pausebtn);
		this.BrdWin.pausebtn = this.pausebtn;

		// ���尴ť
		BackButton =  new WithdrawBtn(LeftPos, 
				UpPos + 3 * YDistance, this, BrdWin, pausebtn);
		this.add(BackButton);

		//���¿�ʼ��ť
		AllResetBtn = new ResetBtn(LeftPos, 
				UpPos + 4 * YDistance, BrdWin, this, pausebtn);
		this.add(AllResetBtn);

		// �������װ�ť
		SaveManualBtn = new LoadOrSaveManualBtn(
				LeftPos, UpPos + 5 * YDistance, this.BrdWin);
		SaveManualBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				LoadOrSaveManualBtn tmpBtn = 
						(LoadOrSaveManualBtn) arg0.getSource();
				if(tmpBtn.FileName != null && tmpBtn.FileRoot != null) {
					// ����ɹ���ʾ
					new Thread() {
						@Override
						public void run() {
							tmpBtn.setText("����ɹ�");
							tmpBtn.setForeground(Color.red);
							try{
								Thread.sleep(1500);
							}catch(Exception e) {
								e.printStackTrace();
							}
							tmpBtn.setText("��������");
							tmpBtn.setForeground(Color.black);
						}
					}.start();
				}
				else {
					// ����ʲôԭ�򱣴�ʧ�ܣ�������˸��ʾ
					new Thread() {
						@Override
						public void run() {
							tmpBtn.setText("����ʧ��");
							for(int i = 0; i < 10; i++) {
								tmpBtn.setForeground(
										i % 2 == 0 ? Color.red : Color.green);
								try{
									Thread.sleep(100);
								}catch(Exception e) {
									e.printStackTrace();
								}
							}
							tmpBtn.setText("��������");
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
	 * ������Ϣ
	 **************************************************/
	// DoublePlayerPanel�������
	// ���̴���
	public BoarderWin BrdWin;
	// ��ʱ��
	TimerBtn timer;
	// ��ʼ/��ͣ
	PauseBtn pausebtn;
	// ���¿�ʼ
	ResetBtn AllResetBtn;
	// ����
	WithdrawBtn BackButton;
	// ��ת����
	ReverseBtn RevBtn;
	// ��������
	public LoadOrSaveManualBtn SaveManualBtn;

	int LeftPos = 1011;
	int UpPos = 60;
	int YDistance = 80;
}
