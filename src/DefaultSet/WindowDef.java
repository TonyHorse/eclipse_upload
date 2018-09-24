package DefaultSet;

import java.awt.Point;
import java.awt.Color;

/*
 * 游戏窗口某些菜单的默认参数的基数
 * 便于绘制随游戏窗口大小变化而变化的菜单
 */

public class WindowDef {
	// 左侧菜单栏的文字未被点击时的颜色
	public static final int menuWordSize = 58;
	public static final Color menuOriColor = new Color(108, 108, 108);
	// 右侧按钮中文字的大小
	public static final int buttonWordSize = 38;
	// 菜单栏
	public static final String[] menuName = {
			"双人对战",
			"人机对战",
			"棋谱演示",
			"游戏设置",
			"退出游戏"
	};
	// 菜单栏左上角位置
	public static final Point menuLeftPos = new Point(60, 60);
	// 每两个菜单栏目间隔距离
	public static final int menuDis = 88;
	// 游戏窗口的宽和高
	public static final int GameWinWidth = 1366;
	public static final int GameWinHeight = 768;	
	// 右侧button的鼠标事件颜色
	public static final Color btnOriColor = new Color(105, 105, 105);
	public static final Color btnEnteredColor = new Color(168, 168, 168);
	public static final Color btnClickedColor = null;
}
