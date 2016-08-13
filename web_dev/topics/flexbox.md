There are two other answers briefly mentioning flexbox; however, that was more than two years ago, and they don't provide any examples. The specification for flexbox has definitely settled now.

Note: Though CSS Flexible Boxes Layout specification is at the Candidate Recommendation stage, not all browsers have implemented it. WebKit implementation must be prefixed with -webkit-; Internet Explorer implements an old version of the spec, prefixed with -ms-; Opera 12.10 implements the latest version of the spec, unprefixed. See the compatibility table on each property for an up-to-date compatibility status.

(taken from https://developer.mozilla.org/en-US/docs/Web/Guide/CSS/Flexible_boxes)
All major browsers and IE11+ support Flexbox. For IE 10 or older, you can use the FlexieJS shim.

To check current support you can also see here: http://caniuse.com/#feat=flexbox

Working example

With flexbox you can easily switch between any of your rows or columns either having fixed dimensions, content-sized dimensions or remaining-space dimensions. In my example I have set the header to snap to its content (as per the OPs question), I've added a footer to show how to add a fixed-height region and then set the content area to fill up the remaining space.

```css
html,
body {
      height: 100%;
        margin: 0
}

.box {
      display: flex;
      flex-flow: column;
      height: 100%;
}

.box .row {
      border: 1px dotted grey;
}

.box .row.header {
      flex: 0 1 auto;
}

.box .row.content {
      flex: 1 1 auto;
}

.box .row.footer {
      flex: 0 1 40px;
}
```

```html
<!-- Obviously, you could use HTML5 tags like `header`, `footer` and `section` -->

<div class="box">
	<div class="row header">
		<p><b>header</b>
		<br />
		<br />(sized to content)</p>
	</div>
	<div class="row content">
	<p>
		<b>content</b>
		(fills remaining space)
	</p>
	</div>
	<div class="row footer">
	<p><b>footer</b> (fixed height)</p>
	</div>
	</div>
```
In the CSS above, the flex property shorthands the flex-grow, flex-shrink, and flex-basis properties to establish the flexibility of the flex items. Mozilla has a good introduction to the flexible boxes model.


## BMap

### Sample 1

```css
body,html {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
	font-family: "微软雅黑";
}

#mapContainer{
	width:100%;
	height:100%;
	float:left;
	overflow: hidden;
	margin:0;
}

#allmap{
	width:70%;
	margin:3px 0 0;
	height:100%;
	float:left;
	overflow: hidden;
}
```

```javascript
  $(function(){
	  $("#mapContainer").height($(window).height()-110-40);//110是header高度,40是 footer高度 
	  $(window).resize(function() {
		  $("#mapContainer").height($(window).height()-110-40);//110是header高度,40是 footer高度 
	  });
	  var map = new Map();
  });
```

### Sample 2
```css
#nav {
    background-color: #85d989;
        width: 100%;
            height: 50px;
}
#content {
    background-color: #cc85d9;
	width: 100%;
	position: absolute;
	top: 50px;
	bottom: 0px;
	left: 0px;
}
```
