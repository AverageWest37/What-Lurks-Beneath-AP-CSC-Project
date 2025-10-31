import java.util.ArrayList;

public class MapVisible extends MapParentClass{
	
	private int[][] mapVisible;
	private int[][] mapDarkness;
	private MapCurrent mapCurrent;
	private MapInteractables mapInteractable;
	private Player player;
	private int tilesPerRow;
	private int tilesPerSide;
	private int maxAngle = 360;
	ArrayList<Enemy> enemies;
	
	MapVisible(MapCurrent m, Player p, ArrayList<Enemy> e){
		
		
		mapCurrent = m;
		player = p;
		//21, not same as main panel tiles per row
		tilesPerRow = MainPanel.getTilesPerRow() + 2;
		//10
		tilesPerSide = tilesPerRow / 2;
		
		//Should be 21 x 21, and you only see 19 x 19. There should be a border of tiles you can't see
		mapVisible = new int[tilesPerRow][tilesPerRow];
		
		//Creates the array for darkness values, 0 is brightest and 9 is darkest
		mapDarkness = new int[tilesPerRow][tilesPerRow];
		
		enemies = e;
		
		updateMapVisible();
		
		super.setMap(mapVisible);
		
	}
	
	public void setMapInteractable(MapInteractables m) {
		mapInteractable = m;
	}
	
	public void updateMapVisible() {
		
		//constant that simply allows the mapVisible to start at 0 for the indexes
		int indexAtZeroRow =  tilesPerSide - player.getRow();
		int indexAtZeroCol =  tilesPerSide - player.getCol();
		int distance = 0;
		//updates the visible map you see to be 10 tiles on each side of the player, 9 shown
		for (int row = player.getRow() - tilesPerSide; row <= player.getRow() + tilesPerSide; row++) {
			for (int col = player.getCol() - tilesPerSide; col <= player.getCol() + tilesPerSide; col++) {
				try {
					//mapVisible should start at 0, go to 20 // mapCurrent is based on player position
					mapVisible[row + indexAtZeroRow][col + indexAtZeroCol] = mapCurrent.getMap()[row][col];
				}catch(Exception e) {
					mapVisible[row + indexAtZeroRow][col + indexAtZeroCol] = -1;
				}
				
			}			
		}
		

		
		//Resets darkness map
		mapDarkness = new int[tilesPerRow][tilesPerRow];
		
		//TEMPORARY, allows the inner loop to skip 1 to reduce loop times
		mapDarkness[tilesPerSide][tilesPerSide] = MainPanel.brightnesses.length - player.getSightDistance();
		
		
		//Sight line checker, will "draw lines" in 360 degrees and stop when hitting a wall
		//Goes through every single degree of a circle
		for (int angle = 0; angle < maxAngle; angle ++) {
			double rowMoveValue = Math.sin(Math.toRadians(angle));
			double colMoveValue = Math.cos(Math.toRadians(angle));   
			int brightnessInfluencer = 0;
			
			//Possibly start at 0 to reduce run times, or even 2, just move those up. Do math to increase max angle as distance increases? Or keep 0 so you can walk on corner tiles
			int rowCompensator = 0;
			int colCompensator = 0;
			if (Math.abs(rowMoveValue) == 1) {
				rowCompensator = -(int)rowMoveValue;
			}else if(Math.abs(colMoveValue) == 1) {
				colCompensator = -(int)colMoveValue;
			}
			
			outer:
				//Checks the tiles on the "lines" and if it hits a wall it stops
			for (int viewDistance = 1; viewDistance < player.getSightDistance(); viewDistance++) {
				int colViewPoint = (int)((viewDistance * colMoveValue)) + colCompensator;
				int rowViewPoint = (int)((viewDistance * rowMoveValue)) + rowCompensator;
				
				int prevBrightness = mapDarkness[tilesPerSide + rowViewPoint][tilesPerSide + colViewPoint];
				if (viewDistance + (MainPanel.brightnesses.length-1 - player.getSightDistance()) + brightnessInfluencer < 12) {
					
					mapDarkness[tilesPerSide + rowViewPoint][tilesPerSide + colViewPoint] = viewDistance + (MainPanel.brightnesses.length-1 - player.getSightDistance()) + brightnessInfluencer;
					if (!(prevBrightness == 0 || brightnessInfluencer == 0) && prevBrightness < mapDarkness[tilesPerSide + rowViewPoint][tilesPerSide + colViewPoint]) {
						mapDarkness[tilesPerSide + rowViewPoint][tilesPerSide + colViewPoint] = prevBrightness;
					}
				}
				else{
					mapDarkness[tilesPerSide + rowViewPoint][tilesPerSide + colViewPoint] = prevBrightness;
				}
				int tileIdentifier = mapVisible[tilesPerSide + rowViewPoint][tilesPerSide + colViewPoint];
				if (tileIdentifier == 1 || tileIdentifier == -1) {
					break outer;
					
					
					//Brightness influencer is affected by pipes and goop to reduce how far you can see
				}else if (tileIdentifier >= 2 && tileIdentifier <= 4) {
					brightnessInfluencer += 3;
				}
				try {
					int goopTileIdentifier = mapInteractable.getMap()[player.getRow() + rowViewPoint][player.getCol() + colViewPoint];
					
					if (goopTileIdentifier == 3) {
						brightnessInfluencer += 1;
					}
					else if (goopTileIdentifier == 2) {
						brightnessInfluencer += 1;
					}
					else if (goopTileIdentifier == 1) {
						brightnessInfluencer += 2;
					}
				}catch(Exception u) {
					
				}
			}
		}
		
		//Will update the darkness map for the angler fish to make it light up
		for (int e = 0; e < enemies.size(); e++) {
			if (enemies.get(e).getID() == 6 && mapInteractable.getMap()[enemies.get(e).getRow()][enemies.get(e).getCol()] != 1 && mapInteractable.getMap()[enemies.get(e).getRow()][enemies.get(e).getCol()] != 2) {
				try {
					
					if (mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] > 4) {
						mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] = mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] - 4;
					}else if (mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] != 0){
						mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] = mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] - (mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] - 1);
					}else{
						mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] = MainPanel.brightnesses.length - 4;
					}
				}catch(Exception s){
					//mapDarkness[tilesPerSide + (enemies.get(e).getRow() - player.getRow())][tilesPerSide + (enemies.get(e).getCol() - player.getCol())] = 1;
				}
			}
		}
	}
	
	public int[][] getMapDarkness(){
		return mapDarkness;
	}
}
