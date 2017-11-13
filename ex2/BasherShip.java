/**
 * Represents a ship overrun with Reavers.
 * @author jherskow
 */
public class BasherShip extends SpaceShip {
	final double SHEILDDISTANCE = 0.19;

	/**
	 * This method provides the ship's behaviour:
	 * - Ram them like a united airlines passenger.
	 * @param Game The game object.
	 */
	public void doAction(SpaceWars Game){

		//raise shields if within bashing distance
		if (distanceToClosestShip(Game) < SHEILDDISTANCE) shieldOn();
		else shieldsUp = false;

		//move as towards closest ship
		int turn = (int) Math.signum(angleToClosestShip(Game));
		getPhysics().move(true,turn);

		rechargeEnergy();
	}
}
