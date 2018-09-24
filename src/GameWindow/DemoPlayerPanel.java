package GameWindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import ChessBase.ManualInfoParse;
import DefaultSet.WindowDef;
import GameWindow.MyButton.LoadOrSaveManualBtn;
import GameWindow.MyButton.InfoBtn;
import GameWindow.BoarderWin;

public class DemoPlayerPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final int LeftPos = 1011;
	private final int DownPos = 560;
	private final int YDistance = 80;

	public DemoPlayerPanel() {
		// �������������ʼ��
		this.setVisible(false);
		this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);

		this.NowParseInit();

		this.DemoBrdWinInit();
		this.add(this.DemoBrdWin);

		// ������ʾ����
		this.infoText = new JTextArea();
		this.infoText.setFont(new Font("����", Font.CENTER_BASELINE, 20));
		this.infoText.setVisible(true);
		this.setBackground(null);
		this.infoText.setEditable(false);


		this.infoScroll = new JScrollPane(this.infoText);
		this.infoScroll.setBounds(1011, 60, 160, 250);
		this.infoScroll.setVisible(false);
		this.add(this.infoScroll);

		this.LastStepBtn = new InfoBtn("��һ��", LeftPos, DownPos - 2 * YDistance);
		this.LastStepBtn.setBackground(WindowDef.btnEnteredColor);
		this.LastStepBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(WindowDef.btnEnteredColor);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(null);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(NowParse.AllManuInfo[0] == null) {
					JOptionPane.showMessageDialog(DemoBrdWin,
							"������һ������", "�й�����", JOptionPane.OK_OPTION);
				}
				else if(NowParse.InfoPos == 0) {
					JOptionPane.showMessageDialog(DemoBrdWin,
							"�Ѿ��ָ�����ʼ״̬", "�й�����", JOptionPane.OK_OPTION);
				}
				else {
					LastStep();
				}
			}
		});
		this.add(this.LastStepBtn);

		this.NextStepBtn = new InfoBtn("��һ��", LeftPos, DownPos - 1 * YDistance);
		this.NextStepBtn.setBackground(WindowDef.btnEnteredColor);
		this.NextStepBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(WindowDef.btnEnteredColor);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(null);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				setBackground(null);
				if(NowParse.AllManuInfo[0] == null) {
					JOptionPane.showMessageDialog(DemoBrdWin,
							"������һ������", "�й�����", JOptionPane.OK_OPTION);
				}
				else {
					NextStep();
				}
			}
		});
		this.add(this.NextStepBtn);

		this.LoadManualBtn = 
				new LoadOrSaveManualBtn(1011, DownPos, this.NowParse);
		this.LoadManualBtn.setBackground(WindowDef.btnEnteredColor);
		this.LoadManualBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(WindowDef.btnEnteredColor);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(null);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				LoadOrSaveManualBtn tmpBtn = 
						(LoadOrSaveManualBtn) arg0.getSource(); 
				if(tmpBtn.FileName != null && tmpBtn.FileRoot != null) {
					infoScroll.setVisible(true);
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
		this.add(LoadManualBtn);
	}


	public void NowParseInit() {
		if(this.NowParse == null) {
			this.NowParse = new ManualInfoParse();
		}
		else {
			this.NowParseInit();
		}
	}

	public void DemoBrdWinInit() {
		if(this.DemoBrdWin == null) {
			this.DemoBrdWin = new BoarderWin();
		}
		else {
			this.DemoBrdWinInit();
		}
	}


	public void NextStep() {
		System.out.println("[DemoPlayerPanel]:" + 
				this.NowParse.AllManuInfo[this.NowParse.InfoPos]);

		if(this.NowParse.AllManuInfo[this.NowParse.InfoPos] != null) {
			Point[] tmp = this.NowParse.GetMove(this.DemoBrdWin.NowMatch);
			if(tmp != null) {
				this.DemoBrdWin.NowMatch.PieceMove(tmp[0], tmp[1]);
			}
			else {
				System.out.println("[DemoPlayerPanel]:����ʧ�ܻ��߽������");
			}
			this.DemoBrdWin.repaint();
		}
		else {
			JOptionPane.showMessageDialog(DemoBrdWin,
					"��ʾ���", "�й�����", JOptionPane.OK_OPTION);
		}
		this.ShowManual();
	}

	public void LastStep() {
		if(this.NowParse.InfoPos > 0) {
			// �����Լ�������
			this.DemoBrdWin.NowMatch.WithDrawOneStep();
			this.NowParse.WithdrawParseOneStep();
			this.DemoBrdWin.repaint();
		}
		this.ShowManual();
	}

	public void ShowManual() {
		this.infoText.setText("");
		if(NowParse != null && NowParse.AllManuInfo != null) {
			int cnt = 0;
			while(NowParse.AllManuInfo[cnt] != null && cnt < NowParse.InfoPos) {
				this.infoText.append(NowParse.AllManuInfo[cnt++] + "\n");
			}
		}
	}

	/***************************************************
	 * ������Ϣ
	 **************************************************/
	public ManualInfoParse NowParse;

	public BoarderWin DemoBrdWin;

	public InfoBtn NextStepBtn;

	public InfoBtn LastStepBtn;

	public LoadOrSaveManualBtn LoadManualBtn;

	public JTextArea infoText;

	public JScrollPane infoScroll;

}
