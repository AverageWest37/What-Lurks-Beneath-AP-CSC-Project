import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Entity {
	
	private double speed;
	private int rowPos;
	private int colPos;
	private int prevRow;
	private int prevCol;
	//0 None, 1 North, 2 South, 3 East, 4 West
	private int direction;
	private double speedOG;
	
	private double rowDecPos;
	private double colDecPos;
	
	public static MapEntity mapEntity;
	
	private RescaleOp brightness;
	private BufferedImage sprite;
	private BufferedImage originalSprite;
	
	public AffineTransform rotation;
	private int angle;
	
	private int flipTimer;
	private int  flip;
	
	Entity(){
		speed = 1.8;
		rowPos = 11;
		colPos = 11;
		prevRow = rowPos;
		prevCol = colPos;
		
		rowDecPos = rowPos;
		colDecPos = colPos;
		
		direction = 0;
		speedOG = speed;
		
		rotation = new AffineTransform();
		
		flipTimer = 0;
		flip = 1;
	}
	
	Entity(int row, int col, double s, BufferedImage b){
		speed = s;
		rowPos = row;
		colPos = col;
		prevRow = rowPos;
		prevCol = colPos;
		
		rowDecPos = rowPos;
		colDecPos = colPos;
		
		direction = 0;
		speedOG = speed;
		
		originalSprite = b;
		sprite = b;
		
		rotation = new AffineTransform();
		
		flipTimer = 0;
		flip = 1;
	}	
	
	//BASIC ACCESORS
	public int getRow() {
		return rowPos;
	}
	public int getCol() {
		return colPos;
	}	
	public int getPrevRow() {
		return prevRow;
	}
	public int getPrevCol() {
		return prevCol;
	}		
	public double getSpeed() {
		return speed;
	}
	public int getDirection() {
		return direction;
	}	

	//BASIC MUTATORS
	public void setRow(int r) {
		rowPos = r;
	}
	public void setCol(int c) {
		colPos = c;
	}	
	public void setPrevRow(int r) {
		prevRow = r;
	}
	public void setPrevCol(int c) {
		prevCol = c;
	}		
	public void setSpeed(double s) {
		speed = s;
	}	

	public void setDirection(int d) {
		direction = d;
	}
	
	
	//DECIMAL ACCESORS AND MUTATORS
	public double getDecimalCol() {
		return colDecPos;
	}	
	public void setDecimalCol(double r) {
		colDecPos = r;
	}		
	
	public double getDecimalRow() {
		return rowDecPos;
	}	
	public void setDecimalRow(double r) {
		rowDecPos = r;
	}	
	
	public double getSpeedOG() {
		return speedOG;
	}
	
	//BRIGHTNESSES AND SPRITES
	public RescaleOp getBrightness() {
		return brightness;
	}
	public void setBrightness(float b) {
		brightness = new RescaleOp(b, 0, null);
	}	
	
	public BufferedImage getSprite() {
		return sprite;
	}
	
	public void setSprite(BufferedImage i) {
		sprite = brightness.filter(i, null);
	}
	//A base for the darkening so it does not get darker and darker
	public BufferedImage getOriginalSprite() {
		return originalSprite;
	}
	public void setOriginalSprite(BufferedImage b) {
		originalSprite = b;
		setSprite(originalSprite);
	}
	
	//SPRITE ROTATIONS AND ANIMATIONS
	public AffineTransform getRotation() {
		return rotation;
	}
	public void setRotation() {
		rotation.rotate(Math.toRadians(angle), sprite.getWidth() / 2, sprite.getHeight() / 2);	
	}	
	public int getAngle() {
		return angle;
	}
	public void setAngle(int a) {
		angle = a;
	}
	public int getFlipTimer() {
		return flipTimer;
	}	
	public void setFlipTimer(int f) {
		flipTimer = f;
	}
	public int getFlip() {
		return flip;
	}	
	public void setFlip(int h) {
		flip = h;
	}
	
	//MAP STUFF
	public static void setMapEntity(MapEntity m) {
		mapEntity = m;
	}
	public void updateMapEntity(int prevRow, int prevCol, int row, int col, int identity){
		mapEntity.getMap()[prevRow][prevCol] = 0;
		mapEntity.getMap()[row][col] = identity;
	}	
}
