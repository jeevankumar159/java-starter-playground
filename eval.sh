#!/bin/bash
set -e

# 1. Compile student Logger code
javac -cp . src/*.java

# 2. Set up testing dir
mkdir -p /home/damner/.labtests
cp src/TestFile.java /home/damner/.labtests/TestFile.java
cd /home/damner/.labtests

# 3. Download JUnit test runner JAR (if not cached)
[ -e junit5main.jar ] || curl -s https://raw.githubusercontent.com/codedamn-classrooms/java-junit-files/main/junit5main.jar -o junit5main.jar

# 4. Compile test file
javac -cp .:junit5main.jar TestFile.java

# 5. Run tests
java -jar junit5main.jar -cp .:/home/damner/code --select-class TestFile --reports-dir . || true

# 6. Generate boolean test results for evaluation
yarn add xml2js
cat > process-result.js << 'EOF'
const fs = require('fs');
const xml = fs.readFileSync('./TEST-junit-jupiter.xml', 'utf8');
require('xml2js').parseString(xml, (_, data) => {
  const bools = data.testsuite.testcase.map(tc => !('failure' in tc));
  fs.writeFileSync(process.env.UNIT_TEST_OUTPUT_FILE, JSON.stringify(bools));
});
EOF
node process-result.js
