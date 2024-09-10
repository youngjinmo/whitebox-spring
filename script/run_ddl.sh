#!/bin/bash

set -e # exit if error happen

# result message
function printFailMessage() {
  echo "#########################################"
  echo "######## FAILED TO EXECUTION DDL ########"
  echo "#########################################"
}

function printSuccessResult() {
  echo "==========================================="
  echo "=== DDL script executed successfully..! ==="
  echo "==========================================="
}

# MySQL connection info
source ./mysql_config.sh

# load sql file
TARGET_SQL="../sql/ddl.sql"

echo "DDL script executing..."

trap printFailMessage EXIT

# run ddl
mysql -u $USER -p$PASSWORD $DATABASE < $TARGET_SQL

printSuccessResult
