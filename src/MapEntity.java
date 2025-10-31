public class MapEntity extends MapParentClass{

	private Player player;
	private int[][] mapEntity; 
	
	
	//LITERALLY NOT USED AT ALL COMPLETELY USELESS!!!!!!!!!!!!!!!!!
	MapEntity(){
		
		
	}

	MapEntity(int[][] m){
		mapEntity = new int[m.length][m[0].length];
		super.setMap(mapEntity);
	}	
	
	public void setPlayer(Player p) {
		player = p;
		
	}
}
