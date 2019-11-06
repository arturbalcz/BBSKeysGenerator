import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.Random;

public class Generator {

    private static final long defaultKeyLength = 20000; 
    private static Random random = new Random(); 
    private static final int randomNumberBitLength = 20; 

    private static final BigInteger numberToMultiply = new BigInteger("4"); 
    private static final BigInteger numberToAdd = new BigInteger("3"); 

    private static BigInteger getPrimeNumber() {
        BigInteger number = new BigInteger(randomNumberBitLength, random); 
        BigInteger primeNumber = number.multiply(numberToMultiply).add(numberToAdd);

        while(!primeNumber.isProbablePrime(100)) {
            number = number.add(BigInteger.ONE); 
            primeNumber = number.multiply(numberToMultiply).add(numberToAdd);        }

        return primeNumber; 
    }

    private static BigInteger getCoprimeNumber(BigInteger x) {
        BigInteger result = new BigInteger(randomNumberBitLength, random); 

        while(!result.gcd(x).equals(BigInteger.ONE)) {
            result = result.add(BigInteger.ONE).mod(x);  
        } 

        return result; 
    }
    public static void main(String[] args) throws IOException {

        String filename = "key" + LocalTime.now().getNano() + ".txt"; 
        long keyLength = defaultKeyLength; 

        for(int i = 0; i < args.length; i++) {
            if(args[i].equals("-k")) {
                keyLength = Long.parseLong(args[++i]);  
            }

            else if (args[i].equals("-f")) {
                filename = args[++i]; 
            }
        }
        BigInteger primeNumberQ = getPrimeNumber(); 
        // System.out.println(primeNumberQ);

        BigInteger primeNumberP = getPrimeNumber(); 
        // System.out.println(primeNumberP);

        BigInteger numberN = primeNumberP.multiply(primeNumberQ); 
        // System.out.println(numberN);

        BigInteger numberX = getCoprimeNumber(numberN); 
        // System.out.println(numberX);

        String key = ""; 

        System.out.println("Generating key of length = " + keyLength);


        BigInteger keyElement = numberX.multiply(numberX).mod(numberN); 
        for(int i=1; i <= keyLength; i++) {
            keyElement = keyElement.multiply(keyElement).mod(numberN);

            char keyBit = keyElement.and(BigInteger.ONE).equals(BigInteger.ONE) ? '1' : '0'; 
            key +=  keyBit;  
        }

        System.out.println("Writing to file " + filename);
        Files.write(Paths.get(filename), key.getBytes()); 

    }
}