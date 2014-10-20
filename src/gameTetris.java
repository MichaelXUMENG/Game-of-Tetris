import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.math.*;


public class gameTetris extends Panel implements MouseMotionListener, ActionListener,MouseListener, MouseWheelListener{

	static int current, next;  // these two variables are used for store the shape of objects
	static boolean start = true;  //decide whether the game is starting
	static boolean round = false; //decide whether one object in terminated
	static boolean falling = true;// decide whether the game is pause or not
	
	//declare the button and labels
	static public Button quit, startgame; 
	static public Label level, line, score, level_data, line_data, score_data;
	
	static Frame outFrame = new Frame(); // a Frame to contain the mainarea
		
	//declare the variables used to calculate the coordinates
	static int centerX, centerY; // the central pixel's position
	static float pixelSize, width, height, //width and height is the logical width and height of my elements
	rWidth = 100.0F, rHeight = 130.0F,     //used to calculate the pixel size
			sqSize; //logical size of a square
		
	static int PX, PY, SX, SY, Es; //the actual position of pixel: PX,PY are position; SX,SY are actual size, and Es is the actual size of a square.
	static int[][] sqPositionX = new int[20][10]; // store all the device x-coordinates of all the cubes
	static int[][] sqPositionY = new int[20][10]; // store all the device y-coordinates of all the cubes
	static int[][] stored = new int[20][10]; // store the index of cubes which have been occupied

	static drawElements dE = new drawElements( );
		
	static int row, column; // variables of index of row and column for the starting cube of each object
	static int shape; //used for deciding what shape of each object
	static int fallingSpeed = 500;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		gameTetris gT = new gameTetris();
		
		// set up the frame size
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
		
		// add the functional part
		outFrame.add(gT);
		
		outFrame.setResizable(true);
		outFrame.setVisible(true);
		
		
		//initialize all the cubes to be unused
		for (int i = 0; i<20; i++){
			for(int j = 0; j<10; j++){
				stored[i][j] = 0;
			}
		}
		
		//Randomly choose the next object and the current one
		next = new Random().nextInt(6);
		
