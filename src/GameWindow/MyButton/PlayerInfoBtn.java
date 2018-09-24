package GameWindow.MyButton;

import java.awt.Color;
import java.awt.Font;

import DefaultSet.WindowDef;
import SomeListener.MatchPlayerListener;

public class PlayerInfoBtn extends InfoBtn implements MatchPlayerListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param.x��y  PlayerInfoBtn�ڴ����е����Ͻ�����
	 * ����Ĭ��Ϊ�췽ִ��
	 * **/
	public PlayerInfoBtn(int x, int y, int FontSize){
		super("�췽ִ��", x, y, FontSize);
		this.setBackground(null);
		this.setForeground(Color.red);
	}
	public PlayerInfoBtn(int x, int y){
		this(x, y, WindowDef.buttonWordSize);
	}

	@Override
	public void OnPlayerChange(char NowPlayer) {
		if(NowPlayer == '��' || NowPlayer == '��') {
			this.setText("�췽ִ��");
			this.setForeground(Color.red);
		}
		else {
			this.setText("�ڷ�ִ��");
			this.setForeground(Color.black);
		}
		this.setFont(new Font("�����п�", 
				Font.CENTER_BASELINE, this.fontSize));
		this.setBackground(null);
	}
	
}
