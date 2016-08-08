## IP Address

112.126.83.108 worker -- 573884 
101.200.218.39 master 
 

Chaomeng Cluster 
ChaomengSpark! 
 
 
Hadoop User 
ChaomengHadoop! 
 
RabbitMQ 
User: cm_data 
Passwd: ChaomengParser! 
 
Management plugin 
http://101.200.218.39:15672/ 
 
Celery 
Flower: data.ichaomeng.com:5555 
```
celery -A chaomeng flower --basic_auth=cm_data:cmdata2016 
celery -A chaomeng worker -l info 
celery -A chaomeng worker -l info 
```
 
 
 
 
## Parser
 
```
java -cp ./TradeParser-1.0-SNAPSHOT-jar-with-dependencies.jar:./ com.chaomeng.trade.exe.ServerApp 8088
```


## DB

Connect to psql

```
psql -h $CM_DB_HOST -U $CM_DB_NAME
```