		/************* Function of falling ***********************/
		while(true){
			while(start){ // Start to fall
				current = next;
				next = new Random().nextInt(7);
				shape = 0;
				/*********** Customerize the start point of each shape *************************/
				switch(current){
				case 0:{//left Z shape
					row = 1;
					column = 4;
					break;
				}
				case 1:{//right Z shape
					row = 1;
					column = 5;
					break;
				}
				case 2:{//left L shape
					row = 1;
					column = 4;
					break;
				}
				case 3:{//right L shape
					row = 1;
					column = 5;
					break;
				}
				case 4:{//cube
					row = 1;
					column = 4;
					break;
				}
				case 5:{//Hill
					row = 1;
					column = 4;
					break;
				}
				case 6:{//Line
					row = 0;
					column = 3;
					break;
				}
				}
				
				gT.repaint();
				round = true;
				/************* Falling functions *******************************/
				while(round){
					if(falling){
						try {
							Thread.sleep(fallingSpeed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						/********** Check if the shape hit the bottom **************************/
						
						if(falling){
							if(current == 0){ // left Z shape
								if(Math.abs(shape)%2 == 0){
									if((row == 19)||(stored[row+1][column] == 1)||(stored[row+1][column-1] == 1)||(stored[row][column+1] == 1)){
										stored[row][column] = 1;
										stored[row][column-1] = 1;
										stored[row-1][column] = 1;
										stored[row-1][column+1] = 1;
										break;
									}
								}
								else{
									if((row == 18)||(stored[row+2][column] == 1)||(stored[row+1][column-1] == 1)){
										stored[row][column] = 1;
										stored[row+1][column] = 1;
										stored[row][column-1] = 1;
										stored[row-1][column-1] = 1;
										break;
									}
								}
							}
							else if (current == 1){// Right Z shape
								if(Math.abs(shape)%2 == 0){
									if((row == 19)||(stored[row+1][column] == 1)||(stored[row+1][column+1] == 1)||(stored[row][column-1] == 1)){
										stored[row][column] = 1;
										stored[row][column+1] = 1;
										stored[row-1][column] = 1;
										stored[row-1][column-1] = 1;
										break;
									}
								}
								else{
									if((row == 18)||(stored[row+1][column] == 1)||(stored[row+2][column-1] == 1)){
										stored[row][column] = 1;
										stored[row-1][column] = 1;
										stored[row][column-1] = 1;
										stored[row+1][column-1] = 1;
										break;
									}
								}
							}
							else if (current == 2){// Left L shape
								if(Math.abs(shape)%4 == 0){
									if((row == 19)||(stored[row+1][column] == 1) || (stored[row+1][column-1] == 1) || (stored[row+1][column+1] ==1)){
										stored[row][column]=1;
										stored[row][column+1]=1;
										stored[row][column-1]=1;
										stored[row-1][column-1]=1;
										break;
									}
								}
								else if(Math.abs(shape)%4 ==1){
									if((row == 18)||(stored[row+2][column] == 1) || (stored[row][column+1] == 1)){
										stored[row][column]=1;
										stored[row+1][column]=1;
										stored[row-1][column]=1;
										stored[row-1][column+1]=1;
										break;
									}
								}
								else if(Math.abs(shape)%4 ==2){
									if((row == 18)||(stored[row+1][column] == 1) || (stored[row+1][column-1] == 1) || (stored[row+2][column+1] == 1)){
										stored[row][column]=1;
										stored[row][column+1]=1;
										stored[row][column-1]=1;
										stored[row+1][column+1]=1;
										break;
									}
								}
								else if (Math.abs(shape)%4 ==3){
									if((row == 18)||(stored[row+2][column] == 1) || (stored[row+2][column-1] == 1)){
										stored[row][column]=1;
										stored[row+1][column]=1;
										stored[row-1][column]=1;
										stored[row+1][column-1]=1;
										break;
									}
								}
							}
							else if (current == 3){// Right L shape
								if(Math.abs(shape)%4 == 0){
									if((row == 19)||(stored[row+1][column] == 1) || (stored[row+1][column-1] == 1) || (stored[row+1][column+1] == 1)){
										stored[row][column]=1;
										stored[row][column+1]=1;
										stored[row][column-1]=1;
										stored[row-1][column+1]=1;
										break;
									}
								}
								else if(Math.abs(shape)%4 ==1){
									if((row == 18)||(stored[row+2][column] == 1) || (stored[row+2][column+1] == 1)){
										stored[row][column]=1;
										stored[row+1][column]=1;
										stored[row-1][column]=1;
										stored[row+1][column+1]=1;
										break;
									}
								}
								else if(Math.abs(shape)%4 ==2){
									if((row == 18)||(stored[row+1][column] == 1) || (stored[row+2][column-1] == 1) || (stored[row+1][column+1] == 1)){
										stored[row][column]=1;
										stored[row][column+1]=1;
										stored[row][column-1]=1;
										stored[row+1][column-1]=1;
										break;
									}
								}
								else if (Math.abs(shape)%4 ==3){
									if((row == 18)||(stored[row+2][column] == 1) || (stored[row][column-1] ==1)){
										stored[row][column]=1;
										stored[row+1][column]=1;
										stored[row-1][column]=1;
										stored[row-1][column-1]=1;
										break;
									}
								}
							}
							else if (current == 4){// cube
								if((row == 19)||(stored[row+1][column] == 1) || (stored[row+1][column+1] == 1)){
									stored[row][column] = 1;
									stored[row][column+1] = 1;
									stored[row-1][column] = 1;
									stored[row-1][column+1] = 1;
									break;
								}
							}
							else if (current == 5){// Hill
								if(Math.abs(shape)%4 == 0){
									if((row == 19)||(stored[row+1][column] == 1) || (stored[row+1][column-1] == 1) || (stored[row+1][column+1] == 1)){
										stored[row][column]=1;
										stored[row][column+1]=1;
										stored[row][column-1]=1;
										stored[row-1][column]=1;
										break;
									}
								}
								else if(Math.abs(shape)%4 ==1){
									if((stored[row+2][column] == 1) || (stored[row+1][column+1] == 1)||(row == 18)){
										stored[row][column] = 1;
										stored[row][column+1] = 1;
										stored[row-1][column] = 1;
										stored[row+1][column] = 1;
										break;
									}
								}
								else if(Math.abs(shape)%4 ==2){
									if((row == 18)||(stored[row+2][column] == 1) || (stored[row+1][column-1] == 1) || (stored[row+1][column+1] == 1)){
										stored[row][column]=1;
										stored[row][column+1]=1;
										stored[row][column-1]=1;
										stored[row+1][column]=1;
										break;
									}
								}
								else if (Math.abs(shape)%4 ==3){
									if((row == 18)||(stored[row+2][column] == -1) || (stored[row+1][column-1] == 1)){
										stored[row][column] = 1;
										stored[row][column-1] = 1;
										stored[row-1][column] = 1;
										stored[row+1][column] = 1;
										break;
									}
								}
							}
							else if (current == 6){// Line
								if(Math.abs(shape)%2 == 0){
									if((row == 19)||(stored[row+1][column] == 1) || (stored[row+1][column-1] == 1) || (stored[row+1][column+1] == 1) || (stored[row+1][column+2] == 1)){
										stored[row][column] = 1;
										stored[row][column-1] = 1;
										stored[row][column+1] = 1;
										stored[row][column+2] = 1;
										break;
									}
								}
								else{
									if((row == 17)||(stored[row+3][column] == 1)){
										stored[row][column] = 1;
										stored[row-1][column] = 1;
										stored[row+1][column] = 1;
										stored[row+2][column] = 1;
										break;
									}
								}
							}
							
							
							row++;
							gT.repaint();
						}
						
					}
				} 
			}
		}
	}
	
	/********* initialize the useful variables *****************/
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
	
	//functions used to transfer the logical coordinates to device coordinates
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
		//start button
		startgame = new Button("START");
		startgame.addActionListener(this);
		
		this.add(level);
		this.add(level_data);
		this.add(line);
		this.add(line_data);
		this.add(score);
		this.add(score_data);
		this.add(startgame);
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
		
		quit.setBounds(PX,iY(rHeight/2 - 5*height - 10),SX*2 , SY/2);
		startgame.setBounds(PX,iY(rHeight/2 - 4*height - 10),SX*2 , SY/2);
		
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
		

		if(start){
			switch(next){
			case 0:{//left Z shape
				dE.drawLeftZShape(g, Es, ePX, ePY,0);
				break;
			}
			case 1:{//right Z shape
				dE.drawRightZShape(g, Es, ePX, ePY,0);
				break;
			}
			case 2:{//left L shape
				dE.drawLeftLShape(g, Es, ePX, ePY,0);
				break;
			}
			case 3:{//right L shape
				dE.drawRightLShape(g, Es, ePX, ePY,0);
				break;
			}
			case 4:{//cube
				dE.drawCube(g, Es, ePX, ePY,0);
				break;
			}
			case 5:{//Hill
				dE.drawHillShape(g, Es, ePX, ePY,0);
				break;
			}
			case 6:{//Line
				dE.drawLine(g, Es, ePX, ePY,0);
				break;
			}
			
			}
			
			
			switch(current){
			case 0:{//left Z shape
				dE.drawLeftZShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			case 1:{//Right Z shape
				dE.drawRightZShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			case 2:{//left L shape
				dE.drawLeftLShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			case 3:{//right L shape
				dE.drawRightLShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			case 4:{//cube
				dE.drawCube(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			case 5:{//Hill
				dE.drawHillShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			case 6:{//Line
				dE.drawLine(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				break;
			}
			
			}
		}
		
	}
	
/************************************************************************************************************************
* These are the movement
* *********************************************************************************************************************/
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
/****************************************************************************************************
 * Left Click
 * ****************************************************************************************************/
		if(e.getButton() == e.BUTTON1){
			if(falling){
				switch(current){
				case 0:{// Left Z shape
					if(Math.abs(shape)%2 == 0){
						if(column>1){
							if((stored[row][column-2] == 0) & (stored[row-1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else{
						if(column > 1){
							if((stored[row-1][column-2] == 0) & (stored[row][column-2] == 0) & (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					break;
				}
				case 1:{// Right Z shape
					if(Math.abs(shape)%2 == 0){
						if(column > 1){
							if((stored[row-1][column-2] == 0) & (stored[row][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else{
						if(column > 0){
							if((stored[row-1][column-1] == 0) & (stored[row][column-2] == 0) & (stored[row+1][column-2] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					break;
				}
				case 2:{//Left L shape
					if(Math.abs(shape)%4 == 0){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row-1][column-2] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)& (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 2){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row+1][column] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 3){
						if(column > 1){
							if((stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)& (stored[row+1][column-2] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					break;
				}
				case 3:{//Right L shape
					if(Math.abs(shape)%4 == 0){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row-1][column] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)& (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 2){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row+1][column-2] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 3){
						if(column > 1){
							if((stored[row][column-1] == 0) & (stored[row-1][column-2] == 0)& (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					break;
				}
				case 4:{//cube
					if(column > 0){
						if((stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)){
							column--;
							repaint();
						}
					}
					
					break;
				}
				case 5:{//Hill
					if(Math.abs(shape)%4 == 0){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row-1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)& (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 2){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 3){
						if(column > 1){
							if((stored[row][column-2] == 0) & (stored[row-1][column-1] == 0)& (stored[row+1][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					break;
				}
				case 6:{//Line
					if(Math.abs(shape%2) == 0){
						if(row > 1){
							if((stored[row][column-2] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					else{
						if(column > 0){
							if((stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)& (stored[row+1][column-1] == 0)& (stored[row+2][column-1] == 0)){
								column--;
								repaint();
							}
						}
						
					}
					break;
				}
				}
			}
			
		}
		
/****************************************************************************************************
 * Right click
 * ********************************************************************************************************/
		
		else if(e.getButton() == e.BUTTON3){
			if(falling){
				switch(current){
				case 0:{// Left Z shape
					if(Math.abs(shape)%2 == 0){
						if(column < 8){
							if((stored[row][column+1] == 0) & (stored[row-1][column+2] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else{
						if(column < 9){
							if((stored[row-1][column] == 0) & (stored[row][column+1] == 0) & (stored[row+1][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					break;
				}
				case 1:{// Right Z shape
					if(Math.abs(shape)%2 == 0){
						if(column < 8){
							if((stored[row-1][column+1] == 0) & (stored[row][column+2] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else{
						if(column < 9){
							if((stored[row-1][column+1] == 0) & (stored[row][column+1] == 0) & (stored[row+1][column] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					break;
				}
				case 2:{//Left L shape
					if(Math.abs(shape)%4 == 0){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row-1][column] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 1){
						if((stored[row][column+1] == 0) & (stored[row-1][column+2] == 0)& (stored[row+1][column+1] == 0)&(column < 8)){
							column++;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row+1][column+2] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 3){
						if(column < 9){
							if((stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)& (stored[row+1][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					break;
				}
				case 3:{//Right L shape
					if(Math.abs(shape)%4 == 0){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row-1][column+2] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 1){
						if(column < 8){
							if((stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)& (stored[row+1][column+2] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 2){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row+1][column] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 3){
						if(column < 9){
							if((stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)& (stored[row+1][column+1] == 0)){
								column++;
								repaint();
							}	
						}
						
					}
					break;
				}
				case 4:{//cube
					if(column < 8){
						if((stored[row][column+2] == 0) & (stored[row-1][column+2] == 0)){
							column++;
							repaint();
						}
					}
					
					break;
				}
				case 5:{//Hill
					if(Math.abs(shape)%4 == 0){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row-1][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 1){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row-1][column+1] == 0)& (stored[row+1][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 2){
						if(column < 8){
							if((stored[row][column+2] == 0) & (stored[row+1][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else if(Math.abs(shape)%4 == 3){
						if(column < 9){
							if((stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)& (stored[row+1][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					break;
				}
				case 6:{//Line
					if(Math.abs(shape%2) == 0){
						if(row < 7){
							if((stored[row][column+3] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					else{
						if(column < 9){
							if((stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)& (stored[row+1][column+1] == 0)& (stored[row+2][column+1] == 0)){
								column++;
								repaint();
							}
						}
						
					}
					break;
				}
				
				}
				
			}
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
		else if(e.getSource() == startgame){
			start = true;
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
	
/****************************************************************************************************
 * Wheel Move
 * ****************************************************************************************************/
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
		/********************************
		 * rotate counter clockwise
		 * *************************************/
		if(e.getWheelRotation() == 1){
			if(falling){
				switch(current){
				
				/************* Left Z shape*******************************/
				case 0:{
					if(Math.abs(shape)%2 == 0){
						if(row<19){
							if((stored[row+1][column]==0) &(stored[row-1][column-1]==0)){
								shape++;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if((stored[row-1][column]==0) &(stored[row-1][column+1]==0)){
								shape++;
								repaint();
							}
						}
					}
					break;
				}
				
				/************* Right Z shape******************************/
				case 1:{
					if(Math.abs(shape)%2 == 0){
						if(row < 19){
							if((stored[row][column-1]==0) &(stored[row+1][column-1]==0)){
								shape++;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if((stored[row][column+1]==0) &(stored[row-1][column-1]==0)){
								shape++;
								repaint();
							}
						}
					}
					break;
				}
				
				/*************** Left L shape**************************/
				case 2:{
					if(Math.abs(shape)%4 == 0){
						if(row<19){
							if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row-1][column+1] == 0)){
								shape++;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column+1] == 0) & (stored[row][column-1] == 0) & (stored[row-1][column+1] == 0)){
								shape++;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row+1][column-1] == 0)){
							shape++;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 3){
						if(column<9){
							if((stored[row][column+1] == 0) & (stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)){
								shape++;
								repaint();
							}
						}
					}
					break;
				}
				
				/************* Right L shape**************************/
				case 3:{
					if(Math.abs(shape)%4 == 0){
						if(row<19){
							if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row+1][column+1] == 0)){
								shape++;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column+1] == 0) & (stored[row][column-1] == 0) & (stored[row+1][column-1] == 0)){
								shape++;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if((stored[row+1][column] == 0) & (stored[row-1][column] == 0) & (stored[row-1][column-1] == 0)){
							shape++;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 3){
						if(column<9){
							if((stored[row][column-1] == 0) & (stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)){
								shape++;
								repaint();
							}
						}
					}
					break;
				}
				
				/*********** Cube **********************/
				case 4:{
					break;
				}
				
				/**************** Hill shape *****************************/
				case 5:{
					if(Math.abs(shape)%4 == 0){
						if(row < 19){
							if(stored[row+1][column] == 0){
								shape++;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 1){
						if(column>0){
							if(stored[row][column-1] == 0){
								shape++;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if(stored[row-1][column] == 0){
							shape++;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 3){
						if(column<9){
							if(stored[row][column+1] == 0){
								shape++;
								repaint();
							}
						}
					}
					break;
				}
				
				/*************** Line *****************************/
				case 6:{
					if(Math.abs(shape)%2 == 0){
						if(row < 18){
							if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row+2][column] == 0)){
								shape++;
								repaint();
							}
						}
					}
					else{
						if((column>0) & (column<8)){
							if((stored[row][column-1] == 0) & (stored[row][column+1] == 0) & (stored[row][column+2] == 0)){
								shape++;
								repaint();
							}
						}
					}
					break;
				}		
				}
			}
		}
		
		/***********************************************
		 * rotate clockwise
		 * *****************/
		else if(e.getWheelRotation() == -1){
			if(falling){
				switch(current){
				
				/************* Left Z shape*******************************/
				case 0:{
					if(Math.abs(shape)%2 == 0){
						if(row<19){
							if((stored[row+1][column]==0) &(stored[row-1][column-1]==0)){
								shape--;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if((stored[row-1][column]==0) &(stored[row-1][column+1]==0)){
								shape--;
								repaint();
							}
						}
					}
					break;
				}
				
				/************* Right Z shape******************************/
				case 1:{
					if(Math.abs(shape)%2 == 0){
						if(row < 19){
							if((stored[row][column-1]==0) &(stored[row+1][column-1]==0)){
								shape--;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if((stored[row][column+1]==0) &(stored[row-1][column-1]==0)){
								shape--;
								repaint();
							}
						}
					}
					break;
				}
				
				/*************** Left L shape**************************/
				case 2:{
					if(Math.abs(shape)%4 == 0){
						if(row<19){
							if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row+1][column-1] == 0)){
								shape--;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column+1] == 0) & (stored[row][column-1] == 0) & (stored[row-1][column-1] == 0)){
								shape--;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if((stored[row-1][column+1] == 0) & (stored[row-1][column] == 0) & (stored[row+1][column] == 0)){
							shape--;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 3){
						if(column<9){
							if((stored[row][column-1] == 0) & (stored[row][column+1] == 0) & (stored[row+1][column+1] == 0)){
								shape--;
								repaint();
							}
						}
					}
					break;
				}
				
				/************* Right L shape**************************/
				case 3:{
					if(Math.abs(shape)%4 == 0){
						if(row<19){
							if((stored[row-1][column-1] == 0) & (stored[row-1][column] == 0) & (stored[row+1][column] == 0)){
								shape--;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 1){
						if(column > 0){
							if((stored[row][column-1] == 0) & (stored[row][column+1] == 0) & (stored[row-1][column+1] == 0)){
								shape--;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row+1][column+1] == 0)){
							shape--;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 3){
						if(column<9){
							if((stored[row][column-1] == 0) & (stored[row][column+1] == 0) & (stored[row+1][column-1] == 0)){
								shape--;
								repaint();
							}
						}
					}
					break;
				}
				
				/*********** Cube **********************/
				case 4:{
					break;
				}
				
				/**************** Hill shape *****************************/
				case 5:{
					if(Math.abs(shape)%4 == 0){
						if(row < 19){
							if(stored[row+1][column] == 0){
								shape--;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 1){
						if(column>0){
							if(stored[row][column-1] == 0){
								shape--;
								repaint();
							}
						}
					}
					else if(Math.abs(shape)%4 == 2){
						if(stored[row-1][column] == 0){
							shape--;
							repaint();
						}
					}
					else if(Math.abs(shape)%4 == 3){
						if(column<9){
							if(stored[row][column+1] == 0){
								shape--;
								repaint();
							}
						}
					}
					break;
				}
				
				/*************** Line *****************************/
				case 6:{
					if(Math.abs(shape)%2 == 0){
						if(row < 18){
							if((stored[row-1][column] == 0) & (stored[row+1][column] == 0) & (stored[row+2][column] == 0)){
								shape--;
								repaint();
							}
						}
					}
					else{
						if((column>0) & (column<8)){
							if((stored[row][column-1] == 0) & (stored[row][column+1] == 0) & (stored[row][column+2] == 0)){
								shape--;
								repaint();
							}
						}
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
				g.setColor(Color.white);
				g.fillRect(Px+j*size, Py+i*size, size+1, size+1);
				
			}
		}
		
		for(int i =0; i< height; i++){
			for(int j = 0; j< length; j++){
				if(black[i][j] == 1){
					g.setColor(Color.cyan);
					g.fillRect(Px+j*size, Py+i*size, size, size);
					g.setColor(Color.black);
					g.drawRect(Px+j*size, Py+i*size, size, size);
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
