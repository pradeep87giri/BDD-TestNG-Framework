set DIR_HOME=%~dp0
cd %DIR_HOME%
start java -Dwebdriver.ie.driver=%DIR_HOME%\IEDriverServer.exe -Dwebdriver.chrome.driver=%DIR_HOME%\chromedriver.exe  -Dwebdriver.gecko.driver=%DIR_HOME%\geckodriver.exe -jar selenium-server-standalone-3.8.1.jar -role node -nodeConfig node1.json

