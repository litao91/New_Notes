# Cursor Based Pagination

## Ordinary Pagination
Difficult to provide accurate paginated data due to the frequent udpates. 

* Assume the data is static and doesn't change frequently
* Pagination only consider record count. 

Assume: 

* 10 records
* 5 as the limit 

```
=== Page 1 ==
10
9
8
7
6
=== Page 2 ==
5
4
3
2
1
=============
```

Assume the result set is updated by two new records, while we are on the first 
page

```
=== Page 1 == 
12 new
11 new
10
9
8
=== Page 2 ==
7
6
5
4
3
=== Page 3 ==
2
1
```

Now we navigate to the second page. Base on our first image, it should retrieve 
ercords from 1-5. Hoever, **records with 7-3** will be retrieved. 

=> Records 6 7 displayed in both pages. 

# Practice Use Cases of Real Time Data Pagination
## Twitter
Sample request:

```
https://api.twitter.com/1.1/search/tweets.json?q=php&since_id=24012619984051000&max_id=250126199840518145&result_type=recent&count=10
```

* `count` -- break the result set into blocks of 10
* `since_id` and `max_id` -- enalbes cursor based pagination


Part of the response
```json
"search_metadata": {
  "max_id": 250126199840518145,
  "since_id": 24012619984051000,
  "refresh_url": "?since_id=250126199840518145&q=php&result_type=recent&include_entities=1",

  "next_results": "?max_id=249279667666817023&q=php&count=10&include_entities=1&result_type=recent",

  "count": 10,
  "completed_in": 0.035,
  "since_id_str": "24012619984051000",
  "query": "php",
  "max_id_str": "250126199840518145"
}
```
