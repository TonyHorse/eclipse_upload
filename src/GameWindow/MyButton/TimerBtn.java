package GameWindow.MyButton;

import DefaultSet.WindowDef;

import java.util.Timer;

import GameWindow.BoarderWin;

/*
 * ¼ÆÊ±Æ÷
 */
public class TimerBtn extends InfoBtn{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MyTimerBtnTask MyTask;
	public Timer MyTimer;
	
	public TimerBtn(int x, int y, int FontSize, BoarderWin tmpWin) {
		super(String.valueOf(MyTimerBtnTask.ThinkingTime), x, y, FontSize);
		this.setBounds(x, y, 80, 50);
		this.setBackground(null);
		this.MyTask = new MyTimerBtnTask(this, tmpWin);
		this.MyTimer = new Timer();
		this.MyTimer.scheduleAtFixedRate(this.MyTask, 0, MyTimerBtnTask.period);
	}
	public TimerBtn(int x, int y, BoarderWin tmpWin) {
		this(x, y, WindowDef.buttonWordSize, tmpWin);
	}
	
	public void Init() {
		this.setText("0");
		this.MyTask.Init();
	}
	
	public void SwitchPauseState() {
		this.MyTask.SwitchPausedState();
	}
	
	public void SetIsPausedTrue() {
		this.MyTask.SetIsPausedTrue();
	}
	
	public void SetIsPausedFalse() {
		this.MyTask.SetIsPausedFalse();
	}
	
	public void LastTimeInit() {
		this.MyTask.LastTimeInit();
	}
	
	public void Restart() {
		this.MyTask.Init();
	}
}
