

public class Player extends Entity{
	//The direction where you currently want to move
	private int nextDirection;
	private int sightDistance;
	private boolean isRunning;
	private int energy;
	private int energyMax;
	private int energyCount;
	
	private int health;
	private int healthMax;
	private int invincibleCounter;
	private double speedMult;
	
	private int seeValve;
	private boolean isHiding;
	
	Player(){
		
		nextDirection = 0;
		//Max is 11, 8 default
		sightDistance = 8;
		isRunning = false;
		energy = 15;
		energyMax = energy;
		energyCount = 0;
		
		health = 15;
		healthMax = health;
		invincibleCounter = 0;
		speedMult = 0;
		
		seeValve = -1;
		isHiding = false;
		
	}
	
	public int getNextDirection() {
		return nextDirection;
	}	
	public void setNextDirection(int d) {
		nextDirection = d;
	}	
	
	public int getSightDistance() {
		return sightDistance;
	}	
	public void setSightDistance(int d) {
		sightDistance = d;
	}	
	
	public boolean getRunning() {
		return isRunning;
	}	
	public void setRunning(boolean r) {
		isRunning = r;
	}
	
	//Energy stuff
	public int getEnergy() {
		return energy;
	}	
	public void setEnergy(int e) {
		energy = e;
	}
	public int getEnergyMax() {
		return energyMax;
	}	
	public void setEnergyMax(int m) {
		energyMax = m;
		
	}	
	
	public int getEnergyCount() {
		return energyCount;
	}	
	public void setEnergyCount(int e) {
		energyCount = e;
	}	

	//Health stuff
	public int getHealth() {
		return health;
	}	
	public void setHealth(int h) {
		health = h;
	}
	public int getInvincibilityCounter() {
		return invincibleCounter;
	}	
	public void setInvincibilityCounter(int i) {
		invincibleCounter = i;
	}
	
	public int getHealthMax() {
		return healthMax;
	}

	public double getSpeedMult() {
		return speedMult;
	}
	public void setSpeedMult(double d) {
		speedMult = d;
		
	}

	//Valve hint ability
	public void setValveHint(int b) {
		seeValve = b;
		
	}
	public int getValveHint() {
		return seeValve;
	}

	//Hiding
	public void setHiding(boolean b) {
		isHiding = b;
		
	}
	public boolean getHiding() {
		return isHiding;
	}
	
	
}
