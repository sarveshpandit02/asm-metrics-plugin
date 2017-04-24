# asm-metrics-plugin
Steps to generate static analysis:

1) Build asm-metrics-plugin inside the directory linked above
     mvn clean install

2) Download/clone any project where you want to run static analysis plugin. (e.g: common-dbutils)

3) Add code-analysis plugin to project pom under <build><plugins>.

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

4) Build project under test
    mvn metrics:metrics

5) Results are generated in method-metrics.txt
