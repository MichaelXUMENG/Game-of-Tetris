import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class gameTetris extends Frame implements MouseListener{

	int current, next;
	boolean start = true;
	boolean round = false;
	mainArea mA;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new gameTetris();
	}
	
	gameTetris(){
		super("Game of Tetris");
		mA = new mainArea();
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
		
		
		add(mA);
		this.setVisible(true);
		this.addMouseListener(this);

		next = new Random().nextInt(6);
		current = next;
		
		while(true){
			while(start){
				current = next;
				next = new Random().nextInt(6);
				mA.setShapes(current, next);
				round = true;
				while(round){
					try {
						mA.fallDown();
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == e.BUTTON1){
			mA.leftClick();
		}
		
		else if(e.getButton() == e.BUTTON3){
			mA.rightClick();
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


}

/************
 * To show the mainArea with the painting area and labels and buttons together,
 * All the elements must be put in the same layer
 * ****************************************/
class mainArea extends Panel implements MouseMotionListener, ActionListener, MouseListener{

	
	//declare the button and labels
	public Button quit; 
	public Label level, line, score, level_data, line_data, score_data;
	
	//declare the variables used to calculate the coordinates
	int centerX, centerY; // the central pixel's position
	float pixelSize, width, height, //width and height is the logical width and height of my elements
	rWidth = 100.0F, rHeight = 130.0F,     //used to calculate the pixel size
			sqSize; //logical size of a square
	
	int PX, PY, SX, SY, Es; //the actual position of pixel: PX,PY are position; SX,SY are actual size, and Es is the actual size of a square.
	int[][] sqPositionX = new int[20][10];
	int[][] sqPositionY = new int[20][10];

	drawElements dE = new drawElements( );
	
	int current, next;
	boolean start = true;
	boolean round = true;
	int row = 0, collum = 3;



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
//		this.addMouseListener(this);
		
		

	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		initial();
		g.setColor(Color.white);
		
		//create the big white area
		PX=iX(-rWidth/2); PY=iY(rHeight/2 - 5); //get the position left-top corner of the big area
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
		ePY = iY((rHeight/2 - 10)- (height-sqSize)/2);
		
/*		g.setColor(Color.black);
		for(int i = 0; i<4; i++){
			g.drawRect(ePX+i*Es, ePY, Es, Es);
		}
		g.setColor(Color.green);
		for(int i = 0; i<4; i++){
			g.fillRect(ePX+i*Es+1, ePY+1, Es-1, Es-1);
		}
		*/
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
			dE.drawLeftZShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		case 1:{
			dE.drawRightZShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		case 2:{
			dE.drawLeftLShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		case 3:{
			dE.drawRightLShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		case 4:{
			dE.drawCube(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		case 5:{
			dE.drawHillShape(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		case 6:{
			dE.drawLine(g, Es, sqPositionX[row][collum], sqPositionY[row][collum],0);
			break;
		}
		
		}
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
					repaint();
				}
			}
		}
		else{
			if(!inside){
				inside = false;
				repaint();
			}
		}
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == e.BUTTON1){
			if(dE.getLeft()>sqPositionX[0][0]){
				collum--;
				repaint();
			}
		}
		
		else if(e.getButton() == e.BUTTON3){
			if(dE.getRight() < sqPositionX[0][9]){
				collum++;
				repaint();
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
	
	
	/**************************************************
	 * Here are some functions that will be used by mainArea
	 * *******************************************/
	void leftClick(){
		if(dE.getLeft()>sqPositionX[0][0]){
			collum--;
			repaint();
		}
	}
	
	void rightClick(){
		if(dE.getRight() < sqPositionX[0][9]){
			collum++;
			repaint();
		}
	}
	
	void fallDown(){
		repaint();
		row--;
	}
	
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
		shape = index%2;
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
		shape = index%4;
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
		shape = index%4;
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
		shape = index%2;
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
			leftMost = Px; rightMost = Px + size; bottom = Py+size;
			g.setColor(Color.black);
			g.drawRect(Px, Py, size, size);
			g.drawRect(Px, Py-size, size, size);
			g.drawRect(Px+size, Py, size, size);
			g.drawRect(Px+size, Py+size, size, size);
			
			g.setColor(Color.yellow);
			g.fillRect(Px+1, Py+1, size-1, size-1);
			g.fillRect(Px+1, Py-size+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+1, size-1, size-1);
			g.fillRect(Px+size+1, Py+size+1, size-1, size-1);
			break;
		}
		
		}
	}
	
	void drawRightZShape(Graphics g, int size, int Px, int Py, int index){
		int shape;
		shape = index%2;
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
			leftMost = Px; rightMost = Px + size; bottom = Py+size;
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
		shape = index%4;
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
