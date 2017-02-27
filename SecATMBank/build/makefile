JFLAGS = -cp
JC = javac
JAR = ":./src/jars/javax.json-1.0.4.jar:./src/jars/commons-codec-1.4.jar:./src/jars/commons-io-2.4.jar:."
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $(JAR) $*.java

CLASSES = \
	src/atm/ATMMain.java \
	src/atm/ATM.java \
	src/bank/BankMain.java \
	src/bank/Bank.java \

default: classes
	@rm -f atm bank
	@unzip -q ./src/jars/commons-codec-1.4.jar -d . -x META-INF/*
	@unzip -q ./src/jars/commons-io-2.4.jar -d . -x META-INF/*
	@unzip -q ./src/jars/javax.json-1.0.4.jar -d . -x META-INF/*	
	@ find javax -name "*.class" > classes.txt
	@ find org -name "*.class" > classesJAVAX.txt
	@echo Main-Class: src.atm.ATMMain'\n' >> atm.MF
	@more atm.MF
	@jar -cvfm0 atm atm.MF src/atm/* @classes.txt @classesJAVAX.txt
	@chmod +x atm
	@echo Main-Class: src.bank.BankMain'\n'>> bank.MF
	@jar -cvfm0 bank bank.MF src/bank/* @classes.txt @classesJAVAX.txt
	@rm -f *.class *.MF classesJAVAX.txt classes.txt 
	@ rm -rf javax/ org/
	@chmod +x bank

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class *.MF atm bank