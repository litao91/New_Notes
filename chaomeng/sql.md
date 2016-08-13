# Queries

Mean of each device

```sql
SELECT name,address,lat as x, lng as y, dids as device_id FROM shops as s
LEFT JOIN (
SELECT 
shop_id, GROUP_CONCAT(device_id) as dids
FROM devices
GROUP BY shop_id) as d
on d.shop_id=s.id;
```

Export data for Huifan

```sql
select devices.user_id, users.username, count(devices.id) 
FROM devices,users 
WHERE devices.user_id = users.id  
AND devices.created BETWEEN '2016-07-01 00:00:00' and '2016-07-31 23:59:59' 
GROUP BY devices.user_id,users.username;
```
