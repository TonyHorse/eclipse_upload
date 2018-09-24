package GameWindow.MyButton;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DefaultSet.WindowDef;
import GameWindow.BoarderWin;

public class ResetBtn extends InfoBtn{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public PauseBtn srcPBtn;
	public BoarderWin srcBrdWin;
	public JPanel fPane;

	public ResetBtn(int x, int y, int FontSize, BoarderWin tmpWin, JPanel tmpfp, PauseBtn tmpp){
		super("���¿�ʼ", x, y, FontSize);
		this.srcPBtn = tmpp;
		this.srcBrdWin = tmpWin;
		this.fPane = tmpfp;

		this.setBackground(WindowDef.btnEnteredColor);
		this.setForeground(Color.BLACK);

		this.addMouseListener(new MouseAdapter() {
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
				if(((ResetBtn)arg0.getSource()).srcBrdWin.NowMatch.StepsList.empty()) {
					JOptionPane.showMessageDialog(((ResetBtn)arg0.getSource()).fPane, "�����Ѿ�Ϊ����״̬", 
							"�й�����", JOptionPane.OK_OPTION);
					((ResetBtn)arg0.getSource()).srcPBtn.Init();
				}
				else {
					int confChoice = JOptionPane.showConfirmDialog(((ResetBtn)arg0.getSource()).fPane, "ȷ�����¿�ʼ��", 
							"�й�����", JOptionPane.OK_CANCEL_OPTION);
					if(confChoice == 0) { // ȷ�����¿�ʼ
						((ResetBtn)arg0.getSource()).srcPBtn.Init();
						((ResetBtn)arg0.getSource()).srcBrdWin.BoarderWinInit();
						/*((ResetBtn)arg0.getSource()).srcBrdWin.SetAll(((ResetBtn)arg0.getSource()).srcBrdWin.NowMatch, 
								Toolkit.getDefaultToolkit().getImage(
										MainWindow.class.getResource(
												"/imageLibary/background.png")), 
								BoarderDef.CanvasPosX, BoarderDef.CanvasPosY, 
								BoarderDef.CanvasPosX+661, BoarderDef.CanvasPosY+728);*/
						((ResetBtn)arg0.getSource()).srcBrdWin.paintImmediately(0, 0, 
								((ResetBtn)arg0.getSource()).srcBrdWin.getWidth(), 
								((ResetBtn)arg0.getSource()).srcBrdWin.getHeight());
					}
					else if(confChoice == 2) { // ȡ�����¿�ʼ
						// ʲô������
					}
				}
			}
		});

	}

	public ResetBtn(int x, int y, BoarderWin tmpWin, JPanel tmpfp, PauseBtn tmpp) {
		this(x, y, WindowDef.buttonWordSize, tmpWin, tmpfp, tmpp);
	}
}
