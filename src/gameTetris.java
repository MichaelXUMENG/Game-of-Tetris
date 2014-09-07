import java.awt.*;
import java.awt.event.*;


public class gameTetris extends Frame{

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new gameTetris();
	}
	
	gameTetris(){
		super("Game of Tetris");
		addWindowListener(new WindowAdapter()
		   {public void windowClosing(WindowEvent e){System.exit(0);}});
		
		Dimension screenSize = 
				Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		
		int x = screenWidth/8;
		int y = screenHeight/16;
		
		setSize(600,screenHeight*7/8);
		this.setLocation(x, y);
		
		this.setLayout(new BorderLayout());
		this.add(new mainArea(), BorderLayout.EAST);
		show();
	}

}

class mainArea extends Canvas{
	int centerX, centerY;
	
	void initial(){
		Dimension d = this.getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		centerX = maxX/2;
		centerY = maxY/2;
	}
	
	public void paint(Graphics g){
		initial();
		g.drawString("Hello, this is the main area", centerX, centerY);
	}
}
