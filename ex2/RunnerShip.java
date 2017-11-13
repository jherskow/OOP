/**
 * Represents a spaceship controlled by WWII era France.
 *
 * @author jherskow
 */
public class RunnerShip extends SpaceShip {
	final double FEARDISTANCE = 0.25;
	final double FEARANGLE = 0.23;
	/**
	 *
	 * @param Game The Game object.
	 */
	public void doAction(SpaceWars Game){

		//teleport, if ship is threatened.
		if (distanceToClosestShip(Game) < FEARDISTANCE && angleFromClosestShip(Game) < FEARANGLE){
			teleport();
		}
		//move as per description.
		int turn = (int) Math.signum(angleFromClosestShip(Game));
		getPhysics().move(true,turn);

		rechargeEnergy();
	}


}
