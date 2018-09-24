package Sound;

import javax.media.bean.playerbean.MediaPlayer;
import GameWindow.MainWindow;

public class Mp3Player {
	private String MusicName;
	private MediaPlayer playMp3;
	// 是否被启用，IsEnabled为true代表可以播放，否则不可以播放
	// 默认关闭即IsEnabled为false
	private boolean IsEnabled;


	public Mp3Player(String str_) {
		this(str_, true);
	}

	public Mp3Player(String str_, boolean IsLoop) {
		this.MusicName = str_;
		this.SetEnabledFalse(); // 默认音效为关闭状态
		playMp3 = new MediaPlayer();
		playMp3.setMediaLocation("file://"+this.MusicName);
		playMp3.realize();
		playMp3.setPlaybackLoop(IsLoop);
	}


	public void Start() {
		if(this.IsEnabled) {
			try {
				playMp3.start();
				System.out.println("[Mp3Player]:播放"+this.MusicName);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		else {
			return;
		}
	}

	public void Close() {
		try{
			playMp3.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void Stop() {
		try {
			playMp3.stop();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void SetVolumeLevel(String Level) {
		this.playMp3.setVolumeLevel(Level);
	}
	
	public void SetPlayBackLoop(boolean IsLoop) {
		this.playMp3.setPlaybackLoop(IsLoop);
	}
	


	// 设置音乐是否启用
	public void SetEnabledFalse() {
		this.IsEnabled = false;
	}

	public void SetEnabledTrue() {
		this.IsEnabled = true;
	}


	// 测试JMF是否能成功运行
	public static void main(String[] args) {
		String testURL = MainWindow.class.getResource(
				"/music/win.wav").toString();

		// System.out.println(testURL);


		Mp3Player testMp3 = new Mp3Player(
				testURL.substring(testURL.indexOf("/")+1), false);
		testMp3.SetEnabledTrue();
		testMp3.Start();
		try{
			Thread.sleep(1000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		testMp3.Close();

	}

}
