/**
 * Represents a vessel belonging to the United Federation of Planets.
 * From the popular sci-fi franchise STAR TREK.
 *
 * In accordance with the PRIME DIRECTIVE, this vessel will avoid fire,
 * and attempt to avoid other vessels.
 *
 * https://www.wikiwand.com/en/Prime_Directive
 *
 * However, if the ship is fired upon, it will got to RED ALERT
 * and engage all ships in the area.
 *
 * Additionally, the ship will attempt to raise shields if it is in the firing
 * cone of the nearest ship.
 *
 * @author jherskow
 */
public class FederationShip extends SpaceShip {
	final double FEARDISTANCE = 0.25;
	final double FEARANGLE = 0.23;
	final double ATTACKANGLE = 0.21;
	private boolean redAlert = false;
	/**
	 * This method provides the ship's behaviour:
	 * Follow the PRIME DIRECTIVE - Respond only if directly attacked.
	 * @param Game The game object.
	 */
	public void doAction(SpaceWars Game){
		// raise shields if in attack cone
		if (distanceToClosestShip(Game) < FEARDISTANCE && angleFromClosestShip(Game) < FEARANGLE){
			shieldOn();
		}else shieldsUp = false;

		// "It would seem we have no choice - Mr. Worf, Take us to Red Alert".
		if (currentHealth < STARTINGHEALTH) redAlert = true;
		else redAlert = false;

		if (!redAlert){
			getPhysics().move(false,0);
			rechargeEnergy();
		}else{
			//fire if ship in firing cone
			if (angleToClosestShip(Game) < ATTACKANGLE) fire(Game);

			//move as per description.
			int turn = (int) Math.signum(angleToClosestShip(Game));
			getPhysics().move(true,turn);

			rechargeEnergy();
		}
	}
}
