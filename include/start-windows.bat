where java >nul 2>nul
if %errorlevel%==1 (
    @echo You need Java to run the randomizer.
    exit
)

java -jar botw-randomizer*.jar