package Sound;

import GameWindow.MainWindow;

public class SoundResource {
	public static Mp3Player BgMusic = new Mp3Player(
			MainWindow.class.getResource(
					"/music/shimianmaifu.wav").getPath(), 
			true);

	public static Mp3Player PieceMoveSound = new Mp3Player(
			MainWindow.class.getResource(
					"/music/move.wav").getPath(), 
			false);
	public static Mp3Player PieceEatSound = new Mp3Player(
			MainWindow.class.getResource(
					"/music/capture.wav").getPath(), 
			false);

	public static Mp3Player WinSound = new Mp3Player(
			MainWindow.class.getResource(
					"/music/Win.wav").getPath(), 
			false);

	public static Mp3Player JiangjunSound = new Mp3Player(
			MainWindow.class.getResource(
					"/music/Jiangjun.wav").getPath(), 
			false);

	public static void init() {
		SoundResource.BgMusic.SetVolumeLevel("1");
		SoundResource.PieceEatSound.SetVolumeLevel("4");
		SoundResource.PieceMoveSound.SetVolumeLevel("4");
	}

	public static void SetToneEnabled() {
		SoundResource.PieceMoveSound.SetEnabledTrue();
		SoundResource.PieceEatSound.SetEnabledTrue();
		SoundResource.WinSound.SetEnabledTrue();
		SoundResource.JiangjunSound.SetEnabledTrue();
	}
	public static void SetToneUnabled() {
		SoundResource.PieceMoveSound.SetEnabledFalse();
		SoundResource.PieceEatSound.SetEnabledFalse();
		SoundResource.WinSound.SetEnabledFalse();
		SoundResource.JiangjunSound.SetEnabledFalse();
	}

	// 如果需要其他的音乐在此处添加

	public static void main(String[] args) {
		SoundResource.PieceEatSound.SetEnabledTrue();
		SoundResource.PieceEatSound.Stop();
		SoundResource.PieceEatSound.Start();
		try {
			Thread.sleep(100);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		SoundResource.PieceEatSound.Stop();
		SoundResource.PieceEatSound.Start();
	}

}
