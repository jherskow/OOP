/**
 * ְA factory that creates a list of spaceships from the program's command line arguments.
 * @author jherskow
 */
public class SpaceShipFactory {

    /**
     *
     * @param args A list of strings specifying the number and type of desired ships.
     * @return an array of spaceships.
     */
    public static SpaceShip[] createSpaceShips(String[] args) {
        SpaceShip[] shipList = new SpaceShip[args.length];
        for (int i = 0; i <args.length ; i++) {
            switch (args[i]){
                case "h":
                    shipList[i] = new HumanShip();
                    break;
                case "a":
                    shipList[i] = new AgressiveShip();
                    break;
                case "b":
                    shipList[i] = new BasherShip();
                    break;
                case "s":
                    shipList[i] = new FederationShip();
                    break;
                case "r":
                    shipList[i] = new RunnerShip();
                    break;
                case "d":
                    shipList[i] = new DrunkShip();
                    break;
            }
        }
        return shipList;
    }
}
