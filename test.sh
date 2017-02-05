#!/usr/bin/env bash
export BEHAVE_FRONTEND=noninteractive

cat <<EOF > config.properties
users.admin.type=Parent
EOF

cat <<EOF | java -classpath out/production/behave-app com.company.Main config.properties
login admin
add-child alice
add-child john
status
EOF