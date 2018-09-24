package GameWindow.MyButton;

import javax.swing.JButton;

import java.awt.Color;
import java.util.TimerTask;

import GameWindow.BoarderWin;

public class MyTimerBtnTask extends TimerTask{
	public static int ThinkingTime;
	public int LastTime;
	// IsPaused Ϊ trueʱ����Ϊ����״̬��taskʲô��������֪���ȴ�ȥУ����
	public boolean IsPaused; 
	public JButton srcBtn;
	public BoarderWin srcBrdWin;

	public static final int period = 100;
	private static final int millseconds = 1000;

	public MyTimerBtnTask(JButton tmpBtn, BoarderWin tmpWin) {
		this(30, tmpBtn, tmpWin); // Ĭ��Ϊ30s˼��ʱ��
	}
	
	public MyTimerBtnTask(int totalTime, JButton tmpBtn, BoarderWin tmpWin){
		this.srcBtn = tmpBtn;
		MyTimerBtnTask.ThinkingTime = totalTime;
		this.LastTime = totalTime * MyTimerBtnTask.millseconds;
		// Ĭ��Ϊ��ͣ״̬������Ϸδ��ʼ
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
		this.SetIsPausedTrue(); // Ĭ��Ϊ����״̬
	}

	@Override
	public void run() {
		if(!this.IsPaused) {
			// ������״̬
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
			// ����״̬ʲô������
		}

	}
}
