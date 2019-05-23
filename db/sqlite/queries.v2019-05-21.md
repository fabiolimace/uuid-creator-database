
SQLite test on 2019-05-21
----------------------------------------------

In this FIRST test 16 threads was instantiated to generate 1 million UUIDs each.

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

#### Number of distinct clock sequences used by all thread: 950

```
sqlite> select count(distinct uuid_clockseq) from tb_uuid;
950
```

#### Number of clock sequences used by each thread: ~59 (950 / 16)

```
sqlite> select uuid_threadid, count(distinct uuid_clockseq) from tb_uuid group by uuid_threadid;
195755345|57
202877133|57
203737722|59
211796497|50
411737551|60
521971641|61
613285094|61
642279083|62
962121367|65
1087192898|60
1594765693|63
1730726863|60
1888325704|59
1980656955|58
2071827043|61
2145640378|57
```

#### Number of counter values used by each thread: 10 thousand

```
sqlite> select uuid_threadid, count(distinct uuid_counter) from tb_uuid group by uuid_threadid;

195755345|10000
202877133|10000
203737722|10000
211796497|10000
411737551|10000
521971641|10000
613285094|10000
642279083|10000
962121367|10000
1087192898|10000
1594765693|10000
1730726863|10000
1888325704|10000
1980656955|10000
2071827043|10000
2145640378|10000
```

#### Number of clock sequences used by more than one thread: ZERO

```
sqlite> select uuid_clockseq, count(distinct uuid_threadid) from tb_uuid group by uuid_clockseq having count(distinct uuid_threadid) > 1;
Empty set
```

#### Number of UUIDs generated per thread: 1 million

```
sqlite> select uuid_threadid, count(distinct uuid_binary) from tb_uuid group by uuid_threadid;
195755345|1000000
202877133|1000000
203737722|1000000
211796497|1000000
411737551|1000000
521971641|1000000
613285094|1000000
642279083|1000000
962121367|1000000
1087192898|1000000
1594765693|1000000
1730726863|1000000
1888325704|1000000
1980656955|1000000
2071827043|1000000
2145640378|1000000
```

#### Number of distinct counters values used by all threads: 10 thousand

```
sqlite> select count(distinct uuid_counter) from tb_uuid;
10000
```

#### Minimum and maximum counter values: 0 and 9,999

```
sqlite> select min(uuid_counter), max(uuid_counter) from tb_uuid;
0|9999
```

#### Minimum and maximum date and times: 2019-05-22 01:01:03 and 2019-05-22 01:01:05 (2 seconds interval)

```
sqlite> select min(datetime(uuid_datetime/1000, 'unixepoch')), max(datetime(uuid_datetime/1000, 'unixepoch')) from tb_uuid;
2019-05-22 01:01:03|2019-05-22 01:01:05
```

#### Number of UUIDs by each clock sequence

