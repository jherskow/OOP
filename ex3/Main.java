import java.util.Scanner;

public class Main {
	static SimpleHashSet set;
	static String type ="2";

	static void newByType(){
		if (type.equals("1")) {
			set = new OpenHashSet();
			System.out.println("Open");
		}else {
			set = new ClosedHashSet();
			System.out.println("Closed");
		}
	}
	static void newByType(String[] array){
		if (type.equals("1")) {
			set = new OpenHashSet(array);
		}else {
			set = new ClosedHashSet(array);
		}
	}

	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		System.out.println("1-Open 2-Closed");
		type = scanner.nextLine();
		newByType();

		while (true){
			System.out.println("1-add\t2-contain\t3-delete\t4-size\t5-normalData" +
					" \t6-badData\t7-capacity\t99-exit");
			String input = scanner.nextLine();
			switch (input){
				case "1":{
					input = scanner.nextLine();
					System.out.println(set.add(input));
					break;
				}
				case "2":{
					input = scanner.nextLine();
					System.out.println(set.contains(input));
					break;
				}
				case "3":{
					input = scanner.nextLine();
					System.out.println(set.delete(input));
					break;
				}
				case "4":{
					System.out.println(set.size());
					break;
				}
				case "5":{
					System.out.println("adding normal data\n"); // data 2
					String[] array = Ex3Utils.file2array("/cs/usr/jherskow/safe/OOP/ex3/data2.txt");
					newByType(array);
					System.out.println(set.size());
					break;
				}
				case "6":{
					System.out.println("adding bad data (lore)\n"); // data 1
					String[] array = Ex3Utils.file2array("/cs/usr/jherskow/safe/OOP/ex3/data1.txt");
					newByType(array);
					System.out.println(set.size());
					break;
				}
				case "7":{
					System.out.println(set.capacity);
					break;
				}
				case "99":{
					System.exit(0);
				}
			}
		}
	}
}
