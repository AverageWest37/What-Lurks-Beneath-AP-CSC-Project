public class MapCurrent extends MapParentClass{
	

	private int[][] mapCurrent;
	
	MapCurrent(int[][] m){
		
		mapCurrent = new int[m.length][m[0].length];
		
		//Copies the constant map
		for (int row = 0; row < m.length; row++) {
			for (int col = 0; col < m[0].length; col++) {
				mapCurrent[row][col] = m[row][col];
			}
		}
		
		super.setMap(mapCurrent);
	}
	
	//Changes all trough tiles to water tiles
	public void increaseWaterLevel() {
		for (int row = 0; row < mapCurrent.length; row++) {
			for (int col = 0; col < mapCurrent[0].length; col++) {	
				//VERY RUDAMENTARY
				if (mapCurrent[row][col] >= 5 && mapCurrent[row][col] <= 7) {
					mapCurrent[row][col] += 3;
					
				}
				else if(mapCurrent[row][col] == 11 || mapCurrent[row][col] == 12) {
					mapCurrent[row][col] += 3;
				}
			}			
		}
	}
}
