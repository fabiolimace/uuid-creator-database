
# Log in as root
shell> sudo mysql -u root -p

# Create a new database
mysql> CREATE DATABASE uuidcreator;

# Select the database
mysql> use uuidcreator;

# Create user 'mysql'
mysql> CREATE USER 'uuidcreator'@'localhost' IDENTIFIED BY '123456';

# Grant privileges to 'uuidcreator'
mysql> grant all privileges on uuidcreator.* to 'uuidcreator'@'localhost';

# Grant privilege to dump file
# For Ubuntu, edit the file /etc/mysql/mysql.conf.d/mysqld.cnf and add the following line at the end:
# secure_file_priv=""
GRANT FILE ON *.* TO 'uuidcreator'@'localhost';
