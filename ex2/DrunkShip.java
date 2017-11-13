import java.util.Random;

/**
 * Represents a spaceship that has been commandeered by
 * Ja .... uh ... CAPTAIN Jack Sparrow.
 *
 * @author jherskow
 */
public class DrunkShip extends SpaceShip {
	int timer = 0;
	int randomTurn;
	Random random = new Random();

	/**
	 * This method provides the ship's behaviour:
	 * Try to hold a steady course.
	 * Every now and then the Captain might lose it a little.
	 *
	 * @param Game The game object.
	 */
	public void doAction(SpaceWars Game){

		if (timer < 70) {
			getPhysics().move(false, 0);
		}else if (90>timer && timer>70 ) {
			getPhysics().move(false,randomTurn);
		}else{
			getPhysics().move(true, 0);
		}

		if (timer == 120) {
			timer=0;
			randomTurn = (int) Math.signum(random.nextInt());
		}
		rechargeEnergy();
		timer++;
	}


}
