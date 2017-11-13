import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * A program to run and output the required tests.
 * Created by jherskow on 4/29/17.
 */
public class SimpleSetPerformanceAnalyzer {
	static OpenHashSet open;
	static ClosedHashSet closed;
	static CollectionFacadeSet linkedList, hashSet, treeSet;
	static long startTime, time;
	static int MILLION = 1000000;
	static int TEST_NUMBER = 70000;
	static int LINKED_LIST_TEST_NUMBER = 7000;
	static int WARMUP = 0;

	static String[] fileChooser(int fileNumber){
		String[] dataFile;
		if (fileNumber==1){
			dataFile = Ex3Utils.file2array("/cs/usr/jherskow/safe/OOP/ex3/data1.txt");
		}else{
			dataFile = Ex3Utils.file2array("/cs/usr/jherskow/safe/OOP/ex3/data2.txt");
		}
		return dataFile;
	}

// ----------- MAIN ---------------------------------------------------------------------------
	public static void main(String[] args) {
		System.out.println("Comparing Load Time for data 2");
		fileLoadCheck(2);

		System.out.println("Warming up Contains() using data 2...");
		warmup();

		System.out.println("Contains checks w/ data 2");
		multiContainsChecks(2,"23");
		multiContainsChecks(2,"hi");

		System.out.println("Comparing Load Time for data 1");
		fileLoadCheck(1);

		System.out.println("Contains checks w/ data 1");
		multiContainsChecks(1,"-13170890158");
		multiContainsChecks(1,"hi");

		System.out.println("Thank you, come again!");
	}
// ----------- HELPER METHODS ---------------------------------------------------------------------------

	// Runs several contains check on String searchVal, printing output with the fileNumber
	static void multiContainsChecks(int fileNumber, String searchVal){
		containsCheck(searchVal,fileNumber,open," OpenHashSet");
		containsCheck(searchVal,fileNumber,closed," ClosedHashSet");
		containsCheck(searchVal,fileNumber,treeSet," java treeSet");
		linkedListContainsCheck(searchVal,fileNumber,linkedList," java linkedList");
		containsCheck(searchVal,fileNumber,hashSet," java hashSet");
	}

	// uses multiContainsCheck to warmup according to desired numbers
	static void warmup(){
		multiContainsChecks(WARMUP,"random string");
	}

	// runs a single contains check on most types
	static void containsCheck(String searchVal, int fileNumber, SimpleSet set, String type){

		if (fileNumber != WARMUP){
			System.out.print("Checking Contains \"" + searchVal + "\" ? in data"
					+ fileNumber +".txt for" + type);
		}
		startTime = System.nanoTime();
		for (int i = 0; i <TEST_NUMBER ; i++) {
			set.contains(searchVal);
		}
		time = System.nanoTime() - startTime;
		if (fileNumber!=WARMUP){
			System.out.println(" Took "+ time/TEST_NUMBER +" nanoseconds. Result: "+
					set.contains(searchVal));
		}
	}

	// runs a single contains check on  a linked list using different number of iterations
	static void linkedListContainsCheck(String searchVal, int fileNumber, SimpleSet set, String type){

		if (fileNumber != WARMUP){
			System.out.print("Checking Contains \"" + searchVal + "\" ? in data"
					+ fileNumber +".txt for" + type);
		}
		startTime = System.nanoTime();
		for (int i = 0; i <LINKED_LIST_TEST_NUMBER ; i++) {
			set.contains(searchVal);
		}
		time = System.nanoTime() - startTime;
		if (fileNumber!=WARMUP){
			System.out.println(" Took "+ time/LINKED_LIST_TEST_NUMBER +" nanoseconds. Result: "+
					set.contains(searchVal));
		}
	}

	// adds a String[] to a CollectionFacadeSet
	static void addAll(SimpleSet set, String[] array){
		for (String string: array) {
			set.add(string);
		}
	}

	// checks the loading of all files.
	static void fileLoadCheck(int fileNumber){
		System.out.println("Comparing with data"+fileNumber+".txt");

		String[] dataFile = fileChooser(fileNumber);

		System.out.print("Adding data" + fileNumber +".txt to openHashSet");
		startTime = System.nanoTime();
		open = new OpenHashSet(dataFile);
		time = System.nanoTime() - startTime;
		System.out.println(" Took "+ time/(MILLION) +" milliseconds");

		System.out.print("Adding data" + fileNumber +".txt to closedHashSet");
		startTime = System.nanoTime();
		closed = new ClosedHashSet(dataFile);
		time = System.nanoTime() - startTime;
		System.out.println(" Took "+ time/(MILLION) +" milliseconds");

		System.out.print("Adding data" + fileNumber +".txt to java treeSet");
		startTime = System.nanoTime();
		treeSet = new CollectionFacadeSet(new TreeSet<>());
		addAll(treeSet, dataFile);
		time = System.nanoTime() - startTime;
		System.out.println(" Took "+ time/(MILLION) +" milliseconds");

		System.out.print("Adding data" + fileNumber +".txt to java linkedList");
		startTime = System.nanoTime();
		linkedList = new CollectionFacadeSet(new LinkedList<>());
		addAll(linkedList, dataFile);
		time = System.nanoTime() - startTime;
		System.out.println(" Took "+ time/(MILLION) +" milliseconds");

		System.out.print("Adding data" + fileNumber +".txt to java hashSet");
		startTime = System.nanoTime();
		hashSet = new CollectionFacadeSet(new HashSet<>());
		addAll(hashSet, dataFile);
		time = System.nanoTime() - startTime;
		System.out.println(" Took "+ time/(MILLION) +" milliseconds");

	}
}
