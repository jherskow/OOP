package oop.ex6.main;

import java.io.*;
import java.util.*;

/**
 * Reads the file and create an array list from the input. Throws IOError if there was an IO problem.
 */
class FileReader {

    /**
     * Takes the file path and returns an array list of the contents.
     * @param filename the filename
     * @return an array list of the content
     * @throws FileNotFoundException thrown if not found
     */
	static ArrayList<String> processInput(String filename) throws FileNotFoundException{
        Scanner scanner = new Scanner(new File(filename));
        ArrayList<String> list = new ArrayList<String>();
        while (scanner.hasNextLine()) {
            list.add(scanner.nextLine());
        }
        scanner.close();
        return list;
	}

}
