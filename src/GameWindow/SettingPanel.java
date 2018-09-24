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
	// �������ֿ����߹�
	public JButton BgMusicCtlBtn;
	public JButton BgMusicInfoBtn;

	// ��ʾ��Ч���������ӣ���������������
	public JButton ToneCtlBtn;
	public JButton ToneInfoBtn;

	// ��������˼��ʱ�������
	public JButton ThinkingTimeCtlBtn;

	// ÿ������ѡ��֮������Ͻ�����ͼ��
	public int LeftPos1 = 400;
	public int UpPos1 = 80;
	public int XDistance = 20;
	public int YDistance = 100;

	public int wordFontSize = WindowDef.buttonWordSize;

	public SettingPanel() {
		// JPanel ��ʼ��
		this.setVisible(false);
		this.setLayout(null);
		this.setOpaque(false);
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);
		
		// ��ʼ��JPanel�ڲ���JButton
		this.BgMusicBtnInit();
		this.ToneBtnInit();
	}

	public void BgMusicBtnInit() {
		this.BgMusicInfoBtn = new InfoBtn("��������", 
				this.LeftPos1, this.UpPos1 + 0 * this.YDistance, 
				this.wordFontSize);
		this.add(this.BgMusicInfoBtn);
		this.BgMusicCtlBtn = new InfoBtn("����", 
				this.LeftPos1 + this.BgMusicInfoBtn.getWidth() + this.XDistance, 
				this.UpPos1 + 0 * this.YDistance, 
				this.wordFontSize);
		this.add(this.BgMusicCtlBtn);
		this.BgMusicCtlBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(BgMusicCtlBtn.getText().equals("����")) {
					BgMusicCtlBtn.setText("�ر�");
					SoundResource.BgMusic.SetEnabledTrue();
					SoundResource.BgMusic.Start();
				}
				else {
					BgMusicCtlBtn.setText("����");
					SoundResource.BgMusic.Stop();
					SoundResource.BgMusic.SetEnabledFalse();
				}
			}
		});
	}

	public void ToneBtnInit() {
		this.ToneInfoBtn = new InfoBtn("��ʾ��Ч", 
				this.LeftPos1, this.UpPos1 + 1 * this.YDistance, 
				this.wordFontSize);
		this.add(this.ToneInfoBtn);
		this.ToneCtlBtn = new InfoBtn("����",
				this.LeftPos1 + this.ToneInfoBtn.getWidth() + 1 * this.XDistance,
				this.UpPos1 + 1 * this.YDistance,
				this.wordFontSize);
		this.add(this.ToneCtlBtn);
		this.ToneCtlBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(ToneCtlBtn.getText().equals("����")) {
					ToneCtlBtn.setText("�ر�");
					SoundResource.SetToneEnabled();
				}
				else {
					ToneCtlBtn.setText("����");
					SoundResource.SetToneUnabled();
				}
			}
		});
	}

}
