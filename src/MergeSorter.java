import java.io.*;

public class MergeSorter {
    private final boolean sortAscending;
    private final boolean typeString;
    private final String nameOutputFile;
    private final String[] nameInputFiles;
    private final BufferedReader[] arrayReaders;
    private boolean isCompleteWithoutErrors;

    public MergeSorter(boolean sortAscending, boolean typeString, String nameOutputFile, String[] nameInputFiles) {
        this.sortAscending = sortAscending;
        this.typeString = typeString;
        this.nameOutputFile = nameOutputFile;
        this.nameInputFiles = nameInputFiles;
        this.arrayReaders = new BufferedReader[nameInputFiles.length];
    }

    public boolean startSorting() {
        isCompleteWithoutErrors = true;
        String outElement = "";
        int indexOutElement = 0;
        String[] arrayStrings = new String[nameInputFiles.length];
        int numOfEmptyFiles = 0;
        File outFile = new File(nameOutputFile);
        if (outFile.delete()) {
            System.out.println("The old output file with the same name has been removed.");
        }
        initReaders();
        for (int i = 0; i < nameInputFiles.length; i++) {
            arrayStrings[i] = readLine(arrayStrings[i], i);
        }
        if (numOfEmptyFiles == nameInputFiles.length) {
            System.out.println("Nothing to sort. All input files not found or empty.");
            System.exit(5);
        }
        boolean isLineContainData = true;
        do {
            for (int i = 0; i < nameInputFiles.length; i++) {
                if (arrayStrings[i] != null) {
                    outElement = arrayStrings[i];
                    indexOutElement = i;
                    break;
                }
            }
            if (outElement.equals("")) {
                isLineContainData = false;
            }
            for (int i = 0; i < nameInputFiles.length; i++) {
                if (isNextElement(outElement, arrayStrings[i])) {
                    outElement = arrayStrings[i];
                    indexOutElement = i;
                }
            }
            writeToFile(outElement, outFile);
            outElement = "";
            arrayStrings[indexOutElement] = readLine(arrayStrings[indexOutElement], indexOutElement);
        } while (isLineContainData);
        closeReaders();
        return isCompleteWithoutErrors;
    }

    private void initReaders() {
        for (int i = 0; i < nameInputFiles.length; i++) {
            try {
                File file = new File(nameInputFiles[i]);
                FileReader fr = new FileReader(file);
                arrayReaders[i] = new BufferedReader(fr);
            } catch (FileNotFoundException e) {
                System.out.println("File \"" + nameInputFiles[i] + "\" not found!");
            }
        }
    }

    private void closeReaders() {
        for (int i = 0; i < nameInputFiles.length; i++) {
            if (arrayReaders[i] != null) {
                try {
                    arrayReaders[i].close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private boolean isDataValid(String line, int i) {
        boolean isDataWithoutError = false;
        if (line != null) {
            if (!line.contains(" ") && !line.isEmpty()) {
                isDataWithoutError = true;
                if (!typeString) {
                    try {
                        Integer.parseInt(line);
                    } catch (NumberFormatException nfe) {
                        System.out.println("Skip line with error: \"" + line + "\" - in file " + nameInputFiles[i]);
                        isCompleteWithoutErrors = false;
                        isDataWithoutError = false;
                    }
                }
            } else {
                System.out.println("Skip line with error: \"" + line + "\" - in file " + nameInputFiles[i]);
                isCompleteWithoutErrors = false;
            }
        } else {
            isDataWithoutError = true;
        }
        return isDataWithoutError;
    }

    private boolean isPresortWithoutError(String prevElement, String nextElement, int i) {
        boolean isPresortWithoutError = true;
        if (typeString) {
            if (sortAscending) {
                if (prevElement.compareTo(nextElement) > 0) {
                    System.out.println("Skip line with presort error: \"" + nextElement + "\" - in file " + nameInputFiles[i]);
                    isCompleteWithoutErrors = false;
                    isPresortWithoutError = false;
                }
            } else {
                if (prevElement.compareTo(nextElement) < 0) {
                    System.out.println("Skip line with presort error: \"" + nextElement + "\" - in file " + nameInputFiles[i]);
                    isCompleteWithoutErrors = false;
                    isPresortWithoutError = false;
                }
            }
        } else {
            if (sortAscending) {
                if (Integer.parseInt(nextElement) < Integer.parseInt(prevElement)) {
                    System.out.println("Skip line with presort error: \"" + nextElement + "\" - in file " + nameInputFiles[i]);
                    isCompleteWithoutErrors = false;
                    isPresortWithoutError = false;
                }
            } else {
                if (Integer.parseInt(nextElement) > Integer.parseInt(prevElement)) {
                    System.out.println("Skip line with presort error: \"" + nextElement + "\" - in file " + nameInputFiles[i]);
                    isCompleteWithoutErrors = false;
                    isPresortWithoutError = false;
                }
            }
        }
        return isPresortWithoutError;
    }

    private void writeToFile(String line, File outFile) {
        try (FileWriter writer = new FileWriter(outFile, true)) {
            writer.write(line + "\n");
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean isNextElement(String outElement, String prevElement) {
        boolean isNextElement = false;
        if (typeString) {
            if (sortAscending) {
                if (prevElement != null && prevElement.compareTo(outElement) < 0) {
                    isNextElement = true;
                }
            } else {
                if (prevElement != null && prevElement.compareTo(outElement) > 0) {
                    isNextElement = true;
                }
            }
        } else {
            if (sortAscending) {
                if (prevElement != null && Integer.parseInt(outElement) > Integer.parseInt(prevElement)) {
                    isNextElement = true;
                }
            } else {
                if (prevElement != null && Integer.parseInt(outElement) < Integer.parseInt(prevElement)) {
                    isNextElement = true;
                }
            }
        }
        return isNextElement;
    }

    private String readLine(String prevElement, int index) {
        boolean isPendingLine = true;
        String bufferElement = "";
        while (isPendingLine) {
            if (arrayReaders[index] != null) {
                try {
                    bufferElement = arrayReaders[index].readLine();
                } catch (IOException e) {
                    System.out.println("Unable to read a line!");
                    isCompleteWithoutErrors = false;
                }
                if (bufferElement != null) {
                    if (isDataValid(bufferElement, index)) {
                        isPendingLine = false;
                    } else {
                        continue;
                    }
                    if (prevElement != null && !isPresortWithoutError(prevElement, bufferElement, index)) {
                        isPendingLine = true;
                    }
                } else {
                    isPendingLine = false;
                }
            } else {
                isPendingLine = false;
                bufferElement = null;
            }
        }
        return bufferElement;
    }
}
