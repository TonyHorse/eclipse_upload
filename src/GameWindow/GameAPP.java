package GameWindow;

import java.awt.EventQueue;

public class GameAPP {

	public static MainWindow gameWindow;

	public static void main(String[] args) {
		System.out.println("In Test2");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					gameWindow = new MainWindow();
					gameWindow.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
