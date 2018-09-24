package GameWindow.MyButton;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

import DefaultSet.WindowDef;

/*
 * ���а�ť�Ĺ�ͬ����
 * ����setBounds��setFont��setText
 * ����Ϊ�˱��������ʾbug��Bounds�Ĵ�С����Font�Ĵ�С���е���
 */
public class InfoBtn extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int fontSize;

	public InfoBtn(String text, int x, int y, int FontSize) {
		this.setFontSize(FontSize);
		this.setText(text);
		this.setBounds(x, y, 
				(int) (((float)FontSize * (1.73 - 0.1 * text.length()))* (text.length())),
				(int) ((float)FontSize * 1.48));
		this.setFont(new Font("�����п�", Font.CENTER_BASELINE, this.fontSize));
		this.setBackground(WindowDef.btnEnteredColor);
		this.setForeground(Color.black);
	}
	public InfoBtn(String text, int x, int y) {
		// ʹ��Ĭ�ϵ�FontSize
		this(text, x, y, WindowDef.buttonWordSize);
		this.setFontSize(WindowDef.buttonWordSize);
	}
	
	public void setFontSize(int f) {
		this.fontSize = f;
	}
}
