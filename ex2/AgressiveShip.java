/**
 * Represents a Klingon Warbird.
 * - The best defence is a constant and unyielding offense.
 * @author jherskow
 */
public class AgressiveShip extends SpaceShip {
	final double ATTACKANGLE = 0.21;

	/**
	 * This method provides the ship's behaviour:
	 * - Seek and destroy the nearest spaceship.
	 * @param Game The game object.
	 */
	public void doAction(SpaceWars Game){
		//fire if ship in firing cone
		if (angleToClosestShip(Game) < ATTACKANGLE) fire(Game);

		//move towards closest ship.
		int turn = (int) Math.signum(angleToClosestShip(Game));
		getPhysics().move(true,turn);

		rechargeEnergy();
	}
}
