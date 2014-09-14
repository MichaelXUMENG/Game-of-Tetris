import java.awt.*;
import java.awt.event.*;


public class gameTetris extends Frame{

	functionArea func;
	
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
		
		setSize(screenWidth/2,screenHeight*7/8);
		this.setLocation(x, y);
		
		func = new functionArea();
		
		add(func);
//		add(new mainArea());
		show();
	}

}

class mainArea extends Canvas{
	int centerX, centerY;
	float ris = 0.95F;
	float pixelSize, width, height, rWidth = 100.0F, rHeight = 200.0F;
	
	void initial(){
		Dimension d = this.getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
		centerX = maxX/2;
		centerY = maxY/2;
		width = rWidth*3/5;
		height = 2 * width;
	}
	
	int iX(float x){return Math.round(centerX + x/pixelSize);}
	int iY(float y){return Math.round(centerY - y/pixelSize);}
	
	public void paint(Graphics g){
		initial();
		g.setColor(Color.white);
		g.fillRect( iX(-rWidth/2) , iY(rHeight*ris/2), Math.round(width / pixelSize) , Math.round(height/pixelSize));
		width = rWidth/5; height = width/2;
		g.fillRect(iX(rWidth/5), iY(rHeight*ris/2), Math.round(width/pixelSize) , Math.round(height/pixelSize));
	}
}

class functionArea extends Panel{
	public Button quit;
	public Label level, line, score, level_data, line_data, score_data;
	
	int centerX, centerY;
	float ris = 0.95F;
	float pixelSize, width, height, rWidth = 100.0F, rHeight = 200.0F;
	
	void initial(){
		Dimension d = this.getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
		centerX = maxX/2;
		centerY = maxY/2;
		width = rWidth*3/5;
		height = 2 * width;
	}
	
	int iX(float x){return Math.round(centerX + x/pixelSize);}
	int iY(float y){return Math.round(centerY - y/pixelSize);}
	
	functionArea(){
		initial();
		this.setSize(Math.round(width/pixelSize) , Math.round(height/pixelSize));
		this.setLocation(iX(rWidth/5), iY(rHeight*ris/2 - rWidth*0.1F));
		this.setLayout(null);
		level = new Label("Level: ");
		level_data = new Label("1");
		line = new Label("Lines: ");
		line_data = new Label("0");
		score = new Label("Score: ");
		score_data = new Label("0");
		quit = new Button("QUIT");
		
		this.add(level);
		this.add(level_data);
		this.add(line);
		this.add(line_data);
		this.add(score);
		this.add(score_data);
//		this.add(quit);
		
		level.setBounds(320,15,30,20);
		level_data.setBounds(320,45,30,20);
		line.setBounds(320,75,30,20);
		line_data.setBounds(320,105,30,20);
		score.setBounds(320,135,30,20);
//		score_data.setBounds(iX(rWidth/5), iY(rHeight*ris/2), Math.round(width/pixelSize) , Math.round(height/pixelSize));
		
	}
}
