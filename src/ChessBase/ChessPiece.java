package ChessBase;

import java.awt.Image;
import java.awt.Toolkit;

public class ChessPiece {
	public ChessPiece() {
		this.id = -1;
		this.Icon = null;
		this.name = null;
	}

	public ChessPiece(int pieceID) {
		int realID = -1;
		// ����pieceID�жϳ�����һ������
		if(pieceID < 5) {
			realID = pieceID;
		}
		else if(pieceID < 9) {
			realID = 8 - pieceID;
		}
		else if(pieceID < 11) {
			realID = 5;
		}
		else if(pieceID < 16) {
			realID = 6;
		}
		else if(pieceID < 21) {
			realID = pieceID - 9;
		}
		else if(pieceID < 25) {
			realID = 31 - pieceID;
		}
		else if(pieceID < 27) {
			realID = 12;
		}
		else if(pieceID < 32) {
			realID = 13;
		}
		if(realID != -1) {
			this.id = pieceID;
			this.name = new String(PiecesName[realID]);
			try {
				this.Icon = Toolkit.getDefaultToolkit().getImage(
						// ͨ�����·����λͼƬλ��
						ChessPiece.class.getResource(
								"/imageLibary/" 
						+ PiecesImgName[realID] + ".png"));
			}
			catch(Exception e) {
				System.err.println("[ChessPiece]:ͼƬ����ʧ��");
			}
		}
		else {
			System.out.println("[ChessPiece]:pieceID("
					+ pieceID + ")����0~32֮��");
		}
	}


	/***************************************************
	 * ������Ϣ
	 **************************************************/
	/**
	 *  ���ӵ���ʵID��ȡֵ��Χ0~31
	 *  0~15Ϊ�ڷ���16~31Ϊ�췽
	 * **/
	public int id = -1;

	/**
	 * ���ӵ�ͼƬ
	 * **/
	public Image Icon = null;

	/**
	 * ͨ��nameֱ���ж����ӵ����Ժ��жϿ��е���Ϊ
	 * **/
	public String name = null;

	
	/***************************************************
	 * ������Ϣ
	 **************************************************/
	/**
	 * 0���ڳ�
	 * 1������
	 * 2������
	 * 3����ʿ
	 * 4���ڽ�
	 * 5������
	 * 6������
	 * ---------
	 * 7���쳵
	 * 8������
	 * 9������
	 * 10����ʿ
	 * 11����˧
	 * 12������
	 * 13�����
	 * ʵ��IdΪ32��,����ֱ��ͨ��ID��λĳ�����ӵ�λ��
	 * ������ͨ���������̵õ�
	 * black��0 1 2 3   4 5 6 7 8          9 10    11 12 13 14 15
	 * ju ma xiang shi  jiang shi xiang ma ju pao*2    zu*5
	 * red��16 17 18 19  20 21 22 23 24          25 26   27 28 29 30 31
	 * ju ma xiang shi   shuai shi xiang ma ju   pao*2   bing*5
	 */
	public static final String[] PiecesName = {
			"�ڳ�", "����", "����", "��ʿ", "�ڽ�", "����", "����", 
			"�쳵", "����", "����", "����", "��˧", "����", "���"};
	public static final String[] PiecesImgName = {
			"black-ju", "black-ma", "black-xiang", "black-shi",
			"black-jiang", "black-pao", "black-zu",
			"red-ju", "red-ma", "red-xiang", "red-shi",
			"red-shuai", "red-pao", "red-bing"};

}
