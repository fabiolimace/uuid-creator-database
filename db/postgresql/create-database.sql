
# Log in as postgres
sudo psql -u postgres

# Create a new database
CREATE DATABASE uuidcreator;

# Select the database
\c databasename;

# Create user 'uuidcreator'
CREATE ROLE uuidcreator LOGIN PASSWORD '123456';

# alter owner of database
ALTER DATABASE uuidcreator OWNER TO uuidcreator;

# Grant privileges to 'uuidcreator'
grant all privileges on database uuidcreator to uuidcreator;

