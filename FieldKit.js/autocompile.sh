#!/bin/bash
for (( ; ; ))
do
	echo "--"
	ant -f build.xml
	sleep 2
done