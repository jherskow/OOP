import java.awt.Image;
import oop.ex2.*;

/**
 * The API spaceships need to implement for the SpaceWars game. 
 * It is your decision whether SpaceShip.java will be an interface, an abstract class,
 *  a base class for the other spaceships or any other option you will choose.
 *  
 * @author jherskow
 */
public abstract class SpaceShip{
	protected static final int STARTINGHEALTH = 25;
	protected static final int STARTINGMAXENERGYLEVEL = 250;
	protected static final int STARTINGENERGYLEVEL = 200;

	protected static final int TELEPORTCOST = 100;
	protected static final int SHIELDCOST = 3;
	protected static final int HITMAXCOST = 5;
	protected static final int HITENERGYCOST = 10;
	protected static final int SHEILDHITCOST = 2;
	protected static final int FIRECOST = 15;
	protected static final int BASHBONUS = 20;
	protected static final int BASHCOST = 15;

	protected static final int SHOTDAMAGE = 1;
	protected static final int COLLISIONDAMAGE = 1;

	protected static final int DEADHEALTH = 0;

	protected static final int GUNDELAY = 7;


	protected int currentHealth;
	protected int gunTimer;
	protected int currentEnergyLevel;
	protected int maxEnergyLevel;
	protected boolean shieldsUp;
	protected SpaceShipPhysics physics;

	/**
	 *
	 */
	public SpaceShip(){
		this.currentHealth = STARTINGHEALTH;
		this.currentEnergyLevel = STARTINGENERGYLEVEL;
		this.maxEnergyLevel = STARTINGMAXENERGYLEVEL;
		this.shieldsUp = false;
		this.physics = new SpaceShipPhysics();
		this.gunTimer = 0;
	}


	/**
	 * Does the actions of this ship for this round.
	 * This is called once per round by the SpaceWars game driver.
	 *
	 * Note - This method should include a call to rechargeEnergy() when
	 * implemented in a subclass.
	 *
	 * @param game the game object to which this ship belongs.
	*/
	public abstract void doAction(SpaceWars game);

	/**
	 * Recharges energy for each turn, as specified.
	 * Also checks the ships Firing timer.
	 * Called by each subclasses' doAction.
	 */
	protected void rechargeEnergy(){
		if (shieldsUp) currentEnergyLevel -= SHIELDCOST;

		double maxVelocity = physics.getMaxVelocity();
		double currentVelocity = physics.getVelocity();
		this.currentEnergyLevel += (Math.floor(2*(currentVelocity/maxVelocity))+1);

		if (currentEnergyLevel < 0) currentEnergyLevel = 0;
		if (currentEnergyLevel > maxEnergyLevel) currentEnergyLevel = maxEnergyLevel;
		if (gunTimer != 0) gunTimer--;
	}

	/**
	 * This method is called every time a collision with this ship occurs
	 */
	public void collidedWithAnotherShip() {
		if (!shieldsUp || currentEnergyLevel < SHEILDHITCOST) {
			maxEnergyLevel -= HITMAXCOST;
			currentEnergyLevel -= HITENERGYCOST;
			currentHealth -= COLLISIONDAMAGE;
		}else{
			currentEnergyLevel -= BASHCOST;
			maxEnergyLevel += BASHBONUS;
		}
	}

	/**
	 * This method is called whenever a ship has died. It resets the ship's
	 * attributes, and starts it at a new random position.
	 */
	public void reset(){
		currentHealth = STARTINGHEALTH;
		currentEnergyLevel = STARTINGENERGYLEVEL;
		maxEnergyLevel = STARTINGMAXENERGYLEVEL;
		shieldsUp = false;
		physics = new SpaceShipPhysics();
	}

	/**
	 * Checks if this ship is dead.
	 *
	 * @return true if the ship is dead. false otherwise.
	 */
	public boolean isDead() {
	    return (currentHealth == DEADHEALTH);
	}

	/**
	 * Gets the physics object that controls this ship.
	 *
	 * @return the physics object that controls the ship.
	 */
	public SpaceShipPhysics getPhysics() {
	    return this.physics;
	}

	/**
	 * This method is called by the SpaceWars game object when ever this ship
	 * gets shot at (with or without a shield).
	 */
	public void gotShot() {
		if (!shieldsUp || currentEnergyLevel <SHEILDHITCOST){
			maxEnergyLevel -= HITMAXCOST;
			currentEnergyLevel -= HITENERGYCOST;
			currentHealth -= SHOTDAMAGE;
		}else{
			currentEnergyLevel -= SHEILDHITCOST;
		}
	}

	/**
	 * Gets the image of this ship. This method should return the image of the
	 * ship with or without the shield. This will be displayed on the GUI at
	 * the end of the round.
	 *
	 * @return the image of this ship.
	 */
	public Image getImage(){
		return shieldsUp ? GameGUI.ENEMY_SPACESHIP_IMAGE_SHIELD
				: GameGUI.ENEMY_SPACESHIP_IMAGE;
	}

	/**
	 * Attempts to fire a shot.
	 *
	 * @param game the game object.
	 */
	public void fire(SpaceWars game) {
		if (currentEnergyLevel >= FIRECOST && gunTimer == 0) {
			game.addShot(this.getPhysics());
			currentEnergyLevel -= FIRECOST;
			gunTimer = GUNDELAY;
		}
	}

	/**
	 * Attempts to turn on the shield.
	 */
	public void shieldOn() {
		if (!shieldsUp && currentEnergyLevel >= SHIELDCOST){
			shieldsUp = true;
			currentEnergyLevel -= SHIELDCOST;
		}else if (shieldsUp && currentEnergyLevel >= SHIELDCOST){
			currentEnergyLevel -= SHIELDCOST;
		}else{
			shieldsUp = false;
		}
	}

	/**
	 * Attempts to teleport.
	 */
	public void teleport() {
		if (currentEnergyLevel >= TELEPORTCOST){
			this.physics = new SpaceShipPhysics();
			currentEnergyLevel -= TELEPORTCOST;
		}
	}

// Methods available for creating different ship behaviours:

	/**
	 * Checks the angle off the nearest ship's bow towards this ship.
	 * @param Game The game object.
	 * @return the angle the closest ship needs to have this ship cleanly in sights.
	 */
	protected double angleFromClosestShip(SpaceWars Game){
		return Game.getClosestShipTo(this).getPhysics().angleTo(this.physics);
	}

	/**
	 * Checks the angle off this ships bow to the closest ship.
	 * @param Game The game object.
	 * @return The angle this ship needs to have the closest ship cleanly in sights.
	 */
	protected double angleToClosestShip(SpaceWars Game){
		return this.physics.angleTo(Game.getClosestShipTo(this).getPhysics());
	}

	/**
	 * Checks the distance to the closest ship.
	 * @param Game The game object.
	 * @return The distance to the closest ship.
	 */
	protected double distanceToClosestShip(SpaceWars Game){
		return Game.getClosestShipTo(this).getPhysics().distanceFrom(this.physics);
	}
}
