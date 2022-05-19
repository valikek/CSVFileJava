public class TestMain {
    private final static CSVFile sampleCSV = new CSVFile("resources\\sample.csv",",");

    // Test the class here
    public static void main(String[] args) {
        for(String value : sampleCSV.getValues(0)){
            System.out.println(value);
        }
    }
}