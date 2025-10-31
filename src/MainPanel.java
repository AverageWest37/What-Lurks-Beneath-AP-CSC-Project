
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;

public class MainPanel extends JPanel implements ActionListener{

	private static final int TILE_SIZE = 50;
	private static final int FRAME_SIZE = 950;
	
	//make customizable, shouldn't interfere with too much. Game speed will change which is an issue (2 FPS very slow movement / enemies)
	public static int FPS = 60;
	
	//make finals
	private static int tilesPerRow = FRAME_SIZE / TILE_SIZE;
	private int visibleCenter = tilesPerRow / 2 + 1;
	
	private Image nothingSprite;

	private BufferedImage bridgeVertical;
	private BufferedImage bridgeHorizontal;

	private Player player;
	
	//Possibly make this an array
	ArrayList<MapParentClass> maps;
	ArrayList<BufferedImage> mapSprites;
	ArrayList<BufferedImage> interactableSprites;
	ArrayList<BufferedImage> enemyAltSpritesOne;
	ArrayList<BufferedImage> enemyAltSpritesTwo;
	ArrayList<BufferedImage> itemSprites;
	ArrayList<Enemy> enemies;
	private int globalFlipTimer;
	private int playerFlipTimer;
	
	public static double moveCheckThreshhold;
	

	private double decimalDifferenceCol = 0;
	private double decimalDifferenceRow = 0;
	
	
	//FLOATS ARE 32 BIT VS DOUBLE 64, Maybe use a math formula to construct float values				11 number values after 0											| not in use
	public static float[] brightnesses = {0, 1, (float).9, (float).8, (float).7, (float).6, (float).5, (float).4, (float).3, (float).2, (float).1, (float).05};
	private RescaleOp brightness = new RescaleOp(1, 0, null);
	
	private double ballMult = 2;
	public static boolean titleScreen = true;
	public static boolean deathScreen = false;
	public static boolean winScreen = false;
	public static boolean gameScreen = false;
	
	public static int difficulty = 1;
	
	private Timer timer;
	
	
	
	
	private ArrayList<Item> lanterns;
	private ArrayList<Item> plank;
	private ArrayList<Item> pickaxe;
	
	//Initializes all the maps, player, sprites, etc
	MainPanel(Player p, ArrayList<Item> plank, ArrayList<Item> pickaxe, ArrayList<Item> lanterns)
{
		
		//CAN MOVE A LOT OF THIS ABOVE TO SAVE ON LINES AND LOOK BETTER?
		
		//Creates the initial large panel
		this.setPreferredSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		this.setMaximumSize(new Dimension(FRAME_SIZE, FRAME_SIZE));
		this.setMinimumSize(new Dimension(FRAME_SIZE, FRAME_SIZE));		
		this.setBackground(Color.black);
		
		this.plank = plank;
		this.pickaxe = pickaxe;
		
		this.lanterns = lanterns;
		
		//Player initializer
		player = p;
		player.setBrightness(brightnesses[brightnesses.length - player.getSightDistance()]);
		
		
		//Enemy initializer		
		Enemy.setPlayer(player);
		
		//The threshold to where a double is close enough to an integer
		moveCheckThreshhold = player.getSpeed()*1.1/FPS;
		

		//Sprite initializers, make lists when there is a lot
		mapSprites = new ArrayList<BufferedImage>();
		interactableSprites = new ArrayList<BufferedImage>();
		enemyAltSpritesOne = new ArrayList<BufferedImage>();
		enemyAltSpritesTwo = new ArrayList<BufferedImage>();
		
		itemSprites = new ArrayList<BufferedImage>();
		
		try {
			BufferedImage playerSpriteTemp = ImageIO.read(new File("Graphics\\PlayerUpTest.png"));
			
			//MapSprites 
			mapSprites.add(ImageIO.read(new File("Graphics\\PixelerFloor.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\PixelerWall.png")));
			
			//Pipes
			mapSprites.add(ImageIO.read(new File("Graphics\\PipeHorizontal.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\PipeVertical.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\PipeIntersection.png")));
			
			//Water
			mapSprites.add(ImageIO.read(new File("Graphics\\WaterUnFilledHorizontal.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\WaterUnFilledVertical.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\WaterUnFilledIntersection.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\WaterFilledHorizontal.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\WaterFilledVertical.png")));
			mapSprites.add(ImageIO.read(new File("Graphics\\WaterFilledIntersection.png")));
			
			//Bridges, they have to be seperate
			bridgeHorizontal = ImageIO.read(new File("Graphics\\BridgeHorizontal.png"));
			bridgeVertical = ImageIO.read(new File("Graphics\\BridgeVertical.png"));
						
			//Goop sprites
			interactableSprites.add(ImageIO.read(new File("Graphics\\GoopHeavy.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\GoopMedium.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\GoopLight.png")));
			
			//Interactable sprites
			interactableSprites.add(ImageIO.read(new File("Graphics\\BoxHiding.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\Valve.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\ValveDone.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\Hatch.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\HatchOpen.png")));
			interactableSprites.add(ImageIO.read(new File("Graphics\\ValveExploded.png")));
			
			//Alternative enemy sprites:
			enemyAltSpritesOne.add(ImageIO.read(new File("Graphics\\EnemyRatOne.png")));
			enemyAltSpritesTwo.add(ImageIO.read(new File("Graphics\\EnemyRatTwo.png")));
			enemyAltSpritesOne.add(ImageIO.read(new File("Graphics\\EnemyBlobOne.png")));
			enemyAltSpritesTwo.add(ImageIO.read(new File("Graphics\\EnemyBlobTwo.png")));
			enemyAltSpritesOne.add(ImageIO.read(new File("Graphics\\EnemyRunnerOne.png")));
			enemyAltSpritesTwo.add(ImageIO.read(new File("Graphics\\EnemyRunnerTwo.png")));
			enemyAltSpritesOne.add(ImageIO.read(new File("Graphics\\EnemyBlobFatOne.png")));
			enemyAltSpritesTwo.add(ImageIO.read(new File("Graphics\\EnemyBlobFatTwo.png")));
			enemyAltSpritesOne.add(ImageIO.read(new File("Graphics\\EnemyAnglerOne.png")));
			enemyAltSpritesTwo.add(ImageIO.read(new File("Graphics\\EnemyAnglerTwo.png")));
			
			
			
			//Item own sprites, Benny
			itemSprites.add(ImageIO.read(new File("Graphics\\Plank.png")));
			itemSprites.add(ImageIO.read(new File("Graphics\\Shoes.png")));
			itemSprites.add(ImageIO.read(new File("Graphics\\Lantern.png")));
			itemSprites.add(ImageIO.read(new File("Graphics\\Pickaxe.png")));

			
			//Has to be in here because its complaining
			player.setOriginalSprite(playerSpriteTemp);
			
		}catch (Exception e) {
			//prints error? Need exception for ImageIO to work (not print)
			e.printStackTrace();
			
		}
		
		resetLevel();
		globalFlipTimer = 0;
		playerFlipTimer = 0;
		//Timer initializer, maybe have it start when the game starts (not in title screen), slower than expected, fixed with multiplier
		timer = new Timer((int)(1000/(FPS * 1.2)), this);
		timer.start();		
	}
	
