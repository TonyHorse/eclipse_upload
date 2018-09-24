package GameWindow;

import java.util.Map;
import java.util.HashMap;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;

import DefaultSet.WindowDef;

public class MainWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 游戏主窗体
	public JPanel ContentPane;

	// 游戏菜单按钮
	public JButton[] MenuBtn;

	// 游戏模式
	int GameMode;

	// 游戏按钮与对应的卡片窗体的对应
	public Map<JButton, JPanel> PanelOfBtn = new HashMap<JButton, JPanel>();

	// 不同的菜单模式下的布局窗体
	public DoublePlayerPanel DPP;
	public SinglePlayerPanel SPP;
	public DemoPlayerPanel DemoP;
	public SettingPanel SP;
	public ExitPanel EP;


	public MainWindow() {
		GameMode = -1; // 代表当前不在任何模式中
		this.ContentPaneInit();
		this.BackgroundInit();
		this.MenuBtnInit();
		this.MenuPanelInit();
		this.PanelOfBtnInit();

		for(int i = 0; i < WindowDef.menuName.length; i++) {
			this.MenuBtnMouseEvent(i);
		}
	}

	// 游戏背景初始化
	public void BackgroundInit(ImageIcon bgImg) {
		// 窗口小图标
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainWindow.class.getResource(
						"/imageLibary/black-jiang.png")));
		this.setTitle("中国象棋");
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);
		// 设置窗口不可改变大小
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		//添加背景图片
		JLabel BackGround = new JLabel("");
		BackGround.setIcon(bgImg);
		BackGround.setBounds(0, 0,
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);
		//放在分层布局的最底层
		this.getLayeredPane().add(BackGround, new Integer(Integer.MIN_VALUE));
	}
	public void BackgroundInit() { // 使用默认背景
		this.BackgroundInit(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(MainWindow.class.getResource(
						"/imageLibary/background.png"))));
	}

	// 游戏主窗体初始化
	public void ContentPaneInit() {
		if(this.ContentPane == null) {
			this.ContentPane = new JPanel();
			this.setContentPane(this.ContentPane);
			this.ContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.ContentPane.setOpaque(false);
			this.ContentPane.setLayout(null);
		}
	}

	// 按钮初始化
	public void MenuBtnInit() {
		if(this.MenuBtn == null) {
			this.MenuBtn = new JButton[WindowDef.menuName.length];
			for(int i = 0; i < WindowDef.menuName.length; i++) {
				this.MenuBtn[i] = new JButton();
				this.MenuBtn[i].setVisible(true);
				this.MenuBtn[i].setText(WindowDef.menuName[i]);
				this.MenuBtn[i].setFont(new Font("华文行楷", 
						Font.CENTER_BASELINE, WindowDef.menuWordSize));
				this.MenuBtn[i].setBackground(WindowDef.menuOriColor);
				this.MenuBtn[i].setBounds(WindowDef.menuLeftPos.x, 
						WindowDef.menuLeftPos.y + WindowDef.menuDis * i, 294, 62);
				this.getContentPane().add(this.MenuBtn[i]);
			}
		}
	}

	// 游戏布局初始化
	public void MenuPanelInit() {
		this.DPP = new DoublePlayerPanel();
		this.getContentPane().add(this.DPP);

		this.SPP = new SinglePlayerPanel();
		this.getContentPane().add(this.SPP);

		this.DemoP = new DemoPlayerPanel();
		this.getContentPane().add(this.DemoP);

		this.SP = new SettingPanel();
		this.getContentPane().add(this.SP);

		this.EP = new ExitPanel();
		this.getContentPane().add(this.EP);
	}

	// 将菜单按钮与对应窗体建立映射关系
	public void PanelOfBtnInit() {
		this.PanelOfBtn.put(this.MenuBtn[0], this.DPP);
		this.PanelOfBtn.put(this.MenuBtn[1], this.SPP);
		this.PanelOfBtn.put(this.MenuBtn[2], this.DemoP);
		this.PanelOfBtn.put(this.MenuBtn[3], this.SP);
		this.PanelOfBtn.put(this.MenuBtn[4], this.EP);
	}

	// 重写菜单按钮的鼠标事件监听
	public void MenuBtnMouseEvent(int i) {
		this.MenuBtn[i].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// System.out.println("[MainWindow]:MenuBtn[" + i + "]Entered");
				for(int j = 0; j < WindowDef.menuName.length; j++) {
					if(j == i && j != GameMode) {
						// 鼠标当前在的位置
						MenuBtn[j].setBackground(WindowDef.btnEnteredColor);
					}
					else if(j == GameMode){
						// 当前的游戏模式按钮
						MenuBtn[j].setBackground(WindowDef.btnClickedColor);
					}
					else {
						// 其余按钮
						MenuBtn[j].setBackground(WindowDef.btnOriColor);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// System.out.println("[MainWindow]:MenuBtn[" + i + "]Exited");
				for(int j = 0; j < WindowDef.menuName.length; j++) {
					if(j == GameMode) {
						// 当前的游戏模式按钮
						MenuBtn[j].setBackground(WindowDef.btnClickedColor);
					}
					else {
						// 其余按钮
						MenuBtn[j].setBackground(WindowDef.btnOriColor);
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// System.out.println("[MainWindow]:MenuBtn["+i+"] Clicked");
				for(int j = 0; j < WindowDef.menuName.length; j++) {
					// 设置对应的游戏模式
					// 并将相应按钮的对应的JPanel设置为可见
					PanelOfBtn.get(MenuBtn[j]).setVisible(j == i);
					MenuBtn[j].setBackground( j == i ? 
							WindowDef.btnClickedColor : WindowDef.btnOriColor);

				}
				// 判断是否为退出按钮
				if(i == WindowDef.menuName.length - 1) {
					// 是退出按钮，弹出确认对话框
					int confChoice = JOptionPane.showConfirmDialog(
							ContentPane,
							"确定退出游戏？", 
							"中国象棋", JOptionPane.OK_CANCEL_OPTION);
					// System.out.println(confChoice);
					if(confChoice == 0) { // 确认退出游戏
						dispose();
						System.exit(0);
					}
					else if(confChoice == 2) { // 取消
						// 恢复原来的menuPane[GameMode]为true
						PanelOfBtn.get(MenuBtn[GameMode]).setVisible(true);
						PanelOfBtn.get(MenuBtn[i]).setVisible(false);
					}
				}
				else {
					GameMode = i;
				}
			}

		});
	}

}
