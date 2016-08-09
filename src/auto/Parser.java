package auto;

import java.io.*;

import util.*;

/**
 * Parses the script file into commands
 * @author Trevor
 *
 */
public class Parser{	
	
	/**
	 * Parses the script into a double array to be interpreted by interpreter
	 * @param fileName name of file at location /home/lvuser/
	 * @return
	 */
	public static double[][] parse(String fileName) {
		String fileLocation = "/home/lvuser/" + fileName + ".txt";
		double data[][] = new double[30][3];  //TODO make this smart and not hard coded
		
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				data[i][j] = -1;
			}
		}
		
		for (int i = 0; i < data.length; i++) {
			System.out.print("\n");
			for (int j = 0; j < data[i].length; j++) {
				System.out.print(data[i][j] + "\t");
			}
		}
		
		int lineCounter = 0;
		
		try {
			FileInputStream fis = new FileInputStream(new File(fileLocation));
			DataInputStream in = new DataInputStream(fis);
			BufferedReader bis = new BufferedReader(new InputStreamReader(in)); 
			String input;
			
			
			input = bis.readLine();
			do{
				System.out.print(input + "\n");
				System.out.print(" " + lineCounter + '\n');
				String words[] = input.split(",");

				data[lineCounter][0] = Steps.getStep(Type.valueOf(words[0].toUpperCase())); 
				
				for(int i = 1; i<words.length; i++) {
					data[lineCounter][i] = Double.parseDouble(words[i]);
				}
				lineCounter++;
				input = bis.readLine();
			}
			while(input != null);
				
	
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		} catch (NullPointerException e) {
			System.out.println("File is empty");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IOException");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("Exception parsing auto file");
		}
		return data;
	}
	
}
