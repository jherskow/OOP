import oop.ex4.data_structures.AvlTree;
import java.util.Scanner;

/**
 * Blaga blaarg blarg.
 * @author jherskow
 */
public class Main {


	public static void main(String[] args){
		int NUM = 17;
		int[] numList = new int[NUM];
		int[] myList = new int[] {10,15,5,20,13,7,3,21}; //{10,15,5,20,13,7,3,21,2,4,8};
		int[] myList2 = new int[] {1,2,3,4,5,6,7,8,9,10};
		int[] rightRotate = new int[] {1,2};
		for (int i = 1; i <=NUM ; i++) {
			numList[i-1]=i;
		}

		AvlTree tree = new AvlTree();
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("1-add\t2-contain\t3-delete\t4-size\t5-1-to-"+NUM+"" +
					" \t6-myList\t7-rightrotate\t8-iterate" +
					"\t9-max\t10-min\t99-???");
			int input = scanner.nextInt();
			switch (input) {
				case 1: {
					input = scanner.nextInt();
					System.out.println(tree.add(input));
					tree.print();
					break;
				}
				case 2: {
					input = scanner.nextInt();
					System.out.println(tree.contains(input));
					tree.print();
					break;
				}
				case 3: {
					input = scanner.nextInt();
					System.out.println(tree.delete(input));
					tree.print();
					break;
				}
				case 4: {
					tree.print();
					System.out.println(tree.size());
					break;
				}
				case 5: {
					System.out.println("adding "+NUM+"\n");
					tree = new AvlTree(numList);
					tree.print();
					System.out.println(tree.size());
					break;
				}
				case 6: {
					System.out.println("my list\n");
					tree = new AvlTree(myList);
					tree.print();
					System.out.println(tree.size());
					break;
				}
				case 7: {
					System.out.println("rightrotate\n");
					tree = new AvlTree(rightRotate);
					break;
				}
				case 8: {
					for (int i:tree) {
						System.out.println(i);
					}
					break;
				}
				case 9: {
					input = scanner.nextInt();
					System.out.println(AvlTree.findMaxNodes(input));
					break;
				}
				case 10: {
					input = scanner.nextInt();
					System.out.println(AvlTree.findMinNodes(input));
					break;
				}

				case 99: {
					scanner.close();
					System.exit(0);
				}
			}
		}
	}
}

