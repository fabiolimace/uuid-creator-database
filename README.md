## uuid-creator-database

This goal of this simple project was to test the [UUID Creator](https://github.com/f4b6a3/uuid-creator) with three databases: sqlite, postgresql and mysql.

This test was done in four steps:

1. Instantiate 16 threads;
2. Make each thread to generate a list of 1 million time-based UUIDs (version 1);
3. Insert each list of 1 million UUIDs in the database;
4. Check the 16 million records inserted in the database via database queries.

The queries used to check the records in the database are in the directory `db`. This is the directory structure:

```text
├── [  42]  db
│   ├── [ 218]  mysql
│   │   ├── [147K]  batch-inserter-log.txt
│   │   ├── [ 563]  create-database.sql
│   │   ├── [ 307]  dump-command.txt
│   │   ├── [ 48K]  queries.txt
│   ├── [ 228]  postgresql
│   │   ├── [147K]  batch-inserter-log.txt
│   │   ├── [ 391]  create-database.sql
│   │   ├── [ 112]  dump-command.txt
│   │   ├── [ 42K]  queries.txt
│   └── [ 272]  sqlite
│       ├── [147K]  batch-inserter-log.txt
│       ├── [  84]  create-database.sh
│       ├── [  48]  dump-command.txt
│       ├── [ 12K]  queries.txt
│       └── [ 52M]  uuidcreator.v2019-05-21.sqlite.db.7z
```

There's a Sqlite database in the folder `db/sqlite` that can be tested with this command:

```bash
sqlite3 uuidcreator-v2019-05-21.sqlite.db
```

This repository has **100 MB** because of the Sqlite database.

// the end.
