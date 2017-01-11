# FreeToolbar
An expand native toolbar, support 4 way show &amp; hide. include 2 automatic behavior.


#Usage:
```groovy
compile 'com.susyimes.ntype:freetoolbar:1.0.5'
```

1.0.5

NestedScrollView inside toolbar

android.support.v7.widget.Toolbar  --->

com.susyimes.freetoolbar.FreeToolbarTop  or com.susyimes.freetoolbar.FreeToolbarBottom


can include automatic show & hide.



And com.susyimes.freetoolbar.FreeToolbar not bind behavior use:

```java

toolbar.hide()

toolbar.show()

toolbar.move(int direnction,int offset, int duration)


this is force move when bar in nestedscroll or other height dymanic view .


toolbar.hide(int direction)  hide to where  0:top 1:bottom 2:left 3:right  ;


toolbar.show(int direction)  0,1:vertical show  3,4 horizantal show ;
```
Add one Freebar to get more free layout .  


```java
------------------------------susyimes custom usage--------------------------------------

Usage:

compile 'com.susyimes.ntype:freetoolbar:1.0.1.s'


1.0.1.s

NestedScrollView inside toolbar

android.support.v7.widget.Toolbar  --->  com.susyimes.freetoolbar.FreeToolbarTop  or com.susyimes.freetoolbar.FreeToolbarBottom

any scroll will hide toolbar. 

```
