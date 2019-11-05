import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Random;

public class Generator {

    private static final long defaultKeyLength = 20000; 
    private static Random random = new Random(); 
    private static final int maxFactor = (int) Math.pow(2, 10); 

    private static boolean isPrimeNumber(int x) {
        for(int i = 2; i * i <= x ; i++) {
            if(x % i == 0) return false; 
        }

        return true; 
    }

    private static int getPrimeNumber() {
        int number = random.nextInt(maxFactor);
        int primeNumber = number * 4 + 3;

        while(!isPrimeNumber(primeNumber)) {
            number++; 
            primeNumber = number * 4 + 3;
        }

        return primeNumber; 
    }

    private static long getNWD(long x, long y) {
        long temp = y; 

        while(y > 0) {
            temp = y; 
            y = x % y; 
            x = temp; 
        }

        return x; 
    }

    private static long getCoprimeNumber(long x) {
        long result = random.nextLong() % x; 

        while(getNWD(result, x) != 1) {
            result = (result + 1) % x; 
            if(result==1) result = random.nextLong() % x; 
        } 

        return result; 
    }
    public static void main(String[] args) throws IOException {

        String filename = "key" + Calendar.getInstance().getTimeInMillis() + ".txt"; 
        long keyLength = defaultKeyLength; 

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-k")) {
                keyLength = Long.parseLong(args[++i]);  
            }

            else if (args[i].equals("-f")) {
                filename = args[++i]; 
            }
        }

        int primeNumberQ = getPrimeNumber(); 
        int primeNumberP = getPrimeNumber(); 

        // System.out.println(primeNumberQ);
        // System.out.println(primeNumberP);

        long numberN = primeNumberP * primeNumberQ; 
        // System.out.println(numberN);

        long numberX = getCoprimeNumber(numberN); 
        // System.out.println(numberX);

        String key = ""; 

        System.out.println("Generating key of length = " + keyLength);


        long keyElement = (numberX * numberX) % numberN; 
        for(int i=1; i <= keyLength; i++) {
            keyElement = (keyElement * keyElement) % numberN;

            char keyBit = (keyElement & 1) == 1 ? '1' : '0'; 
            key +=  keyBit;  
        }

        System.out.println("Writing to file " + filename);
        Files.write(Paths.get(filename), key.getBytes()); 

    }
}