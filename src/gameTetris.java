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
		
		setSize(screenWidth*3/7,screenHeight*6/8);
		this.setLocation(x, y);
		
		
		add(new mainArea());
		show();
	}

}

/************
 * To show the mainArea with the painting area and labels and buttons together,
 * All the elements must be put in the same layer
 * ****************************************/
class mainArea extends Panel{
	
	//declare the button and labels
	public Button quit; 
	public Label level, line, score, level_data, line_data, score_data;
	
	//declare the variables used to calculate the coordinates
	int centerX, centerY;
	float ris = 0.95F;
	float pixelSize, width, height, rWidth = 100.0F, rHeight = 130.0F;
	
	int PX, PY, SX, SY;
	
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
	
	mainArea(){
		this.setLayout(null);
		
		//label 1 show the level of game
		level = new Label("Level: ");
		level_data = new Label("1");
		
		//label 2 shows the lines of game
		line = new Label("Lines: ");
		line_data = new Label("0");
		
		//label 3 shows the score of game
		score = new Label("Score: ");
		score_data = new Label("0");
		
		//quit button
		quit = new Button("QUIT");
		
		this.add(level);
		this.add(level_data);
		this.add(line);
		this.add(line_data);
		this.add(score);
		this.add(score_data);
		this.add(quit);
	}
	
	public void paint(Graphics g){
		initial();
		g.setColor(Color.white);
		
		//create the big white area
		PX=iX(-rWidth/2); PY=iY(rHeight/2 - 5); SX= Math.round(width / pixelSize); SY=Math.round(height/pixelSize);
		g.fillRect( PX , PY, SX , SY);
		
		//create the small area
		width = rWidth/5; height = width/2;
		PX=iX(rWidth/5); PY=iY(rHeight/2 - 5); SX= Math.round(width / pixelSize); SY=Math.round(height/pixelSize);
		g.fillRect(PX, PY, SX , SY);
		
		
		level.setBounds(PX,iY(rHeight/2 - 2*height - 10),SX , SY);
		line.setBounds(PX,iY(rHeight/2 - 4*height - 10),SX , SY);
		score.setBounds(PX,iY(rHeight/2 - 6*height - 10),SX , SY);
		
		quit.setBounds(PX,iY(rHeight/2 - 8*height - 10),SX , SY);
		
		PX = iX(rWidth/5 + width);
		level_data.setBounds(PX,iY(rHeight/2 - 2*height - 10),SX , SY);
		line_data.setBounds(PX,iY(rHeight/2 - 4*height - 10),SX , SY);
		score_data.setBounds(PX, iY(rHeight/2 - 6*height - 10), SX , SY);
	}
}


/*
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
}   */
