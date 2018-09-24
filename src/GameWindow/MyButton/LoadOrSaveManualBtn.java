package GameWindow.MyButton;

import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import DefaultSet.WindowDef;
import GameWindow.BoarderWin;
import GameWindow.MainWindow;
import GameWindow.GameAPP;
import ChessBase.ManualInfoParse;

/**
 * ���ݲ�ͬģʽ�����ò�ͬ����
 * **/
public class LoadOrSaveManualBtn extends InfoBtn{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String FileName = null;
	public String FileRoot = null;

	public FileDialog FileDia = null;

	public BufferedReader fileReader = null;
	public BufferedWriter fileWriter = null;

	public BoarderWin srcBrdWin;
	public ManualInfoParse srcParse;

	public static final int SaveMode = 0;
	public static final int LoadMode = 1;
	public static final int[] BtnMode = {FileDialog.SAVE, FileDialog.LOAD};
	public static final String[] btnInfo = {"��������", "������"};

	/**
	 * @param tmpo ���tmpo instanceof BoarderWin Ϊtrue, Ϊ���水ť
	 * 	����ΪManualInfoParseΪ�򿪰�ť
	 * **/
	private LoadOrSaveManualBtn(int x, int y, int FontSize, Object tmpo) {

		super(btnInfo[tmpo instanceof BoarderWin ? 0 : 1], x, y, FontSize);

		int btnMode = tmpo instanceof BoarderWin ? 0 : 1;
		this.srcBrdWin = btnMode == 0 ? ((BoarderWin) tmpo) : null;
		this.srcParse = btnMode == 1 ? ((ManualInfoParse) tmpo) : null;
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent arg0) {
				setBackground(WindowDef.btnEnteredColor);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				setBackground(null);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				LoadOrSaveManualBtn tmpBtn = 
						(LoadOrSaveManualBtn) arg0.getSource();
				tmpBtn.FileDia = new FileDialog(GameAPP.gameWindow,
						"�й�����-"+LoadOrSaveManualBtn.btnInfo[btnMode], 
						LoadOrSaveManualBtn.BtnMode[btnMode]);
				tmpBtn.FileDia.setIconImage(Toolkit.getDefaultToolkit().getImage(
						MainWindow.class.getResource(
								"/imageLibary/black-jiang.png")));
				tmpBtn.FileDia.setVisible(true);

				tmpBtn.FileName = tmpBtn.FileDia.getFile();
				tmpBtn.FileRoot = tmpBtn.FileDia.getDirectory();

				// System.out.println(tmpBtn.FileName);
				// System.out.println(tmpBtn.FileRoot);

				if(tmpBtn.FileName != null && tmpBtn.FileRoot != null) {
					if(btnMode == 0) {
						tmpBtn.SaveManual();
					}
					else {
						tmpBtn.LoadManual();
					}
				}
			}

		});
	}

	public LoadOrSaveManualBtn(int x, int y, Object tmpo) {
		this(x, y, WindowDef.buttonWordSize, tmpo);
	}


	public void MainInit() {
		this.FileName = null;
		this.FileRoot = null;
		this.FileDia = null;

		if(this.fileReader != null) {
			try{
				this.fileReader.close();
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		this.fileReader = null;

		if(this.fileWriter != null) {
			try {
				this.fileWriter.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		this.fileWriter = null;
	}


	public void SaveManual() {
		String str = null;
		int Size = 0;
		// �����ļ�
		try{
			// �ļ�Ϊtxt����Ϊqipu�ļ�ʱ������Ӻ�׺
			// Ĭ�����.qipu��׺
			if(this.FileName.indexOf(".txt") == -1 
					&& this.FileName.indexOf(".qipu") == -1) {
				this.FileName += ".qipu";
			}
			fileWriter = new BufferedWriter(
					new FileWriter(new File(FileRoot, FileName)));
			Size = this.srcBrdWin.NowMatch.NowManual.size();
			while(Size-- > 0) {
				str = this.srcBrdWin.NowMatch.NowManual.removeFirst();
				fileWriter.write(str);
				fileWriter.write("\r\n");
				// ���ܰ�֮ǰ�ĶԾ���Ϣɾ���ˣ���Ҫ����д��Manual
				// ����������ٴα���
				this.srcBrdWin.NowMatch.NowManual.addLast(str);
			}
			fileWriter.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		// System.out.println("[LoadOrSaveManualBtn]:�ļ���"+this.FileName+"������ɹ�");
	}

	public void LoadManual() {
		try{
			if(this.FileName.indexOf(".txt") == -1
					&& this.FileName.indexOf(".qipu") == -1) {
				// ���������ļ�
				this.MainInit();
				return;
			}
			fileReader = new BufferedReader(
					new FileReader(new File(FileRoot, FileName)));
			this.srcParse.SetManual(fileReader);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		// System.out.println("[LoadOrSaveManualBtn]:�ļ���"+this.FileName+"�����سɹ�");
	}



}
