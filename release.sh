#Clean
rm -rf ./release

#Build program
mvn clean package

#Prepare dir
mkdir release

#Copy program
cp ./target/UniversalSP-1.0-SNAPSHOT.jar ./release/USP.jar

#Copy additional data
cp -r ./data ./release/data
