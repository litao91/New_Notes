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
wz.has_perm('device.view_own_shop')
```

```json
/_api/v1.0/shops

{
    "filters": ["created__gt=2016-04-16T00:00:00","created__lt=2016-05-16T00:00:00"],
    "order_by": ["created", "name_pinyin"],
    "fields": ["user", "created", "name"]
}
```

```
https://github.com/carltongibson/django-filter/blob/develop/docs/usage.txt
```

# Aug 11-th

RESTful API: `/_api/v1.0/shops`

```json
{
"o":["-user_id", "-created"],
"lng__gt":116,
"lng__lt": 116.35,
"created__lt": "2016-01-01 00:00:00",
"s":["user_id", "lng", "created"]
}
```

```
{
    "per_page":3,
    "page":2,
    "o":["-user_id", "-created"],
    "lng__gt":116,
    "lng__lt": 116.35,
    "created__lt": "2016-01-01 00:00:00",
    "s":["id", "user_id", "lng", "created"]
}
```


shop id: 13
kerchant id: 1

# Aug 12-th

```
r = device.models.Shop.manager.get_nearest(39, 117)
r = device.models.Shop.manager.get_nearest(39, 117, {'user_id': 10})
```

```html
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
        body, html {width: 100%;height: 100%; margin:0;font-family:"微软雅黑";}
        #l-map{height: 220px;width:100%;}
        #r-result,#r-result table{width:100%;}
        .nav { width: 100%; height: 2em; line-height: 2em; background: #EDEDED; border: 1px solid #ADADAD;}
        .nav .nav-inner{ width: 30%; margin-left: 35%;}
        .nav .nav-sub { float: left; width: 33%;}
        .nav .nav-sub a { text-decoration: none; }
        .nav .nav-sub a i { display: inline-block; background: url("http://webmap1.map.bdstatic.com/wolfman/static/common/images/ui3/mo_banner_ba37b5d.png")}
        .nav .nav-sub a.bus i { background-position: -1px -192px; position: relative; top: 2px; width: 13px; height: 16px;}
        .nav .nav-sub a.driver i { background-position: -29px -194px; width: 15px; height: 14px;}
        .nav .nav-sub a.walk i { background-position: -102px -189px; width: 16px; height: 18px;}
        .nav .nav-sub a.bus.cur i { background-position: -15px -192px; }
        .nav .nav-sub a.driver.cur i { background-position: -45px -194px; }
        .nav .nav-sub a.walk.cur i { background-position: -120px -189px;}
        .hide { display: none;}
        input { font-family: "micrsoft yahei"; width: 80%; height: 2em; font-size: 1em; line-height: 2em; border: 0px; outline: 0px; padding: .2em 1em; margin: 0em 10%;}
        .btn-group { width: 100%; border-top: 1px solid #DDD; border-bottom: 2px solid #DDD;}
        button {width: 32%; text-align: center; border: 0; border-radius: 0; background-color: inherit; height: 44px; line-height: 44px; font-size: 15px;}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=FbzOyQ4YujPrZsxiQKoB07aB"></script>
    <script type="text/javascript" src="http://libs.baidu.com/jquery/1.8.2/jquery.min.js"></script>
    <title>导航示例</title>
</head>
<body>
    <div id="search">
        <input type="text" id="start" placeholder="正在定位您的位置..." style="border-bottom: 1px solid #DDD; " />
        <input type="text" id="end" value="汽车西站-公交车站" readonly="true" />
        <input type="hidden" id="start_point" value=""/>
        <input type="hidden" id="end_point" value="112.918571,28.214124"/>
        <input type="hidden" id="start_location" value=""/>
        <div class="btn-group">
            <button id="bus-search">公交</button>
            <button id="driver-search">驾车</button>
            <button id="walk-search">步行</button>
        </div>
    </div>
    <div id="showMap" class="hide">
        <div class="nav">
            <div class="nav-inner">
                <div class="nav-sub"><a href="#" class="bus"><i></i></a></div>
                <div class="nav-sub"><a href="#" class="driver cur"><i></i></a></div>
                <div class="nav-sub"><a href="#" class="walk"><i></i></a></div>
            </div>
            <!-- <a href="javascript:;" id="reLocation">重新导航</a> -->
        </div>
        <div id="l-map"></div>
        <div id="r-result"></div>
    </div>
 
<script type="text/javascript">
 
$(function(){
 
    var ep = $("#end_point").val().split(",");
    var map = new BMap.Map("l-map");
    var point = new BMap.Point(ep[0], ep[1]);
    map.centerAndZoom(point, 16);
 
    // 定位对象
    var geoc = new BMap.Geocoder();
    var geolocation = new BMap.Geolocation();
    geolocation.getCurrentPosition(function(r){
        if(this.getStatus() == BMAP_STATUS_SUCCESS){
            //var mk = new BMap.Marker(r.point);
            //map.addOverlay(mk);
            //map.panTo(r.point);
            $("#start_point").val(r.point.lng+','+r.point.lat);
            setLocation(r.point);
            showMap();
 
        }else {
            $("#start").attr("placeholder","请输入您的当前位置")
            alert('无法定位到您的当前位置，导航失败，请手动输入您的当前位置！'+this.getStatus());
        }
    },{enableHighAccuracy: true});
 
    $(".nav .nav-sub a").click(function(){
        $(".nav .nav-sub a").removeClass('cur');
        $(this).addClass('cur');
        searchRoute();
    })
 
    $("#reLocation").click(function(){
        reLocation();
    });
 
    $("#bus-search,#driver-search,#walk-search").click(function(){
        var id = $(this).attr("id");
        $(".nav .nav-sub a").removeClass('cur');
        if(id == "bus-search"){
            $(".nav .nav-sub a.bus").addClass('cur');
        }else if(id == "driver-search"){
            $(".nav .nav-sub a.driver").addClass('cur');
        }else if(id == "walk-search"){
            $(".nav .nav-sub a.walk").addClass('cur');
        }
        showMap();
    })
 
    function reLocation(){
        $("#search").show();
        $("#showMap").hide();
        map = new BMap.Map("l-map");
    }
 
    function showMap(){
        $("#srarch").hide();
        $("#showMap").show();
        searchRoute();
    }
 
    function setLocation(point){
        geoc.getLocation(point, function(rs){
            var addComp = rs.addressComponents;
            var result = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
            $("#start").val(result);
            $("#start_location").val(result);
            searchRoute();
        });
    }
 
    function searchRoute(s_, e_){
        map = new BMap.Map("l-map");
        var cur = $(".nav .nav-sub a.cur");
        var type = "";
 
        if(cur.hasClass('bus')){
            type = "bus";
        }else if(cur.hasClass('driver')){
            type = "driver";
        }else if(cur.hasClass('walk')){
            type = "walk";
        }else{
            type = "driver";
        }
 
        var s_;
        var e_;
 
        var sl = $("#start_location").val();
        var s = $("#start").val();
        var sp = $("#start_point").val();
        var e = $("#end").val();
        var ep = $("#end_point").val();
 
        if(s != sl){// 如果用户修改了地址（与定位的位置不一致）则使用地址搜索
            s_ = s;
            e_ = e;
        }else if(sp){// 否则使用坐标搜索
            var ps = sp.split(",");
            var pe = ep.split(",");
            s_ = new BMap.Point(ps[0], ps[1]);
            e_ = new BMap.Point(pe[0], pe[1]);
        }
 
        if(type == "bus"){
            var transit = new BMap.TransitRoute(map, {renderOptions: {map: map, panel: "r-result", autoViewport: true}});
            transit.search(s_, e_);
        }else if(type == "driver"){
            var driving = new BMap.DrivingRoute(map, {renderOptions: {map: map, panel: "r-result", autoViewport: true}});
            driving.search(s_, e_);
        }else if(type == "walk"){
            var walking = new BMap.WalkingRoute(map, {renderOptions: {map: map, panel: "r-result", autoViewport: true}});
            walking.search(s_, e_);
        }
    }
})
 
</script>
</body>
</html>
```

```
http://api.map.baidu.com/place/v2/search?query=超市&scope=2&output=json&location=39.915,116.404&radius=2000&filter=sort_name:distance|sort_rule:1&ak=KtZXesBLWhHHstsBrvMNW2tV
```

## Aug-16

Shop Schema

```
+-------------------+---------------------+------+-----+---------+----------------+
| Field             | Type                | Null | Key | Default | Extra          |
+-------------------+---------------------+------+-----+---------+----------------+
| id                | int(11) unsigned    | NO   | PRI | NULL    | auto_increment |
| user_id           | int(11) unsigned    | NO   |     | NULL    |                |
| update_user_id    | int(11) unsigned    | YES  |     | NULL    |                |
| merchant_id       | int(11) unsigned    | NO   | MUL | NULL    |                |
| name              | varchar(255)        | NO   | MUL |         |                |
| name_pinyin       | varchar(255)        | YES  | MUL | NULL    |                |
| city_id           | int(5) unsigned     | NO   |     | NULL    |                |
| city_id_path      | varchar(64)         | YES  |     | NULL    |                |
| address           | varchar(255)        | NO   |     |         |                |
| phone             | varchar(20)         | NO   |     |         |                |
| lat               | decimal(16,12)      | YES  |     | NULL    |                |
| lng               | decimal(16,12)      | YES  |     | NULL    |                |
| type              | tinyint(1) unsigned | YES  |     | 0       |                |
| scale             | tinyint(1) unsigned | YES  |     | 0       |                |
| area              | varchar(128)        | YES  |     | NULL    |                |
| is_all_day        | tinyint(1) unsigned | YES  |     | 0       |                |
| cash_register_num | tinyint(3) unsigned | YES  |     | 0       |                |
| print_ticket      | tinyint(1) unsigned | YES  |     | 0       |                |
| keeper            | varchar(64)         | YES  |     | NULL    |                |
| keeper_telephone  | varchar(20)         | YES  |     | NULL    |                |
| keeper_age        | int(3) unsigned     | YES  |     | NULL    |                |
| img_url           | varchar(255)        | YES  |     | NULL    |                |
| comment           | varchar(255)        | YES  |     | NULL    |                |
| created           | datetime            | NO   |     | NULL    |                |
| updated           | datetime            | YES  |     | NULL    |                |
+-------------------+---------------------+------+-----+---------+----------------+
```

```
http://stackoverflow.com/questions/4006520/using-html5-file-uploads-with-ajax-and-jquery
```

```python
import uuid, datetimefrom os.path import join, basename, splitext
image = models.ImageField(verbose_name=_(u'Image'), upload_to = tag_image_upload, blank = True, null=True)
def tag_image_upload(instance, filename):
    ext = filename.split('.')[-1]    
    filename = "%s.%s" % (uuid.uuid4(), ext)    
    return join('tags',  filename)
```

```javascript
var marker = new BMap.Marker(point, { 
    // 指定Marker的icon属性为Symbol 
    icon: new BMap.Symbol(BMap_Symbol_SHAPE_POINT, { 
        scale: 1.5,//图标缩放大小 
        fillColor: "red",//填充颜色
         fillOpacity: 0.8//填充透明度
     })
 });//创建标注
```

```python
from django.core.exceptions import ValidationError

def clean_keywords(self):
    clean_keywords = self.cleaned_data['keywords']
    cursor = connection.cursor()
    out = collections.OrderedDict()
        for k in clean_keywords:
            c = k.strip()            
            if StandardCompany.objects.exclude(id=self.instance.id).filter(keywords__contains=[c]).exists():
                raise ValidationError(u'关键词 %s 已经存在' % c)
                out[c]=1        
                return out.keys()
```
