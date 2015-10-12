import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class GridExercise{
    private static List<List<String>> alist = new ArrayList<List<String>>();
    private static Scanner scReader;
    private static Scanner sc = new Scanner(System.in);
    
    public static void main(String[] args){
        GridExercise ge = new GridExercise();
        ge.initializeGrid();
        ge.showOptions();   
    }
    
    private void sortRow(){
        boolean endLoop = false;
        int rowToSort = 0;
        while(!endLoop){
            try{
                System.out.print("\nEnter row to sort: ");
                rowToSort = sc.nextInt();
                endLoop = true;
            } catch(InputMismatchException e){
                System.out.println("Input the right row to sort");
                sc.nextLine();
            }
        }
        
        if(rowToSort <= alist.size()){
            List<String> tempList = alist.get(rowToSort - 1);
            
            System.out.println("Sort by: ");
            System.out.println("[1] Ascending");
            System.out.println("[2] Descending");
            System.out.print("Choose your option: ");
            String choice = sc.next();
            
            if(choice.matches("[12]")){
                if(choice.equals("1")){                            
                    Collections.sort(tempList);
                }
                else if(choice.equals("2")){
                    Collections.sort(tempList, Collections.reverseOrder());
                }
                alist.set(rowToSort - 1, tempList);
                writeToFile();
            }
            else{
                System.out.println("Option not found");
                sortRow();
            }
        }
        else{
            System.out.println("Row not found");
            sortRow();
        }
    }
    
    private void addNewRow(){
        int rows = alist.size();
        int cols = alist.get(rows - 1).size();
        List<String> tempString = new ArrayList<String>();
        String val;
        
        System.out.println("Adding new row [" + (rows + 1) + "]:");
        for(int i = 0; i < cols; i++){
            do{
                System.out.print("Add value for cell ["+ (rows + 1) + "][" + (i + 1) + "]: ");
                val =  sc.next();
                if(val.length() != 3){
                    System.out.println("Input value must not be longer/shorter than 3");
                }
            }while(val.length() != 3);
            tempString.add(val);
        }
        alist.add(tempString);
        writeToFile();
    }
    
    private void searchString(){
        System.out.print("\nEnter search parameter: ");
        sc.nextLine();
        String searchParam = sc.nextLine();
        searchParam = searchParam.replaceAll("\\s+", "");
        if(searchParam.length() == 3){
            String output = "";
            int occurences = 0;
            
            for(List<String> ls : alist){
                for(String tempString : ls){
                    if(tempString.contains(searchParam)){
                        occurences++;
                        output += "["+ (alist.indexOf(ls) + 1) + "][" +
                        (ls.indexOf(tempString) + 1) + "] ";
                    }
                }
            }
            System.out.println("Search parameter \"" + searchParam + 
                "\" occured (" + occurences + ") time(s) at \n" + output);
        }
        else{
            System.out.println("Search parameter must be not shorter/longer than 3!");
            searchString();
        }
    }
    
    private String getContent(){
        String updated = "";
        int ctr = 1;
        for(List<String> ls : alist){
            for(String tempString : ls){
                updated += tempString;
                if(ls.size() > ctr){
                    updated += ",";
                    ctr++;
                }
            }
            ctr = 1;
            updated += "\n";
        }
        return updated;
    }
    
    private void writeToFile(){    
        try {
			String content = getContent();
			File file = new File("tryfile.txt");
			
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
    
    private void editData(){
        int rowToEdit = 0, colToEdit = 0;
        boolean endLoop = false;
        while(endLoop == false){    
            try{
                System.out.print("\nEnter row to edit: ");
                rowToEdit = sc.nextInt();
                System.out.print("Enter column to edit: ");
                colToEdit = sc.nextInt();
                endLoop = true;
            } catch (InputMismatchException e){
                System.out.println("Input the right row/column index!");
                sc.nextLine();
            }
        }
        try{        
            if(alist.get(rowToEdit - 1).get(colToEdit - 1) != null){
                System.out.print("Enter value to replace: ");
                String value = sc.next();
                if(value.length() == 3){
                    List<String> tempList = alist.get(rowToEdit - 1);
                    tempList.set(colToEdit - 1, value);
                    alist.set(rowToEdit - 1, tempList);
                    writeToFile();
                }
                else{
                    System.out.println("Input must not be longer/shorter than 3");
                    editData();
                }
            }            
        } catch(IndexOutOfBoundsException e){
            System.out.println("Row/Column not found!");
            editData();
        }
    }
    
    private void showOptions(){
        System.out.print("\nOPTIONS:\n" + 
                        "[1] Print\n" +
                        "[2] Edit \n" +
                        "[3] Search \n" + 
                        "[4] Add New Row\n" +
                        "[5] Sort Row\n" +
                        "[6] Exit\n" +
                        "Choose Your Option: ");
        String option = sc.next();
        
        if(option.matches("[123456]")){
            if(option.equals("1")){
                initializeGrid();
                showOptions();
            }
            else if(option.equals("2")){
                editData();
                showOptions();
            }
            else if(option.equals("3")){
                searchString();
                showOptions();  
            }
            else if(option.equals("4")){
                addNewRow();
                showOptions();
            }
            else if(option.equals("5")){
                sortRow();
                showOptions();
            }
            else if(option.equals("6")){
                return;
            }
        }
        else{
            System.out.println("Option not available");
            showOptions();
        }
    }
    
    private void readFile(){
        File file = new File("tryfile.txt");
        try{
            scReader = new Scanner(file);
        }
        catch(FileNotFoundException e){}
    }
  
    private void populateCollection(){
        alist = new ArrayList<List<String>>();
        String tempString = "";
        readFile();
        while(scReader.hasNextLine()){
            tempString = scReader.nextLine();
            List<String> accessList = new ArrayList<String>();
            for(String temp : tempString.split(",")){
                accessList.add(temp);
            }
            alist.add(accessList);
        }
    }
    
    private void initializeGrid(){
        readFile();
        populateCollection();
        System.out.println();
        for(List<String> list : alist){
        System.out.print("| ");
            for(String tempString : list){
                System.out.print(tempString);
                System.out.print(" | ");
            }
        System.out.println();
        }
    }
}
