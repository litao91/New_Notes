# Queries

Mean of each device

```
SELECT name,address,lat as x, lng as y, dids as device_id FROM shops as s
LEFT JOIN (
SELECT 
shop_id, GROUP_CONCAT(device_id) as dids
FROM devices
GROUP BY shop_id) as d
on d.shop_id=s.id;
```
