# Geo Distance Search with MySQL

## Task
Find 10 nearby hotels and sort by distance. 

What do we have:

* Given point on Earth: Latitude, Longitude
* Hotels table Name, Lat, Lng

## The Haversine Formula

* Sphere of radius $$R$$
* Latitudes $$\phi_1$$ and $$\phi_2$$
* Latitude separation $$\Delta \phi = \phi_1 - \phi_2$$
* Longitude separation $$\Delta\lambda$$ 
* distance $$d$$ between the two points

$$\text{haversin}(\frac{d}{R}) = \text{haversin}(\Delta \phi) + \cos(\phi_1)\cos(\phi_2) \text{haversin}(\Delta\lambda)$$

$$\text{haversin}(\theta) = \frac{\text{versine}(\theta)}{2} = \sin^2(\frac{\theta}{2})$$

$$\text{versin}(\theta) = 1 - \cos(\theta) = 2 \sin^2(\frac{\theta}{2})$$


## Haversine Formula in MySQL

$$R = \text{earth's radius} = 3956$$

$$\Delta \text{lat} = \text{lat}_1 - \text{lat}_2$$

$$\Delta \text{long}$$

$$ a = \sin^2 (\Delta \text{lat} / 2) + \cos(\text{lat}_1) \cdot \cos(\text{lat}_2) \cdot \sin^2(\Delta \text{long} /2 )$$

$$ c = 2 \cdot \text{atan}^2(\sqrt{a}, \sqrt{1-a})$$

$$ d = R \cdot c$$



### The corresponding SQL

```sql
3956 * 2 * ASIN (SQRT(
    POWER(SIN((orig.lat - dest.lat)*pi()/180/2),2) + 
    COS(orig.lat * pi()/180) * COS(dest.lat * pi() / 180) * POWER(SIN((orig.lon - dest.lon) * pi() / 180 / 2), 2)))
AS distance
```

```sql
set @orig_lat=122.4058; 
set @orig_lon=37.7907;set @dist=10;

SELECT *,
3956 * 2 * ASIN(
  SQRT(
    POWER(SIN((@orig_lat -abs(dest.lat)) * pi()/180 / 2),2) + 
          COS(@orig_lat * pi() / 180 ) * 
          COS(abs(dest.lat) *  pi()/180) * 
          POWER(SIN((@orig_lon – dest.lon) * pi()/180 / 2), 2)))
AS distance
FROM hotels dest
HAVING distance < @dist
ORDER BY distance limit 10;
```

This query is very slow for web query


We don't need the hotel very far away. 

* 1 of latitude `~= 69 miles`
* 1 of longitude`~= cos(latitude)*69`


To calculate lon and lat for the rectantle

```sql
set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
set lat1 = mylat-(dist/69);
set lat2 = mylat+(dist/69);
```


The modifield query:

```sql
SELECT destination.*,
3956 * 2 * ASIN(SQRT( POWER(SIN((orig.lat - dest.lat) *  pi()/180 / 2), 2) +
COS(orig.lat * pi()/180) * COS(dest.lat * pi()/180) * 
POWER(SIN((orig.lon -dest.lon) * pi()/180 / 2), 2)))
AS distance
FROM users destination, users origin
WHERE origin.id=userid
AND destination.longitude between lon1 and lon2
AND destination.latitude between lat1 and lat2
```


## Store the procedure
```sql
CREATE PROCEDURE geodist (IN userid int, IN dist int)
  BEGIN
declare mylon double; 
declare mylat double;
declare lon1 float; 
declare lon2 float;
declare lat1 float; 
declare lat2 float;
-- get the original lon and lat for the userid 
select longitude, latitude into mylon, mylat from users5
where id=userid limit 1;
-- calculate lon and lat for the rectangle:
set lon1 = mylon-dist/abs(cos(radians(mylat))*69);
set lon2 = mylon+dist/abs(cos(radians(mylat))*69);
set lat1 = mylat-(dist/69); 
set lat2 = mylat+(dist/69);
-- run the query:
SELECT destination.*,
3956 * 2 * ASIN(SQRT( POWER(SIN((orig.lat - 
dest.lat) * pi()/180 / 2), 2) +
COS(orig.lat * pi()/180) * COS(dest.lat * pi()/180) *
POWER(SIN((orig.lon -dest.lon) * pi()/180 / 2), 2) )) as distance 
FROM users destination, users origin
WHERE origin.id=userid
and destination.longitude between lon1 and lon2 
and destination.latitude between lat1 and lat2 
having distance < dist ORDER BY Distance limit 10;
END
```



## Other queries
[Stackoverflow](http://stackoverflow.com/questions/11112926/how-to-find-nearest-location-using-latitude-and-longitude-from-sql-database)

```sql
SELECT id, 
( 3959 * acos( cos( radians(37) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(-122) ) + sin( radians(37) ) * sin( radians( lat ) ) ) ) AS distance FROM markers HAVING distance < 25 ORDER BY distance LIMIT 0 , 20;
```

Best(?)

```sql
SELECT id, ( 3959 * acos( cos( radians(37) ) * cos( radians( lat ) ) * 
cos( radians( lng ) - radians(-122) ) + sin( radians(37) ) * 
sin( radians( lat ) ) ) ) AS distance FROM your_table_name HAVING
distance < 25 ORDER BY distance LIMIT 0 , 20;
```

Note: 6371 for killometers
