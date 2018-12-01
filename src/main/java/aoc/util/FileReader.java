package aoc.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Reads a file and puts the content in a String
 * @author Steen
 *
 */
public class FileReader
{
	
	public List<String> readFile(String filename)
	{
        List<String> result = new ArrayList<>();

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.add(line);
            }

            scanner.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
	}
}