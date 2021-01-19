import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setContentPane(new AppPanel());
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Paint App");
		
		frame.pack();
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}
