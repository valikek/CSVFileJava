import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Simple class for reading/writing to simple CSV files
 * @author Valentino Solcan
 * @version 3
 */
public class CSVFile {
    private final String filePath;              // Path of file we want to read
    private final String delimiter;             // Delimiter of the CSV file
    private ArrayList<String> fileContent;      // ArrayList containing the file content
    private final static String REGEX = "(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    /**
     * @param path The path of the CSV file
     * @param delimiter The delimiter of the CSV file. For example: "," "." ";"...
     */
    public CSVFile(String path, String delimiter){
        this.filePath = path;
        this.delimiter = delimiter;
        readFile();
    }

    /**
     * Reads the file and stores it into the "fileContent" ArrayList.
     * If the file is not found it will raise an exception.
     */
    private void readFile(){
        BufferedReader bufferedReader;
        this.fileContent = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(filePath));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                fileContent.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Updates the file content of the CSV file with the content of the "fileContent" ArrayList
     */
    private void updateFileContent(){
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            for (String line : fileContent) {
                fileWriter.write(line+"\n");
            }
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Formats the String[] values into a CSV line
     * Example input: "[this],[is],[an],[example]" with delimiter = ";"
     * Example output: "this;is;an;example"
     */
    private String valuesToString(String[] values){
        StringBuilder line = new StringBuilder();
        for(String value : values)
            line.append(value).append(delimiter);
        line.deleteCharAt(line.length()-1);
        return line.toString();
    }

    /**
     * Gets a line from the CSV file
     * @param lineIndex The index of the line that we want to read from the file
     * @return A string containing the line from the CSV file
     */
    public String getLine(int lineIndex){
        return fileContent.get(lineIndex);
    }

    /**
     * Gets all the lines from the CSV file
     * @return A String[] containing all the lines from the CSV file
     */
    public String[] getLines(){
        String[] lines = new String[fileContent.size()]; //fileContent.size() is equal to the number of lines
        for(int i = 0; i<fileContent.size(); i++){
            lines[i] = getLine(i);
        }
        return lines;
    }

    /**
     * Gets the value from a line in the CSV file
     * @param lineIndex The index of the line that we want to read from the file
     * @param valueIndex The index of the value that we want to read from the line
     * @return A String containing the value from the line of the CSV file
     */
    public String getValue(int lineIndex, int valueIndex){
        try{
            return getLine(lineIndex).split(delimiter+REGEX, -1)[valueIndex];
        }catch(ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Gets all the values of index "valueIndex" from all the lines of the CSV file
     * @param valueIndex The index of the value that we want to read from all the lines
     * @return A String[] containing the values from that index
     */
    public String[] getValues(int valueIndex){
        String[] values = new String[fileContent.size()]; //fileContent.size() is equal to the number of lines
        for(int i = 0; i<fileContent.size(); i++){
            values[i] = getValue(i, valueIndex);
        }
        return values;
    }

    /**
     * Gets all the values of index "valueIndex" from all the lines between startingLine and end of file
     * @param valueIndex The index of the value that we want to read from all the lines
     * @param startingLine The line where we want to start reading the values from, included in the range
     * @return A String[] containing the values from that index
     */
    public String[] getValues(int valueIndex, int startingLine){
        String[] values = new String[fileContent.size()-startingLine]; //fileContent.size() is equal to the number of lines
        for(int i = 0; i<fileContent.size()-startingLine; i++){
            values[i] = getValue(i+startingLine, valueIndex);
        }
        return values;
    }

    /**
     * Gets all the values of index "valueIndex" from all the lines between startingLine and endingLine.
     * @param valueIndex The index of the value that we want to read from all the lines
     * @param startingLine The line where we want to start reading the values from, included in the range
     * @param endingLine The line where we want to stop reading the values from, not included in the range
     * @return A String[] containing the values from that index
     */
    public String[] getValues(int valueIndex, int startingLine, int endingLine){
        String[] values = new String[(endingLine-startingLine)]; //fileContent.size() is equal to the number of lines
        for(int i = 0; i<endingLine-startingLine; i++){
            values[i] = getValue(i+startingLine, valueIndex);
        }
        return values;
    }

    /**
     * Appends a line at the end of the CSV file
     * @param values Array containing the values that we want to append into a line at the end of the file
     */
    public void append(String[] values){
        fileContent.add(valuesToString(values));
        updateFileContent();
    }

    /**
     * Appends a line at the end of the CSV file
     * @param line Line that we want to append at the end of the file
     */
    public void append(String line){
        fileContent.add(line);
        updateFileContent();
    }

    /**
     * Appends a line at the end of the CSV file
     * @param lines Lines that we want to append at the end of the file
     */
    public void appendLines(String[] lines){
        fileContent.addAll(Arrays.asList(lines));
        updateFileContent();
    }

    /**
     * Writes line into the CSV file
     * @param values String array containing the values that we want to write into the file
     * @param lineIndex Index of the line that we want to write to inside the file.
     *                  This index should be smaller than the lines that are present in the file.
     */
    public void writeLine(String[] values, int lineIndex){
        try {
            fileContent.set(lineIndex, valuesToString(values));
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
            return;
        }
        updateFileContent();
    }

    /**
     * Writes line into the CSV file
     * @param line String that we want to write into the file
     * @param lineIndex Index of the line that we want to write to inside the file.
     */
    public void writeLine(String line, int lineIndex){
        try {
            fileContent.set(lineIndex, line);
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
            return;
        }
        updateFileContent();
    }

    /**
     * Write an value at a specific position into the CSV file
     * @param value The value that we want to write
     * @param lineIndex The line where the value is located
     * @param valueIndex Index of the value at the lineIndex line
     */
    public void writeValue(String value, int lineIndex, int valueIndex){
        String[] values;
        try {
            values = fileContent.get(lineIndex).split(delimiter+REGEX, -1);
        }catch(IndexOutOfBoundsException ex){
            ex.printStackTrace();
            return;
        }
        try {
            values[valueIndex] = value;
        }catch(ArrayIndexOutOfBoundsException ex){
            ex.printStackTrace();
            return;
        }
        fileContent.set(lineIndex, valuesToString(values));
        updateFileContent();
    }

    /**
     * Clear the file and write a matrix onto it
     * @param matrix A matrix containing the content that we want to write
     */
    public void clearAndWrite(String[][] matrix){
        fileContent.clear();
        for(String[] values : matrix)
            fileContent.add(valuesToString(values));
        updateFileContent();
    }

    /**
     * Delete a line from the file
     * @param lineIndex Index of the line to delete
     */
    public void deleteLine(int lineIndex){
        fileContent.remove(lineIndex);
        updateFileContent();
    }

    /**
     * Delete a value from the file
     * @param lineIndex Index of the line where the value is located
     * @param valueIndex Index of the value
     */
    public void deleteValue(int lineIndex, int valueIndex){
        String[] values = fileContent.get(lineIndex).split(delimiter+REGEX, -1);
        values[valueIndex] = "";
        fileContent.set(lineIndex, valuesToString(values));
        updateFileContent();
    }

    /**
     * Clear the file
     */
    public void clear(){
        fileContent.clear();
        updateFileContent();
    }

    /**
     * @return Length of the file in lines
     */
    public int length(){
        return fileContent.size();
    }

    /**
     * @return A string containing the file content formatted as a single string
     */
    @Override
    public String toString(){
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i<fileContent.size(); i++)
            buffer.append(getLine(i)).append("\n");
        return buffer.toString();
    }
}