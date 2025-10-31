import java.awt.Color;

import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class MyFrame extends JFrame implements KeyListener{
	
	private MainPanel mainPanel;
	private Player player;
	private MapCurrent mapCurrent;
	private MapInteractables mapInteractable;
	
	private MapItem mapItem;
	
	private ArrayList<Item> lanterns;
	private ArrayList<Item> plank;
	private ArrayList<Item> pickaxe;
	
	//Possibly make this an array
	ArrayList<BufferedImage> mapSprites;
	ArrayList<BufferedImage> itemSprites;
	
	MyFrame(){
		super("Game");
		lanterns = new ArrayList<Item>();
		lanterns.add(new Item(2, 23, 34));
		
		plank = new ArrayList<Item>();
		plank.add(new Item(1, 33, 14));
		plank.add(new Item(1, 26, 9));
		
		pickaxe = new ArrayList<Item>();
		pickaxe.add(new Item(3, 11, 6));
		pickaxe.add(new Item(3, 17, 24));
		
		player = new Player();
		
		//player has to be passed in as constructor needs player to put into a map. Possibly switch to entity list or use a mutator
		mainPanel = new MainPanel(player, plank, pickaxe, lanterns);
		
		
		//Get current map
		mapCurrent = mainPanel.getMapCurrent();
		mapItem = mainPanel.getItemMap();
		//Basic frame assembly
		this.addKeyListener(this);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		this.add(mainPanel);
		this.pack();
		this.setLayout(new GridBagLayout());
		this.getContentPane().setBackground(Color.black);
		
		this.setVisible(true);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
		//Title screen inputs ---> Game
		if (MainPanel.titleScreen) {
			switch(e.getKeyChar()) {
			case('u'):
				MainPanel.FPS = 30;
				break;
			case('i'):
				MainPanel.FPS = 45;
				break;
			case('o'):
				MainPanel.FPS = 60;
				break;
			
			case('f'):
				MainPanel.titleScreen = false;
				MainPanel.gameScreen = true;
				
				mainPanel.resetLevel();
				mainPanel.fullReset();
				mapCurrent = mainPanel.getMapCurrent();
				mapInteractable = mainPanel.getMapInteractable();
				break;
			}
			if (Character.getNumericValue(e.getKeyChar()) != 0 && Character.getNumericValue(e.getKeyChar()) <= 9) {
				MainPanel.difficulty = Character.getNumericValue(e.getKeyChar());
			}
			
			
			
		}
		//Win screen toggle ---> Game
		if (MainPanel.winScreen) {
			if (e.getKeyChar() == 'f') {
				MainPanel.winScreen = false;	
				player.setHealth(1);
				MainPanel.difficulty++;
				MainPanel.gameScreen = true;
				mainPanel.resetLevel();
				mapCurrent = mainPanel.getMapCurrent();
				mapInteractable = mainPanel.getMapInteractable();			
			}
		}
		//Death screen toggle ---> Title
		if (MainPanel.deathScreen) {
			if (e.getKeyChar() == 'f') {
				MainPanel.deathScreen = false;	
				player.setHealth(1);
				MainPanel.difficulty = 1;
				MainPanel.titleScreen = true;
			}
			
			
		}
		if (MainPanel.gameScreen) {
			//Simplifying the if statements to check if player is moving
			boolean notMoving = player.getDirection() == 0;
			
			//Increases speed to run, has to be a toggle as keyPressed is worse than assigning key binds
			if (e.isShiftDown() && !player.getRunning()) {
				player.setSpeed(player.getSpeed()+(player.getSpeedOG()*.5));
				player.setRunning(true);
			}
			else if (e.isShiftDown() && player.getRunning()) {
				player.setSpeed(player.getSpeed()-(player.getSpeedOG()*.5));
				player.setRunning(false);
			}
			switch(e.getKeyChar()) {
		

		//Key down stuff, sets the direction and next direction if moving
			case('w'):
				if (notMoving) {
					player.setDirection(1);
										
				}	
				player.setNextDirection(1);
				player.setHiding(false);
				break;
				
			case('s'):
				if (notMoving) {
					player.setDirection(2);	
				}
				player.setNextDirection(2);
				player.setHiding(false);
				break;
				
			case('d'):
				if (notMoving) {
					player.setDirection(3);	
				}
				player.setNextDirection(3);
				player.setHiding(false);
				break;
				
			case('a'):
				if (notMoving) {
					player.setDirection(4);
				}
				player.setNextDirection(4);
				player.setHiding(false);
				break;
			case('x'):
				if (mapInteractable.getValvesDone() >= mapInteractable.getValves() / 4) {
					player.setValveHint(1);
				}
				break;
				
			case('f'):
				if (mapInteractable.getMap()[player.getRow()][player.getCol()] == 5) {
					mapInteractable.getMap()[player.getRow()][player.getCol()] = 6;
					mapInteractable.setValvesDone(mapInteractable.getValvesDone() + 1);
					if (mapInteractable.getValvesDone() >= mapInteractable.getValves() / 4.0) {
						mapCurrent.increaseWaterLevel();
					}else if(mapInteractable.getValvesDone() == mapInteractable.getValves()) {
						for (int r = 0; r < mapInteractable.getMap().length; r++) {
							for (int c = 0; c < mapInteractable.getMap()[0].length; c++) {
								if (mapInteractable.getMap()[r][c] == 7) {
									mapInteractable.getMap()[r][c] = 8;
								}	
								else if (mapInteractable.getMap()[r][c] == 6) {
									mapInteractable.getMap()[r][c] = 9;
								}	
							}
						}
					}
					
				}else if (mapInteractable.getMap()[player.getRow()][player.getCol()] == 4) {
					player.setHiding(true);
				}else if(mapInteractable.getMap()[player.getRow()][player.getCol()] == 8) {
					MainPanel.winScreen = true;
				}
				
				break;
			
				
			//***** BENNY STUFF**********
				//Entirely checks when you press e if a item is nearby or needs to be used
			case('e'):
				
				for (int x = 0; x < lanterns.size(); x++) {
					if (lanterns.get(x).getCol() == player.getCol() && lanterns.get(x).getRow() == player.getRow() && player.getSightDistance() < 11)
					{
						player.setSightDistance(player.getSightDistance() + 1);
						mainPanel.getMaps().get(5).getMap()[lanterns.get(x).getRow()][lanterns.get(x).getCol()] = 0;
						lanterns.remove(x);
					}
				}
					
				for (int x = 0; x < plank.size(); x++) {
					//Will pick up a plank.get(x) if around you
					if (plank.get(x).getCol() == player.getCol() && plank.get(x).getRow() == player.getRow() && plank.get(x).inHotBar() == false && !plank.get(x).getUsed())  
					{
						plank.get(x).setHotBar(1);
						mainPanel.getMaps().get(5).getMap()[plank.get(x).getRow()][plank.get(x).getCol()] = 0;
					}
				}
				
				for (int x = 0; x < pickaxe.size(); x++) {
					//will pick up pickaxe.get(x) if near you
					if (pickaxe.get(x).getCol() == player.getCol() && pickaxe.get(x).getRow() == player.getRow() && pickaxe.get(x).inHotBar() == false && !pickaxe.get(x).getUsed())  
					{
						pickaxe.get(x).setHotBar(1);
						mainPanel.getMaps().get(5).getMap()[pickaxe.get(x).getRow()][pickaxe.get(x).getCol()] = 0;
					}
				}
				
				for (int x = 0; x < plank.size(); x++) {
					//Will use plank.get(x) if already selected
					if (plank.get(x).inHotBar() && plank.get(x).getSelectedHotBar() && plank.get(x).equals(plank.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()-1][player.getCol()] > 4 && mapCurrent.getMap()[player.getRow()-1][player.getCol()] < 11)
					{
						plank.get(x).useItem();
						mapCurrent.getMap()[player.getRow()-1][player.getCol()] = 14;
						
					}
					
					if (plank.get(x).inHotBar() && plank.get(x).getSelectedHotBar() && plank.get(x).equals(plank.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()+1][player.getCol()] > 4 && mapCurrent.getMap()[player.getRow()+1][player.getCol()] < 11)
					{
						plank.get(x).useItem();
						mapCurrent.getMap()[player.getRow()+1][player.getCol()] = 14;
						
					}
				
						
					if (plank.get(x).inHotBar() && plank.get(x).getSelectedHotBar() && plank.get(x).equals(plank.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()][player.getCol()-1] > 4 && mapCurrent.getMap()[player.getRow()][player.getCol()-1] < 11)
					{
						plank.get(x).useItem();
						mapCurrent.getMap()[player.getRow()][player.getCol()-1] = 14;
						
					}
		
					if (plank.get(x).inHotBar() && plank.get(x).getSelectedHotBar() && plank.get(x).equals(plank.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()][player.getCol()+1] > 4 && mapCurrent.getMap()[player.getRow()][player.getCol()+1] < 11)
					{
						plank.get(x).useItem();
						mapCurrent.getMap()[player.getRow()][player.getCol()+1] = 14;
						
					}
				}	
				
				for (int x = 0; x < pickaxe.size(); x++) {
					//Will use pickaxe.get(x) if selected
					if (pickaxe.get(x).inHotBar() && pickaxe.get(x).getSelectedHotBar() && pickaxe.get(x).equals(pickaxe.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()-1][player.getCol()] == 1)
					{
						pickaxe.get(x).useItem();
						mapCurrent.getMap()[player.getRow()-1][player.getCol()] = 0;
						
					}
					
					if (pickaxe.get(x).inHotBar() && pickaxe.get(x).getSelectedHotBar() && pickaxe.get(x).equals(pickaxe.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()+1][player.getCol()] == 1)
					{
						pickaxe.get(x).useItem();
						mapCurrent.getMap()[player.getRow()+1][player.getCol()] = 0;
						
					}
		
					if (pickaxe.get(x).inHotBar() && pickaxe.get(x).getSelectedHotBar() && pickaxe.get(x).equals(pickaxe.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()][player.getCol()-1] == 1)
					{
						pickaxe.get(x).useItem();
						mapCurrent.getMap()[player.getRow()][player.getCol()-1] = 0;
						
					}
		
					if (pickaxe.get(x).inHotBar() && pickaxe.get(x).getSelectedHotBar() && pickaxe.get(x).equals(pickaxe.get(x).getHotBarItem()) && mapCurrent.getMap()[player.getRow()][player.getCol()+1] == 1)
					{
						pickaxe.get(x).useItem();
						mapCurrent.getMap()[player.getRow()][player.getCol()+1] = 0;
						
					}
				}
			
				break; 
			
			//Hot bar #1, checks if you select it
			case('q'):
				//Turns selection to true that way you actually have a selected hotbar item
				for (int x = 0; x < plank.size(); x++) {
					if (plank.get(x).equals(plank.get(x).getHotBarItem())) {
						plank.get(x).selectHotBar();
					}
				}
					
				for (int x = 0; x < pickaxe.size(); x++) {
					if (pickaxe.get(x).equals(pickaxe.get(x).getHotBarItem()))
					{
						pickaxe.get(x).selectHotBar();
					}
				}	
					
				break;
			
			//**************************
				
				
				
				//CHEAT CODES FOR TESTING
			case('j'):
				mapCurrent.increaseWaterLevel();
				break;
					
			case('b'):
				//11 is sight max
				if (player.getSightDistance() < 11) {
					player.setSightDistance(player.getSightDistance()+1);
					//Move these to lantern
					player.setBrightness(MainPanel.brightnesses[MainPanel.brightnesses.length - player.getSightDistance()]);
					player.setSprite(player.getOriginalSprite());	
				}
				break;
				
				//1 is sight minimum
			case('v'):
				if (player.getSightDistance() > 1) {
					player.setSightDistance(player.getSightDistance()-1);
					player.setBrightness(MainPanel.brightnesses[MainPanel.brightnesses.length - player.getSightDistance()]);
					player.setSprite(player.getOriginalSprite());	
				break;
				}
			
			case('n'):
				player.setHealth(0);
				break;
					
			case('m'):
				//MainPanel.winScreen = true;
				//MainPanel.gameScreen = false;
				mapInteractable.setValvesDone(mapInteractable.getValves());
				for (int r = 0; r < mapInteractable.getMap().length; r++) {
					for (int c = 0; c < mapInteractable.getMap()[0].length; c++) {
						if (mapInteractable.getMap()[r][c] == 7) {
							mapInteractable.getMap()[r][c] = 8;
						}		
					}
				}
				break;
			}
		}
	}

	@Override
	//Key up stuff, will set next direction to none if you release
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub\
		
		//Stops the player
		if (MainPanel.gameScreen) {
			switch(e.getKeyChar()) {
			case('w'):		
				if (player.getNextDirection() == 1) {
					player.setNextDirection(0);						
				}
				break;
			case('s'):	
				if (player.getNextDirection() == 2) {
					player.setNextDirection(0);						
				}
				break;			
			case('d'):	
				if (player.getNextDirection() == 3) {
					player.setNextDirection(0);						
				}
				break;			
			case('a'):
				if (player.getNextDirection() == 4) {
					player.setNextDirection(0);						
				}
				break;					
			}	
		}
	}
}