```
sqlite> select uuid_clockseq, count(distinct uuid_binary) from tb_uuid group by uuid_clockseq;
384|12193
385|14864
386|14096
387|13328
388|14352
389|21520
390|15888
391|12304
392|15376
393|15632
394|15632
395|10512
396|15120
397|59664
398|13840
399|22800
400|15632
401|14864
402|14352
403|13840
404|14096
405|17424
406|13840
407|12304
408|14608
409|14608
410|14864
411|14096
412|14608
413|14352
414|14608
415|14608
416|19984
417|10256
418|11024
419|15632
420|15888
421|14352
422|26896
423|15376
424|15376
425|14864
426|14864
427|15120
428|15376
429|15376
430|17424
431|15120
432|19728
433|14608
434|50960
435|15120
436|15632
437|14864
438|15120
439|13840
440|15376
441|15376
442|15632
443|20991
896|12760
897|15120
898|13584
899|14864
900|13840
901|14096
902|30992
903|11536
904|15376
905|16144
906|22288
907|15632
908|15120
909|15888
910|39184
911|14864
912|16144
913|14352
914|13072
915|15120
916|15376
917|19216
918|11280
919|24848
920|15376
921|15376
922|15632
923|15120
924|15888
925|14864
926|15632
927|11792
928|20240
929|17680
930|15632
931|15632
932|15120
933|14352
934|15120
935|15632
936|15376
937|14352
938|15120
939|15376
940|15376
941|14608
942|23824
943|15120
944|13328
945|24848
946|15376
947|20496
948|14608
949|15376
950|15632
951|15120
952|15120
953|15376
954|15632
955|15120
956|15032
1408|9841
1409|14096
1410|13328
1411|32528
1412|11280
1413|15376
1414|23312
1415|24080
1416|15888
1417|10256
1418|23824
1419|15632
1420|14608
1421|15120
1422|10768
1423|13328
1424|15632
1425|16400
1426|30224
1427|15376
1428|15888
1429|19216
1430|15376
1431|15632
1432|14608
1433|11024
1434|28176
1435|15632
1436|15632
1437|15632
1438|15632
1439|15888
1440|16656
1441|15888
1442|16144
1443|15376
1444|15376
1445|14864
1446|35856
1447|14352
1448|15888
1449|21264
1450|15632
1451|15120
1452|15888
1453|15632
1454|15632
1455|34832
1456|15632
1457|15376
1458|15632
1459|15632
1460|15376
1461|15888
1462|15376
1463|15632
1464|15376
1465|15888
1466|10559
1920|9755
1921|22544
1922|21520
1923|14096
1924|13072
1925|14352
1926|14608
1927|10000
1928|12048
1929|15632
1930|15888
1931|10768
1932|34064
1933|15120
1934|15632
1935|10768
1936|11024
1937|15888
1938|15632
1939|16144
1940|11280
1941|15632
1942|15632
1943|22544
1944|21264
1945|14864
1946|15632
1947|15376
1948|15120
1949|14352
1950|14096
1951|15376
1952|20240
1953|11536
1954|25360
1955|13840
1956|14864
1957|49936
1958|14352
1959|14096
1960|14352
1961|14608
1962|14864
1963|13328
1964|13328
1965|12560
1966|14096
1967|14864
1968|14352
1969|14864
1970|21264
1971|14096
1972|14608
1973|14864
1974|14608
1975|14608
1976|14352
1977|14864
1978|14096
1979|14608
1980|14352
1981|14608
1982|13909
2432|10973
2433|19216
2434|10000
2435|29712
2436|11024
2437|14608
2438|10000
2439|11024
2440|10256
2441|17936
2442|25104
2443|11280
2444|15632
2445|18704
2446|14352
2447|21008
2448|15120
2449|14864
2450|15888
2451|15120
2452|15120
2453|15120
2454|14864
2455|13072
2456|14096
2457|15120
2458|10768
2459|12304
2460|13840
2461|14864
2462|14608
2463|14352
2464|15376
2465|14608
2466|14864
2467|25360
2468|13328
2469|15888
2470|21008
2471|10000
2472|20752
2473|15632
2474|14608
2475|15888
2476|15120
2477|15888
2478|15376
2479|15632
2480|14864
2481|15632
2482|17424
2483|15120
2484|17936
2485|12560
2486|15120
2487|15376
2488|15632
2489|15376
2490|14864
2491|21520
2492|15632
2493|13584
2494|13584
2495|15632
2496|10867
2944|19046
2945|11792
2946|14096
2947|12560
2948|25104
2949|12816
2950|22288
2951|13072
2952|21008
2953|18192
2954|13840
2955|14864
2956|22032
2957|14096
2958|28944
2959|15888
2960|15632
2961|14608
2962|14096
2963|10256
2964|35856
2965|11792
2966|15632
2967|15120
2968|15888
2969|15376
2970|15888
2971|14864
2972|15376
2973|15376
2974|15376
2975|15632
2976|13584
2977|12560
2978|16144
2979|15888
2980|18192
2981|15632
2982|15632
2983|15888
2984|24080
2985|15888
2986|15376
2987|15632
2988|15632
2989|15376
2990|15888
2991|15632
2992|16656
2993|12560
2994|15632
2995|15376
2996|11024
2997|14864
2998|15120
2999|21008
3000|11792
3001|15632
3002|15632
3003|15632
3004|15632
3005|10010
3456|9977
3457|10256
3458|13840
3459|20496
3460|13584
3461|29456
3462|14864
3463|31504
3464|15888
3465|10000
3466|10768
3467|19216
3468|14608
3469|15376
3470|20240
3471|30992
3472|14608
3473|14096
3474|10256
3475|15632
3476|15376
3477|26896
3478|14608
3479|15632
3480|15376
3481|26384
3482|14864
3483|15632
3484|15632
3485|15376
3486|15888
3487|24080
3488|15888
3489|16144
3490|15632
3491|14608
3492|25360
3493|15376
3494|15376
3495|14864
3496|14096
3497|15376
3498|15632
3499|15632
3500|14864
3501|15632
3502|15376
3503|31248
3504|15376
3505|14864
3506|11024
3507|10256
3508|15376
3509|15120
3510|15376
3511|15376
3512|15120
3513|15376
3514|14864
3515|15632
3516|3735
3968|9762
3969|10000
3970|12048
3971|13584
3972|21264
3973|13072
3974|28176
3975|12304
3976|15632
3977|15632
3978|42768
3979|14608
3980|14096
3981|15888
3982|11024
3983|11792
3984|32016
3985|15632
3986|15120
3987|15376
3988|15632
3989|14608
3990|12304
3991|14096
3992|21776
3993|14608
3994|14864
3995|30992
3996|18192
3997|13072
3998|13328
3999|14352
4000|12560
4001|11536
4002|25616
4003|16912
4004|14864
4005|28688
4006|15120
4007|15376
4008|15120
4009|24080
4010|14352
4011|15632
4012|19984
4013|14608
4014|49168
4015|15888
4016|15376
4017|15888
4018|19728
4019|14864
4020|15120
4021|31760
4022|14352
4023|15376
4024|10414
4480|10629
4481|15120
4482|53520
4483|11024
4484|13584
4485|25360
4486|15120
4487|25616
4488|12304
4489|15632
4490|15376
4491|15632
4492|28688
4493|15376
4494|15376
4495|22288
4496|15632
4497|15120
4498|15632
4499|16400
4500|19984
4501|15376
4502|15376
4503|15632
4504|12048
4505|22032
4506|15632
4507|15120
4508|15120
4509|15376
4510|14864
4511|13328
4512|25360
4513|15376
4514|15888
4515|15376
4516|15120
4517|15888
4518|15376
4519|15376
4520|12816
4521|15376
4522|15376
4523|15120
4524|16656
4525|15376
4526|15376
4527|16912
4528|11792
4529|17936
4530|10768
4531|15632
4532|15120
4533|15376
4534|14096
4535|12304
4536|56592
4537|10256
4538|43
4992|12395
4993|14096
4994|14096
4995|13584
4996|15632
4997|10512
4998|10000
4999|11536
5000|21264
5001|11280
5002|25360
5003|10256
5004|11536
5005|13840
5006|12304
5007|27152
5008|15120
5009|11536
5010|18448
5011|10000
5012|19472
5013|14352
5014|15632
5015|30736
5016|10512
5017|11536
5018|15632
5019|14096
5020|15632
5021|14864
5022|15376
5023|15120
5024|15632
5025|15120
5026|15376
5027|22032
5028|15376
5029|16144
5030|15632
5031|13328
5032|15120
5033|14864
5034|14352
5035|23312
5036|14352
5037|60432
5038|15632
5039|15376
5040|21776
5041|23568
5042|15888
5043|15120
5044|15376
5045|15376
5046|15632
5047|14608
5048|15632
5049|15632
5050|13584
5051|32821
5504|17238
5505|13840
5506|14352
5507|14096
5508|14608
5509|13840
5510|12304
5511|19472
5512|10000
5513|11792
5514|11536
5515|16912
5516|11792
5517|11536
5518|11536
5519|11536
5520|13072
5521|27152
5522|13584
5523|15120
5524|15888
5525|36880
5526|18960
5527|28432
5528|15120
5529|15632
5530|15376
5531|15632
5532|15376
5533|32016
5534|45328
5535|15888
5536|15632
5537|15888
5538|14864
5539|15120
5540|53776
5541|15888
5542|14096
5543|15632
5544|15632
5545|15888
5546|12048
5547|15376
5548|14096
5549|13840
5550|21776
5551|15376
5552|15888
5553|15632
5554|17936
5555|11536
5556|15632
5557|14608
5558|14352
5559|43536
5560|10000
5561|106
6016|11139
6017|12816
6018|13840
6019|22032
6020|10000
6021|11280
6022|12048
6023|38416
6024|38416
6025|10000
6026|15632
6027|15120
6028|14608
6029|12816
6030|11792
6031|10768
6032|15376
6033|15632
6034|16144
6035|14608
6036|19472
6037|28176
6038|19728
6039|19984
6040|14352
6041|15632
6042|15376
6043|15376
6044|15376
6045|14608
6046|15376
6047|15376
6048|18960
6049|15888
6050|14864
6051|16144
6052|14864
6053|25104
6054|13584
6055|15632
6056|14864
6057|15376
6058|15376
6059|15376
6060|14608
6061|21008
6062|19472
6063|27408
6064|10256
6065|11280
6066|15376
6067|26384
6068|15632
6069|16144
6070|15120
6071|69136
6072|10829
6528|28746
6529|14096
6530|14352
6531|14864
6532|12048
6533|14864
6534|17680
6535|12048
6536|14096
6537|14864
6538|33808
6539|15632
6540|12816
6541|15120
6542|24848
6543|13328
6544|13328
6545|15120
6546|14096
6547|14096
6548|13840
6549|13840
6550|18448
6551|23824
6552|11792
6553|14352
6554|14352
6555|14096
6556|14096
6557|21776
6558|15120
6559|15632
6560|14096
6561|23056
6562|15888
6563|38672
6564|14864
6565|14352
6566|15632
6567|13840
6568|15376
6569|13072
6570|14608
6571|15120
6572|15376
6573|15120
6574|15632
6575|15120
6576|15632
6577|15120
6578|15632
6579|17424
6580|11280
6581|23824
6582|15376
6583|15376
6584|15632
6585|34832
6586|14864
6587|14166
7040|22363
7041|14352
7042|14352
7043|14096
7044|14864
7045|12560
7046|14864
7047|10768
7048|14352
7049|15888
7050|23824
7051|12560
7052|11536
7053|14864
7054|15632
7055|23056
7056|23056
7057|19216
7058|16144
7059|21520
7060|13584
7061|15632
7062|18704
7063|15376
7064|15632
7065|15376
7066|14096
7067|13840
7068|18704
7069|14096
7070|14864
7071|14096
7072|15120
7073|21008
7074|10768
7075|15888
7076|23312
7077|13840
7078|15632
7079|14608
7080|25872
7081|10256
7082|15632
7083|15376
7084|15120
7085|15376
7086|15376
7087|15120
7088|22288
7089|15376
7090|20496
7091|15632
7092|15120
7093|14864
7094|17424
7095|14352
7096|12048
7097|13328
7098|26896
7099|31504
7100|8501
7552|46871
7553|45584
7554|14608
7555|22800
7556|10000
7557|50960
7558|18448
7559|14608
7560|15632
7561|10512
7562|11280
7563|22544
7564|15632
7565|15120
7566|15120
7567|43536
7568|20752
7569|15376
7570|14096
7571|13072
7572|13072
7573|11280
7574|16656
7575|12304
7576|13840
7577|47376
7578|16144
7579|15888
7580|26640
7581|27664
7582|14096
7583|15632
7584|15632
7585|15632
7586|15120
7587|15632
7588|15632
7589|15376
7590|15376
7591|49936
7592|14352
7593|14352
7594|14864
7595|54288
7596|15632
7597|15376
7598|15632
7599|13328
7600|15632
7601|1065
8064|29990
8065|42512
8066|27408
8067|10000
8068|11536
8069|13584
8070|15888
8071|27664
8072|12048
8073|14864
8074|13584
8075|13328
8076|20752
8077|14864
8078|12560
8079|15120
8080|14096
8081|26128
8082|23824
8083|16400
8084|14352
8085|13584
8086|13584
8087|25360
8088|25104
8089|14608
8090|15632
8091|25360
8092|15632
8093|14608
8094|32016
8095|37904
8096|15376
8097|14096
8098|15632
8099|12048
8100|10512
8101|14608
8102|14864
8103|14864
8104|12304
8105|14608
8106|15120
8107|16656
8108|30480
8109|14864
8110|16144
8111|15376
8112|16400
8113|15632
8114|15376
8115|15632
8116|15376
8117|15120
8118|15120
8119|15632
8120|4266
```
