!#/bin/bash

echo "Creating the database...";

# Default login as root

# Create database (name caltrack) and user caltrack_db
echo "Creating database with name \"caltrack\"";
mysql -u root -Bse "CREATE DATABASE caltrack;"
mysql -u root -Bse "CREATE USER 'caltrack_db'@'' IDENTIFIED BY 'CalTrackF1T!';";
mysql -u root -Bse "GRANT ALL PRIVILEGES ON caltrack.* TO 'caltrack_db'@'' WITH GRANT OPTION;";

# Run create tables script
echo "Running create table script";
mysql -u caltrack_db -pCalTrackF1T! caltrack < create.sql;

# Run insert tables script
echo "Running insert table script";
mysql -u caltrack_db -pCalTrackF1T! caltrack < insert.sql;

echo "Done.";
exit 0;
