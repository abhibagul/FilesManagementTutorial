package ManagementSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

class ManagementSystem{

	public String Path;
	public String IPath;
	


	public String getPath() {
		return Path;
	}

	public String getIPath() {
		return IPath;
	}

	public void setPath(String path) {
		Path = path;
	}

	public void setIPath(String Ipath) {
		IPath = Ipath;
	}

	void Welcome() {
		System.out.println("\n\n======================================");
		System.out.println("Application Developer : Abhishek Bagul");
		System.out.println("File Management Application");
		System.out.println("======================================");
	}

	void SetWorkingDirectoryPath() {
		System.out.print("\nPlease enter direcotry path: ");
		Scanner sc = new Scanner(System.in);
		String p = sc.nextLine();
		if(Pattern.matches("((\\/)+|(\\\\)+)", p)) {
			System.out.println("\nPlease provide the absolute path.");
			SetWorkingDirectoryPath();
		}
		else {
			if(new File(p).exists() && new File(p).isDirectory()) {
				setPath(p);
				setIPath(p);
			}else {
				System.out.println("\nInvalid directory path is provided.");
				SetWorkingDirectoryPath();
			}
		}
		
	}

	boolean AddFile(String FileName, String FilePath) {
		System.out.println("\n\n========== [Creating File: "+ FileName + "] ==============");
		try {
			File nFile = new File(FilePath+"//"+FileName);
			if(!nFile.exists()) {
				if(nFile.createNewFile()) {
					System.out.println( FileName + " succesfully created");
					return true;
				}else {
					System.out.println("Unable to create a "+ FileName +" . Please try again.");
					return GetAddFileNameDetails(FilePath);
				}
			}else {
				System.out.println("File with the name " + FileName + " already exist in the directory. Try again with different name.");
				return GetAddFileNameDetails(FilePath);
			}
		}
		catch (IOException e){
			System.out.println("Error creating "+ FileName +" .");
			return false;
		}

	}


	boolean GetAddFileNameDetails(String FilePath) {


		System.out.print("\nName of new file:");
		Scanner sc = new Scanner(System.in);
		String FileName = sc.next();
		return AddFile(FileName, FilePath);

	}

	void SearchFolder(String FilePath, String SearchQuery) {
		int SearchResults = 0;
		System.out.println("==================[Serach results for : "+ SearchQuery +" ]================]");
		System.out.println("===========[Directory: " + FilePath + "]==========");

		File folder = new File(FilePath);
		for (File fileEntry : folder.listFiles()) {
			if(!fileEntry.isDirectory()) {
				//check if has a match in name
				String TempFileName = fileEntry.getName().toString();
				//System.out.println(TempFileName + ", ");
				if(TempFileName.matches("(.*)"+ SearchQuery + "(.*)")) {
					SearchResults++;
					System.out.println("[File] " + fileEntry.getName() );
				}				
			}
		}

		if(SearchResults < 1) {
			System.out.println("No files matching " + SearchQuery + " (⌐■_■)");
		}
		System.out.println("===============================");
		System.out.println("["+ (1) +"]  [Try different search..]");
		System.out.println("["+ (2) +"]  [Go Back to actions menu]");
		System.out.println("["+ (3) +"]  [Go Back to main menu]");
		System.out.println("["+ (4) +"]  [Exit the application]");
		int op = ScannerResult(1,4);

		if(op == 1) {
			SearchQuery(getIPath());
		}else if(op == (2)) {

			int y = ActionsMenu();
			ActionMenuActionFilter(y);	

		}
		else if(op == (3)) {
			//main menu
			int x = MainMenu();
			MainMenuActionFilter(x);

		}else if(op == (4)) {
			byebye();
		}else {
			System.out.println("This will never happen after all these validation. Still if you get it, theres alot to learn! ٩(◕‿◕)۶");
		}


	}

	void SearchQuery(String FilePath) {
		System.out.print("\nSearch Query:");
		Scanner sc = new Scanner(System.in);
		String SearchQuery = sc.next();
		
		SearchFolder(FilePath,SearchQuery);
	}



	boolean DeleteFile(String FilePath, String FileName) {
		//get confirmation first
		System.out.println("Are you sure you want to delete "+ FileName +"?(y/n)" );

		Scanner sc = new Scanner(System.in);
		String cond = sc.next();
		
		cond = cond.toLowerCase();
		if(cond.equals("y")) {

			File file1 = new File(FilePath + "\\" +FileName);
			if(file1.exists()) {
				if (file1.delete()) {
					System.out.println( FileName + " deleted successfully");

					return true;
				}
				else {
					System.out.println("Failed to delete" + FileName);
					return false;
				}
			}else {
				System.out.println("File does not exist for deletion!");
				return false;
			}

		}else if (cond.equals("n")){
			System.out.println("Deletion of " + FileName +" is aborted by the user.");
			return false;
		}else {
			System.out.println("Invalid input is provided.");
			boolean a = DeleteFile(FilePath,FileName);
			return a;
		}
	}

