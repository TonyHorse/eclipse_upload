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

	// ��Ϸ������
	public JPanel ContentPane;

	// ��Ϸ�˵���ť
	public JButton[] MenuBtn;

	// ��Ϸģʽ
	int GameMode;

	// ��Ϸ��ť���Ӧ�Ŀ�Ƭ����Ķ�Ӧ
	public Map<JButton, JPanel> PanelOfBtn = new HashMap<JButton, JPanel>();

	// ��ͬ�Ĳ˵�ģʽ�µĲ��ִ���
	public DoublePlayerPanel DPP;
	public SinglePlayerPanel SPP;
	public DemoPlayerPanel DemoP;
	public SettingPanel SP;
	public ExitPanel EP;


	public MainWindow() {
		GameMode = -1; // ����ǰ�����κ�ģʽ��
		this.ContentPaneInit();
		this.BackgroundInit();
		this.MenuBtnInit();
		this.MenuPanelInit();
		this.PanelOfBtnInit();

		for(int i = 0; i < WindowDef.menuName.length; i++) {
			this.MenuBtnMouseEvent(i);
		}
	}

	// ��Ϸ������ʼ��
	public void BackgroundInit(ImageIcon bgImg) {
		// ����Сͼ��
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				MainWindow.class.getResource(
						"/imageLibary/black-jiang.png")));
		this.setTitle("�й�����");
		this.setBounds(0, 0, 
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);
		// ���ô��ڲ��ɸı��С
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);

		//��ӱ���ͼƬ
		JLabel BackGround = new JLabel("");
		BackGround.setIcon(bgImg);
		BackGround.setBounds(0, 0,
				WindowDef.GameWinWidth, WindowDef.GameWinHeight);
		//���ڷֲ㲼�ֵ���ײ�
		this.getLayeredPane().add(BackGround, new Integer(Integer.MIN_VALUE));
	}
	public void BackgroundInit() { // ʹ��Ĭ�ϱ���
		this.BackgroundInit(new ImageIcon(Toolkit.getDefaultToolkit()
				.getImage(MainWindow.class.getResource(
						"/imageLibary/background.png"))));
	}

	// ��Ϸ�������ʼ��
	public void ContentPaneInit() {
		if(this.ContentPane == null) {
			this.ContentPane = new JPanel();
			this.setContentPane(this.ContentPane);
			this.ContentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			this.ContentPane.setOpaque(false);
			this.ContentPane.setLayout(null);
		}
	}

	// ��ť��ʼ��
	public void MenuBtnInit() {
		if(this.MenuBtn == null) {
			this.MenuBtn = new JButton[WindowDef.menuName.length];
			for(int i = 0; i < WindowDef.menuName.length; i++) {
				this.MenuBtn[i] = new JButton();
				this.MenuBtn[i].setVisible(true);
				this.MenuBtn[i].setText(WindowDef.menuName[i]);
				this.MenuBtn[i].setFont(new Font("�����п�", 
						Font.CENTER_BASELINE, WindowDef.menuWordSize));
				this.MenuBtn[i].setBackground(WindowDef.menuOriColor);
				this.MenuBtn[i].setBounds(WindowDef.menuLeftPos.x, 
						WindowDef.menuLeftPos.y + WindowDef.menuDis * i, 294, 62);
				this.getContentPane().add(this.MenuBtn[i]);
			}
		}
	}

	// ��Ϸ���ֳ�ʼ��
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

	// ���˵���ť���Ӧ���彨��ӳ���ϵ
	public void PanelOfBtnInit() {
		this.PanelOfBtn.put(this.MenuBtn[0], this.DPP);
		this.PanelOfBtn.put(this.MenuBtn[1], this.SPP);
		this.PanelOfBtn.put(this.MenuBtn[2], this.DemoP);
		this.PanelOfBtn.put(this.MenuBtn[3], this.SP);
		this.PanelOfBtn.put(this.MenuBtn[4], this.EP);
	}

	// ��д�˵���ť������¼�����
	public void MenuBtnMouseEvent(int i) {
		this.MenuBtn[i].addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// System.out.println("[MainWindow]:MenuBtn[" + i + "]Entered");
				for(int j = 0; j < WindowDef.menuName.length; j++) {
					if(j == i && j != GameMode) {
						// ��굱ǰ�ڵ�λ��
						MenuBtn[j].setBackground(WindowDef.btnEnteredColor);
					}
					else if(j == GameMode){
						// ��ǰ����Ϸģʽ��ť
						MenuBtn[j].setBackground(WindowDef.btnClickedColor);
					}
					else {
						// ���ఴť
						MenuBtn[j].setBackground(WindowDef.btnOriColor);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// System.out.println("[MainWindow]:MenuBtn[" + i + "]Exited");
				for(int j = 0; j < WindowDef.menuName.length; j++) {
					if(j == GameMode) {
						// ��ǰ����Ϸģʽ��ť
						MenuBtn[j].setBackground(WindowDef.btnClickedColor);
					}
					else {
						// ���ఴť
						MenuBtn[j].setBackground(WindowDef.btnOriColor);
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// System.out.println("[MainWindow]:MenuBtn["+i+"] Clicked");
				for(int j = 0; j < WindowDef.menuName.length; j++) {
					// ���ö�Ӧ����Ϸģʽ
					// ������Ӧ��ť�Ķ�Ӧ��JPanel����Ϊ�ɼ�
					PanelOfBtn.get(MenuBtn[j]).setVisible(j == i);
					MenuBtn[j].setBackground( j == i ? 
							WindowDef.btnClickedColor : WindowDef.btnOriColor);

				}
				// �ж��Ƿ�Ϊ�˳���ť
				if(i == WindowDef.menuName.length - 1) {
					// ���˳���ť������ȷ�϶Ի���
					int confChoice = JOptionPane.showConfirmDialog(
							ContentPane,
							"ȷ���˳���Ϸ��", 
							"�й�����", JOptionPane.OK_CANCEL_OPTION);
					// System.out.println(confChoice);
					if(confChoice == 0) { // ȷ���˳���Ϸ
						dispose();
						System.exit(0);
					}
					else if(confChoice == 2) { // ȡ��
						// �ָ�ԭ����menuPane[GameMode]Ϊtrue
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