	//Full reset from the title screen, accounts for FPS
	public void fullReset(){
		timer.stop();
		timer = new Timer((int)(1000/(FPS * 1.2)), this);
		timer.start();
		moveCheckThreshhold = player.getSpeed()*1.1/FPS;
		player.setSightDistance(8);
		resetLevel();
	}
	public boolean isEnemySpotOpen(int row, int col) {
		if (maps.get(1).getMap()[row][col] == 0 && Math.sqrt(Math.pow(player.getRow() - row,  2) + (Math.pow(player.getRow() - row,  2))) > 9) {
			return true;
		}
		return false;
	}
	
	//Resets the level and scales with difficulty
	public void resetLevel(){
		
		player.setHealth(player.getHealthMax());
		player.setEnergyMax(player.getHealthMax());
		player.setEnergy(player.getEnergyMax());
		player.setInvincibilityCounter(-1);
		player.setRunning(false);
		player.setSpeed(player.getSpeedOG());
		ballMult = 2;
		
		enemies = new ArrayList<Enemy>();
		//Initializes all the maps
		maps = new ArrayList<MapParentClass>();
		
		maps.add(new MapConstant());
		
		int[][] randomMap;
		
		if (difficulty <= 5) {
			randomMap = ((MapConstant)maps.get(0)).getMapsList().get((int)(Math.random()* 2));
		}else {
			randomMap = ((MapConstant)maps.get(0)).getMapsList().get((int)(Math.random()* 2) + 2);
		}
		
		
		
		maps.add(new MapCurrent(randomMap));
		maps.add(new MapEntity(randomMap));
		maps.add(new MapVisible((MapCurrent)maps.get(1), player, enemies));
		double valveMult = Math.pow(1.15, difficulty);
		maps.add(new MapInteractables((MapCurrent)maps.get(1), valveMult));
				
		//Benny thing
		maps.add(new MapItem(maps.get(1).getMap()));
		
		
		//Gives entity player and player entity, SWAP WHEN UPDATING ENTITY MAP, JK not using that at all
		((MapEntity)maps.get(2)).setPlayer(player);
		Entity.setMapEntity((MapEntity)maps.get(2));
		Enemy.setMapCurrent(getMapCurrent());
		((MapVisible)maps.get(3)).setMapInteractable((MapInteractables)maps.get(4));
		
		
		
		//BENNY CODE BEWARE **********
		
		for (int x = 0; x < lanterns.size(); x++)
		{
			((MapItem)maps.get(5)).setItem(lanterns.get(x));
		}
		
		for (int x = 0; x < plank.size(); x++)
		{
			((MapItem)maps.get(5)).setItem(plank.get(x));
		}
		
		for (int x = 0; x < pickaxe.size(); x++)
		{
			((MapItem)maps.get(5)).setItem(pickaxe.get(x));
		}
		
		
		//****
		
		
		int mapRowSize = maps.get(4).getMap().length;
		int mapColSize = maps.get(4).getMap()[0].length;
		
		//sets player random position
		int randomRow = (int)(Math.random() * mapRowSize);
		int randomCol = (int)(Math.random() * mapColSize);
		while(maps.get(1).getMap()[randomRow][randomCol] != 0) {
			randomRow = (int)(Math.random() * mapRowSize);
			randomCol = (int)(Math.random() * mapColSize);
			
		}
		player.setRow(randomRow);
		player.setCol(randomCol);
		player.setDecimalRow(randomRow);
		player.setDecimalCol(randomCol);
		
		
		//Adds enemies to the map, randomly	
		try {
			for (int i = 0; i < (int)(6 * (Math.pow(1.23,  difficulty))); i++) {
				int enemyChance = (int)(Math.random() * 100);
				randomRow = player.getRow();
				randomCol = player.getCol();
				while(!isEnemySpotOpen(randomRow, randomCol)) {
					randomRow = (int)(Math.random() * mapRowSize);
					randomCol = (int)(Math.random() * mapColSize);
				}
				double speedInfluencer = Math.random() * .2 + Math.random() * -.2 + 1;
				
				if (enemyChance >= 94) {//94
					enemies.add(new Enemy(randomRow, randomCol, 1.5 * speedInfluencer, 7, 12, ImageIO.read(new File("Graphics\\EnemyAngler.png")), 6));
				}else if (enemyChance >= 88) {//88
					enemies.add(new Enemy(randomRow, randomCol, .5 * speedInfluencer, 6, 15, ImageIO.read(new File("Graphics\\EnemyBlobFat.png")), 5));
				}else if (enemyChance >= 67) {
					enemies.add(new Enemy(randomRow, randomCol, .9 * speedInfluencer, 6, 4, ImageIO.read(new File("Graphics\\EnemyRunner.png")), 4));
				}else if (enemyChance >= 30) {
					enemies.add(new Enemy(randomRow, randomCol, .7 * speedInfluencer, 6, 7, ImageIO.read(new File("Graphics\\EnemyBlob.png")), 3));
				}else {
					enemies.add(new Enemy(randomRow, randomCol, .8 * speedInfluencer, 4, 0, ImageIO.read(new File("Graphics\\EnemyRat.png")), 2));
				}		
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	//GUI MAIN DRAWING
	public void paint(Graphics g) {
		//Resets the frame so no overlap
		super.paint(g);
		Graphics2D g2D = (Graphics2D)g;
		
		//Draws the game
		if (gameScreen) {
			//Gets the decimal value to move the whole map incrementally
			if (player.getDirection() != 0) {
				decimalDifferenceCol = player.getDecimalCol() - player.getPrevCol();
				decimalDifferenceRow = player.getDecimalRow() - player.getPrevRow();
			}
	
			//Main drawing of the map, scans through the visible map
							//plus 2 is for the stuff draw outside the frame, 1 one is to offset tiles left and up, so they don't start at 0
			for (int row = 0; row < tilesPerRow + 2; row++) {  
				for (int col = 0; col < tilesPerRow + 2; col++) {
					
					//Checks if darkness isn't 0, if it isn't set the darkness then find the right image
					if (!(((MapVisible)maps.get(3)).getMapDarkness()[row][col] == 0)) {
						brightness = new RescaleOp(brightnesses[((MapVisible)maps.get(3)).getMapDarkness()[row][col]], 0, null);
						
						if (maps.get(3).getMap()[row][col] == -1) {
							BufferedImage imageTile = brightness.filter(mapSprites.get(1), null);
							g2D.drawImage(imageTile, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);						
						}					
						else if (maps.get(3).getMap()[row][col] <= 10) {
							BufferedImage imageTile = brightness.filter(mapSprites.get(maps.get(3).getMap()[row][col]), null);
							g2D.drawImage(imageTile, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);
							
						}else if (maps.get(3).getMap()[row][col] > 10 && maps.get(3).getMap()[row][col] <= 15) {
							BufferedImage imageTile = brightness.filter(mapSprites.get(maps.get(3).getMap()[row][col] - 6), null);
							g2D.drawImage(imageTile, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);
						}
	
						
						//Update because this is bad, has to recreate everything for the bridges
						switch(maps.get(3).getMap()[row][col]) {
							//For the bridge, move so you don't need double stuff? **EXTREMELY RUDAMENTARY**
						case(11):
							BufferedImage bridgeDark1 = brightness.filter(bridgeHorizontal, null);
							g2D.drawImage(bridgeDark1, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);
							break;							
						case(12):
							BufferedImage bridgeDark2 = brightness.filter(bridgeVertical, null);
							g2D.drawImage(bridgeDark2, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);
							break;						
						case(14):
							BufferedImage bridgeDark3 = brightness.filter(bridgeHorizontal, null);
							g2D.drawImage(bridgeDark3, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);
							break;		
						case(15):
							BufferedImage bridgeDark4 = brightness.filter(bridgeVertical, null);
							g2D.drawImage(bridgeDark4, (int)((col-decimalDifferenceCol-1)*TILE_SIZE), (int)((row-decimalDifferenceRow-1)*TILE_SIZE), null);
							break;							
						}
					}
				}
			}	
			
			//Draws the interactable stuff and the goop
			for (int row = 0; row < maps.get(4).getMap().length; row++) {  
				for (int col = 0; col < maps.get(4).getMap()[0].length; col++) {
					if (maps.get(4).getMap()[row][col] >= 1) {
						try {
							
							int darknessAmount = ((MapVisible)maps.get(3)).getMapDarkness()[(tilesPerRow/2 + 1 + (row - player.getRow()))][(tilesPerRow/2 + 1 + (col - player.getCol()))];
							int darknessAmountMoving = ((MapVisible)maps.get(3)).getMapDarkness()[(tilesPerRow/2 + 1 + (row - player.getPrevRow()))][(tilesPerRow/2 + 1 + (col - player.getPrevCol()))];
							
							//Has to check is player is moving or not to draw properly
							if (player.getDirection() == 0) {
								if (player.getValveHint() != -1 && maps.get(4).getMap()[row][col] == 5) {
									darknessAmount = brightnesses.length - 1 - player.getEnergy()/2;
								}								
								if (darknessAmount != 0) {
									brightness = new RescaleOp(brightnesses[darknessAmount], 0, null);
									BufferedImage interactImage = brightness.filter(interactableSprites.get(maps.get(4).getMap()[row][col] - 1), null);
									g2D.drawImage(interactImage, (int)((tilesPerRow/2 + (col - player.getDecimalCol())) *TILE_SIZE), (int)((tilesPerRow/2 + (row - player.getDecimalRow())) *TILE_SIZE), null);
								}						
							}else {	
								if (player.getValveHint() != -1 && maps.get(4).getMap()[row][col] == 5) {
									darknessAmountMoving = brightnesses.length - 1 - player.getEnergy()/2;
								}
								
								if (darknessAmountMoving != 0) {
									brightness = new RescaleOp(brightnesses[darknessAmountMoving], 0, null);
									BufferedImage interactImage = brightness.filter(interactableSprites.get(maps.get(4).getMap()[row][col] - 1), null);
									g2D.drawImage(interactImage, (int)((tilesPerRow/2 + (col - player.getDecimalCol())) *TILE_SIZE), (int)((tilesPerRow/2 + (row - player.getDecimalRow())) *TILE_SIZE), null);
								}	
							}
						}catch (Exception e) {
							
						}	
					}
				}
			}
			
			
			//BENNY CODE BEWARE *******
			
			for (int x = 0; x < plank.size(); x++) {
				if (plank.get(x).inHotBar()) {
					g2D.drawImage(itemSprites.get(0), (int)(1) * TILE_SIZE, (int)(16) * TILE_SIZE, null);					
				}
			}
			
			for (int x = 0; x < pickaxe.size(); x++) {
				if (pickaxe.get(x).inHotBar()) {
					g2D.drawImage(itemSprites.get(3), (int)(1) * TILE_SIZE, (int)(16) * TILE_SIZE, null);					
				}
			}
			
			
			for (int row = 0; row < maps.get(5).getMap().length; row++) {
	            for (int col = 0; col < maps.get(5).getMap()[0].length; col++) {
	                //for loop to generate each and every little object we have
	            	try {
	            		for (int x = 0; x < plank.size(); x++) {
		                    if (maps.get(5).getMap()[row][col] == 1 && !plank.get(x).inHotBar() && !plank.get(x).getUsed()) {
		                    	int darknessAmount = ((MapVisible)maps.get(3)).getMapDarkness()[(tilesPerRow/2 + 1 + (row - player.getRow()))][(tilesPerRow/2 + 1 + (col - player.getCol()))];
		                    	brightness = new RescaleOp(brightnesses[darknessAmount], 0, null);
		                    	BufferedImage itemImage = brightness.filter(itemSprites.get(0), null);
		                        g2D.drawImage(itemImage, (int)((tilesPerRow/2 + (col - player.getDecimalCol())) * TILE_SIZE), (int)((tilesPerRow/2 + (row - player.getDecimalRow())) * TILE_SIZE), null);
		                    }
		                }

		                for (int x = 0; x < pickaxe.size(); x++) {
		                    if (maps.get(5).getMap()[row][col] == 3 && !pickaxe.get(x).inHotBar() && !pickaxe.get(x).getUsed()) {
		                    	int darknessAmount = ((MapVisible)maps.get(3)).getMapDarkness()[(tilesPerRow/2 + 1 + (row - player.getRow()))][(tilesPerRow/2 + 1 + (col - player.getCol()))];
		                    	brightness = new RescaleOp(brightnesses[darknessAmount], 0, null);
		                    	BufferedImage itemImage = brightness.filter(itemSprites.get(3), null);
		                        g2D.drawImage(itemImage, (int)((tilesPerRow/2 + (col - player.getDecimalCol())) * TILE_SIZE), (int)((tilesPerRow/2 + (row - player.getDecimalRow())) * TILE_SIZE), null);
		                    }
		                }

		                for (int x = 0; x < lanterns.size(); x++) {
		                    if (maps.get(5).getMap()[row][col] == 2 && !lanterns.get(x).getUsed()) {
		                    	int darknessAmount = ((MapVisible)maps.get(3)).getMapDarkness()[(tilesPerRow/2 + 1 + (row - player.getRow()))][(tilesPerRow/2 + 1 + (col - player.getCol()))];
		                    	brightness = new RescaleOp(brightnesses[darknessAmount], 0, null);
		                    	BufferedImage itemImage = brightness.filter(itemSprites.get(2), null);
		                        g2D.drawImage(itemImage, (int)((tilesPerRow/2 + (col - player.getDecimalCol())) * TILE_SIZE), (int)((tilesPerRow/2 + (row - player.getDecimalRow())) * TILE_SIZE), null);
		                    }
		                }
	            	}catch(Exception p) {
	            		
	            	}    
	            }  
	        }

			
		
			//*******
			
			
			//Draws the player, also sets the rotation and animations
			player.rotation = new AffineTransform();
			player.getRotation().translate(tilesPerRow / 2 * TILE_SIZE, tilesPerRow / 2 * TILE_SIZE);
			
			try {
				if (player.getDirection() == 0) {
					player.setSprite(ImageIO.read(new File("Graphics\\PlayerUpTest.png")));
				}else if (playerFlipTimer < 27 / player.getSpeed()) {
					player.setSprite(ImageIO.read(new File("Graphics\\PlayerUpTestOne.png")));
				}else if (playerFlipTimer < 54 / player.getSpeed()){
					player.setSprite(ImageIO.read(new File("Graphics\\PlayerUpTestTwo.png")));
				}else {
					playerFlipTimer = 0;
				}
			}catch(Exception v) {
				
			}
			player.setRotation();
			if (!player.getHiding()) {
				g2D.drawImage(player.getSprite(), player.getRotation(), null);	
			}
			
			//The animation timer, does not work the best for enemies as they all use the same timer, and its influence by speed
			//Has to use global timer for enemies so it doesn't lag really bad for some reason
			globalFlipTimer++;
			playerFlipTimer++;
			
			//Enemies drawing, goes through every enemy
			for (int e = 0; e < enemies.size(); e++) {//Have a switch case to detect monster size so 70 x 70 monsters are centered
				//g2D.drawImage(enemies.get(e).getSprite(), (int)((tilesPerRow/2 + (enemies.get(e).getDecimalCol() - player.getDecimalCol())) *TILE_SIZE) , (int)((tilesPerRow/2 + (enemies.get(e).getDecimalRow() - player.getDecimalRow())) *TILE_SIZE) , null);
				try {
					
					int darknessAmount = ((MapVisible)(maps.get(3))).getMapDarkness()[(tilesPerRow / 2 + 1) + (enemies.get(e).getRow() - player.getRow())][(tilesPerRow / 2 + 1) + (enemies.get(e).getCol() - player.getCol())];
					int darknessAmountMoving = ((MapVisible)(maps.get(3))).getMapDarkness()[(tilesPerRow / 2 + 1) + (enemies.get(e).getRow() - player.getPrevRow())][(tilesPerRow / 2 + 1) + (enemies.get(e).getCol() - player.getPrevCol())];
					//Fixes an annoying bug where angler enemy would go off the light tile its on
					boolean fishBright = false;
					//Brightness is one darker than the tile so you don't see it when you go towards the thing immediately, idk if this is even true anymore ngl
					//Handles brightness
					if (player.getDirection() == 0) {
						enemies.get(e).setBrightness(brightnesses[darknessAmount]);
					}else {
						enemies.get(e).setBrightness(brightnesses[darknessAmountMoving]);
					}
					if ((enemies.get(e).getID() == 6 || enemies.get(e).getID() == 5) && (brightnesses[((MapVisible)(maps.get(3))).getMapDarkness()[(tilesPerRow / 2 + 1) + (enemies.get(e).getRow() - player.getPrevRow())][(tilesPerRow / 2 + 1) + (enemies.get(e).getCol() - player.getPrevCol())]] < brightnesses[((MapVisible)(maps.get(3))).getMapDarkness()[(tilesPerRow / 2 + 1) + (enemies.get(e).getPrevRow() - player.getPrevRow())][(tilesPerRow / 2 + 1) + (enemies.get(e).getPrevCol() - player.getPrevCol())]])) {
						enemies.get(e).setBrightness(brightnesses[((MapVisible)(maps.get(3))).getMapDarkness()[(tilesPerRow / 2 + 1) + (enemies.get(e).getPrevRow() - player.getPrevRow())][(tilesPerRow / 2 + 1) + (enemies.get(e).getPrevCol() - player.getPrevCol())]]);
						fishBright = true;
					}
					BufferedImage darkEnemy = enemies.get(e).getBrightness().filter(enemies.get(e).getSprite(), null);
					//Animation stuff
					if (enemies.get(e).getDirection() != 0) {
						if (globalFlipTimer < 18 / enemies.get(e).getSpeed()) {
							darkEnemy = enemies.get(e).getBrightness().filter(enemies.get(e).getSprite(), null);
						}else if (globalFlipTimer < 36 / enemies.get(e).getSpeed()) {
							darkEnemy = enemies.get(e).getBrightness().filter(enemyAltSpritesOne.get(enemies.get(e).getID()-2), null);
						}else if (globalFlipTimer < 54 / enemies.get(e).getSpeed()) {
							darkEnemy = enemies.get(e).getBrightness().filter(enemyAltSpritesTwo.get(enemies.get(e).getID()-2), null);			
						}else {
							globalFlipTimer = 0;
						}
					}

					enemies.get(e).rotation = new AffineTransform();
	
					//-10 if for large enemies
					//Draws the enemies, large enemies are 70 x 70 so they a little different
					if (enemies.get(e).getID() >= 5 ) {
						enemies.get(e).getRotation().translate(((tilesPerRow/2 + (enemies.get(e).getDecimalCol() - player.getDecimalCol())) *TILE_SIZE) - 10, ((tilesPerRow/2 + (enemies.get(e).getDecimalRow() - player.getDecimalRow())) *TILE_SIZE) - 10);
					}else {
						enemies.get(e).getRotation().translate(((tilesPerRow/2 + (enemies.get(e).getDecimalCol() - player.getDecimalCol())) *TILE_SIZE), ((tilesPerRow/2 + (enemies.get(e).getDecimalRow() - player.getDecimalRow())) *TILE_SIZE));
					}
					enemies.get(e).setRotation();
					
					//Actually draws the think if it has brightness
					if (darknessAmount != 0 || darknessAmountMoving != 0 || fishBright) {
						g2D.drawImage(darkEnemy, enemies.get(e).getRotation(), null);
					}
					
				}catch(Exception b) {
					
				}
			}
					
			//Hot bar, displays stamina and health
				g2D.setPaint(new Color(100, 120, 170));
				g2D.fillRect(50, FRAME_SIZE - 50, (int)(player.getEnergy()*20), -10);
				g2D.setStroke(new BasicStroke(3));
				g2D.setPaint(new Color(40, 50, 70));
				g2D.drawRect(50, FRAME_SIZE - 60, (int)(player.getEnergyMax()*20), 10);
				
				g2D.setPaint(new Color(150, 100, 90));
				if (player.getHealth() > 0) {
					g2D.fillRect(50, FRAME_SIZE - 70, (int)(player.getHealth()*20), -10);
				}
				g2D.setStroke(new BasicStroke(3));
				g2D.setPaint(new Color(100, 20, 10));
				g2D.drawRect(50, FRAME_SIZE - 80, (int)(player.getHealthMax()*20), 10);
				
				//Cool lil red hit effect
				try {
					g2D.setStroke(new BasicStroke((int)(-(Math.pow(player.getInvincibilityCounter() - Math.sqrt(40), 2)) + 40)));
				}catch(Exception b) {
					g2D.setStroke(new BasicStroke(1));
	
				}
				g2D.drawRect(-1, -1, FRAME_SIZE + 1, FRAME_SIZE + 1);
				
			//}
			}
			
			//Death and win screen graphics, scary ball ahhhhh
			if (deathScreen || player.getHealth() <= 0 || winScreen) {
				if (winScreen) {
					g2D.setPaint(new Color(5, 15, 5));
				}else {
					g2D.setPaint(new Color(20, 5, 15));
				}
					
				g2D.fillOval(FRAME_SIZE / 2 - (int)ballMult / 2, FRAME_SIZE / 2 - (int)ballMult / 2, (int)ballMult, (int)ballMult);
				
				if (ballMult > 5000000) {
					//gameEnd = true;
					
					if (winScreen) {
						g2D.setPaint(new Color(70, 160, 40));
						g2D.setFont(new Font("Chiller", Font.PLAIN, 100));
						g2D.drawString("You Are Alive", FRAME_SIZE / 2 - 210, FRAME_SIZE / 2);
						
					}else {
						g2D.setPaint(new Color(190, 50, 40));
						g2D.setFont(new Font("Chiller", Font.PLAIN, 100));
						g2D.drawString("You Are Dead", FRAME_SIZE / 2 - 200, FRAME_SIZE / 2);
						deathScreen = true;
					}
					
					g2D.setFont(new Font("Ink Free", Font.PLAIN, 20));
					g2D.drawString("Press \"F\"", FRAME_SIZE / 2 - 45, FRAME_SIZE - 50);
					gameScreen = false;
						
				}else {
					ballMult *= 1.25;
				}
			}
			
			//All the title screen graphics, pretty cool ngl ngl
			if (titleScreen) {
				g2D.setPaint(new Color(35, 15, 55));
				g2D.fillRect(0, 0, FRAME_SIZE, FRAME_SIZE);
				g2D.setFont(new Font("Viner Hand ITC", Font.BOLD, 80));
				g2D.setPaint(new Color(80, 90, 70));
				//ETERNAL WASTES
				g2D.drawString("What Lurks Beneath", FRAME_SIZE / 2 - 420, FRAME_SIZE / 2 - 150);
				g2D.setFont(new Font("Viner Hand ITC", Font.BOLD, 79));
				g2D.setPaint(new Color(60, 70, 50));
				//ETERNAL WASTES
				g2D.drawString("What Lurks Beneath", FRAME_SIZE / 2 - 414, FRAME_SIZE / 2 - 149);
				
				g2D.setPaint(new Color(65, 77, 55));
				g2D.setFont(new Font("Viner Hand ITC", Font.BOLD, 50));
				g2D.drawString("FPS: ", FRAME_SIZE / 2 + 150, FRAME_SIZE / 2 + 50);
				g2D.drawString("  u:  30", FRAME_SIZE / 2 + 200, FRAME_SIZE / 2 + 110);
				g2D.drawString("  i:  45", FRAME_SIZE / 2 + 200, FRAME_SIZE / 2 + 170);
				g2D.drawString("  o:  60", FRAME_SIZE / 2 + 200, FRAME_SIZE / 2 + 230);
				g2D.fillOval(FRAME_SIZE / 2 + 160, FRAME_SIZE / 2 + 90 + (FPS - 30) * 4, 20, 20);
				
				g2D.setFont(new Font("Viner Hand ITC", Font.BOLD, 40));
				g2D.drawString("Starting Difficuly: (1-9)", FRAME_SIZE / 2 - 410, FRAME_SIZE / 2 + 50);
				g2D.setFont(new Font("Viner Hand ITC", Font.BOLD, 60));
				g2D.drawString(Integer.toString(difficulty), FRAME_SIZE / 2 - 190, FRAME_SIZE / 2 + 180);
				
				g2D.setFont(new Font("Viner Hand ITC", Font.BOLD, 40));
				g2D.drawString("\"F\" To Start", FRAME_SIZE / 2 - 110, FRAME_SIZE - 50);
			}
	}

	//EXECUTED EVERY FRAME WITH THE TIMER
	//The main juice of the code. It was check like a million thing for player and enemies every frame
	//Changes positions, checks actions, etc
	public void actionPerformed(ActionEvent e) {
		
		//Will actually run if game is happening
		if (gameScreen && player.getHealth() > 0) {
			//Checks if player is at the correct tile, has to use intervals to see if double is super close to integer. May cause issues at higher speeds
			if (player.getDecimalRow() - moveCheckThreshhold  * (1 + player.getSpeedMult()) <= player.getRow() && player.getDecimalRow() + moveCheckThreshhold * (1 + player.getSpeedMult()) >= player.getRow() && player.getDecimalCol() - moveCheckThreshhold * (1 + player.getSpeedMult()) <= player.getCol() && player.getDecimalCol() + moveCheckThreshhold * (1 + player.getSpeedMult()) >= player.getCol()) {
				
				//Resets all map movement stuff, and direction. Direction will be in the way its trying to move
				player.setDecimalRow(player.getRow());
				player.setDecimalCol(player.getCol());
				player.setDirection(player.getNextDirection());
				
				//More map movement stuff, for the decimal difference equation, and checks. Previous positions
				player.setPrevRow(player.getRow());
				player.setPrevCol(player.getCol());
				decimalDifferenceCol = 0;
				decimalDifferenceRow = 0;
				
				//Updates the visible map so your view is that square you see
				((MapVisible)maps.get(3)).updateMapVisible();
				
				//Moves the player tile position once when not moving
				try {
					switch(player.getDirection()) {
					case(1):
						player.setRow(player.getRow() - 1);
						player.setAngle(0);					
						break;
					case(2):
						player.setRow(player.getRow() + 1);
						player.setAngle(180);						
						break;
					case(3):
						player.setCol(player.getCol() + 1);
						player.setAngle(90);						
						break;
					case(4):
						player.setCol(player.getCol() - 1);
						player.setAngle(270);	
						break;
					}	
				}catch(Exception f)	{
					
				}
				
	
				//Checks if tile can be walked on and tile stuff
				if (player.getDirection() != 0) {
				//Checks if you can move to next tile, if not, undo everything					
					int tileTypeGoingTo = maps.get(3).getMap()[visibleCenter + (player.getRow()-player.getPrevRow())][visibleCenter + (player.getCol()-player.getPrevCol())];
					
					if ((tileTypeGoingTo >= 1 && tileTypeGoingTo <= 4)|| tileTypeGoingTo == -1 || (tileTypeGoingTo >= 8 && tileTypeGoingTo <= 10) ) {
						player.setRow(player.getPrevRow());
						player.setCol(player.getPrevCol());
						
						//Perhaps remove if you want to slam head into wall (silly)
						player.setDirection(0);
						
					//Checks if player is running to reduce the energy
					}else if(player.getRunning() && player.getEnergy() > 0) {
						player.setEnergy(player.getEnergy()-1);
					}
					
					
					//Updates the entity map, USELESS >:((((
					player.updateMapEntity(player.getPrevRow(), player.getPrevCol(), player.getRow(), player.getCol(), 1);
				}
				
			}//EXIT THRESHOLD
			
			//Checks for the running and the valve hint, handles all that stuff
			if (!player.getRunning() || player.getDirection() == 0) {
				player.setEnergyCount(player.getEnergyCount() + 1);
			}else {
				player.setEnergyCount(-FPS);
			}
			if (player.getEnergy() < player.getEnergyMax() && player.getEnergyCount() == FPS){
				
				player.setEnergy(player.getEnergy()+1);
				player.setEnergyCount(0);
	
			}
			if (player.getEnergy() <= 0 && player.getEnergyCount() == -FPS) {
				if (player.getRunning()) {
					player.setSpeed(player.getSpeed()-(player.getSpeedOG()*.5));
				}
				player.setRunning(false);
				player.setValveHint(-1);	
			}
			
			//Moves the actual location (decimal values) of the player, even between tiles. 
			switch(player.getDirection()) {
			case(1):
				player.setDecimalRow(player.getDecimalRow() - (((double)player.getSpeed())/FPS));
				break;
			case(2):
				player.setDecimalRow(player.getDecimalRow() + (((double)player.getSpeed())/FPS));
				break;
			case(3):
				player.setDecimalCol(player.getDecimalCol() + (((double)player.getSpeed())/FPS));
				break;
			case(4):
				player.setDecimalCol(player.getDecimalCol() - (((double)player.getSpeed())/FPS));
				break;
			}			
			
			
			//Player invincibility, for i frames and speed boost
			if (player.getInvincibilityCounter() != 0) {
				player.setInvincibilityCounter(player.getInvincibilityCounter() + 1);
				
			}
			if (player.getInvincibilityCounter() == 4 * FPS) {
				player.setInvincibilityCounter(0);
				player.setSpeed(player.getSpeed()-(player.getSpeedOG()*(player.getSpeedMult())));
				player.setSpeedMult(0);
				
			}
			
			//More energy and valve hint stuff
			if(player.getValveHint() >= (.05 * FPS) && player.getEnergy() > 0) {
				player.setEnergy(player.getEnergy()-1);
				player.setEnergyCount(-61);
				player.setValveHint(0);
			}
			
			if (player.getValveHint() != -1) {
				player.setValveHint(player.getValveHint()+1);
			}
	
			
			
			
			//**********
			//ENEMIES DOWN BELOW, BEWARE
			//**********
			
			
			
			
			
			//Enemy update stuff, handles movement, checks, damage, etc
			for (int en = enemies.size() - 1; en >= 0; en--) {
				//Same as player where it uses a threshold to check if it is directly on a tile
				if (enemies.get(en).getDecimalRow() - moveCheckThreshhold <= enemies.get(en).getRow() && enemies.get(en).getDecimalRow() + moveCheckThreshhold >= enemies.get(en).getRow() && enemies.get(en).getDecimalCol() - moveCheckThreshhold <= enemies.get(en).getCol() && enemies.get(en).getDecimalCol() + moveCheckThreshhold >= enemies.get(en).getCol()) {
					
					//Sets the decimal positions directly to the tile position to be accurate
					enemies.get(en).setDecimalRow(enemies.get(en).getRow());
					enemies.get(en).setDecimalCol(enemies.get(en).getCol());
					
					//For random nick nacks I might need this for
					enemies.get(en).setPrevRow(enemies.get(en).getRow());
					enemies.get(en).setPrevCol(enemies.get(en).getCol());
	
					//Path finding checker, will randomly also go to a spot (like minecraft!!) if player is not found. 
					if (player.getHiding() || !(enemies.get(en).getRow() == player.getRow() && enemies.get(en).getCol() == player.getCol())) {
						
						//Gets a suitable spot to randomly go to
						if (enemies.get(en).getPathCounter() / FPS == enemies.get(en).getRandomTime()) {
							int tileIdentity = -1;
							int tempRow = 0;
							int tempCol = 0;
							int randomRowAdd = 0;
							int randomColAdd = 0;
							while(tileIdentity == -1 || (tileIdentity >= 1 && tileIdentity <= 4)) {
	
								tempRow = enemies.get(en).getRow();
								tempCol = enemies.get(en).getCol();
								
								randomRowAdd = (int)(Math.random()*30 + Math.random()*-30);
								randomColAdd = (int)(Math.random()*30 + Math.random()*-30);
								try {
									tileIdentity = maps.get(1).getMap()[tempRow + randomRowAdd][tempCol + randomColAdd];
								}catch(Exception u) {
									tileIdentity = -1;
								}
							}
							enemies.get(en).setRandomTime((int)(Math.random()*10));
							enemies.get(en).pathFinding(tempRow, tempCol, tempRow + randomRowAdd, tempCol + randomColAdd);
							
						//Increases counter to actually path find randomly
						}else if(!(enemies.get(en).getPathFound())) {
							enemies.get(en).setPathCounter(enemies.get(en).getPathCounter() + 1);
						}
						
						//Checks for a player in their sight line
						if (Math.sqrt(Math.pow(player.getRow() - enemies.get(en).getRow(), 2) + Math.pow(player.getCol() - enemies.get(en).getCol(), 2)) <= tilesPerRow / 2 + 1) {
							enemies.get(en).sightLineChecker();
						}
							
						//Will follow the path if it is found
						if (enemies.get(en).getPathFound()) {
							enemies.get(en).followPath();
							
							
							//Running stuff for the running enemy dude
							if (enemies.get(en).getID() == 4) {
								if (enemies.get(en).getRunTimer() > 0) {
									enemies.get(en).setSpeed(enemies.get(en).getSpeedOG() * 3);
									enemies.get(en).setRunTimer(enemies.get(en).getRunTimer());
								}
								if (enemies.get(en).getRunTimer() == 9) {
									enemies.get(en).setRunTimer(-1);
									enemies.get(en).setSpeed(enemies.get(en).getSpeedOG());
								}
							}
						}
					}else {
						//enemy wont move if on player
						enemies.get(en).setDirection(0);						
					}
	
	
					//Moves the enemies position once when not moving		
					try {
						switch(enemies.get(en).getDirection()) {
						case(1):
							enemies.get(en).setRow(enemies.get(en).getRow() - 1);
							enemies.get(en).setAngle(0);
							break;
						case(2):
							enemies.get(en).setRow(enemies.get(en).getRow() + 1);
							enemies.get(en).setAngle(180);
							break;
						case(3):
							enemies.get(en).setCol(enemies.get(en).getCol() + 1);
							enemies.get(en).setAngle(90);
							break;
						case(4):
							enemies.get(en).setCol(enemies.get(en).getCol() - 1);
							enemies.get(en).setAngle(270);
							break;
						}	
					}catch(Exception f)	{
						
					}
					
					
					//Checks if tile can be walked on and tile stuff
					if (enemies.get(en).getDirection() != 0) {
					//Checks if enemy can move to next tile, if not, undo everything		
						int tileTypeGoingTo;
						try {
							tileTypeGoingTo = maps.get(1).getMap()[enemies.get(en).getRow()][enemies.get(en).getCol()];
						}catch(Exception b) {
							tileTypeGoingTo = -1;
						}
						//Might not be necessary if path finding is correct********
						if ((tileTypeGoingTo >= 1 && tileTypeGoingTo <= 4) || tileTypeGoingTo == -1 ) {
							enemies.get(en).setRow(enemies.get(en).getPrevRow());
							enemies.get(en).setCol(enemies.get(en).getPrevCol());
							enemies.get(en).setDirection(0);
							
						}else {
							//Updates the entity map USLESS :(((
							enemies.get(en).updateMapEntity(enemies.get(en).getPrevRow(), enemies.get(en).getPrevCol(), enemies.get(en).getRow(), enemies.get(en).getCol(), enemies.get(en).getID());
						}
					}
					
					//Fat blob goop drawer to make fat blob work right
					if (enemies.get(en).getID() == 5 && enemies.get(en).getDirection() != 0) {
						
						if (maps.get(1).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] == 0) {
							
							if (maps.get(4).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] == 0){
								maps.get(4).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] = 3;
								
							}else if(maps.get(4).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] <= 3 && maps.get(4).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] != 1){
								maps.get(4).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] = maps.get(4).getMap()[enemies.get(en).getPrevRow()][enemies.get(en).getPrevCol()] - 1;
							}
						}	
					}
				}//EXIT THRESHOLD
				
				
				//Player damage, checks if close enough to hit
				if (Math.abs(player.getDecimalRow() - enemies.get(en).getDecimalRow()) <= .5 && Math.abs(player.getDecimalCol() - enemies.get(en).getDecimalCol()) <= .5 && !player.getHiding()) {
					if (player.getInvincibilityCounter() == 0) {
						if (enemies.get(en).getID() != 2) {
							player.setHealth(player.getHealth() - enemies.get(en).getDamage());
							player.setInvincibilityCounter(player.getInvincibilityCounter() + 1);
							player.setSpeedMult(enemies.get(en).getDamage() * enemies.get(en).getSpeedOG() * .08);
							player.setSpeed(player.getSpeed() + (player.getSpeedOG()*(player.getSpeedMult())));
							if (enemies.get(en).getID() == 4) {
								enemies.get(en).setRunTimer(-1);
								enemies.get(en).setSpeed(enemies.get(en).getSpeedOG());
							}
							
							//Rat damage, damages the max energy not health
						}else if (player.getEnergyMax() > 0){
							player.setEnergyMax(player.getEnergyMax() - 2);
							player.setInvincibilityCounter(player.getInvincibilityCounter() + 1);
							player.setSpeedMult(.2);
							player.setSpeed(player.getSpeed() + (player.getSpeedOG()*(player.getSpeedMult())));							
						}
					}
				}
				
