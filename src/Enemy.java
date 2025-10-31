import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Enemy extends Entity{
	private int identity;
	
	// {{row, col, F cost, G cost}, {row, col, F cost, G cost}}  add on prev row and prev col?

	private ArrayList<int[]> path;
	private int pathTileOn = 0;
	private boolean pathFound = false;
	private int randomPathCounter;
	private int randomTime;
	
	private int[][][] pathFindingMap;
	
	private int[][] directionCheckers = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	
	private int sightDistance;
	
	//3rd array: {H cost, G cost, F cost, prevTileRow, prevTileCol, ID}
	private static MapCurrent mapCurrent;
	private static Player player;
	
	private int damage;
	private int runTimer;
	
	Enemy(){
		
	}
	Enemy(int row, int col, double s, int sD, int d, BufferedImage b, int i){
		super(row, col, s, b);
		sightDistance = sD;
		identity = i;
		damage = d;
		pathFindingMap = new int[mapCurrent.getMap().length][mapCurrent.getMap()[0].length][6];
		randomTime = (int)(Math.random()*10);
	}
	
	public int getDamage() {
		return damage;
	}
	public int getID() {
		return identity;
	}
	public static void setMapCurrent(MapCurrent c) {
		mapCurrent = c;

	}
	public static void setPlayer(Player p) {
		player = p;
	}
	public boolean getPathFound() {
		return pathFound;
	}
	public int getPathCounter() {
		return randomPathCounter;	
	}
	public void setPathCounter(int c) {
		randomPathCounter = c;	
	}
	public int getRandomTime() {
		return randomTime;	
	}
	public void setRandomTime(int s) {
		randomTime = s;	
	}
	public int getRunTimer() {
		return runTimer;	
	}
	public void setRunTimer(int t) {
		runTimer = t;	
	}
	
	//Follows the path you found from the path finding
	public void followPath() {
		
		//System.out.println(pathTileOn + "---" + path.size());
		
		
		if (pathTileOn == path.size()) {
			//System.out.println("***");
			pathFound = false;
			pathTileOn = 0;
			
			super.setSpeed(super.getSpeedOG());
			runTimer = 0;
			super.setDirection(0);
		}else {
			int[] currentBack = path.get(pathTileOn);
			
			pathTileOn++;
			if(currentBack[0] < currentBack[2]) {
				super.setDirection(1);
			}else if(currentBack[0] > currentBack[2]) {
				super.setDirection(2);
			}else if(currentBack[1] > currentBack[3]) {
				super.setDirection(3);
			}else if(currentBack[1] < currentBack[3]) {
				super.setDirection(4);
			}
			
			randomPathCounter = 0;
		}
	}
	
	
	
	
	//Like the player, it has sight lines in a 360 view to try and find the player. If player found, path find to get path to player
	public void sightLineChecker() {
		outerer:
		for (int angle = 0; angle < 360; angle += 2) {
			double rowMoveValue = Math.sin(Math.toRadians(angle));
			double colMoveValue = Math.cos(Math.toRadians(angle));   
			
			//Possibly start at 0 to reduce run times, or even 2, just move those up. Do math to increase max angle as distance increases? Or keep 0 so you can walk on corner tiles
			
			outer:
			for (int viewDistance = 1; viewDistance < sightDistance; viewDistance++) {
				int colViewPoint = (int)((viewDistance * colMoveValue));
				int rowViewPoint = (int)((viewDistance * rowMoveValue));
				
				if (super.getRow() + rowViewPoint == player.getRow() && super.getCol() + colViewPoint == player.getCol()  && !player.getHiding()) {
					pathFinding(super.getRow(), super.getCol(), player.getRow(), player.getCol());
					if (identity != 4) {
						super.setSpeed(super.getSpeedOG() * 2);
					}else if (runTimer != -1){
						runTimer++;
					}
					
					break outerer;
				}
				try {
					int tileIdentifier = mapCurrent.getMap()[super.getRow() + rowViewPoint][super.getCol() + colViewPoint];
					if (tileIdentifier >= 1 && tileIdentifier <= 4) {
						break outer;
					}
				}catch(Exception x) {
					break outer;
				}	
			}
		}
	}
	
	
	//H cost: Distance from a tile to the goal
	//G cost: Distance from a tile to the start (lie?), also additive cost of a path G cost. Cost of moving to a tile
	//F score: G + H together
	
	//G score total adds as you move from tile to tile
	
	//Recalculate H and F costs when checking tile closer
	
	//return direction? or set direction
	//				0		1		2 			3			4		5
	//3rd array: {H cost, G cost, F cost, prevTileRow, prevTileCol, ID}
	
	//Uses A* path finding algorithm to generate a path that will reach the set goal
	public void pathFinding(int startRow, int startCol, int endRow, int endCol) {
		//resets the map again to re do path finding
		pathFindingMap = new int[mapCurrent.getMap().length][mapCurrent.getMap()[0].length][6];
		path = new ArrayList<int[]>();
		pathFound = false;
		pathTileOn = 0;
		//0 empty, 1 open, 2 closed, 3 start, 4 end
										//current tile position 6   7			
		int[] current = {0, 0, 0, startRow, startCol, 3, startRow, startCol};
		int[]tempStart = {0, 0, 0, startRow, startCol, 3};

		pathFindingMap[startRow][startCol] = tempStart;
		
		//Main path finding loop, will run until it reaches the end tile
		while(current[6] != endRow || current[7] != endCol) {
			
			//Loop that puts surrounding tiles to be open, and checks if new open is less F cost
			for (int o = 0; o < 4; o++) {
				int rowCheckValue = directionCheckers[o][0];
				int colCheckValue = directionCheckers[o][1];

				//Position to check if the spot is open
				int tempRow = current[6] + rowCheckValue;
				int tempCol = current[7] + colCheckValue;
				try {
					if (pathFindingMap[tempRow][tempCol][5] == 0 && !(mapCurrent.getMap()[tempRow][tempCol] >= 1 && mapCurrent.getMap()[tempRow][tempCol] <= 4)) {
						int hCost = getHcost(tempRow, tempCol, endRow, endCol);
						int gCost = current[1] + 10;
						
						int[] tileData = {hCost, gCost, hCost + gCost, current[6], current[7], 1};
						pathFindingMap[tempRow][tempCol] = tileData;
						
								
					}else if(pathFindingMap[tempRow][tempCol][5] == 1) {
						int hCost = getHcost(tempRow, tempCol, endRow, endCol);
						int gCost = current[1] + 10;
						int fCost = hCost + gCost;
						if (fCost < pathFindingMap[tempRow][tempCol][2]) {
							int[] tileData = {hCost, gCost, fCost, current[6], current[7], 1};
							pathFindingMap[tempRow][tempCol] = tileData;
						}				
					}
				}catch (Exception e) {
					
				}
			}
			
			//Finds the lowest fCost tile that is open and sets that to current
			int lowestFcostRow = 0;
			int lowestFcostCol = 0;
			int lowestFcost = 9999999;
			
			for (int r = 0; r < pathFindingMap.length; r++) {
				for (int c = 0; c < pathFindingMap[0].length; c++) {
					//Checks if spot is an open spot, then finds lowest f cost and sets position
					if (pathFindingMap[r][c][5] == 1 && pathFindingMap[r][c][2] < lowestFcost) {
						lowestFcost = pathFindingMap[r][c][2];
						lowestFcostRow = r;
						lowestFcostCol = c;
					}
				}
			}
			//Puts the found tile into the closed list and sets it to current
			pathFindingMap[lowestFcostRow][lowestFcostCol][5] = 2;
			int hCost = pathFindingMap[lowestFcostRow][lowestFcostCol][0];
			int gCost = pathFindingMap[lowestFcostRow][lowestFcostCol][1];
			current = new int[]{hCost, gCost, hCost + gCost,  pathFindingMap[lowestFcostRow][lowestFcostCol][3],  pathFindingMap[lowestFcostRow][lowestFcostCol][4], 2, lowestFcostRow, lowestFcostCol};
		}
		
		int[] currentBack = {current[6], current[7], current[3], current[4]};
		
		path.add(currentBack);
		
		//Goes back through the past closed tiles and goes back and adds each previous closed tile to get a path
		while (!pathFound) {
			int row = currentBack[0];
			int col = currentBack[1];

			if (pathFindingMap[row][col][3] == startRow && pathFindingMap[row][col][4] == startCol) {
				pathFound  = true;
				break;
				
			}
			currentBack = new int[] {currentBack[2], currentBack[3], pathFindingMap[currentBack[2]][currentBack[3]][3], pathFindingMap[currentBack[2]][currentBack[3]][4]};
			path.add(0, currentBack);
		}
	}
	
	private int getHcost(int row, int col, int endRow, int endCol) {
		int hCost = (int)((Math.sqrt((Math.pow(row - endRow,  2) + Math.pow(col - endCol, 2)))) * 10);
		return hCost;
	}
	
	
	
	/*
	public void pathFinding(int startRow, int startCol, int endRow, int endCol) {
		int[] current = new int[4];
		int closedIndexCounter = 0;
		
		
		//Temporary Idea:make a crap "path finding" that just moves towards the player only based on position
		while(current[0] != endRow && current[1] != endCol) {
			//find the lowest F cost tile in the open list
			int lowestFcostTile = 0;
			for (int c = 0; c < openList.size(); c++) {
				if ((openList.get(lowestFcostTile))[2] > openList.get(c)[2]){
					lowestFcostTile = c;
				}
			}
			//Can't use lowest FcostTile after this
			current = openList.get(lowestFcostTile);
			openList2D[openList.get(lowestFcostTile)[0]][openList.get(lowestFcostTile)[1]] = 0;
			closedList.add(openList.remove(lowestFcostTile));
			closedIndexCounter++;
			closedList2D[closedList.getLast()[0]][closedList.getLast()[1]] = closedIndexCounter;
			
			
			for (int o = 0; o < 4; o++) {
				int rowCheck = directionCheckers[o][0];
				int colCheck = directionCheckers[o][1];
				try {
					//Find a way to check if a spot is in the closed list
					int tempRow = current[0] + rowCheck;
					int tempCol = current[1] + colCheck;
					if (!(mapCurrent.getMap()[tempRow][tempCol] >= 2 && mapCurrent.getMap()[tempRow][tempCol] <= 5) || closedList2D[tempRow][tempCol] == 0) {
						if (openList2D[tempRow][tempCol] != 0) {
							if (getFcost(tempRow, tempCol, current[3]) < openList2D[tempRow][tempCol]) {
								openList2D[tempRow][tempCol] = getFcost(tempRow, tempCol, current[3]);
								for (int u = 0; u < openList.size(); u++){
									if (openList.get(u)[0] == tempRow && openList.get(u)[1] == tempCol) {
										openList.get(u)[2] = getFcost(tempRow, tempCol, current[3]);
										openList.get(u)[3] = current[3] + 10;
									}
								}
							}
						}else {
							openList2D[tempRow][tempCol] = 1;
							int[] temp = {tempRow, tempCol, getFcost(tempRow, tempCol, current[3]), current[3] + 10};
							openList.add(temp);
						}

					}					
				}catch(Exception e) {
					
				}
			}	
		}	
	}*/
	

	

}
