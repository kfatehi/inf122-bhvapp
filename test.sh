#!/usr/bin/env bash
export BEHAVE_FRONTEND=noninteractive
cat <<EOF | java -classpath out/production/behave-app com.company.Main config.properties
login bob
status
add-child alice
add-child john
status
EOF