				//Moves the actual location (decimal values) of the enemy, even between tiles. 
				switch(enemies.get(en).getDirection()) {
				case(1):
					enemies.get(en).setDecimalRow(enemies.get(en).getDecimalRow() - (((double)enemies.get(en).getSpeed())/FPS));
					break;
				case(2):
					enemies.get(en).setDecimalRow(enemies.get(en).getDecimalRow() + (((double)enemies.get(en).getSpeed())/FPS));
					break;
				case(3):
					enemies.get(en).setDecimalCol(enemies.get(en).getDecimalCol() + (((double)enemies.get(en).getSpeed())/FPS));
					break;
				case(4):
					enemies.get(en).setDecimalCol(enemies.get(en).getDecimalCol() - (((double)enemies.get(en).getSpeed())/FPS));
					break;
				}	
			}
		}
		
		//Calls the paint method above
		repaint();
		
	}
	//Gives the tiles in a row (that you see) to other classes
	public static int getTilesPerRow() {
		return tilesPerRow;
	}
	
	//Map getters for other classes
	public MapCurrent getMapCurrent() {
		return (MapCurrent)maps.get(1);
	}
	
	public MapInteractables getMapInteractable() {
		return (MapInteractables)maps.get(4);
	}
	public MapItem getItemMap()
	{
		return (MapItem)maps.get(5);
	}
	
	public ArrayList<MapParentClass> getMaps()
	{
		return maps;
	}
}
