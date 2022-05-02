# What is this?
Distributed computing project created for educational purposes. Hadoop job can calculate maximum sharpe ratio of securities. 

# Requirements
* Java 17 (tested on 17.0.2)
* Maven 3 (tested on 3.8.1)
* Hadoop 3 (tested on 3.3.2)

# Getting started
Start **Application::main**.

Input and output directories can be passed as program arguments. E.g.:
```shell
java -jar target/distributed-financial-analysis-1.0.jar /input /output
```

Default input directory is `src/main/resources/input` and default output directory is `src/main/resources/output`.

Each line in input files has to have a format `SYMBOL,FROM_DATE,TO_DATE`. Example can be found in the default input directory.

Output file will contain one line with stock symbol and corresponding maximum sharpe ratio. Example can be found in the default output directory.

# TODO
* Higher test coverage