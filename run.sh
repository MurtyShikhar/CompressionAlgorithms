#!/bin/bash
echo $1 $2 $3
cd bin
if [ $3 == "+" ];then
	java BurrowsWheeler - < ../tests/$1 | java MoveToFront - | java Huffman - > ../tests/$2
else
	java Huffman + < ../tests/$1 | java MoveToFront + | java BurrowsWheeler + > ../tests/$2
fi