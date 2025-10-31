



//Imports
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.util.ArrayList;


//BENNY CODE *******BEWARE********

//Item Class
public class Item {
	
	//Instance Variables
	private int itemID;
	private int trinketCol;
	private int trinketRow;
	
	private int[][] imap;
	
	private static Item hotbarItem;
	private boolean inHotBar;
	
	private boolean used;
	private static boolean hotBarSelected;
	
	private int ptrinketCol;
	private int ptrinketRow;
	
	
	//Item ID constructor
	public Item (int itemID, int r, int c) {
		
		this.itemID = itemID;
		
		trinketCol = c;
		trinketRow = r;
		
		ptrinketCol = c;
		ptrinketRow = r;
		
		inHotBar = false;
		used = false;
	}

	//Accessor methods
	
	//Returns the item in hotbar
	public static Item getHotBarItem()
	{
		return hotbarItem;
	}
	
	public boolean inHotBar()
	{
		return inHotBar;
	}
		
	//Returns Item ID, each id means different item
	public int getitemID()
	{
		return itemID;
	}
	
	//Gets the row of each item
	public int getRow()
	{
		return trinketRow;
	}
	
	public boolean getUsed()
	{
		return used;
	}
	
	//Gets the Col of the items
	public int getCol()
	{
		return trinketCol;
	}
	
	//Gets the map of all items
	public int[][] getMap()
	{
		return imap;
	}
	
	//Returns item that is selected
	public boolean getSelectedHotBar() throws NullPointerException
	{
		if (hotbarItem.equals(this)) {
			return hotBarSelected;
		}
		return false;
	}
	
	//Returns the previous things
	public int getptrinketCol()
	{
		return ptrinketCol;
	}
	
	public int getptrinketRow()
	{
		return ptrinketRow;
	}
	
	//Mutator methods
	
	//allows to drop item
	public void placeDown()
	{
		hotBarSelected = false;
		inHotBar = false;
	}
	
	//Increments Row by 1
	public void incrementRow()
	{
		trinketRow += 1;
	}
	
	//Increments Col by 1
	public void incrementCol()
	{
		trinketCol += 1;
	}
	
	//deIncrements Row by 1
	public void deincrementRow()
	{
		trinketRow -= 1;
	}
		
	//deIncrements Col by 1
	public void deincrementCol()
	{
		trinketCol -= 1;
	}
	
	//Accesses previous col and row to reset it
	public void ptrinketRow()
	{
		trinketRow = ptrinketCol;
	}
	
	public void ptrinketCol()
	{
		trinketCol = ptrinketCol;
	}
	
	//Will update pttrinket
	public void updateptrinketRow(int i)
	{
		ptrinketRow = i;
	}
	
	public void updateptrinketCol(int i)
	{
		ptrinketCol = i;
	}

	//Uses item equipped in hotbar
	public void useItem()
	{
		inHotBar = false;
		used = true;
		hotBarSelected = false;
		hotbarItem = null;
	}
	
	public void useLantern()
	{
		used = true;
	}

	//Will set an item into your hotbar
	public void setHotBar(int i)
	{
		if (i == 1)
		{
			hotbarItem = this;
			inHotBar = true;
		}
	}
	
	//Selects item in hotbar
	public static void selectHotBar()
	{
		hotBarSelected = true;
	}
	
	//resets x and y to position after dropping item
	public void setXY(int x, int y)
	{
		trinketRow = y;
		trinketCol = x;
	}
	
}
