

public class MapParentClass {
	
	int[][] map;
	
	
	//Basic map gets and sets for all the other maps
	MapParentClass(){
		
	}

	MapParentClass(int[][] m){
		map = m;
	}	
	
	public void setMap(int[][] m) {
		map = m;
		
	}
	
	public int[][] getMap(){
		return map;
	}
	
	
	
}
