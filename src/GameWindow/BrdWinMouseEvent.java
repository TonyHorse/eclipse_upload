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
		// ���������λ�ý�����Ӳ��ּ������������̸��
		int x = arg0.getX();
		int y = arg0.getY();
		x = (x - BoarderDef.ChessBoarderXX) / BoarderDef.ChessBoarderPP;
		y = (y - BoarderDef.ChessBoarderYY) / BoarderDef.ChessBoarderPP;

		BoarderWin tmpBrdWin = (BoarderWin) arg0.getSource();

		// �������û�б�����,�޸ĵ���㵽�����⣬����㲻������
		if(!tmpBrdWin.ClickEnabled) {
			x = -1;
			y = -1;
		}

		if(x < 0 || x > 8 || y < 0 || y > 9) {
			// �����������֮��
			return;
		}

		this.SetChosePos(tmpBrdWin, x, y);
	}


	public void PieceFlashing(Point[] Pos, BoarderWin BrdWin) {
		new Thread(() -> {
			ChessPiece[] tmpPieces = new ChessPiece[Pos.length];
			BrdWin.SetClickEnabledFalse();
			for(int i = 0; i < Pos.length; i++) {
				// ����Щλ�ñ�������
				tmpPieces[i] = BrdWin.BrdGetPiece(Pos[i]);
			}
			int cnt = 0;
			while(cnt++ < 3) {
				for(int i = 0; i < Pos.length; i++) {
					// ����Щλ���ÿ�
					BrdWin.SetPiece(Pos[i], null);
					BrdWin.repaint();
				}
				try{
					Thread.sleep(100);
				}catch(Exception e) {
					e.printStackTrace();
				}

				for(int i = 0; i < Pos.length; i++) {
					// ����Щλ�ûָ�
					BrdWin.SetPiece(Pos[i], tmpPieces[i]);
					BrdWin.repaint();
				}
				try{
					Thread.sleep(100);
				}catch(Exception e) {
					e.printStackTrace();
				}
				// System.out.println("[BrdWinMouseEvent]:��˸����");
			}
			for(int i = 0; i < Pos.length; i++) {
				// ������λ�ûָ�
				BrdWin.SetPiece(Pos[i], tmpPieces[i]);
			}
			BrdWin.SetClickEnabledTrue();
		}).start();
	}

	public void PieceFlashing(Point a, Point b, BoarderWin brdWin) {
		Point[] tmp = {a, b};
		this.PieceFlashing(tmp, brdWin);
	}


	// ����BoarderWin��srcp��desp
	private void SetChosePos(BoarderWin tmpBrdWin, int x, int y) {
		new Thread(() -> {
			// ����srcp�����
			// srcp = null
			// srcp ��Ϊ null���Ƕ�Ӧ�����̸��û������
			// srcp ��Ϊnull��Ӧ�����̵������ӵ��ǲ�Ϊ��������
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
				// �ڲ���������������ǰ����
				// ����desp��
				// ��ǰ��������̸��û������
				// ��ǰ��������̸��Ϊ�Է�����
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
							// ���ӷ���ʧ�ܣ�����ѡ��desp
							tmpBrdWin.Setdesp(null);
						}
						else {
							// ���Ӻ󼺷������������Ŷ���
							if(tmpBrdWin.NowMatch.NowPlayer == '��') {
								this.PieceFlashing(tmpBrdWin.ReversePoint(tmp), 
										tmpBrdWin.ReversePoint(
												tmpBrdWin.NowMatch.NowBoarder.PiecesPos[20]), 
										tmpBrdWin);
							}
							else if(tmpBrdWin.NowMatch.NowPlayer == '��'){
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