## Makefile for BuritoJava -> TacoJava compiler
## Requires: jtb132.jar, javacc.jar (or javacc on PATH)
##
## Steps:
##   1. jtb transforms buritojava.jj -> jtb.out  (JTB-annotated grammar + syntaxtree/ + visitor/)
##   2. javacc compiles jtb.out                  -> BuritoJavaParser.java + token classes
##   3. javac compiles everything                -> .class files
##
## Adjust JTB_JAR and JAVACC_JAR paths to match your installation.

JTB_JAR    = jtb132.jar

SRC_GRAMMAR = buritojava.jj

all: compile

compile: BuritoJavaParser.java SymbolTable.java SymbolTableVisitor.java \
         CodeGenContext.java CodeGenVisitor.java P2.java
	javac -cp . \
	      SymbolTable.java SymbolTableVisitor.java \
	      CodeGenContext.java CodeGenVisitor.java \
	      P2.java

## --- Run the compiler ---
run: compile
	java P2 input.java > output.java

clean:
	rm -f *.class syntaxtree/*.class visitor/*.class jtb.out
	rm -f BuritoJavaParser.java TokenMgrError.java ParseException.java
	rm -f Token.java SimpleCharStream.java BuritoJavaParserConstants.java
	rm -f BuritoJavaParserTokenManager.java

.PHONY: all compile run clean
