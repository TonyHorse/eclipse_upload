package GameWindow;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import DefaultSet.BoarderDef;
import ChessBase.ChessPiece;

public class BrdWinMouseEvent extends MouseAdapter{
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// 根据鼠标点击位置结合棋子布局计算出点击的棋盘格点
		int x = arg0.getX();
		int y = arg0.getY();
		x = (x - BoarderDef.ChessBoarderXX) / BoarderDef.ChessBoarderPP;
		y = (y - BoarderDef.ChessBoarderYY) / BoarderDef.ChessBoarderPP;

		BoarderWin tmpBrdWin = (BoarderWin) arg0.getSource();

		// 点击功能没有被开启,修改点击点到棋盘外，点击便不起作用
		if(!tmpBrdWin.ClickEnabled) {
			x = -1;
			y = -1;
		}

		if(x < 0 || x > 8 || y < 0 || y > 9) {
			// 点击点在棋盘之外
			return;
		}

		this.SetChosePos(tmpBrdWin, x, y);
	}


	public void PieceFlashing(Point[] Pos, BoarderWin BrdWin) {
		new Thread(() -> {
			ChessPiece[] tmpPieces = new ChessPiece[Pos.length];
			BrdWin.SetClickEnabledFalse();
			for(int i = 0; i < Pos.length; i++) {
				// 将这些位置保存起来
				tmpPieces[i] = BrdWin.BrdGetPiece(Pos[i]);
			}
			int cnt = 0;
			while(cnt++ < 3) {
				for(int i = 0; i < Pos.length; i++) {
					// 将这些位置置空
					BrdWin.SetPiece(Pos[i], null);
					BrdWin.repaint();
				}
				try{
					Thread.sleep(100);
				}catch(Exception e) {
					e.printStackTrace();
				}

				for(int i = 0; i < Pos.length; i++) {
					// 将这些位置恢复
					BrdWin.SetPiece(Pos[i], tmpPieces[i]);
					BrdWin.repaint();
				}
				try{
					Thread.sleep(100);
				}catch(Exception e) {
					e.printStackTrace();
				}
				// System.out.println("[BrdWinMouseEvent]:闪烁结束");
			}
			for(int i = 0; i < Pos.length; i++) {
				// 将上述位置恢复
				BrdWin.SetPiece(Pos[i], tmpPieces[i]);
			}
			BrdWin.SetClickEnabledTrue();
		}).start();
	}

	public void PieceFlashing(Point a, Point b, BoarderWin brdWin) {
		Point[] tmp = {a, b};
		this.PieceFlashing(tmp, brdWin);
	}


	// 设置BoarderWin的srcp与desp
	private void SetChosePos(BoarderWin tmpBrdWin, int x, int y) {
		new Thread(() -> {
			// 设置srcp的情况
			// srcp = null
			// srcp 不为 null但是对应的棋盘格点没有棋子
			// srcp 不为null对应的棋盘点有棋子但是不为本方棋子
			Point tmpsrcp = tmpBrdWin.Getsrcp();
			if (tmpsrcp == null
					|| tmpBrdWin.BrdGetPiece(tmpsrcp.y, tmpsrcp.x) == null
					|| tmpBrdWin.BrdGetPiece(tmpsrcp.y, tmpsrcp.x).name.charAt(0) 
					!= tmpBrdWin.NowMatch.NowPlayer){
				SwingUtilities.invokeLater(() -> {
					tmpBrdWin.Setsrcp(new Point(x,y));
				});
			}
			else {
				// 在不满足上述条件的前提下
				// 设置desp：
				// 当前点击的棋盘格点没有棋子
				// 当前点击的棋盘格点为对方棋子
				if(tmpBrdWin.BrdGetPiece(y, x) == null 
						|| tmpBrdWin.BrdGetPiece(y, x).name.charAt(0) 
						!= tmpBrdWin.NowMatch.NowPlayer) {
					SwingUtilities.invokeLater(() -> {
						tmpBrdWin.Setdesp(new Point(x,y));
					});

					SwingUtilities.invokeLater(() -> {
						Point tmp = 
								tmpBrdWin.BrdPieceMove();
						if(tmp == null) {
							// do nothing
						}
						else if(new Point(-1, -1).equals(tmp)){
							// 走子犯规失败，重新选择desp
							tmpBrdWin.Setdesp(null);
						}
						else {
							// 走子后己方被将军，播放动画
							if(tmpBrdWin.NowMatch.NowPlayer == '红') {
								this.PieceFlashing(tmpBrdWin.ReversePoint(tmp), 
										tmpBrdWin.ReversePoint(
												tmpBrdWin.NowMatch.NowBoarder.PiecesPos[20]), 
										tmpBrdWin);
							}
							else if(tmpBrdWin.NowMatch.NowPlayer == '黑'){
								this.PieceFlashing(tmpBrdWin.ReversePoint(tmp), 
										tmpBrdWin.ReversePoint(
												tmpBrdWin.NowMatch.NowBoarder.PiecesPos[4]), 
										tmpBrdWin);
							}
						}
					});
				}
				else {
					SwingUtilities.invokeLater(() -> {
						tmpBrdWin.Setsrcp(new Point(x,y));
					});
				}
			}
		}).start();
	}
}