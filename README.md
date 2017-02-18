# bhv app

# Instructions

## Running from jar

```
echo "users.admin.type=Parent" > state.properties
java -jar behave-app.jar state.properties
```

## Running from source

### Interactively:

```
cd behave-app
java -classpath out/production/behave-app com.company.Main config.properties
```

### Non-interactive test script:

```
cd behave-app
bash test.sh
```

## Caveats

The program requires a path to Properties file
The config.properties file is the "database"
In order to be able to login as a user "admin" you
may want to reset it using the following bash or similar:

```
echo "users.admin.type=Parent" > state.properties
```

## Test Commands

The test script (behave-app/test.sh) uses the following commands,
in a series of executions, testing both persistence and UI. Not every
command is invoked through this script (e.g. I omit delete, edit, schedule)

```
login admin
add-child alice
set-mode alice positive
set-redemption alice 3
add-token alice cleaned room
add-token alice did homework
add-token alice baked cookies
add-token alice made bed

login admin
status
logout
login alice
tokens
redeem

login alice
tokens
```