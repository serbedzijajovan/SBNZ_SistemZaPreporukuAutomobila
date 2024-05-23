#!/bin/bash

DIRECTORIES=("kjar" "model" "service")

# Loop through each directory and perform Maven commands
for DIR in "${DIRECTORIES[@]}"; do
  # Check if the directory exists
  if [ -d "$DIR" ]; then
    # Change to the target directory
    cd "$DIR" || exit

    # Perform Maven clean
    mvn clean
    # echo "Maven clean completed in $DIR"

    # Perform Maven install
    mvn install
    # echo "Maven install completed in $DIR"

    # Go back to the original directory
    cd - || exit
  else
    echo "Directory $DIR does not exist"
    exit 1
  fi
done