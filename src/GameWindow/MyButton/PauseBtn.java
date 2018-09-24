package GameWindow.MyButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import DefaultSet.WindowDef;
import GameWindow.BoarderWin;

public class PauseBtn extends InfoBtn{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TimerBtn srcTimer;
	public int nowState;
	public BoarderWin srcBrdWin;
	private static final String[] BtnInfo = {"开始", "暂停"};

	public PauseBtn(int x, int y, int FontSize, BoarderWin tmpWin, TimerBtn tmptimer) {
		super("开始", x, y, FontSize);
		this.setBackground(WindowDef.btnEnteredColor);
		this.nowState = 0; // 0 代表开始
		this.srcTimer = tmptimer;
		this.srcBrdWin = tmpWin;
		this.srcBrdWin.ClickEnabled = false;
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
				((PauseBtn)arg0.getSource()).setBackground(null);
				// 改变计时器计时状态
				((PauseBtn)arg0.getSource()).srcTimer.MyTask.SwitchPausedState();
				((PauseBtn)arg0.getSource()).SwitchState();
				((PauseBtn)arg0.getSource()).setText(
						BtnInfo[((PauseBtn)arg0.getSource()).nowState]);
				if(((PauseBtn)arg0.getSource()).getText().equals("开始")) {
					// 点击更改内容之后为"开始"，说明现在是阻塞状态
					((PauseBtn)arg0.getSource()).srcBrdWin.SetClickEnabledFalse();
				}
				else {
					// 非阻塞状态
					((PauseBtn)arg0.getSource()).srcBrdWin.SetClickEnabledTrue();
				}
			}
		});
	}
	public PauseBtn(int x, int y, BoarderWin tmpWin, TimerBtn tmpTimer) {
		this(x, y, WindowDef.buttonWordSize, tmpWin, tmpTimer);
	}
	public void Init() {
		this.nowState = 0;
		this.setText("开始");
		this.srcTimer.Init();
		this.srcBrdWin.SetClickEnabledFalse();
	}
	public void SwitchState(){
		this.nowState = 1 - this.nowState;
		this.srcBrdWin.SwitchClickEnabledState();
	}
}
