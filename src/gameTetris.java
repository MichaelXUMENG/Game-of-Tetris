import java.awt.*;
import java.awt.event.*;


public class gameTetris extends Frame{

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new gameTetris();
	}
	
	gameTetris(){
		super("Game of Tetris");
		//add the close window action
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
class mainArea extends Panel implements MouseMotionListener, ActionListener{

	
	//declare the button and labels
	public Button quit; 
	public Label level, line, score, level_data, line_data, score_data;
	
	//declare the variables used to calculate the coordinates
	int centerX, centerY; // the central pixel's position
	float pixelSize, width, height, //width and height is the logical width and height of my elements
	rWidth = 100.0F, rHeight = 130.0F, 
			sqSize; //logical size of a square
	
	int PX, PY, SX, SY, Es; //the actual position of pixel: PX,PY are position; SX,SY are actual size, and Es is the actual size of a square.
	int[][] sqPositionX = new int[20][10];
	int[][] sqPositionY = new int[20][10];

	drawElements dE = new drawElements( );




	void initial(){
		Dimension d = this.getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		pixelSize = Math.max(rWidth/maxX, rHeight/maxY);
		centerX = maxX/2;
		centerY = maxY/2;
		width = rWidth*3/5;
		height = 2 * width;
		
		sqSize = width/10; //the size of each square
		Es =  Math.round(sqSize/ pixelSize);//calculate the actual size of a square(how many pixels in one side)
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
		quit.addActionListener(this);
		
		this.add(level);
		this.add(level_data);
		this.add(line);
		this.add(line_data);
		this.add(score);
		this.add(score_data);
		this.add(quit);
		
		this.addMouseMotionListener(this);
	}
	
	public void paint(Graphics g){
		super.paint(g);
		initial();
		g.setColor(Color.white);
		
		//create the big white area
		PX=iX(-rWidth/2); PY=iY(rHeight/2 - 5); //get the position left-top corner of the big area
		SX= Math.round(width / pixelSize); SY=Math.round(height/pixelSize); //get the size of the big area
		dE.fillArea(g, Es, PX, PY, 10, 20);
		
		//store all the position into the array
		for(int i = 0; i<20; i++){
			for(int j = 0; j<10; j++){
				sqPositionX[i][j] = PX + j*Es;
				sqPositionY[i][j] = PY + i*Es;
			}
		}
		
		//create the small area
		width = rWidth*2/5; height = width/2;
		PX=iX(rWidth/5); PY=iY(rHeight/2 - 5); 
		SX= Math.round(width / pixelSize); SY=Math.round(height/pixelSize);
		g.fillRect(PX, PY, SX , SY);
		
		//set the position of labels and button
		SX = Math.round((width/2) / pixelSize);
		level.setBounds(PX,iY(rHeight/2 - height - 10),SX , SY);
		line.setBounds(PX,iY(rHeight/2 - 2*height - 10),SX , SY);
		score.setBounds(PX,iY(rHeight/2 - 3*height - 10),SX , SY);
		
		quit.setBounds(PX,iY(rHeight/2 - 4*height - 10),SX*2 , SY);
		
		PX = iX(rWidth/5 + width/2);
		level_data.setBounds(PX,iY(rHeight/2 - height - 10),SX , SY);
		line_data.setBounds(PX,iY(rHeight/2 - 2*height - 10),SX , SY);
		score_data.setBounds(PX, iY(rHeight/2 - 3*height - 10), SX , SY);
		
		
		/***********
		 * start to draw the elements
		 * *************************/
		int ePX, ePY;
		
		ePX = iX(rWidth/5 + (width- 4*sqSize)/2);
		ePY = iY((rHeight/2 - 5)- (height-sqSize)/2);
		
/*		g.setColor(Color.black);
		for(int i = 0; i<4; i++){
			g.drawRect(ePX+i*Es, ePY, Es, Es);
		}
		g.setColor(Color.green);
		for(int i = 0; i<4; i++){
			g.fillRect(ePX+i*Es+1, ePY+1, Es-1, Es-1);
		}
		*/
		
		dE.drawZShape(g, Es, ePX, ePY);
		
		dE.drawLShape(g, Es, sqPositionX[19][7], sqPositionY[19][7]);
		dE.drawZShape(g, Es, sqPositionX[19][5], sqPositionY[19][5]);
		dE.drawCube(g, Es, sqPositionX[5][5], sqPositionY[5][5]);
		dE.drawLine(g, Es, sqPositionX[10][5], sqPositionY[10][5]);
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==quit){
			System.exit(0);
		}
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		int Mx, My;
		boolean inside = false;
		Mx = e.getX();
		My = e.getY();
		
		Graphics g = getGraphics();
		if(Mx>=sqPositionX[0][0] & Mx<=sqPositionX[19][9]+Es){
			if(My>=sqPositionY[0][0] & My<=sqPositionY[19][9]+Es){
				if(!inside){
					inside = true;
					dE.drawString(g, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
				}
			}
			else{
				if(!inside){
					inside = false;
//					dE.clearSring(g, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
					repaint();
				}
			}
		}
		else{
			if(!inside){
				inside = false;
//				dE.clearSring(g, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
				repaint();
			}
		}
		
	}

	
	
	
}



/*********************************************
 * This is the Class for elements drawing including the line, cube, LShape and ZShape
 * ***************************************/
class drawElements{

	
	void drawLine(Graphics g, int size, int Px, int Py){
		g.setColor(Color.black);
		for(int i = 0; i<4; i++){
			g.drawRect(Px+i*size, Py, size, size);
		}
		g.setColor(Color.red);
		for(int i = 0; i<4; i++){
			g.fillRect(Px+i*size+1, Py+1, size-1, size-1);
		}	
	}
	
	void drawCube(Graphics g, int size, int Px, int Py){
		g.setColor(Color.black);
		for(int i = 0; i<2; i++){
			for(int j = 0; j<2; j++)
				g.drawRect(Px+j*size, Py-i*size, size, size);
		}
		g.setColor(Color.green);
		for(int i = 0; i<2; i++){
			for(int j = 0; j<2; j++)
				g.fillRect(Px+j*size+1, Py-i*size+1, size-1, size-1);
		}
	}
	
	void drawLShape(Graphics g, int size, int Px, int Py){
		g.setColor(Color.black);
		for(int i = 0; i<3; i++){
			g.drawRect(Px+i*size, Py, size, size);
		}
		g.drawRect(Px+2*size, Py-size, size, size);
		g.setColor(Color.blue);
		for(int i = 0; i<3; i++){
			g.fillRect(Px+i*size+1, Py+1, size-1, size-1);
		}
		g.fillRect(Px+2*size+1, Py-size+1, size-1, size-1);
	}
	
	void drawZShape(Graphics g, int size, int Px, int Py){
		g.setColor(Color.black);
		for(int i = 0; i<2; i++){
			for(int j = 0; j<2; j++)
				g.drawRect(Px+(j+i)*size, Py-i*size, size, size);
		}
		g.setColor(Color.yellow);
		for(int i = 0; i<2; i++){
			for(int j = 0; j<2; j++)
				g.fillRect(Px+(j+i)*size+1, Py-i*size+1, size-1, size-1);
		}
	}
	
	void fillArea(Graphics g, int size, int Px, int Py, int length, int height){
		g.setColor(Color.white);
		for(int i =0; i< height; i++){
			for(int j = 0; j< length; j++){
				g.fillRect(Px+j*size, Py+i*size, size+1, size+1);
			}
		}	
	}
	
	void drawString(Graphics g, int Px, int Py, String s){
		g.setColor(Color.black);
		g.drawRect(Px, Py, 215, 65);
		g.setFont(new Font("Times New Romen", Font.BOLD, 60));
		g.drawString(s, Px+10, Py+55);
	}
	
	void clearSring(Graphics g, int Px, int Py, String s){
		g.setColor(Color.white);
		g.drawRect(Px, Py, 215, 65);
		g.setFont(new Font("Times New Romen", Font.BOLD, 60));
		g.drawString(s, Px+10, Py+55);
		
	}
} 
