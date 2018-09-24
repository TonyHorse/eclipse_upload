package GameWindow.MyButton;

import javax.swing.JButton;

import java.awt.Color;
import java.util.TimerTask;

import GameWindow.BoarderWin;

public class MyTimerBtnTask extends TimerTask{
	public static int ThinkingTime;
	public int LastTime;
	// IsPaused 为 true时代表为阻塞状态，task什么都不做，知道等待去校阻塞
	public boolean IsPaused; 
	public JButton srcBtn;
	public BoarderWin srcBrdWin;

	public static final int period = 100;
	private static final int millseconds = 1000;

	public MyTimerBtnTask(JButton tmpBtn, BoarderWin tmpWin) {
		this(30, tmpBtn, tmpWin); // 默认为30s思考时间
	}
	
	public MyTimerBtnTask(int totalTime, JButton tmpBtn, BoarderWin tmpWin){
		this.srcBtn = tmpBtn;
		MyTimerBtnTask.ThinkingTime = totalTime;
		this.LastTime = totalTime * MyTimerBtnTask.millseconds;
		// 默认为暂停状态，即游戏未开始
		this.IsPaused = true;

		this.srcBrdWin = tmpWin;
	}
	
	public void SetThinkingTime(int tt) {
		MyTimerBtnTask.ThinkingTime = tt;
	}
	
	public void LastTimeInit() {
		this.LastTime = MyTimerBtnTask.ThinkingTime * 1000;
		this.LastTime -= 100;
	}
	
	public void SwitchPausedState() {
		this.IsPaused = !this.IsPaused;
	}
	
	public void SetIsPausedTrue() {
		this.IsPaused = true;
	}
	
	public void SetIsPausedFalse() {
		this.IsPaused  =false;
	}
	
	public void Init() {
		this.LastTime = MyTimerBtnTask.ThinkingTime * MyTimerBtnTask.millseconds;
		this.SetIsPausedTrue(); // 默认为阻塞状态
	}

	@Override
	public void run() {
		if(!this.IsPaused) {
			// 非阻塞状态
			if(this.LastTime >= 100) {
				this.LastTime -= 100;
			}
			else {
				this.srcBrdWin.NowMatch.SwitchNowPlayer();
				this.srcBrdWin.Setsrcp(null);
				this.srcBrdWin.Setdesp(null);
				this.LastTime = MyTimerBtnTask.ThinkingTime * MyTimerBtnTask.millseconds;
				this.LastTime -= 100;
			}
			
			this.srcBtn.setText(String.valueOf((int) (this.LastTime / 1000) + 1));
			if(this.LastTime / 1000 > 4) {
				this.srcBtn.setForeground(Color.black);
			}
			else{
				this.srcBtn.setForeground(Color.red);
			}
			
		}
		else {
			// 阻塞状态什么都不做
		}

	}
}
