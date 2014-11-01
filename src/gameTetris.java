import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.*;
import java.math.*;

import javax.swing.JOptionPane;


public class gameTetris extends Panel implements MouseMotionListener, ActionListener,MouseListener, MouseWheelListener, AdjustmentListener{

	static int current, next;  // these two variables are used for store the shape of objects
	static boolean start = true;  //decide whether the game is starting
	static boolean round = false; //decide whether one object in terminated
	static boolean falling = true;// decide whether the game is pause or not
	static boolean active = true;
	static boolean set = true;
	
	//used to determine the mouseMove
	boolean change = false;
	boolean inside = false;
	
	
	//declare the button and labels
	static public Button quit, setting; 
	static int buttonCounter =0;
	static public Label level, line, score, level_data, line_data, score_data;
	static public Label speed, speed_data;
	static public Scrollbar speedBar, scoringBar, rowBar, realSpeed;
	static public Label xNum, yNum;
	static public Scrollbar xModify, yModify;
	static public int xaxis = 10,
			yaxis = 20;
	
	static int lineShowing=0,  //This is the variable of the number of total lines you have reached
			scoreShowing=0,  // Total score you have got
			levelShowing=1,  // The level you are in
			scoringFactor=2,  //  variable for you to calculate your score: scoreShowing += levelShowing*scoringFactor*lineNum;
			rowRequire=10,  // variable of line to level up!!
			levelRow = 0,  // how many rows you have got in your current level
			bonus = 3;  // the variable to give the bonus to more than one lines filled at one time: scoreShowing += levelShowing*scoringFactor*lineNum+(lineNum-1)*bonus
	
	static float speedFactor = (float) 0.2;
	
	static Frame outFrame = new Frame(); // a Frame to contain the mainarea
		
	//declare the variables used to calculate the coordinates
	static int centerX, centerY; // the central pixel's position
	static float pixelSize, width, height, //width and height is the logical width and height of my elements
	rWidth = 100.0F, rHeight = 130.0F,     //used to calculate the pixel size
			sqSize; //logical size of a square
		
	static int PX, PY, SX, SY, Es; //the actual position of pixel: PX,PY are position; SX,SY are actual size, and Es is the actual size of a square.
	static int[][] sqPositionX = new int[20][10]; // store all the device x-coordinates of all the cubes
	static int[][] sqPositionY = new int[20][10]; // store all the device y-coordinates of all the cubes
	static boolean[][] stored = new boolean[20][10]; // store the index of cubes which have been occupied

	static drawElements dE = new drawElements( );
		
