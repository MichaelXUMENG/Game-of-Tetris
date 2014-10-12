import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class gameTetris extends Panel implements MouseMotionListener, ActionListener,MouseListener, MouseWheelListener{

	static int current, next;
	static boolean start = true;
	static boolean round = false;
	static boolean falling = true;
	
	//declare the button and labels
	static public Button quit; 
	static public Label level, line, score, level_data, line_data, score_data;
	
	static Frame outFrame = new Frame();
		
	//declare the variables used to calculate the coordinates
	static int centerX, centerY; // the central pixel's position
	static float pixelSize, width, height, //width and height is the logical width and height of my elements
	rWidth = 100.0F, rHeight = 130.0F,     //used to calculate the pixel size
			sqSize; //logical size of a square
		
	static int PX, PY, SX, SY, Es; //the actual position of pixel: PX,PY are position; SX,SY are actual size, and Es is the actual size of a square.
	static int[][] sqPositionX = new int[20][10];
	static int[][] sqPositionY = new int[20][10];
	static int[][] stored = new int[20][10];
	static int[] highest = new int[10];

	static drawElements dE = new drawElements( );
		
	static int row = 0, collum = 3;
	static int shape;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		gameTetris gT = new gameTetris();
		
		Dimension screenSize = 
				Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		
		int x = screenWidth/8;
		int y = screenHeight/16;
		
		
		outFrame.setTitle("Game of Tetris");
		//add the close window action
		outFrame.addWindowListener(new WindowAdapter()
				   {public void windowClosing(WindowEvent e){System.exit(0);}});
		outFrame.setSize(screenWidth*3/7,screenHeight*6/8);
		outFrame.setLocation(x, y);
		
		outFrame.add(gT);
		
		outFrame.setResizable(true);
		outFrame.setVisible(true);
		
		for(int i = 0; i<10; i++){
			highest[i] = 19;
		}
		for (int i = 0; i<20; i++){
			for(int j = 0; j<10; j++){
				stored[i][j] = 0;
			}
		}
		
		
		next = new Random().nextInt(6);
		current = next;
		
		/************* Function of falling ***********************/
		while(true){
			while(start){ // Start to fall
				current = next;
				next = new Random().nextInt(7);
				shape = 0;
				/*********** Customerize the start point of each shape *************************/
				switch(current){
				case 0:{
					row = 1;
					collum = 4;
					break;
				}
				case 1:{
					row = 1;
					collum = 5;
					break;
				}
				case 2:{
					row = 1;
					collum = 4;
					break;
				}
				case 3:{
					row = 1;
					collum = 5;
					break;
				}
				case 4:{
					row = 1;
					collum = 4;
					break;
				}
				case 5:{
					row = 1;
					collum = 4;
					break;
				}
				case 6:{
					row = 0;
					collum = 3;
					break;
				}
				}
				
				gT.repaint();
				round = true;
				/************* Falling functions *******************************/
				while(round){
					if(falling){
						try {
							Thread.sleep(500);
							
							/********** Check if the shape hit the bottom**************************/
							
							if(current == 0){ // left Z shape
								if(shape%2 == 0){
									if((row == highest[collum])||(row == highest[collum-1])||(row-1 == highest[collum+1])){
										stored[row][collum] = 1;
										stored[row][collum-1] = 1;
										stored[row-1][collum] = 1;
										stored[row-1][collum+1] = 1;
										highest[collum] = row - 2;
										highest[collum-1] = row - 1;
										highest[collum+1] = row - 2;
										break;
									}
								}
								else{
									if((row == highest[collum]-1)||(row == highest[collum-1])){
										stored[row][collum] = 1;
										stored[row+1][collum] = 1;
										stored[row][collum-1] = 1;
										stored[row-1][collum-1] = 1;
										highest[collum] = row - 1;
										highest[collum-1] = row - 2;
										break;
									}
								}
							}
							else if (current == 1){// Right Z shape
								if(shape%2 == 0){
									if((row == highest[collum])||(row == highest[collum+1])||(row == highest[collum-1]+1)){
										stored[row][collum] = 1;
										stored[row][collum+1] = 1;
										stored[row-1][collum] = 1;
										stored[row-1][collum-1] = 1;
										highest[collum] = row - 2;
										highest[collum-1] = row - 2;
										highest[collum+1] = row - 1;
										break;
									}
								}
								else{
									if((row == highest[collum])||(row == highest[collum-1]-1)){
										stored[row][collum] = 1;
										stored[row-1][collum] = 1;
										stored[row][collum-1] = 1;
										stored[row+1][collum-1] = 1;
										highest[collum] = row - 2;
										highest[collum-1] = row - 1;
										break;
									}
								}
							}
							else if (current == 2){// Left L shape
								if(shape%4 == 0){
									if((row == highest[collum]) || (row == highest[collum-1]) || (row == highest[collum+1])){
										stored[row][collum]=1;
										stored[row][collum+1]=1;
										stored[row][collum-1]=1;
										stored[row-1][collum-1]=1;
										highest[collum] = row-1;
										highest[collum-1] = row -2;
										highest[collum+1] = row -1;
										break;
									}
								}
								else if(shape%4 ==1){
									if((row == highest[collum]-1) || (row == highest[collum+1]+1)){
										stored[row][collum]=1;
										stored[row+1][collum]=1;
										stored[row-1][collum]=1;
										stored[row-1][collum+1]=1;
										highest[collum] = row-2;
										highest[collum+1] = row -2;
										break;
									}
								}
								else if(shape%4 ==2){
									if((row == highest[collum]) || (row == highest[collum-1]) || (row == highest[collum+1]-1)){
										stored[row][collum]=1;
										stored[row][collum+1]=1;
										stored[row][collum-1]=1;
										stored[row+1][collum+1]=1;
										highest[collum] = row-1;
										highest[collum-1] = row -1;
										highest[collum+1] = row -1;
										break;
									}
								}
								else if (shape%4 ==3){
									if((row == highest[collum]-1) || (row == highest[collum-1]-1)){
										stored[row][collum]=1;
										stored[row+1][collum]=1;
										stored[row-1][collum]=1;
										stored[row+1][collum-1]=1;
										highest[collum] = row-2;
										highest[collum-1] = row;
										break;
									}
								}
							}
							else if (current == 3){// Right L shape
								if(shape%4 == 0){
									if((row == highest[collum]) || (row == highest[collum-1]) || (row == highest[collum+1])){
										stored[row][collum]=1;
										stored[row][collum+1]=1;
										stored[row][collum-1]=1;
										stored[row-1][collum+1]=1;
										highest[collum] = row-1;
										highest[collum+1] = row -2;
										highest[collum-1] = row -1;
										break;
									}
								}
								else if(shape%4 ==1){
									if((row == highest[collum]-1) || (row == highest[collum+1]-1)){
										stored[row][collum]=1;
										stored[row+1][collum]=1;
										stored[row-1][collum]=1;
										stored[row+1][collum+1]=1;
										highest[collum] = row-2;
										highest[collum+1] = row;
										break;
									}
								}
								else if(shape%4 ==2){
									if((row == highest[collum]) || (row == highest[collum-1]-1) || (row == highest[collum+1])){
										stored[row][collum]=1;
										stored[row][collum+1]=1;
										stored[row][collum-1]=1;
										stored[row+1][collum-1]=1;
										highest[collum] = row-1;
										highest[collum-1] = row -1;
										highest[collum+1] = row -1;
										break;
									}
								}
								else if (shape%4 ==3){
									if((row == highest[collum]-1) || (row == highest[collum-1]+1)){
										stored[row][collum]=1;
										stored[row+1][collum]=1;
										stored[row-1][collum]=1;
										stored[row-1][collum-1]=1;
										highest[collum] = row-2;
										highest[collum-1] = row -2;
										break;
									}
								}
							}
							else if (current == 4){// cube
								if((row == highest[collum]) || (row == highest[collum+1])){
									stored[row][collum] = 1;
									stored[row][collum+1] = 1;
									stored[row-1][collum] = 1;
									stored[row-1][collum+1] = 1;
									highest[collum] = row -2;
									highest[collum+1] = row -2;
									break;
								}
							}
							else if (current == 5){// Hill
								if(shape%4 == 0){
									if((row == highest[collum]) || (row == highest[collum-1]) || (row == highest[collum+1])){
										stored[row][collum]=1;
										stored[row][collum+1]=1;
										stored[row][collum-1]=1;
										stored[row-1][collum]=1;
										highest[collum] = row-2;
										highest[collum+1] = row -1;
										highest[collum-1] = row -1;
										break;
									}
								}
								else if(shape%4 ==1){
									if((row == highest[collum]-1) || (row == highest[collum+1])){
										stored[row][collum] = 1;
										stored[row][collum+1] = 1;
										stored[row-1][collum] = 1;
										stored[row+1][collum] = 1;
										highest[collum] = row -2;
										highest[collum+1] = row -1;
										break;
									}
								}
								else if(shape%4 ==2){
									if((row == highest[collum]-1) || (row == highest[collum-1]) || (row == highest[collum+1])){
										stored[row][collum]=1;
										stored[row][collum+1]=1;
										stored[row][collum-1]=1;
										stored[row+1][collum]=1;
										highest[collum] = row-1;
										highest[collum+1] = row -1;
										highest[collum-1] = row -1;
										break;
									}
								}
								else if (shape%4 ==3){
									if((row == highest[collum]-1) || (row == highest[collum-1])){
										stored[row][collum] = 1;
										stored[row][collum-1] = 1;
										stored[row-1][collum] = 1;
										stored[row+1][collum] = 1;
										highest[collum] = row -2;
										highest[collum-1] = row -1;
										break;
									}
								}
							}
							else if (current == 6){// Line
								if(shape%2 == 0){
									if((row == highest[collum]) || (row == highest[collum-1]) || (row == highest[collum+1]) || (row == highest[collum+2])){
										stored[row][collum] = 1;
										stored[row][collum-1] = 1;
										stored[row][collum+1] = 1;
										stored[row][collum+2] = 1;
										highest[collum] = row -1;
										highest[collum-1] = row -1;
										highest[collum+1] = row -1;
										highest[collum+2] = row -1;
										break;
									}
								}
								else{
									if((row == highest[collum]-2)){
										stored[row][collum] = 1;
										stored[row-1][collum] = 1;
										stored[row+1][collum] = 1;
										stored[row+2][collum] = 1;
										highest[collum] = row -2;
										break;
									}
								}
							}
							
							
							row++;
							gT.repaint();
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} 
			}
		}
	}
	
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
	
	
	gameTetris(){		
		
		this.setLayout(null);
		
		//label 1 show the level of game
		level = new Label("Level: ");
		level.addMouseListener(this);
		level_data = new Label("1");
		level_data.addMouseListener(this);
		
		//label 2 shows the lines of game
		line = new Label("Lines: ");
		line.addMouseListener(this);
		line_data = new Label("0");
		line_data.addMouseListener(this);
		
		//label 3 shows the score of game
		score = new Label("Score: ");
		score.addMouseListener(this);
		score_data = new Label("0");
		score_data.addMouseListener(this);
		
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
		this.addMouseListener(this);
		this.addMouseWheelListener(this);
		
	}

	
	public void paint(Graphics g){
		super.paint(g);
		initial();
		
		//create the big white area
		PX=iX(-rWidth/2); PY=iY(rHeight/2 - 5); //get the position left-top corner of the big area
		dE.fillArea(g, Es, PX, PY, 10, 20, stored);
		
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
		g.setColor(Color.white);
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
		
		
/**************************************************************************************************************
* start to draw the elements
* **********************************************************************************************************/
		int ePX, ePY;
		
		ePX = iX(rWidth/5 + (width- 4*sqSize)/2);
		ePY = iY((rHeight/2 - 10)- (height-sqSize)/2);
		

		switch(next){
		case 0:{
			dE.drawLeftZShape(g, Es, ePX, ePY,0);
			break;
		}
		case 1:{
			dE.drawRightZShape(g, Es, ePX, ePY,0);
			break;
		}
		case 2:{
			dE.drawLeftLShape(g, Es, ePX, ePY,0);
			break;
		}
		case 3:{
			dE.drawRightLShape(g, Es, ePX, ePY,0);
			break;
		}
		case 4:{
			dE.drawCube(g, Es, ePX, ePY,0);
			break;
		}
		case 5:{
			dE.drawHillShape(g, Es, ePX, ePY,0);
			break;
		}
		case 6:{
			dE.drawLine(g, Es, ePX, ePY,0);
			break;
		}
		
		}
		
		
		switch(current){
		case 0:{
			dE.drawLeftZShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		case 1:{
			dE.drawRightZShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		case 2:{
			dE.drawLeftLShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		case 3:{
			dE.drawRightLShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		case 4:{
			dE.drawCube(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		case 5:{
			dE.drawHillShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		case 6:{
			dE.drawLine(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],shape);
			break;
		}
		
		}
	}
	
/************************************************************************************************************************
* These are the movement
* *********************************************************************************************************************/
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == e.BUTTON1){
			collum--;
			if(dE.getLeft() == sqPositionX[0][0]){
				collum++;
			}
			repaint();
		}
		
		else if(e.getButton() == e.BUTTON3){
			collum++;
			if(dE.getRight() == sqPositionX[0][9]){
				collum--;
			}
			repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == quit){
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
					dE.drawString(g, Es, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
					falling = false;
				}
			}
			else{
				if(!inside){
					inside = false;
					repaint();
					falling = true;
				}
			}
		}
		else{
			if(!inside){
				inside = false;
				repaint();
				falling = true;
			}
		}
		
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		if(e.getWheelRotation() == 1){
			if(falling){
				shape--;
				switch(current){
				case 0:{
					if(shape%2 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 1:{
					if(shape%2 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 2:{
					if(shape%4 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 3:{
					if(shape%4 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 4:{
					repaint();
					break;
				}
				case 5:{
					if(shape%4 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 6:{
					if(shape%2 == 0){
						if(row <= 17){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}		
				}
			}
		}
		else if(e.getWheelRotation() == -1){
			if(falling){
				shape++;
				switch(current){
				case 0:{
					if(shape%2 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 1:{
					if(shape%2 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 2:{
					if(shape%4 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 3:{
					if(shape%4 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 4:{
					repaint();
					break;
				}
				case 5:{
					if(shape%4 == 0){
						if(row <= 18){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				case 6:{
					if(shape%2 == 0){
						if(row <= 17){
							repaint();
						}
					}
					else{
						repaint();
					}
					break;
				}
				}
			}
		}
	}

	
/*******************************************************************************************************************************
* Here are some functions that will be used by mainArea
* *******************************************************************************************************************/
	
	void setShapes(int big, int small){
		this.current = big;
		this.next = small;
	}




}



/*********************************************
 * This is the Class for elements drawing including the line, cube, LShape and ZShape
 * ***************************************/
class drawElements{
/* size is the size of one cube element
 * Px and Py must be the coordinate of turning point of any shape!!!!
 * This means that any shape is drawn from bottom, which could result in beyond the top of main area;
 * not the bottom of the main area!!!
*/
	int leftMost, rightMost, bottom;
	
	void drawLine(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = Math.abs(index)%2;
		switch(shape){
		case 0: {
			leftMost = Px; rightMost = Px + 3*size; bottom = Py;
			g.setColor(Color.black);
			for(int i = 0; i<3; i++){
				g.drawRect(Px+i*size, Py, size, size);
				if(i == 1){
					g.drawRect(Px-size, Py, size, size);
				}
			}
			
			g.setColor(Color.red);
			for(int i = 0; i<3; i++){
				g.fillRect(Px+i*size+1, Py+1, size-1, size-1);
				if(i == 1){
					g.fillRect(Px-size+1, Py+1, size-1, size-1);
				}
			}
			break;
		}
		case 1:{
			leftMost = Px; rightMost = Px; bottom = Py+3*size;
			g.setColor(Color.black);
			for(int i = 0; i<3; i++){
				g.drawRect(Px, Py-i*size, size, size);
				if(i == 1){
					g.drawRect(Px, Py+size, size, size);
				}
			}
			
			g.setColor(Color.red);
			for(int i = 0; i<3; i++){
				g.fillRect(Px+1, Py-i*size+1, size-1, size-1);
				if(i == 1){
					g.fillRect(Px+1, Py+size+1, size-1, size-1);
				}
			}
			break;
		}
		
		}
			
	}
	
	void drawCube(Graphics g, int size, int Px, int Py, int index){
		leftMost = Px; rightMost = Px+size; bottom = Py+size;
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
	
	void drawRightLShape(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = Math.abs(index)%4;
		switch(shape){
		case 0:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px+size, Py-size, size, size);
			
			g.setColor(Color.blue);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py-size+1, size-1, size-1);
			break;
		}
		case 1:{
			leftMost = Px; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px+size, Py+size, size, size);
			
			g.setColor(Color.blue);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+size+1, size-1, size-1);
			break;
		}
		case 2:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px-size, Py+size, size, size);
			
			g.setColor(Color.blue);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+size+1, size-1, size-1);
			break;
		}
		case 3:{
			leftMost = Px-size; rightMost = Px; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px-size, Py-size, size, size);
			
			g.setColor(Color.blue);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px-size+1, Py-size+1, size-1, size-1);
			break;
		}
		
		}
		
	}
	
	void drawLeftLShape(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = Math.abs(index)%4;
		switch(shape){
		case 0:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px-size, Py-size, size, size);
			
			g.setColor(Color.gray);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py-size+1, size-1, size-1);
			break;
		}
		case 1:{
			leftMost = Px; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px+size, Py-size, size, size);
			
			g.setColor(Color.gray);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+size+1, Py-size+1, size-1, size-1);
			break;
		}
		case 2:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px+size, Py+size, size, size);
			
			g.setColor(Color.gray);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+size+1, size-1, size-1);
			break;
		}
		case 3:{
			leftMost = Px-size; rightMost = Px; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px-size, Py+size, size, size);
			
			g.setColor(Color.gray);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+size+1, size-1, size-1);
			break;
		}
		
		}
		
	}
	
	void drawLeftZShape(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = Math.abs(index)%2;
		switch(shape){
		case 0:{
			leftMost = Px-size; rightMost = Px +size; bottom = Py;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px+size, Py-size, size, size);
			
			g.setColor(Color.yellow);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+size+1, Py-size+1, size-1, size-1);
			break;
		}
		case 1:{
			leftMost = Px-size; rightMost = Px; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px-size, Py-size, size, size);
			
			g.setColor(Color.yellow);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py-size+1, size-1, size-1);
			break;
		}
		
		}
	}
	
	void drawRightZShape(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = Math.abs(index)%2;
		switch(shape){
		case 0:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px-size, Py-size, size, size);
			
			g.setColor(Color.orange);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px-size+1, Py-size+1, size-1, size-1);
			break;
		}
		case 1:{
			leftMost = Px-size; rightMost = Px; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px-size, Py+size, size, size);
			
			g.setColor(Color.orange);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+size+1, size-1, size-1);
			break;
		}
		
		}
		
	}
	
	void drawHillShape(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = Math.abs(index)%4;
		switch(shape){
		case 0:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px +size, Py, size, size);
			g.drawRect(Px -size, Py, size, size);
			g.drawRect(Px, Py - size, size, size);
			
			g.setColor(Color.pink);
			g.fillRect(Px +1, Py+1, size-1, size-1);
			g.fillRect(Px + size +1, Py+1, size-1, size-1);
			g.fillRect(Px -size +1, Py+1, size-1, size-1);
			g.fillRect(Px +1, Py- size +1, size-1, size-1);
			break;
		}
		case 1:{
			leftMost = Px; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px+size, Py, size, size);
			
			g.setColor(Color.pink);
			g.fillRect(Px +1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			break;
		}
		case 2:{
			leftMost = Px-size; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px +size, Py, size, size);
			g.drawRect(Px -size, Py, size, size);
			g.drawRect(Px, Py+size, size, size);
			
			g.setColor(Color.pink);
			g.fillRect(Px +1, Py+1, size-1, size-1);
			g.fillRect(Px + size +1, Py+1, size-1, size-1);
			g.fillRect(Px -size +1, Py+1, size-1, size-1);
			g.fillRect(Px +1, Py+size +1, size-1, size-1);
			break;
		}
		case 3:{
			leftMost = Px-size; rightMost = Px; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px, Py+size, size, size);
			g.drawRect(Px-size, Py, size, size);
			
			g.setColor(Color.pink);
			g.fillRect(Px +1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+1, Py+size+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			break;
		}
		
		}
		
	}
	
	
	
	void fillArea(Graphics g, int size, int Px, int Py, int length, int height, int black[][]){
		g.setColor(Color.white);
		for(int i =0; i< height; i++){
			for(int j = 0; j< length; j++){
				if(black[i][j] == 1){
					g.setColor(Color.cyan);
					g.fillRect(Px+j*size, Py+i*size, size+1, size+1);
					g.setColor(Color.black);
					g.drawRect(Px+j*size, Py+i*size, size+1, size+1);
				}
				else{
					g.setColor(Color.white);
					g.fillRect(Px+j*size, Py+i*size, size+1, size+1);
				}
			}
		}	
	}
	
	void drawString(Graphics g, int size, int Px, int Py, String s){
		g.setColor(Color.black);
		g.drawRect(Px, Py, size*7, size*2);
		g.setFont(new Font("Times New Romen", Font.BOLD, size*2));
		g.drawString(s, Px, Py+size*2);
	}
	
	void clearSring(Graphics g, int Px, int Py, String s){
		g.setColor(Color.white);
		g.drawRect(Px, Py, 215, 65);
		g.setFont(new Font("Times New Romen", Font.BOLD, 60));
		g.drawString(s, Px+10, Py+55);
		
	}
	
	int getLeft(){
		return leftMost;
	}
	int getRight(){
		return rightMost;
	}
	int getBottom(){
		return bottom;
	}
} 
