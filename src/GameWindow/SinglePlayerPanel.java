package GameWindow;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

import ChessAI.ComputerPlayer;
import DefaultSet.BoarderDef;
import SomeListener.MatchPlayerListener;
import GameWindow.MyButton.*;

public class SinglePlayerPanel extends DoublePlayerPanel 
implements MatchPlayerListener{

	public SinglePlayerPanel() {
		super();
		this.computer = new ComputerPlayer();
		this.computer.pauseBtn = this.pausebtn;
		this.computer.SetMySide('��');
		this.computer.SetBrdWin(this.BrdWin);
		this.BrdWin.NowMatch.addPlayerListner(this);
		this.add(this.computer.ThinkingBtn);

		int XDis = 20;
		int XPos = BoarderDef.CanvasPosX + XDis * 6 + 300;
		int YPos = BoarderDef.CanvasPosY + BoarderDef.BoarderHeight + 10;
		int Width = 70;
		int Height = 40;


		// Ĭ��ѡ��췽
		this.redrb = new JRadioButton("�췽", true);
		this.redrb.setBounds(XPos, YPos, Width, Height);
		this.redrb.setFont(new Font("�����п�", Font.CENTER_BASELINE, 20));
		this.redrb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// �����ʱ���޸�Myside
				computer.SetMySide('��');
				if(computer.BrdWin.NowMatch.StepsList.isEmpty()) {
					computer.BrdWin.Reverse = false;
					computer.BrdWin.repaint();
				}
				System.out.println("[SinglePlayerPane]:���ڵ���Ϊ"+computer.MySide+"��");
			}
		});
		this.add(this.redrb);

		this.blackrb = new JRadioButton("�ڷ�", false);
		this.blackrb.setBounds(XPos + Width + XDis / 2, YPos, Width, Height);
		this.blackrb.setFont(new Font("�����п�", Font.CENTER_BASELINE, 20));
		this.blackrb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// �����ʱ���޸�Myside
				computer.SetMySide('��');
				if(computer.BrdWin.NowMatch.StepsList.isEmpty()) {
					computer.BrdWin.Reverse = true;
					computer.BrdWin.repaint();
				}
				System.out.println("[SinglePlayerPane]:���ڵ���Ϊ"+computer.MySide+"��");
			}
		});
		this.add(this.blackrb);
		this.chosebg = new ButtonGroup();
		this.chosebg.add(this.redrb);
		this.chosebg.add(this.blackrb);

		this.pausebtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(((PauseBtn) arg0.getSource()).getText().contains("��ͣ")) {
					computer.LetMeMove(computer.BrdWin.NowMatch.NowPlayer);
				}
			}
		});

		this.AllResetBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				computer.StopThinking();
			}
		});

	}

	@Override
	public void OnPlayerChange(char nowplayer) {
		if(nowplayer == this.computer.MySide) {
			computer.LetMeMove(nowplayer);
		}
		else {
			// ���������Ai�Ļ���ȫ�����ں�̨����
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	public ComputerPlayer computer;

	public JRadioButton redrb;

	public JRadioButton blackrb;

	public ButtonGroup chosebg;

}