	static int row, column; // variables of index of row and column for the starting cube of each object
	static int upperBound, lowerBound, leftBound, rightBound;
	static int shape; //used for deciding what shape of each object
	static float fallingFactor = 1;
	static float fallingSpeed = 1000/fallingFactor;
	
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
				stored[i][j] = false;
			}
		}
		
		//Randomly choose the next object and the current one
		next = new Random().nextInt(6);
		
		/************* Function of falling ***********************/
		while(true){
			while(start && active){ // Start to fall
				current = next;
				next = new Random().nextInt(7);
				shape = 0;
				/*********** Customerize the start point of each shape *************************/
				switch(current){
				case 0:{//left Z shape
					row = 0;
					column = 4;
					if(stored[row][column]||stored[row][column-1]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case 1:{//right Z shape
					row = 0;
					column = 5;
					
					if(stored[row][column]||stored[row][column+1]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case 2:{//left L shape
					row = 0;
					column = 4;
					
					if(stored[row][column]||stored[row][column+1]||stored[row][column-1]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case 3:{//right L shape
					row = 0;
					column = 5;
					
					if(stored[row][column]||stored[row][column-1]||stored[row][column+1]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case 4:{//cube
					row = 0;
					column = 4;
					
					if(stored[row][column]||stored[row][column+1]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case 5:{//Hill
					row = 0;
					column = 4;
					
					if(stored[row][column]||stored[row][column+1]||stored[row][column-1]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				case 6:{//Line
					row = 0;
					column = 4;
					
					if(stored[row][column]||stored[row][column-1]||stored[row][column+1]||stored[row][column+2]){
						active = false;
						JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
					}
					break;
				}
				}
				
				if(active){
					gT.repaint();
					
				}
				round = true;
				/************* Falling functions *******************************/
				while(round && active){
					if(falling && set && active){
						try {
							Thread.sleep((long) fallingSpeed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						/********** Check if the shape hit the bottom and stop!! **************************/
						
						if(falling && set){
							if(current == 0){ // left Z shape
								if(shape == 0){
									if((row == 19)||stored[row+1][column]||stored[row+1][column-1]||stored[row][column+1]){
										if(row == 0){
											active = false;
											JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
											break;
										}
										stored[row][column] = true;
										stored[row][column-1] = true;
										stored[row-1][column] = true;
										stored[row-1][column+1] = true;
										
										lineDis(row, row-1);
										break;
									}
								}
								else{
									if((row == 18)||stored[row+2][column]||stored[row+1][column-1]){
										stored[row][column] = true;
										stored[row+1][column] = true;
										stored[row][column-1] = true;
										stored[row-1][column-1] = true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
							}
							else if (current == 1){// Right Z shape
								if(shape == 0){
									if((row == 19)||stored[row+1][column]||stored[row+1][column+1]||stored[row][column-1]){
										if(row == 0){
											active = false;
											JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
											break;
										}
										stored[row][column] = true;
										stored[row][column+1] = true;
										stored[row-1][column] = true;
										stored[row-1][column-1] = true;
										
										lineDis(row, row-1);
										break;
									}
								}
								else{
									if((row == 18)||stored[row+1][column]|| stored[row+2][column-1]){
										stored[row][column] = true;
										stored[row-1][column] = true;
										stored[row][column-1] = true;
										stored[row+1][column-1] = true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
							}
							else if (current == 2){// Left L shape
								if(shape == 0){
									if((row == 19)||stored[row+1][column] || stored[row+1][column-1] || stored[row+1][column+1]){
										if(row == 0){
											active = false;
											JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
											break;
										}
										stored[row][column]=true;
										stored[row][column+1]=true;
										stored[row][column-1]=true;
										stored[row-1][column-1]=true;
										
										lineDis(row, row-1);
										break;
									}
								}
								else if(shape ==1){
									if((row == 18)||stored[row+2][column]  || stored[row][column+1]){
										stored[row][column]=true;
										stored[row+1][column]=true;
										stored[row-1][column]=true;
										stored[row-1][column+1]=true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
								else if(shape ==2){
									if((row == 18)||stored[row+1][column]  || stored[row+1][column-1]  || stored[row+2][column+1] ){
										stored[row][column]=true;
										stored[row][column+1]=true;
										stored[row][column-1]=true;
										stored[row+1][column+1]=true;
										
										lineDis(row+1, row);
										break;
									}
								}
								else if (shape ==3){
									if((row == 18)||stored[row+2][column] ||stored[row+2][column-1]){
										stored[row][column]=true;
										stored[row+1][column]=true;
										stored[row-1][column]=true;
										stored[row+1][column-1]=true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
							}
							else if (current == 3){// Right L shape
								if(shape == 0){
									if((row == 19)||stored[row+1][column]  || stored[row+1][column-1]  || stored[row+1][column+1] ){
										if(row == 0){
											active = false;
											JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
											break;
										}
										stored[row][column]=true;
										stored[row][column+1]=true;
										stored[row][column-1]=true;
										stored[row-1][column+1]=true;
										
										lineDis(row, row-1);
										break;
									}
								}
								else if(shape ==1){
									if((row == 18)||stored[row+2][column] || stored[row+2][column+1] ){
										stored[row][column]=true;
										stored[row+1][column]=true;
										stored[row-1][column]=true;
										stored[row+1][column+1]=true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
								else if(shape ==2){
									if((row == 18)||stored[row+1][column] || stored[row+2][column-1] || stored[row+1][column+1]){
										stored[row][column]=true;
										stored[row][column+1]=true;
										stored[row][column-1]=true;
										stored[row+1][column-1]=true;
										
										lineDis(row+1, row);
										break;
									}
								}
								else if (shape ==3){
									if((row == 18)||stored[row+2][column] || stored[row][column-1]){
										stored[row][column]=true;
										stored[row+1][column]=true;
										stored[row-1][column]=true;
										stored[row-1][column-1]=true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
							}
							else if (current == 4){// cube
								if((row == 19)||stored[row+1][column] || stored[row+1][column+1]){
									if(row == 0){
										active = false;
										JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
										break;
									}
									stored[row][column] = true;
									stored[row][column+1] = true;
									stored[row-1][column] = true;
									stored[row-1][column+1] = true;
									
									lineDis(row, row-1);
									break;
								}
							}
							else if (current == 5){// Hill
								if(shape == 0){
									if((row == 19)||stored[row+1][column] || stored[row+1][column-1] || stored[row+1][column+1]){
										if(row == 0){
											active = false;
											JOptionPane.showMessageDialog(null, "GAME OVER!!!", "Game Over!!!", JOptionPane.ERROR_MESSAGE);
											break;
										}
										stored[row][column]=true;
										stored[row][column+1]=true;
										stored[row][column-1]=true;
										stored[row-1][column]=true;
										
										lineDis(row, row-1);
										break;
									}
								}
								else if(shape ==1){
									if((row == 18) || stored[row+2][column] || stored[row+1][column+1]){
										stored[row][column] = true;
										stored[row][column+1] = true;
										stored[row-1][column] = true;
										stored[row+1][column] = true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
								else if(shape ==2){
									if((row == 18)|| stored[row+2][column] || stored[row+1][column-1] || stored[row+1][column+1]){
										stored[row][column]=true;
										stored[row][column+1]=true;
										stored[row][column-1]=true;
										stored[row+1][column]=true;
										
										lineDis(row+1, row);
										break;
									}
								}
								else if (shape ==3){
									if((row == 18)||stored[row+2][column] || stored[row+1][column-1]){
										stored[row][column] = true;
										stored[row][column-1] = true;
										stored[row-1][column] = true;
										stored[row+1][column] = true;
										
										lineDis(row+1, row-1);
										break;
									}
								}
							}
							else if (current == 6){// Line
								if(shape == 0){
									if((row == 19)||stored[row+1][column] || stored[row+1][column-1] || stored[row+1][column+1] || stored[row+1][column+2]){
										stored[row][column] = true;
										stored[row][column-1] = true;
										stored[row][column+1] = true;
										stored[row][column+2] = true;
										
										lineDis(row, row);
										break;
									}
								}
								else{
									if((row == 17)||stored[row+3][column]){
										stored[row][column] = true;
										stored[row-1][column] = true;
										stored[row+1][column] = true;
										stored[row+2][column] = true;
										
										lineDis(row+2, row-1);
										break;
									}
								}
							}
							
							row++;
							gT.repaint();
							
						}
						
					}
				} 
				
				
				
				/*************************************************************************************
				 * Start to check if there are rows full
				 * ************************************************************************************/
				
				
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
		level_data = new Label(Integer.toString(levelShowing));
		level_data.addMouseListener(this);
		rowBar = new Scrollbar(Scrollbar.HORIZONTAL, rowRequire, 1, 10, 51);
		rowBar.setVisible(false);
		rowBar.addAdjustmentListener(this);
		
		//label 2 shows the lines of game
		line = new Label("Lines: ");
		line.addMouseListener(this);
		line_data = new Label(Integer.toString(lineShowing));
		line_data.addMouseListener(this);
		
		//Label and scroll bars about the speed
		speed = new Label("Speed: ");
		speed.setVisible(false);
		speed_data = new Label(String.valueOf(fallingFactor));
		speed_data.setVisible(false);
		realSpeed = new Scrollbar(Scrollbar.HORIZONTAL, (int)fallingFactor*10, 1, 1, 101);
		realSpeed.setVisible(false);
		speedBar = new Scrollbar(Scrollbar.HORIZONTAL, (int)(speedFactor*10), 1, 1, 11);
		speedBar.setVisible(false);
		realSpeed.addAdjustmentListener(this);
		speedBar.addAdjustmentListener(this);
		
		//label 3 shows the score of game
		score = new Label("Score: ");
		score.addMouseListener(this);
		score_data = new Label(Integer.toString(scoreShowing));
		score_data.addMouseListener(this);
		scoringBar = new Scrollbar(Scrollbar.HORIZONTAL, scoringFactor, 1, 1, 11);
		scoringBar.setVisible(false);
		scoringBar.addAdjustmentListener(this);
		
		xNum = new Label("x: "+String.valueOf(xaxis));
		yNum = new Label("y: "+String.valueOf(yaxis));
		xModify = new Scrollbar(Scrollbar.HORIZONTAL, xaxis, 1, 5, 31);
		yModify = new Scrollbar(Scrollbar.HORIZONTAL, yaxis, 1, 5, 61);
		xNum.setVisible(false);
		yNum.setVisible(false);
		xModify.setVisible(false);
		yModify.setVisible(false);
		xModify.addAdjustmentListener(this);
		yModify.addAdjustmentListener(this);
		
		//quit button
		quit = new Button("QUIT");
		quit.addActionListener(this);
		//start button
		setting = new Button("SETTING");
		setting.addActionListener(this);
		
		this.add(level);
		this.add(level_data);
		this.add(rowBar);
		this.add(line);
		this.add(line_data);
		this.add(speedBar);
		this.add(score);
		this.add(score_data);
		this.add(scoringBar);
		this.add(setting);
		this.add(quit);
		this.add(speed);
		this.add(speed_data);
		this.add(realSpeed);
		this.add(xNum);
		this.add(yNum);
		this.add(xModify);
		this.add(yModify);
		
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
		dE.setBound(sqPositionY[0][0]);
		
		//create the small area
		width = rWidth*2/5; height = width/2;
		PX=iX(rWidth/5); PY=iY(rHeight/2 - 5); 
		SX= Math.round(width / pixelSize); SY=Math.round(height/pixelSize);
		g.setColor(Color.white);
		g.fillRect(PX, PY, SX , SY);
		
		
		
		//set the position of labels and button
		SY=Math.round(height/pixelSize)/2;
		
		xNum.setBounds(PX, iY(rHeight/2 - (height*3) - 10),SX/6 , SY);//label of x
		yNum.setBounds(PX + SX*5/8, iY(rHeight/2 - (height*3) - 10),SX/6 , SY); // label of y
		xModify.setBounds(PX + SX*11/48, iY(rHeight/2 - (height*3) - 10),SX/3 , SY);  //Scroll bar of x
		yModify.setBounds(PX + SX*41/48, iY(rHeight/2 - (height*3) - 10),SX/3 , SY);  // scroll bar of y
		
		
		SX = Math.round((width*2/5) / pixelSize);
		level.setBounds(PX,iY(rHeight/2 - height - 10),SX , SY);
		line.setBounds(PX,iY(rHeight/2 - height*3/2 - 10),SX , SY);
		speed.setBounds(PX,iY(rHeight/2 - (height*2) - 10),SX , SY);
		score.setBounds(PX,iY(rHeight/2 - (height*12/5) - 10),SX , SY);
		
		
		SY=Math.round(height/pixelSize);
		quit.setBounds(PX,iY(rHeight/2 - 5*height - 10),SX*2 , SY/2);
		setting.setBounds(PX,iY(rHeight/2 - 4*height - 10),SX*2 , SY/2);
		
		PX = iX(rWidth/5 + width*3/7);
		SX = Math.round((width/8) / pixelSize);
		SY=Math.round(height/pixelSize)/2;
		level_data.setBounds(PX,iY(rHeight/2 - height - 10),SX , SY);
		line_data.setBounds(PX,iY(rHeight/2 - height*3/2 - 10),SX , SY);
		speed_data.setBounds(PX,iY(rHeight/2 - (height*2) - 10),SX , SY);
		score_data.setBounds(PX, iY(rHeight/2 - (height*12/5) - 10), SX , SY);
		
		SX = Math.round((width/2) / pixelSize);
		PX = iX(rWidth/5 + width*3/5);
		rowBar.setBounds(PX,iY(rHeight/2 - height - 10),SX , SY);
		speedBar.setBounds(PX,iY(rHeight/2 - height*3/2 - 10),SX , SY);
		realSpeed.setBounds(PX,iY(rHeight/2 - (height*2) - 10),SX , SY);
		scoringBar.setBounds(PX, iY(rHeight/2 - (height*12/5) - 10), SX , SY);
		
		
		
		
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
				getEdgeValues();
				break;
			}
			case 1:{//Right Z shape
				dE.drawRightZShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				getEdgeValues();
				break;
			}
			case 2:{//left L shape
				dE.drawLeftLShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				getEdgeValues();
				break;
			}
			case 3:{//right L shape
				dE.drawRightLShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				getEdgeValues();
				break;
			}
			case 4:{//cube
				dE.drawCube(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				getEdgeValues();
				break;
			}
			case 5:{//Hill
				dE.drawHillShape(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				getEdgeValues();
				break;
			}
			case 6:{//Line
				dE.drawLine(g, Es, sqPositionX[row][column], sqPositionY[row][column],shape);
				getEdgeValues();
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
			if(falling && set){
				switch(current){
				case 0:{// Left Z shape
					if(shape == 0){
						if(column>1){
							if(!(stored[row][column-2] ||stored[row-1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else{
						if(column > 1){
							if(!(stored[row-1][column-2] ||stored[row][column-2] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 1:{// Right Z shape
					if(shape == 0){
						if(column > 1){
							if(!(stored[row-1][column-2] ||stored[row][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else{
						if(column > 1){
							if(!(stored[row-1][column-1] ||stored[row][column-2] ||stored[row+1][column-2] )){
								column--;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 2:{//Left L shape
					if(shape == 0){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row-1][column-2] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column-1] ||stored[row-1][column-1] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 2){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row+1][column] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 3){
						if(column > 1){
							if(!(stored[row][column-1] ||stored[row-1][column-1] ||stored[row+1][column-2] )){
								column--;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 3:{//Right L shape
					if(shape == 0){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row-1][column] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column-1] ||stored[row-1][column-1] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 2){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row+1][column-2] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 3){
						if(column > 1){
							if(!(stored[row][column-1] ||stored[row-1][column-2] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 4:{//cube
					if(column > 0){
						if(!(stored[row][column-1] ||stored[row-1][column-1] )){
							column--;
							repaint();
						}
					}
					
					break;
				}
				case 5:{//Hill
					if(shape == 0){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row-1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column-1] ||stored[row-1][column-1] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 2){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					else if(shape == 3){
						if(column > 1){
							if(!(stored[row][column-2] ||stored[row-1][column-1] ||stored[row+1][column-1] )){
								column--;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 6:{//Line
					if(Math.abs(shape) == 0){
						if(row > 1){
							if(!stored[row][column-2]){
								column--;
								repaint();
							}
						}
						
					}
					else{
						if(column > 0){
							if(!(stored[row][column-1] ||stored[row-1][column-1] ||stored[row+1][column-1] ||stored[row+2][column-1] )){
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
			if(falling && set){
				switch(current){
				case 0:{// Left Z shape
					if(shape == 0){
						if(column < 8){
							if(!(stored[row][column+1] ||stored[row-1][column+2] )){
								column++;
								repaint();
							}
						}
						
					}
					else{
						if(column < 9){
							if(!(stored[row-1][column] ||stored[row][column+1] ||stored[row+1][column+1] )){
								column++;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 1:{// Right Z shape
					if(shape == 0){
						if(column < 8){
							if(!(stored[row-1][column+1] ||stored[row][column+2] )){
								column++;
								repaint();
							}
						}
						
					}
					else{
						if(column < 9){
							if(!(stored[row-1][column+1] ||stored[row][column+1] ||stored[row+1][column] )){
								column++;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 2:{//Left L shape
					if(shape == 0){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row-1][column] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 1){
						if(column < 8){
							if(!(stored[row][column+1] ||stored[row-1][column+2] ||stored[row+1][column+1])){
								column++;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row+1][column+2] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 3){
						if(column < 9){
							if(!(stored[row][column+1] ||stored[row-1][column+1] ||stored[row+1][column+1] )){
								column++;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 3:{//Right L shape
					if(shape == 0){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row-1][column+2] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 1){
						if(column < 8){
							if(!(stored[row][column+1] ||stored[row-1][column+1] ||stored[row+1][column+2] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 2){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row+1][column] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 3){
						if(column < 9){
							if(!(stored[row][column+1] ||stored[row-1][column+1] ||stored[row+1][column+1] )){
								column++;
								repaint();
							}	
						}
						
					}
					
					break;
				}
				case 4:{//cube
					if(column < 8){
						if(!(stored[row][column+2] ||stored[row-1][column+2] )){
							column++;
							repaint();
						}
					}
					
					break;
				}
				case 5:{//Hill
					if(shape == 0){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row-1][column+1] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 1){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row-1][column+1] ||stored[row+1][column+1] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 2){
						if(column < 8){
							if(!(stored[row][column+2] ||stored[row+1][column+1] )){
								column++;
								repaint();
							}
						}
						
					}
					else if(shape == 3){
						if(column < 9){
							if(!(stored[row][column+1] ||stored[row-1][column+1] ||stored[row+1][column+1] )){
								column++;
								repaint();
							}
						}
						
					}
					
					break;
				}
				case 6:{//Line
					if(Math.abs(shape) == 0){
						if(column < 7){
							if(!(stored[row][column+3] )){
								column++;
								repaint();
							}
						}
						
					}
					else{
						if(column < 9){
							if(!(stored[row][column+1] ||stored[row-1][column+1] ||stored[row+1][column+1] ||stored[row+2][column+1] )){
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
		
		else if(e.getSource() == setting){
			if(buttonCounter%2 == 0){
				setting.setLabel("OK");
				set = false;
				
				level.setText("Row Required:");
				level_data.setText(String.valueOf(rowRequire));
				rowBar.setVisible(true);
				
				line.setText("Speed Factor:");
				line_data.setText(String.valueOf(speedFactor));
				speedBar.setVisible(true);
				
				score.setText("Scoring Factor:");
				score_data.setText(String.valueOf(scoringFactor));
				scoringBar.setVisible(true);
				
				speed.setVisible(true);
				speed_data.setVisible(true);
				realSpeed.setVisible(true);
				
				xNum.setVisible(true);
				yNum.setVisible(true);
				xModify.setVisible(true);
				yModify.setVisible(true);
				buttonCounter++;
			}
			else{
				setting.setLabel("SETTING");
				
				level.setText("Level");
				level_data.setText(String.valueOf(levelShowing));
				rowBar.setVisible(false);
				
				line.setText("Lines");
				line_data.setText(String.valueOf(lineShowing));
				speedBar.setVisible(false);
				
				score.setText("Score");
				score_data.setText(String.valueOf(scoreShowing));
				scoringBar.setVisible(false);
				
				speed.setVisible(false);
				speed_data.setVisible(false);
				realSpeed.setVisible(false);
				
				xNum.setVisible(false);
				yNum.setVisible(false);
				xModify.setVisible(false);
				yModify.setVisible(false);
				set = true;
				buttonCounter++;
			}
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
		Mx = e.getX();
		My = e.getY();
		
		
		Graphics g = getGraphics();
		if(Mx>=sqPositionX[0][0] && Mx<=sqPositionX[19][9]+Es){
			if(My>=sqPositionY[0][0] && My<=sqPositionY[19][9]+Es){
				if(!inside){
					inside = true;
					dE.drawString(g, Es, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
					falling = false;
				}
			}
			else{
				if(inside){
					inside = false;
					repaint();
					falling = true;
				}
			}
		}
		else{
			if(inside){
				inside = false;
				repaint();
				falling = true;
			}
		}
		
		
		if(Mx>=leftBound && Mx<=rightBound){
			if(My>=upperBound && My<=lowerBound){
				if(!change){
					change = true;
					current = next;
					shape =0;
					next = new Random().nextInt(7);
					repaint();
					scoreShowing -= levelShowing*scoringFactor;
					score_data.setText(String.valueOf(scoreShowing));
				}
				dE.drawString(g, Es, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");

			}
			else{
				if(change){
					change = false;
					dE.drawString(g, Es, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
				}
			}
		}
		else{
			if(change){
				change = false;
				dE.drawString(g, Es, sqPositionX[7][2], sqPositionY[7][2], "PAUSE");
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
			if(falling && set){
				switch(current){
				
				/************* Left Z shape*******************************/
				case 0:{
					if(shape == 0){
						if(row<19){
							if(!(stored[row+1][column]||stored[row-1][column-1])){
								shape = 1;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if(!(stored[row-1][column]||stored[row-1][column+1])){
								shape = 0;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/************* Right Z shape******************************/
				case 1:{
					if(shape == 0){
						if(row < 19){
							if(!(stored[row][column-1]||stored[row+1][column-1])){
								shape=1;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if(!(stored[row][column+1]||stored[row-1][column-1])){
								shape=0;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/*************** Left L shape**************************/
				case 2:{
					if(shape == 0){
						if(row<19){
							if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row-1][column+1] )){
								shape=1;
								repaint();
							}
						}
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column+1] ||stored[row][column-1] ||stored[row-1][column+1] )){
								shape=2;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row+1][column-1] )){
							shape=3;
							repaint();
						}
					}
					else if(shape == 3){
						if(column<9){
							if(!(stored[row][column+1] ||stored[row][column-1] ||stored[row-1][column-1] )){
								shape=0;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/************* Right L shape**************************/
				case 3:{
					if(shape == 0){
						if(row<19){
							if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row+1][column+1] )){
								shape=1;
								repaint();
							}
						}
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column+1] ||stored[row][column-1] ||stored[row+1][column-1] )){
								shape=2;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(!(stored[row+1][column] ||stored[row-1][column] ||stored[row-1][column-1] )){
							shape=3;
							repaint();
						}
					}
					else if(shape == 3){
						if(column<9){
							if(!(stored[row][column-1] ||stored[row][column+1] ||stored[row-1][column+1] )){
								shape=0;
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
					if(shape == 0){
						if(row < 19){
							if(!stored[row+1][column] ){
								shape=1;
								repaint();
							}
						}
					}
					else if(shape == 1){
						if(column>0){
							if(!stored[row][column-1] ){
								shape=2;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(!stored[row-1][column] ){
							shape=3;
							repaint();
						}
					}
					else if(shape == 3){
						if(column<9){
							if(!stored[row][column+1] ){
								shape=0;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/*************** Line *****************************/
				case 6:{
					if(shape == 0){
						if(row < 18){
							if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row+2][column] )){
								shape=1;
								repaint();
							}
						}
					}
					else{
						if((column>0) && (column<8)){
							if(!(stored[row][column-1] ||stored[row][column+1] ||stored[row][column+2] )){
								shape=0;
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
			if(falling && set){
				switch(current){
				
				/************* Left Z shape*******************************/
				case 0:{
					if(shape == 0){
						if(row<19){
							if(!(stored[row+1][column]||stored[row-1][column-1])){
								shape=1;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if(!(stored[row-1][column]||stored[row-1][column+1])){
								shape=0;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/************* Right Z shape******************************/
				case 1:{
					if(shape == 0){
						if(row < 19){
							if(!(stored[row][column-1]||stored[row+1][column-1])){
								shape=1;
								repaint();
							}
						}
					}
					else{
						if(column<9){
							if(!(stored[row][column+1]||stored[row-1][column-1])){
								shape=0;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/*************** Left L shape**************************/
				case 2:{
					if(shape == 0){
						if(row<19){
							if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row+1][column-1] )){
								shape=3;
								repaint();
							}
						}
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column+1] ||stored[row][column-1] ||stored[row-1][column-1] )){
								shape=0;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(!(stored[row-1][column+1] ||stored[row-1][column] ||stored[row+1][column] )){
							shape=1;
							repaint();
						}
					}
					else if(shape == 3){
						if(column<9){
							if(!(stored[row][column-1] ||stored[row][column+1] ||stored[row+1][column+1] )){
								shape=2;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/************* Right L shape**************************/
				case 3:{
					if(shape == 0){
						if(row<19){
							if(!(stored[row-1][column-1] ||stored[row-1][column] ||stored[row+1][column] )){
								shape=3;
								repaint();
							}
						}
					}
					else if(shape == 1){
						if(column > 0){
							if(!(stored[row][column-1] ||stored[row][column+1] ||stored[row-1][column+1] )){
								shape=0;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row+1][column+1] )){
							shape=1;
							repaint();
						}
					}
					else if(shape == 3){
						if(column<9){
							if(!(stored[row][column-1] ||stored[row][column+1] ||stored[row+1][column-1] )){
								shape=2;
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
					if(shape == 0){
						if(row < 19){
							if(!stored[row+1][column] ){
								shape=3;
								repaint();
							}
						}
					}
					else if(shape == 1){
						if(column>0){
							if(!stored[row][column-1] ){
								shape=0;
								repaint();
							}
						}
					}
					else if(shape == 2){
						if(!stored[row-1][column] ){
							shape=1;
							repaint();
						}
					}
					else if(shape == 3){
						if(column<9){
							if(!stored[row][column+1] ){
								shape=2;
								repaint();
							}
						}
					}
					
					break;
				}
				
				/*************** Line *****************************/
				case 6:{
					if(shape == 0){
						if(row < 18){
							if(!(stored[row-1][column] ||stored[row+1][column] ||stored[row+2][column] )){
								shape=1;
								repaint();
							}
						}
					}
					else{
						if((column>0) && (column<8)){
							if(!(stored[row][column-1] ||stored[row][column+1] ||stored[row][column+2] )){
								shape=0;
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

	public static void lineDis(int max, int min){
		int lineNum = 0;
		int[] disLine = new int[4]; // used to store the line# of a full line
		for(int i = 19; i> min-1; i--){
			if(stored[i][0] && stored[i][1] && stored[i][2] && stored[i][3] && stored[i][4] && stored[i][5] && stored[i][6] && stored[i][7] && stored[i][8] && stored[i][9]){
				disLine[lineNum] = i;
				lineNum++;
			}
		}
		
		for(int k = lineNum; k>=0; k--){
			for(int j = disLine[k]; j>0; j--){
				for(int t = 0; t<10; t++){
					stored[j][t] = stored[j-1][t];
				}
			}
			for(int t = 0; t<10; t++){
				stored[0][t] = false;
			}			
			
		}
		
		if(lineNum >0){
			lineShowing += lineNum;
			levelRow += lineNum;
			scoreShowing += levelShowing*scoringFactor*(lineNum+(lineNum-1)*bonus); // the more line you fill, the more score you will get one time
			line_data.setText(Integer.toString(lineShowing));
			score_data.setText(Integer.toString(scoreShowing));
			
			/*****************  level up!!! *************************************/
			if(levelRow > rowRequire){ 
				levelShowing++;
				level_data.setText(Integer.toString(levelShowing));
				levelRow -= rowRequire;
				
				fallingFactor = fallingFactor*(1+levelShowing*speedFactor);
				fallingSpeed = 1000/fallingFactor;
			}
		}
	}
	
	static void getEdgeValues(){
		upperBound = dE.getUpEdge();
		lowerBound = dE.getBottomEdge();
		leftBound = dE.getLeftEdge();
		rightBound = dE.getRightEdge();
	}


@Override
public void adjustmentValueChanged(AdjustmentEvent e) {
	// TODO Auto-generated method stub
	if(e.getSource() == rowBar){
		int val = rowBar.getValue();
		level_data.setText(String.valueOf(val));
		rowRequire = val;
	}
	
	else if(e.getSource() == speedBar){
		float val = (float)speedBar.getValue()/10;
		line_data.setText(String.valueOf(val));
		speedFactor = val;
		
	}
	
	else if(e.getSource() == scoringBar){
		int val = scoringBar.getValue();
		score_data.setText(String.valueOf(val));
		scoringFactor = val;
	}
	
	else if(e.getSource() == realSpeed){
		float val = (float)realSpeed.getValue()/10;
		speed_data.setText(String.valueOf((float)val));
		fallingFactor = val;
		fallingSpeed = 1000/fallingFactor;
	}
	
	else if(e.getSource() == xModify){
		int val = xModify.getValue();
		xNum.setText("x: "+String.valueOf(val));
		xaxis = val;
	}
	
	else if(e.getSource() == yModify){
		int val = yModify.getValue();
		yNum.setText("y: "+String.valueOf(val));
		yaxis = val;
	}
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
	
	int upBound, upEdge, bottomEdge, leftEdge, rightEdge;
	
	void drawLine(Graphics g, int size, int Px, int Py, int index){
		switch(index){
		case 0: {
			upEdge = Py;
			bottomEdge = Py+size;
			leftEdge = Px-size;
			rightEdge = Px+3*size;
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
			upEdge = Py-size;
			bottomEdge = Py+3*size;
			leftEdge = Px;
			rightEdge = Px+size;
			g.setColor(Color.black);
			for(int i = 0; i<3; i++){
				g.drawRect(Px, Py+i*size, size, size);
				if(i == 1){
					g.drawRect(Px, Py-size, size, size);
				}
			}
			
			g.setColor(Color.red);
			for(int i = 0; i<3; i++){
				g.fillRect(Px+1, Py+i*size+1, size-1, size-1);
				if(i == 1){
					g.fillRect(Px+1, Py-size+1, size-1, size-1);
				}
			}
			break;
		}
		
		}
			
	}
	
	void drawCube(Graphics g, int size, int Px, int Py, int index){
		upEdge = Py-size;
		bottomEdge = Py+size;
		leftEdge = Px;
		rightEdge = Px+2*size;
		g.setColor(Color.black);
		for(int i = 0; i<2; i++){
			for(int j = 0; j<2; j++)
				if((Py-i*size)>=upBound){
					g.drawRect(Px+j*size, Py-i*size, size, size);
				}
		}
		g.setColor(Color.green);
		for(int i = 0; i<2; i++){
			for(int j = 0; j<2; j++)
				if((Py-i*size)>=upBound){
					g.fillRect(Px+j*size+1, Py-i*size+1, size-1, size-1);
				}
		}
	}
	
	void drawRightLShape(Graphics g, int size, int Px, int Py, int index){
		switch(index){
		case 0:{
			upEdge = Py-size;
			bottomEdge = Py+size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			if((Py-size)>=upBound){
				g.drawRect(Px+size, Py-size, size, size);
			}
			
			g.setColor(Color.blue);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			if((Py-size)>=upBound){
				g.fillRect(Px+size+1, Py-size+1, size-1, size-1);
			}
			break;
		}
		case 1:{
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px;
			rightEdge = Px+2*size;
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
			upEdge = Py;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
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
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+size;
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
		switch(index){
		case 0:{
			upEdge = Py-size;
			bottomEdge = Py+size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			if((Py-size)>=upBound){
				g.drawRect(Px-size, Py-size, size, size);
			}
			
			g.setColor(Color.gray);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			if((Py-size)>=upBound){
				g.fillRect(Px-size+1, Py-size+1, size-1, size-1);
			}
			break;
		}
		case 1:{
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px;
			rightEdge = Px+2*size;
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
			upEdge = Py;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
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
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+size;
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
		switch(index){
		case 0:{
			upEdge = Py-size;
			bottomEdge = Py+size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px-size, Py, size, size);
			if((Py-size)>=upBound){
				g.drawRect(Px, Py-size, size, size);
				g.drawRect(Px+size, Py-size, size, size);
			}
			
			g.setColor(Color.yellow);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px-size+1, Py+1, size-1, size-1);
			if((Py-size)>=upBound){
				g.fillRect(Px+1, Py-size+1, size-1, size-1);
				g.fillRect(Px+size+1, Py-size+1, size-1, size-1);
			}
			break;
		}
		case 1:{
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+size;
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
		switch(index){
		case 0:{
			upEdge = Py-size;
			bottomEdge = Py+size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px+size, Py, size, size);
			if((Py-size)>=upBound){
				g.drawRect(Px, Py-size, size, size);
				g.drawRect(Px-size, Py-size, size, size);
			}
			
			g.setColor(Color.orange);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			if((Py-size)>=upBound){
				g.fillRect(Px+1, Py-size+1, size-1, size-1);
				g.fillRect(Px-size+1, Py-size+1, size-1, size-1);
			}
			break;
		}
		case 1:{
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+size;
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
		switch(index){
		case 0:{
			upEdge = Py-size;
			bottomEdge = Py+size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px +size, Py, size, size);
			g.drawRect(Px -size, Py, size, size);
			if((Py-size)>=upBound){
				g.drawRect(Px, Py - size, size, size);
			}
			
			g.setColor(Color.pink);
			g.fillRect(Px +1, Py+1, size-1, size-1);
			g.fillRect(Px + size +1, Py+1, size-1, size-1);
			g.fillRect(Px -size +1, Py+1, size-1, size-1);
			if((Py-size)>=upBound){
				g.fillRect(Px +1, Py- size +1, size-1, size-1);
			}
			break;
		}
		case 1:{
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px;
			rightEdge = Px+2*size;
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
			upEdge = Py;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+2*size;
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
			upEdge = Py-size;
			bottomEdge = Py+2*size;
			leftEdge = Px-size;
			rightEdge = Px+size;
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
	
	
	
	void fillArea(Graphics g, int size, int Px, int Py, int length, int height, boolean black[][]){
		g.setColor(Color.white);
		for(int i =0; i< height; i++){
			for(int j = 0; j< length; j++){
				g.setColor(Color.white);
				g.fillRect(Px+j*size, Py+i*size, size+1, size+1);
				
			}
		}
		
		for(int i =0; i< height; i++){
			for(int j = 0; j< length; j++){
				if(black[i][j]){
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
	
	
	void setBound(int bound){
		this.upBound = bound;
	}
	
	int getUpEdge(){
		return this.upEdge;
	}
	int getBottomEdge(){
		return this.bottomEdge;
	}
	int getLeftEdge(){
		return this.leftEdge;
	}
	int getRightEdge(){
		return this.rightEdge;
	}
} 
