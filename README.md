# BBSKeysGenerator
### Project for CS studies 

Simple program for generating BBS keys  
The default length of random prime numbers p and q is 20 bits  
The default generated key length is 20000 bits  

### Compilation and run 
javac Generator.java  
java Generator -k <key_length> -f <output_file>  

## Statistic tests 
There is also program performing 4 FIPS	140-2 statistic tests for generated key  

### Compilation and tests run 
javac StatisticTests.java  
java StatisticTests -f <input_file> 
