#Path separator is different on Windows versus Unix based OSes
ifeq ($(OS),Windows_NT)
	SEP=;
else
	SEP=:
endif

JFLAGS = -g
JCLASS = -cp "src$(SEP).$(SEP)../junit-4.5.jar"
JC = javac
JVM = java

.PHONY: test doc

test:
	find . -name '*.class' -exec rm -f {} \;
	$(JC) $(JCLASS) $(JFLAGS) src/TestGameBoard.java
	$(JVM) $(JCLASS) org.junit.runner.JUnitCore src.TestGameBoard

doc:
	doxygen doxConfig
	cd latex && $(MAKE)

clean:
	rm -rf html
	rm -rf latex
	cd src
	rm **/*.class