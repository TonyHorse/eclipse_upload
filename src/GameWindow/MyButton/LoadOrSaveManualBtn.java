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
 * 根据不同模式，启用不同功能
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
	public static final String[] btnInfo = {"保存棋谱", "打开棋谱"};

	/**
	 * @param tmpo 如果tmpo instanceof BoarderWin 为true, 为保存按钮
	 * 	否则为ManualInfoParse为打开按钮
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
						"中国象棋-"+LoadOrSaveManualBtn.btnInfo[btnMode], 
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
		// 保存文件
		try{
			// 文件为txt或者为qipu文件时，不添加后缀
			// 默认添加.qipu后缀
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
				// 不能把之前的对局信息删除了，需要重新写入Manual
				// 方便后续的再次保存
				this.srcBrdWin.NowMatch.NowManual.addLast(str);
			}
			fileWriter.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		// System.out.println("[LoadOrSaveManualBtn]:文件【"+this.FileName+"】保存成功");
	}

	public void LoadManual() {
		try{
			if(this.FileName.indexOf(".txt") == -1
					&& this.FileName.indexOf(".qipu") == -1) {
				// 不是棋谱文件
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
		// System.out.println("[LoadOrSaveManualBtn]:文件【"+this.FileName+"】加载成功");
	}



}
