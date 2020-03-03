set DIR_HOME=%~dp0
cd %DIR_HOME%
start java -jar selenium-server-standalone-3.8.1.jar -role hub -hubConfig hubconfig.json

