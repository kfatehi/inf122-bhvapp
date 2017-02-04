cat <<EOF | BEHAVE_FRONTEND=noninteractive java -classpath out/production/behave-app com.company.Main config.properties
login bob
add-child joe
edit-child joe joseph 
EOF
