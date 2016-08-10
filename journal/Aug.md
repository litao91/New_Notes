# Journal - August

## Aug 8th
Accidentally installed
```
  fontconfig hicolor-icon-theme ibus-sunpinyin libatk1.0-0 libatk1.0-data
  libcairo2 libcups2 libdatrie1 libgdk-pixbuf2.0-0 libgdk-pixbuf2.0-common
  libglade2-0 libgraphite2-3 libgtk2.0-0 libgtk2.0-bin libgtk2.0-common
  libharfbuzz0b libibus-1.0-5 libjasper1 libpango-1.0-0 libpangocairo-1.0-0
  libpangoft2-1.0-0 libpixman-1-0 libsunpinyin3 libthai-data libthai0
  libxcb-render0 libxcb-shm0 libxcomposite1 libxcursor1 libxdamage1 libxfixes3
  libxinerama1 libxrandr2 python-cairo python-glade2 python-gobject-2
  python-gtk2 sunpinyin-data
```


django permission

```
https://github.com/lambdalisue/django-permission
```

## Aug 9-th


```
/_api/v1.0/parsingrules/extend/populaterule/
```

```json
{
    "device_id": "102065"
}
```

```
mysql -h rm-2ze87zk13156710bi.mysql.rds.aliyuncs.com chaomeng --user=chaomeng --password=Ichaomeng2015A -e "select name,address,lat as x, lng as y from shops" > /tmp/shops.csv
```

```python
from core.rest_utils import Query
from device.models import Merchant
q = Query('{"filter": {"telephone":"010-56429392"}}', Merchant)
```

```python
wz = user.models.CMUser.objects.get(pk=10)  
wz.has_perm('shop.view_by_author')
```

```json
{
    "filters": ["created__gt=2016-04-16T00:00:00","created__lt=2016-05-16T00:00:00"],
    "order_by": ["created", "name_pinyin"],
    "fields": ["user", "created", "name"]
}
```

```
https://github.com/carltongibson/django-filter/blob/develop/docs/usage.txt
```