	void DeletionList() {
		System.out.println("======= [ Select the number of file to delete] ======");
		File folder = new File(getIPath());
		int numFiles = folder.list().length;
		if(numFiles < 1) {
			System.out.println("===[[No files in the directory (•ิ_•ิ)? ]]===");
		}
		String[] FileNames = new String[numFiles+1];
		int i=0;
		for (File fileEntry : folder.listFiles()) {

			if (!fileEntry.isDirectory()) {
				i++;
				FileNames[i] = fileEntry.getName();
				System.out.println("["+ i +"] " + fileEntry.getName());
			}

		}

		System.out.println("["+ (i+1) +"]  [Go Back to main menu]");
		System.out.println("["+ (i+2) +"]  [Go Back to actions menu]");
		System.out.println("["+ (i+3) +"]  [Exit the application]");
		int op = ScannerResult(1,i+3);
		if(op <= i) {
			if(!DeleteFile(getIPath(),FileNames[op])) {
				//file has been deleted update the list
				System.out.println("Unable to delete the file!");
			}
			DeletionList();
		}else {
			if(op == (i+1)) {
				//main menu
				int x = MainMenu();
				MainMenuActionFilter(x);

			}else if(op == (i+2)) {

				int y = ActionsMenu();
				ActionMenuActionFilter(y);	

			}else if(op == (i+3)) {
				byebye();
			}else {
				System.out.println("This will never happen after all these validation. Still if you get it, theres alot to learn! ٩(◕‿◕)۶");
			}
		}
	}


	void ListFiles() {
		System.out.println("\n\n========== [Files in: "+ getPath() + "] ==============");

		File folder = new File(getPath());
		int numFiles = folder.list().length;
		if(numFiles < 1) {
			System.out.println("===[[No files in the directory (•ิ_•ิ)? ]]===");
		}
		String[] FileNames = new String[numFiles+2];
		int i=0;

		for (File fileEntry : folder.listFiles()) {
			i++;
			if (fileEntry.isDirectory()) {
				FileNames[i] = fileEntry.getName();
				System.out.println("["+ i +"]  [Open Directory]  : " + fileEntry.getName());
			} else {
				FileNames[i] = fileEntry.getName();
				System.out.println("["+ i +"]  [Delete File]     : " + fileEntry.getName());
			}

		}
		System.out.println("["+ (i+1) +"]  [Add New file.... ]");
		System.out.println("["+ (i+2) +"]  [Go to parent folder... ]");
		System.out.println("["+ (i+3) +"]  [Go Back to main menu]");
		System.out.println("["+ (i+4) +"]  [Exit the application]");
		int op = ScannerResult(1,i+4);

		if(op <= i) {
			//respective condition
			File folder1 = new File(getPath() + "\\" +FileNames[op]);
			if( folder1.isDirectory() ) {
				//update the directory path
				setPath(getPath()+ "\\"+FileNames[op]);
				//open the new directory
				ListFiles();
			}else {
				//it is a file remove it
				if(!DeleteFile(getPath(),FileNames[op])) {
					//file has been deleted update the list
					System.out.println("Unable to delete the file!");
				}
				ListFiles();
			}
		}else {
			//extra options
			if(op == (i+1)) {
				//add file
				System.out.println("\n\n========== [New file details] ==============");
				if(!GetAddFileNameDetails(getPath())) {
					System.out.println("File creation failed.");
				}
				ListFiles();


			}else if(op == (i+2)) {
				//go to parent directory
				if(folder.getParentFile() ==  null) {
					System.out.println("=================================================="
							+ "============No Parent Directory exists!==================="
							+ "===================================================");
					//throw back to menu!
					int x = MainMenu();
					MainMenuActionFilter(x);
				}else {
					setPath(folder.getParentFile().toString());
					ListFiles();
				}

			}else if(op == (i+3)) {
				//Go to main menu
				int x = MainMenu();
				MainMenuActionFilter(x);

			}else if(op == (i+4)) {
				//exit
				byebye();
			}else {
				System.out.println("This will never happen after all these validation. Still if you get it, theres alot to learn! ٩(◕‿◕)۶");
			}
		}

	}

