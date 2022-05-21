public class TestMain {
    private final static CSVFile sampleCSV = new CSVFile("resources\\sample.csv",",");

    // Test the class here
    public static void main(String[] args) {
        sampleCSV.deleteValue(2412,8);
    }
}