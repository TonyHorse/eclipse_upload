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
		this.computer.SetMySide('黑');
		this.computer.SetBrdWin(this.BrdWin);
		this.BrdWin.NowMatch.addPlayerListner(this);
		this.add(this.computer.ThinkingBtn);

		int XDis = 20;
		int XPos = BoarderDef.CanvasPosX + XDis * 6 + 300;
		int YPos = BoarderDef.CanvasPosY + BoarderDef.BoarderHeight + 10;
		int Width = 70;
		int Height = 40;


		// 默认选择红方
		this.redrb = new JRadioButton("红方", true);
		this.redrb.setBounds(XPos, YPos, Width, Height);
		this.redrb.setFont(new Font("华文行楷", Font.CENTER_BASELINE, 20));
		this.redrb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// 鼠标点击时，修改Myside
				computer.SetMySide('黑');
				if(computer.BrdWin.NowMatch.StepsList.isEmpty()) {
					computer.BrdWin.Reverse = false;
					computer.BrdWin.repaint();
				}
				System.out.println("[SinglePlayerPane]:现在电脑为"+computer.MySide+"方");
			}
		});
		this.add(this.redrb);

		this.blackrb = new JRadioButton("黑方", false);
		this.blackrb.setBounds(XPos + Width + XDis / 2, YPos, Width, Height);
		this.blackrb.setFont(new Font("华文行楷", Font.CENTER_BASELINE, 20));
		this.blackrb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// 鼠标点击时，修改Myside
				computer.SetMySide('红');
				if(computer.BrdWin.NowMatch.StepsList.isEmpty()) {
					computer.BrdWin.Reverse = true;
					computer.BrdWin.repaint();
				}
				System.out.println("[SinglePlayerPane]:现在电脑为"+computer.MySide+"方");
			}
		});
		this.add(this.blackrb);
		this.chosebg = new ButtonGroup();
		this.chosebg.add(this.redrb);
		this.chosebg.add(this.blackrb);

		this.pausebtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(((PauseBtn) arg0.getSource()).getText().contains("暂停")) {
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
			// 如果真正有Ai的话完全可以在后台计算
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/***************************************************
	 * 变量信息
	 **************************************************/
	public ComputerPlayer computer;

	public JRadioButton redrb;

	public JRadioButton blackrb;

	public ButtonGroup chosebg;

}
