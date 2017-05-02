# ASM Metrics Plugin

## Installation
Steps to generate static analysis:

1) Build metrics-maven-plugin inside the directory linked above
     mvn clean install

2) Download/clone any project where you want to run metrics-maven-plugin. (e.g: common-dbutils)

3) Add metrics-maven-plugin plugin to project pom under ```<build><plugins>```.

```xml
<plugin>
 <groupId>edu.utdallas</groupId>
 <artifactId>metrics-maven-plugin</artifactId>
 <version>1.0-SNAPSHOT</version>
 <executions>
   <execution>
     <goals>
       <goal>metrics</goal>
     </goals>
   </execution>
 </executions>
</plugin>
```

4) Build project under test
    mvn metrics:metrics

5) Results are generated in method-metrics.txt

## Projects for testing the plugin
| Project Name | Github URL | SHA |
| ------ | ------ | ------ |
| common-dbutils | https://github.com/apache/commons-dbutils | 633749db5b0fd25b9a3ca133e7496a353de4fd5d | 
| joda-time | https://github.com/JodaOrg/joda-time | acff94148b2110b95f7aeae6a1bdcafb756061f0 |

## Example Output

For a certain method, the static analysis would look something like this
```
Method Name: org.apache.commons.dbutils.AbstractQueryRunner.fillStatement
Unique Number of operators:
2

All Number of operators:
6

Number of Statements:
31

Variables Referenced:
1

Number of expressions:
2

Class References:
2

Number of casts:
2

Number of loops:
18

Arguments:
2

Modifiers:
transient

Number of external methods:
9

Exceptions Referenced:
2

Number of operands:
13

Exceptions Thrown:
1

Halstead length:
6

Halstead Vocabulary:
2

Halstead Difficulty:
0

Halstead Volume:
4.1588830833596715

Halstead Effort:
0.0

Halstead Bugs:
0.0013862943611198904
```

### Note
Metrics which have a value of 0 are not shown in the static analysis for a certain method.

#### Example:

If a certain method does not throw an exception then the metrics 'Exceptions thrown' will not be displayed in the static analysis of the method.

If a certain method throws one or more exceptions then the metrics 'Exceptions thrown' will be displayed in the static analysis of the method (along with the list of exceptions)


