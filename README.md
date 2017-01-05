# FreeToolbar
An expand native toolbar, support 4 way show &amp; hide. include 2 automatic behavior.


#Usage:
```groovy
compile 'com.susyimes.ntype:freetoolbar:1.0.2'
```

1.0.2

NestedScrollView inside toolbar

android.support.v7.widget.Toolbar  ---> 

com.susyimes.freetoolbar.FreeToolbarTop  or com.susyimes.freetoolbar.FreeToolbarBottom


can include automatic show & hide.



And com.susyimes.freetoolbar.FreeToolbar not bind behavior use:

```java

toolbar.hide()

toolbar.show()


toolbar.hide(int where)  hide to where  0:top 1:bottom 2:left 3:right  ;


toolbar.show(int where)  0,1:vertical show  3,4 horizantal show ;
```



------------------------------susyimes custom usage--------------------------------------

Usage:

compile 'com.susyimes.ntype:freetoolbar:1.0.1.s'


1.0.1.s

NestedScrollView inside toolbar

android.support.v7.widget.Toolbar  --->  com.susyimes.freetoolbar.FreeToolbarTop  or com.susyimes.freetoolbar.FreeToolbarBottom

any scroll will hide toolbar. 


