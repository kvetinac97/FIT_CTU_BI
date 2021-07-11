#!/bin/bash

# Usage: ./script.sh <number>
bin/qparse --query=queries/query$1.sparql --explain
bin/sparql --data=data/data.xml --query=queries/query$1.sparql