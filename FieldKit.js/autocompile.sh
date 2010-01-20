#!/bin/bash
for (( ; ; ))
do
	echo "--"
	ant -q -f build.xml compile
	sleep 1
done