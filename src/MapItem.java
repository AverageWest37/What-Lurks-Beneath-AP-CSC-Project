

public class MapItem extends MapParentClass{
	private int[][] mapItem;
	
	//Constructor for item map
	MapItem(int[][] m)
	{
		super();
		mapItem = new int[m.length][m[0].length];
		super.setMap(mapItem);
	}
	//Mutators
	
	//Sets the item on the map
	public void setItem(Item item)
	{
		mapItem[item.getRow()][item.getCol()] = item.getitemID();
	}

	
	//Accessors
	
	//Returns map of items
	public int[][] getmapItem()
	{
		return mapItem;
	}
	
	//Returns the item on the map
	public int getItem(int r, int c)
	{
		return mapItem[r][c];
	}
	
	//will remove an item from the map
	public void removeItem(int r, int c)
	{
		mapItem[r][c] = 0;
	}
}
