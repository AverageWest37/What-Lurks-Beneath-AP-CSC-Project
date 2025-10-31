

public class MapInteractables extends MapParentClass{
	private MapCurrent mapCurrent;
	private int[][] mapInteractables; 
	private int[][] directionCheckers = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
	
	private int numValves;
	private int numValvesDone;
	private int numBoxes;

	
	MapInteractables(){
		
		
	}

	MapInteractables(MapCurrent m, double v){
		mapCurrent = m;
		mapInteractables = new int[m.getMap().length][m.getMap()[0].length];
		numValves = (int)(6 * v);
		numBoxes = (int)(7 * (v / 2));
		super.setMap(mapInteractables);
		addGoop();
		addObjectives();
	}	
	
	public void setValvesDone(int v) {
		numValvesDone = v;
	}
	
	public int getValvesDone() {
		return numValvesDone;
	}
	
	public int getValves() {
		return numValves;
	}
	
	private void addObjectives() {
		
		int randomRowOG = 0;
		int randomColOG = 0;
		
		//Adds the interactable stuff, checks if spot is okay and if it finds a good spot it sets the position
		for (int q = 0; q < numValves + 1 + numBoxes; q++) {
			int tileIdentity = -1;
			int interactableIdentity = -1;
			while(tileIdentity != 0 || interactableIdentity != 0) {
				randomRowOG = (int)(Math.random()*mapCurrent.getMap().length);
				randomColOG = (int)(Math.random()*mapCurrent.getMap()[0].length);
				tileIdentity = mapCurrent.getMap()[randomRowOG][randomColOG];
				interactableIdentity = mapInteractables[randomRowOG][randomColOG];
			}
			if (q > numValves) {
				mapInteractables[randomRowOG][randomColOG] = 4;
			}else if (q == numValves) {
				mapInteractables[randomRowOG][randomColOG] = 7;
			}else {
				mapInteractables[randomRowOG][randomColOG] = 5;
			}
			
		}
		/*
		for (int i = 0; i < mapInteractables.length; i++) {
			for (int f = 0; f < mapInteractables[0].length; f++) {
				System.out.print(mapInteractables[i][f] + ", ");
			}
			System.out.println();
		}
		System.out.println();
		*/
	}
	
	
	private void addGoop() {

		//Adds the dark goop, checks if spot is okay and adds the first "seed"
		for (int i = 0; i < (int)(2 * (Math.pow(1.2, MainPanel.difficulty))); i++) {
			int tileIdentity = -1;
			int randomRowOG = 0;
			int randomColOG = 0;
			while(tileIdentity != 0) {
				randomRowOG = (int)(Math.random()*mapCurrent.getMap().length);
				randomColOG = (int)(Math.random()*mapCurrent.getMap()[0].length);
				tileIdentity = mapCurrent.getMap()[randomRowOG][randomColOG];

			}
			

			//Algorithm for spreading the goop. It will randomly stop spreading.
			//Adds "seed"
			mapInteractables[randomRowOG][randomColOG] = 1;
			for (int q = 0; q < 4; q++) {
				
				int randomRow = randomRowOG + directionCheckers[q][0];
				int randomCol = randomColOG + directionCheckers[q][1];
				
				int spreadChance = 1;
				while(spreadChance != 0) {
					for (int o = 0; o < 4; o++) {
						try {
							int rowCheckValue = directionCheckers[o][0];
							int colCheckValue = directionCheckers[o][1];
							tileIdentity = mapCurrent.getMap()[randomRow + rowCheckValue][randomCol + colCheckValue];
							int intensityChance = (int)(Math.random() * 2) + mapInteractables[randomRow][randomCol];
							if (intensityChance == 3) {
								int chanceZero = (int)(Math.random() * 15);
								if (chanceZero == 0) {
									intensityChance = 0;
								}
							}
							
							if (tileIdentity == 0 && intensityChance <= 3 && (mapInteractables[randomRow + rowCheckValue][randomCol + colCheckValue] == 0)) {
								mapInteractables[randomRow + rowCheckValue][randomCol + colCheckValue] = intensityChance;
								randomRow = randomRow + rowCheckValue;
								randomCol = randomCol + colCheckValue;
							}
						}catch(Exception g) {
							
						}
					}
					spreadChance = (int)(Math.random()*25);
				}
			}
		}	
	}
}
