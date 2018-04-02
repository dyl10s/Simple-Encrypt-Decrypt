import java.io.*;
import java.util.Scanner;

public class UnMix {
	private List<Character> message;

	public UnMix() {
		message = new List<Character>();
	}

	public static void main(String[] args) {
		String userMessage = "";
		userMessage = args[1];
		UnMix v = new UnMix();
		v.unMixture(args[0], userMessage);
	}

	public String processCommand(String command) {
		Scanner scan = new Scanner(command);
		char charInput;

		try {
			command = scan.next();
			switch (command.charAt(0)) {

			case 'b':	
				Character c = (char) scan.next().charAt(0);
				if (c == '~' || c == null){
					c = ' ';
				}
				message.add(scan.nextInt(), c);
				break;

			case 'r': 
				int index = scan.nextInt();
				message.remove(index);
				break;
			case 's':
				
				int replaceIndex = scan.nextInt();
				char chr = scan.next().charAt(0);
				
				if (chr == '~'){
					chr = ' ';
				}
				
				message.set(replaceIndex, chr);
				break;
			}
		} catch (Exception e) {
			System.out.println("Error in command!  Problem!!!! in undo commands");
			System.exit(0);
		}
		finally {
			scan.close();
		}

		return message.toString();
	}

	private void unMixture(String filename, String userMessage) {
		String original = UnMixUsingFile (filename, userMessage);
		System.out.println ("The Original message was: " + original);
	}

	private void setInitialMessage(String userMessage) {
		for (char c : userMessage.toCharArray())
			message.add(message.size(), c);
	}

	public String UnMixUsingFile(String filename, String userMessage) {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		setInitialMessage(userMessage);
		while (scanner.hasNext()) {
			String command = scanner.nextLine();
			userMessage = processCommand(command);
		} 
		return userMessage;
	}
}