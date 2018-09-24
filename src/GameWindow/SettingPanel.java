package GameWindow;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JButton;

import DefaultSet.WindowDef;
import Sound.SoundResource;
import GameWindow.MyButton.InfoBtn;

public class SettingPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 背景音乐开或者关
	public JButton BgMusicCtlBtn;
	public JButton BgMusicInfoBtn;

	// 提示音效：包括走子，将军，警告音等
	public JButton ToneCtlBtn;
	public JButton ToneInfoBtn;

	// 象棋走棋思考时间的设置
	public JButton ThinkingTimeCtlBtn;

	// 每个设置选项之间的左上角坐标和间隔
	public int LeftPos1 = 400;
	public int UpPos1 = 80;
	public int XDistance = 20;
	public int YDistance = 100;

	public int wordFontSize = WindowDef.buttonWordSize;

	public SettingPanel() {
		// JPanel 初始化
		this.setVisible(false);
		this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);
		
		// 初始化JPanel内部的JButton
		this.BgMusicBtnInit();
		this.ToneBtnInit();
	}

	public void BgMusicBtnInit() {
		this.BgMusicInfoBtn = new InfoBtn("背景音乐", 
				this.LeftPos1, this.UpPos1 + 0 * this.YDistance, 
				this.wordFontSize);
		this.add(this.BgMusicInfoBtn);
		this.BgMusicCtlBtn = new InfoBtn("开启", 
				this.LeftPos1 + this.BgMusicInfoBtn.getWidth() + this.XDistance, 
				this.UpPos1 + 0 * this.YDistance, 
				this.wordFontSize);
		this.add(this.BgMusicCtlBtn);
		this.BgMusicCtlBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(BgMusicCtlBtn.getText().equals("开启")) {
					BgMusicCtlBtn.setText("关闭");
					SoundResource.BgMusic.SetEnabledTrue();
					SoundResource.BgMusic.Start();
				}
				else {
					BgMusicCtlBtn.setText("开启");
					SoundResource.BgMusic.Stop();
					SoundResource.BgMusic.SetEnabledFalse();
				}
			}
		});
	}

	public void ToneBtnInit() {
		this.ToneInfoBtn = new InfoBtn("提示音效", 
				this.LeftPos1, this.UpPos1 + 1 * this.YDistance, 
				this.wordFontSize);
		this.add(this.ToneInfoBtn);
		this.ToneCtlBtn = new InfoBtn("开启",
				this.LeftPos1 + this.ToneInfoBtn.getWidth() + 1 * this.XDistance,
				this.UpPos1 + 1 * this.YDistance,
				this.wordFontSize);
		this.add(this.ToneCtlBtn);
		this.ToneCtlBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(ToneCtlBtn.getText().equals("开启")) {
					ToneCtlBtn.setText("关闭");
					SoundResource.SetToneEnabled();
				}
				else {
					ToneCtlBtn.setText("开启");
					SoundResource.SetToneUnabled();
				}
			}
		});
	}

}
