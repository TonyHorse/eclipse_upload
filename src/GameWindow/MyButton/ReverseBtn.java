package GameWindow.MyButton;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import DefaultSet.WindowDef;
import GameWindow.BoarderWin;


public class ReverseBtn extends InfoBtn{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BoarderWin srcBrdWin;

	public ReverseBtn(int x, int y, int FontSize, BoarderWin tmpWin) {
		super("·­×ªÆåÅÌ", x, y, FontSize);
		this.srcBrdWin = tmpWin;
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
				SwingUtilities.invokeLater(()->{
					((ReverseBtn)arg0.getSource()).srcBrdWin.SwitchReverseState();
				});
				((ReverseBtn)arg0.getSource()).srcBrdWin.paintImmediately(0, 0, 
						((ReverseBtn)arg0.getSource()).srcBrdWin.getWidth(), 
						((ReverseBtn)arg0.getSource()).srcBrdWin.getHeight());
			}
		});
	}
	public ReverseBtn(int x, int y, BoarderWin tmpWin) {
		this(x, y, WindowDef.buttonWordSize, tmpWin);
	}
}
