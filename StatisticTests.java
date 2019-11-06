import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

public class StatisticTests {
    private static final long defaultKeyLength = 20000;

    private static final long maxBitCount = 10275;
    private static final long minBitCount = 9725;

    private static final double maxPokerTestValue = 46.17;
    private static final double minPokerTestValue = 2.16;

    private static int calculateValueFromBits(char[] bitArray) {
        int result = 0; 
        for (int i = 0; i < bitArray.length; i++) {
            int bitValue = bitArray[i] == '1' ? 1 : 0; 
            result += bitValue * Math.pow(2, i);   
        } 

        return result; 
    } 

    private static double calculateX(int sum) {
        return (16 * sum)/5000 - 5000; 
    }

    private static boolean singleBitsTest(String key) {
        long bitCount = key.chars().filter(e -> e == '1').count();
        return bitCount < maxBitCount && bitCount > minBitCount; 

    } 

    private static boolean longBitSeriesTest(String key) {
        Pattern bitSeriesPattern = Pattern.compile("1{26,}|0{26,}"); 
        return !bitSeriesPattern.matcher(key).find(); 

    }

    private static int countSeriesLength(char[] keyArray, char c, int startIndex) {
        int length = 0; 
        for (int i = startIndex; i < keyArray.length; i++) {
            if(keyArray[i] == c) length++; 
            else return length; 
        }

        return length; 
    }

    private static boolean bitSeriesLengthTest(String key) {
        char[] keyArray = key.toCharArray(); 

        int[] bitOneOccurrencesArray = new int[6]; 
        int[] bitZeroOccurrencesArray = new int[6];
        
        int currentSeriesLength = 0;

        for (int i = 0; i < keyArray.length; i+=currentSeriesLength) {

            if(keyArray[i] == '1') {
                currentSeriesLength = countSeriesLength(keyArray, '1', i); 
                if(currentSeriesLength < 6) bitOneOccurrencesArray[currentSeriesLength-1]++; 
                else bitOneOccurrencesArray[5]++; 
            }
            
            else if(keyArray[i] == '0') {
                currentSeriesLength = countSeriesLength(keyArray, '0', i); 
                if(currentSeriesLength < 6) bitZeroOccurrencesArray[currentSeriesLength-1]++; 
                else bitZeroOccurrencesArray[5]++; 
            }
        }

        // System.out.println(bitOneOccurrencesArray[0]);
        // System.out.println(bitOneOccurrencesArray[1]);
        // System.out.println(bitOneOccurrencesArray[2]);
        // System.out.println(bitOneOccurrencesArray[3]);
        // System.out.println(bitOneOccurrencesArray[4]);
        // System.out.println(bitOneOccurrencesArray[5]);

        // System.out.println(bitZeroOccurrencesArray[0]);
        // System.out.println(bitZeroOccurrencesArray[1]);
        // System.out.println(bitZeroOccurrencesArray[2]);
        // System.out.println(bitZeroOccurrencesArray[3]);
        // System.out.println(bitZeroOccurrencesArray[4]);
        // System.out.println(bitZeroOccurrencesArray[5]);

        if(bitOneOccurrencesArray[0] < 2315 || bitOneOccurrencesArray[0] > 2685) return false; 
        if(bitOneOccurrencesArray[1] < 1114 || bitOneOccurrencesArray[1] > 1386) return false; 
        if(bitOneOccurrencesArray[2] < 527 || bitOneOccurrencesArray[2] > 723) return false; 
        if(bitOneOccurrencesArray[3] < 240 || bitOneOccurrencesArray[3] > 384) return false; 
        if(bitOneOccurrencesArray[4] < 103 || bitOneOccurrencesArray[4] > 209) return false; 
        if(bitOneOccurrencesArray[5] < 103 || bitOneOccurrencesArray[5] > 209) return false; 

        if(bitZeroOccurrencesArray[0] < 2315 || bitZeroOccurrencesArray[0] > 2685) return false; 
        if(bitZeroOccurrencesArray[1] < 1114 || bitZeroOccurrencesArray[1] > 1386) return false; 
        if(bitZeroOccurrencesArray[2] < 527 || bitZeroOccurrencesArray[2] > 723) return false; 
        if(bitZeroOccurrencesArray[3] < 240 || bitZeroOccurrencesArray[3] > 384) return false; 
        if(bitZeroOccurrencesArray[4] < 103 || bitZeroOccurrencesArray[4] > 209) return false; 
        if(bitZeroOccurrencesArray[5] < 103 || bitZeroOccurrencesArray[5] > 209) return false; 

        return true; 
    }

    private static boolean pokerTest(String key) {
        char[] keyArray = key.toCharArray(); 
        int[] occurrencesArray = new int[16]; 

        int sum = 0; 
        for (int i = 0; i < keyArray.length - 3; i+=4) {
            char[] bits = new char[4]; 
            for (int j = 0; j < bits.length; j++) {
                bits[j]=keyArray[i+j]; 
            }

            int calculatedValue = calculateValueFromBits(bits); 
            occurrencesArray[calculatedValue]++; 
        }

        for (int i : occurrencesArray) {
            sum += i*i; 
        }

        double x = calculateX(sum); 
        return x > minPokerTestValue && x < maxPokerTestValue; 

    }

    public static void main(String[] args) throws IOException {
        String filename = "key" + ".txt"; 

        for(int i = 0; i < args.length; i++) {

            if (args[i].equals("-f")) {
                filename = args[++i]; 
            }
        }

        System.out.println("Reading from file " + filename);
        List<String> lines = Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8); 
        String key = ""; 

        for (String line : lines) {
            key += line; 
        }

        System.out.println("Performing tests for key of default length = " + defaultKeyLength + "\n");
        System.out.println("Single Bits test result = " + singleBitsTest(key));
        System.out.println("Bit Series Length test result = " + bitSeriesLengthTest(key));
        System.out.println("Long Bit Series length test result = " + longBitSeriesTest(key));
        System.out.println("Poker test result = " + pokerTest(key));


    }
}
