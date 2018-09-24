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
	 * @param.x和y  PlayerInfoBtn在窗口中的左上角坐标
	 * 开局默认为红方执子
	 * **/
	public PlayerInfoBtn(int x, int y, int FontSize){
		super("红方执子", x, y, FontSize);
		this.setBackground(null);
		this.setForeground(Color.red);
	}
	public PlayerInfoBtn(int x, int y){
		this(x, y, WindowDef.buttonWordSize);
	}

	@Override
	public void OnPlayerChange(char NowPlayer) {
		if(NowPlayer == '红' || NowPlayer == '无') {
			this.setText("红方执子");
			this.setForeground(Color.red);
		}
		else {
			this.setText("黑方执子");
			this.setForeground(Color.black);
		}
		this.setFont(new Font("华文行楷", 
				Font.CENTER_BASELINE, this.fontSize));
		this.setBackground(null);
	}
	
}
