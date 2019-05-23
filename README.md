## uuid-creator-database

This goal of this simple project was to test the [UUID Creator](https://github.com/f4b6a3/uuid-creator) with three databases: SQLite, PostgreSQL and MySQL.

### First test on 2019-05-21

The first test was done in four steps:

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
│   │   ├── [ 48K]  queries.v2019-05-21.md
│   ├── [ 228]  postgresql
│   │   ├── [147K]  batch-inserter-log.txt
│   │   ├── [ 391]  create-database.sql
│   │   ├── [ 112]  dump-command.txt
│   │   ├── [ 42K]  queries.v2019-05-21.md
│   └── [ 272]  sqlite
│       ├── [147K]  batch-inserter-log.txt
│       ├── [  84]  create-database.sh
│       ├── [  48]  dump-command.txt
│       ├── [ 12K]  queries.v2019-05-21.md
│       └── [ 52M]  uuidcreator.v2019-05-21.sqlite.db.7z
```

I upoaded a SQLite database to the folder `db/sqlite` that can be tested with this command:

```bash
sqlite3 uuidcreator-v2019-05-21.sqlite.db
```

This repository has **100 MB** because of the SQLite database.

### Second test on 2019-05-22

The second test was done with the same steps, but only 4 threads was used, which is the number of cores in my PC. Each thread generated 10 million UUIDs, resulting the amount of 40 million UUIDs. Now I employed the SQLite database only, because it was the fastest and easiest database among the three.

The results are in the file `db/sqlite/queries.v2019-05-22.md`

Because of the limit of 100 MB per repository, I will not commit upload the SQLite database.

// the end.