	void ListSortedFiles(String FilePath, String SortDirection) {
		System.out.println("\n\n========== [Files in ("+ SortDirection +" ORDER): "+ getPath() + "] ==============");
		File folder = new File(FilePath);
		int numFiles = folder.list().length;
		if(numFiles < 1) {
			System.out.println("===[[No files in the directory (•ิ_•ิ)? ]]===");
		}

		if(SortDirection.equals("ASC")) {
			try {
				Files.list(Paths.get(FilePath)).sorted().forEach(s -> {
					if(!s.toFile().isDirectory()) {
						System.out.println(s.toFile().getName());
					}
				});;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			//reverse order
			try {
				Stream<java.nio.file.Path> FileName = Files.list(Paths.get(FilePath)).sorted(Comparator.reverseOrder());
				FileName.forEach(s -> {
					if(!s.toFile().isDirectory()) {
						System.out.println(s.toFile().getName());
					}
				});;

			} catch (IOException e) {
				e.printStackTrace();
			}

		}


		if(SortDirection.equals("ASC")) {
			System.out.println("["+ (1) +"]  [Sort in descending order.... ]");
		}else {
			System.out.println("["+ (1) +"]  [Sort in ascending order.... ]");
		}
		System.out.println("["+ (2) +"]  [Go Back to main menu]");
		System.out.println("["+ (3) +"]  [Exit the application]");
		int op = ScannerResult(1,3);

		switch(op) {
		case 1:
			if(SortDirection.equals("ASC")) {
				ListSortedFiles(FilePath, "DSC");
			}else {
				ListSortedFiles(FilePath, "ASC");
			}
			break;
		case 2:
			int x = MainMenu();
			MainMenuActionFilter(x);
			break;
		case 3:
			byebye();
			break;
		default:
			int y = MainMenu();
			MainMenuActionFilter(y);
		}

	}



	int ScannerResult(int min, int max) {
		System.out.print("\nPlease enter your option: ");
		Scanner scc = new Scanner(System.in);
		if(scc.hasNextInt()) {
			int s = scc.nextInt();
			if(s >= min && s <= max ) {
				return s;
			}else {
				System.out.println("Option has to be in the limit of "+ min +" to "+ max +", Please try again.");
				int a = ScannerResult(min,max);
				return a;
			}
		}else {
			System.out.println("Option has to be a number.");
			int a = ScannerResult(min,max);
			return a;
		}
		
	}

	int MainMenu() {
		System.out.println("\n\n========== [Main Menu] ==============");
		System.out.println("[1] Files Explorer");
		System.out.println("[2] List of files");
		System.out.println("[3] Actions for Files");
		System.out.println("[4] Update the Folder path");
		System.out.println("[5] Exit");
		return ScannerResult(1,5);
	}

	void MainMenuActionFilter(int x) {
		switch(x) {
		case 1:
			ListFiles();
			break;
		case 2:
			ListSortedFiles(getIPath(),"ASC");
			break;
		case 3:
			int ac = ActionsMenu();
			ActionMenuActionFilter(ac);
			break;
		case 4:
			System.out.println("\n\n========== [Update Directory Path] ==============");
			initialize();
			break;
		case 5:
			byebye();	
			break;
		default:
			//if something goes wrong, not likely to happen!
			int y = MainMenu();
			MainMenuActionFilter(y);
		}
	}

	void byebye() {
		System.out.print("Bye, Bye!! ʕ •ᴥ• ʔ");

	}

	void ActionMenuActionFilter(int x) {
		switch(x) {
		case 1:
			//create file
			System.out.println("\n\n========== [New file details] ==============");
			if(!GetAddFileNameDetails(getIPath())) {
				System.out.println("File creation failed.");
			}else {
				System.out.println("File created Succesfully!");
			}

			//throw back to action page
			int y = ActionsMenu();
			ActionMenuActionFilter(y);				
			break;
		case 2:
			//delete file
			DeletionList();
			break;
		case 3:
			SearchQuery(getIPath());
			break;
		case 4:
			int z = MainMenu();
			MainMenuActionFilter(z);
			break;			
		case 5:
			//do nothing let them exit
			byebye();			
			break;
		}
	}

	int ActionsMenu() {
		System.out.println("\n\n============== [Actions for File] ===================");
		System.out.println("\n\n========== [Directory: "+ getIPath() +"] ==============");
		System.out.println("[1] Create file");
		System.out.println("[2] Delete file");
		System.out.println("[3] Search folder for file");
		System.out.println("[4] Back to main menu");
		System.out.println("[5] Exit");
		return ScannerResult(1,5);
	}

	public void initialize() {
		/*Show details*/
		Welcome();

		/* Set Directory */
		SetWorkingDirectoryPath();

		/* Main Menu */
		int x = MainMenu();
		MainMenuActionFilter(x);


	}
}

public class index {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ManagementSystem m = new ManagementSystem();
		m.initialize();

	}

}
