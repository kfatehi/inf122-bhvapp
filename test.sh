#!/usr/bin/env bash
export BEHAVE_FRONTEND=noninteractive

function behave() {
  java -classpath out/production/behave-app com.company.Main config.properties
}

cat <<EOF > config.properties
users.admin.type=Parent
EOF

clear

cat <<EOF | behave
login admin
add-child alice
set-mode alice positive
set-redemption alice 3
add-token alice cleaned room
add-token alice did homework
add-token alice baked cookies
add-token alice made bed
EOF

cat <<EOF | behave
login admin
status
logout
login alice
tokens
redeem
tokens
EOF
