package GameWindow.MyButton;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import DefaultSet.WindowDef;
import GameWindow.BoarderWin;


public class WithdrawBtn extends InfoBtn{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JPanel srcPane;
	public BoarderWin srcBrdWin;
	public PauseBtn srcPBtn;

	public WithdrawBtn(int x, int y, int FontSize, 
			JPanel fthp, BoarderWin tmpWin, PauseBtn tmpp){
		super("悔棋", x, y, FontSize);
		this.srcPane = fthp;
		this.srcBrdWin = tmpWin;
		this.srcPBtn = tmpp;
		this.setForeground(Color.BLACK);
		this.setBackground(WindowDef.btnEnteredColor);

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
				((WithdrawBtn)arg0.getSource()).srcPBtn.Init();
				((WithdrawBtn)arg0.getSource()).srcBrdWin.Setsrcp(null);
				((WithdrawBtn)arg0.getSource()).srcBrdWin.Setdesp(null);
				if(((WithdrawBtn)arg0.getSource()).srcBrdWin.NowMatch.StepsList.empty()) {
					JOptionPane.showMessageDialog(((WithdrawBtn)arg0.getSource()).srcPane,
							"棋盘已经为开局状态", "中国象棋", JOptionPane.OK_OPTION);
				}
				else {
					// System.out.println("[MainWindow]:悔一步棋");
					((WithdrawBtn)arg0.getSource()).srcBrdWin.NowMatch.WithDrawOneStep();
					((WithdrawBtn)arg0.getSource()).srcBrdWin.repaint();
				}

			}
		});
	}

	public WithdrawBtn(int x, int y, JPanel fthp, BoarderWin tmpWin, PauseBtn tmpp) {
		this(x, y, WindowDef.buttonWordSize, fthp, tmpWin, tmpp);
	}
}
