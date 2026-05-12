#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
mkdir -p target/classes
javac -encoding UTF-8 -d target/classes \
  src/main/java/ru/practice/case3/model/Person.java \
  src/main/java/ru/practice/case3/model/StaffRole.java \
  src/main/java/ru/practice/case3/model/Worker.java \
  src/main/java/ru/practice/case3/validation/WorkerInputValidator.java \
  src/main/java/ru/practice/case3/util/RuInputFormats.java \
  src/main/java/ru/practice/case3/data/InitialWorkers.java \
  src/main/java/ru/practice/case3/data/WorkerFileStorage.java \
  src/main/java/ru/practice/case3/input/PositionPicker.java \
  src/main/java/ru/practice/case3/ConsoleApp.java
exec java -cp target/classes ru.practice.case3.ConsoleApp
