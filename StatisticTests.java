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

    private static boolean bitSeriesLengthTest(String key) {
        Pattern oneBitSeriesPattern = Pattern.compile("010|101");
        Pattern twoBitsSeriesPattern = Pattern.compile("01{2}0|10{2}1");
        Pattern threeBitsSeriesPattern = Pattern.compile("01{3}0|10{3}1");
        Pattern fourBitsSeriesPattern = Pattern.compile("01{4}0|10{4}1");
        Pattern fiveBitsSeriesPattern = Pattern.compile("01{5}0|10{5}1");
        Pattern sixAndMoreBitsSeriesPattern = Pattern.compile("01{6,}0|10{6,}1");

        long oneBitSeriesCount = oneBitSeriesPattern.matcher(key).results().count(); 
        long twoBitsSeriesCount = twoBitsSeriesPattern.matcher(key).results().count(); 
        long threeBitsSeriesCount = threeBitsSeriesPattern.matcher(key).results().count(); 
        long fourBitsSeriesCount = fourBitsSeriesPattern.matcher(key).results().count(); 
        long fiveBitsSeriesCount = fiveBitsSeriesPattern.matcher(key).results().count(); 
        long sixBitsSeriesCount = sixAndMoreBitsSeriesPattern.matcher(key).results().count(); 

        System.out.println(oneBitSeriesCount);
        System.out.println(twoBitsSeriesCount);
        System.out.println(threeBitsSeriesCount);
        System.out.println(fourBitsSeriesCount);
        System.out.println(fiveBitsSeriesCount);
        System.out.println(sixBitsSeriesCount);

        System.out.println(oneBitSeriesCount + 2*twoBitsSeriesCount + 3*threeBitsSeriesCount + 4*fourBitsSeriesCount + 5*fiveBitsSeriesCount + 6*sixBitsSeriesCount);

        if(oneBitSeriesCount < 2315 || oneBitSeriesCount > 2685) return false; 
        if(twoBitsSeriesCount < 1114 || twoBitsSeriesCount > 1386) return false; 
        if(threeBitsSeriesCount < 527 || threeBitsSeriesCount > 723) return false; 
        if(fourBitsSeriesCount < 240 || fourBitsSeriesCount > 384) return false; 
        if(fiveBitsSeriesCount < 103 || fiveBitsSeriesCount > 209) return false; 
        if(sixBitsSeriesCount < 103 || sixBitsSeriesCount > 209) return false; 

        return true; 
    }

    private static boolean pokerTest(String key) {
        char[] keyArray = key.toCharArray(); 
        int[] sAtIArray = new int[16]; 

        int sum = 0; 
        for (int i = 0; i < keyArray.length - 3; i+=4) {
            char[] bits = new char[4]; 
            for (int j = 0; j < bits.length; j++) {
                bits[j]=keyArray[i+j]; 
            }

            int calculatedValue = calculateValueFromBits(bits); 
            sAtIArray[calculatedValue]++; 
        }

        for (int i : sAtIArray) {
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
