import java.util.Arrays;

public class Launcher {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Error! Restart the program and enter the required parameters.");
            Launcher.printHelp();
            System.exit(1);
        }
        boolean sortAscending = true;
        boolean typeString = false;
        boolean requiredParameterEntered = false;
        int numOfParameters = 0;
        String nameOutputFile = "";
        String[] nameInputFiles;
        for (int i = 0; i < 2; i++) {
            if (args[i].indexOf("-") == 0) {
                numOfParameters++;
                switch (args[i]) {
                    case "-a":
                        break;
                    case "-d":
                        sortAscending = false;
                        break;
                    case "-s":
                        typeString = true;
                        requiredParameterEntered = true;
                        break;
                    case "-i":
                        typeString = false;
                        requiredParameterEntered = true;
                        break;
                }
            }
        }
        if (numOfParameters == 2 && args.length < 4) {
            System.out.println("Error! Restart the program and enter the required parameters.");
            Launcher.printHelp();
            System.exit(2);
        }
        int lengthArrayOfNameInputFiles = args.length - (numOfParameters + 1);
        nameInputFiles = new String[lengthArrayOfNameInputFiles];
        if (requiredParameterEntered) {
            nameOutputFile = args[numOfParameters];
            nameInputFiles = Arrays.copyOfRange(args, numOfParameters + 1, args.length);
        } else {
            System.out.println("Error! Restart the program and enter the required parameter: data type.");
            Launcher.printHelp();
            System.exit(3);
        }
        MergeSorter mergeSorter = new MergeSorter(sortAscending, typeString, nameOutputFile, nameInputFiles);
        boolean isCompleteWithoutErrors = mergeSorter.startSorting();
        if (isCompleteWithoutErrors) {
            System.out.println("Sorting complete.");
        } else {
            System.out.println("Sorting complete with errors.");
        }
    }

    public static void printHelp() {
        System.out.print("Help on starting the program. Use the following options at startup:\n" +
                            "1. Data type(required):               -s  for string\n" +
                            "                                   or -i  for integer\n" +
                            "2. Sort mode(optional):               -a  ascending (default)\n" +
                            "                                   or -d  descending\n" +
                            "3. Name of output file, example:      out.txt\n" +
                            "4. Names of input files separated\n" +
                            "   by spaces(at least one), example:  in1.txt in2.txt in3.txt");
    }
}
