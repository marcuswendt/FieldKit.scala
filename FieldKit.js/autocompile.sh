#!/bin/bash
for (( ; ; ))
do
	echo "--"
	ant -q -f build.xml
	sleep 1
done