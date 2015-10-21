#!/bin/bash
if [ ! -d "bin" ]; then
	mkdir bin
fi
javac -sourcepath src -d bin src/SuffixSort.java
javac -sourcepath src -d bin src/CircularSuffixArray.java
javac -sourcepath src -d bin src/HuffMan.java
javac -sourcepath src -d bin src/BurrowsWheeler.java
javac -sourcepath src -d bin src/MoveToFront.java
