
SQLite test on 2020-02-25 (time-based)
----------------------------------------------

UUID Type: ULID-based
Threads: 16
UUIDs per thread: 1 million

#### Number of rows after inserted: 16 million

```
sqlite> select count(*) from tb_uuid;
16000000
```

#### Number of distinct UUIDs inserted: 16 million

```
sqlite> select count(distinct uuid_binary) from tb_uuid;
16000000
```

#### Number of theads that ran during the test: 16

```
sqlite> select count(distinct uuid_threadid) from tb_uuid;
16
```

#### Number of UUIDs generated per thread: 1 million

```
sqlite> select uuid_threadid, count(distinct uuid_binary) from tb_uuid group by uuid_threadid;
214642682|1000000
339727045|1000000
580821020|1000000
653687731|1000000
717658315|1000000
721669627|1000000
773394133|1000000
776322026|1000000
843510759|1000000
1061251815|1000000
1293892638|1000000
1612581101|1000000
1663765650|1000000
1816185641|1000000
2047676928|1000000
2107900504|1000000
```

