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
#License:

 Copyright Susyimes <susyimes@gmail.com>

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
