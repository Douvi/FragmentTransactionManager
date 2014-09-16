FragmentTransactionManager ([Play Store Demo][1])
===========

FragmentTransactionManager is an Open Source Android library that allows developers to easily create applications 
with FragmentTransaction. Feel free to use it all you want in your Android apps provided that you cite this project 
and include the license in your app.

This lib will help you to have differents stacks of fragments by Activity. With those stacks you will be abel to have 
a navigation like iOS NavigationController.

Simple Methods
-----
```java

    mFragmentTransactionBuilder.createTag("Menu", R.id.menuContent, 1);
    
    mFragmentTransactionBuilder.addFragmentInStack("Menu", FTFragment.instantiate(this, MainMenuFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));

    mFragmentTransactionBuilder.removeTopFragmentInStackWithAnimation("Menu", true);

    mFragmentTransactionBuilder.mshowTopFragmentInStack("Menu");

```


Here's an video of the example application in this repository : YoutTube Video[2]


Setup
-----
* In Eclipse, just import the library as an Android library project. Project > Clean to generate the binaries 
you need, like R.java, etc.
* Then, just add FragmentTransactionManager as a dependency to your existing project and you're good to go!


How to Integrate this Library into Your Projects
------------------------------------------------
In order to integrate FragmentTransactionManager into your own projects.

__1.__     You need to set into your layout (xml) as a ROOT 'FTRelativeLayout' or 'FTLinearLayout'. You don't need
to set the both layout, just one will be enough. 


Simple Exemple
-----
RelavitveLayout
```xml

<com.dovi.fragmentTransaction.layout.FTRelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <!-- ADD THE REST OF YOUR LAYOUT -->

<com.dovi.fragmentTransaction.layout.FTRelativeLayout/>

```

LinearLayout
```xml

<com.dovi.fragmentTransaction.layout.FTLinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent" >

    <!-- ADD THE REST OF YOUR LAYOUT -->

<com.dovi.fragmentTransaction.layout.FTLinearLayout/>

```

__2.__       Into you activity you need to init your layout


Simple Exemple
-----
```java

    private FTRelativeLayout mRelativeLayout;

    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);

        setContentView(R.layout.main_activity);

        mRelativeLayout = (FTRelativeLayout) findViewById(R.id.root);
    }

```

__3.__      This part is very important, you have to check, before to add any stack or fragment into your FragmentTransactionManager, if 
FragmentTransactionManager need to be restored. For that check the code below. For info, you HAVE TO init FragmentTransactionManager into 
`onResume()`


Simple Exemple
-----
```java

    @Override
    protected void onResume() {
        super.onResume();

        //Here you init FragmentTransactionManager
        mFragmentTransactionBuilder = mRelativeLayout.getFragmentManger(getSupportFragmentManager(), this);
        
        //You need to check if it don't need to finish to restore the state of your fragments. If not you can init your stacks
        if (mFragmentTransactionBuilder.isNeededToRestoreState()) {
            mFragmentTransactionBuilder.restoreState();
        } else {
            //Before add a fragment into a stack you need to init it
            mFragmentTransactionBuilder.createTag("Menu", R.id.menuContent, 1);
            mFragmentTransactionBuilder.createTag("ContentTab1", R.id.fragmentContent, 1);
            
            mFragmentTransactionBuilder.addFragmentInStack("Menu", FTFragment.instantiate(this, MainMenuFragment.class.getName(), null, Animation.ANIM_NONE, Animation.ANIM_NONE));
            
            Bundle mBundle = new Bundle();
            mBundle.putString("title", "Tab1");
            mFragmentTransactionBuilder.addFragmentInStack("ContentTab1", FTFragment.instantiate(this, MainContentFragment.class.getName(), mBundle, Animation.ANIM_NONE, Animation.ANIM_NONE));
        }
    }

```

BUGS
-------
* I'm still developing this lib so you may have some bugs...
            

Developed By
------------
* Edouard Roussillon

License
-------

    Copyright 2014 Edouard Roussillon
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    
<!-- [1]: http://twitter.com/slidingmenu
[2]: http://actionbarsherlock.com/
[3]: https://play.google.com/store/apps/details?id=com.zappos.android&hl=en
[4]: https://play.google.com/store/apps/details?id=com.levelup.touiteur&hl=en
[5]: https://play.google.com/store/apps/details?id=org.videolan.vlc.betav7neon
[6]: https://play.google.com/store/apps/details?id=com.verge.android
[7]: http://bit.ly/TWejze
[8]: https://play.google.com/store/apps/details?id=com.rdio.android.ui
[9]: https://play.google.com/store/apps/details?id=com.gelakinetic.mtgfam
[10]: https://play.google.com/store/apps/details?id=com.mantano.reader.android
[11]: https://play.google.com/store/apps/details?id=com.phonegap.MW3BarracksFree
[12]: http://forum.xda-developers.com/showthread.php?p=34361296
[13]: http://bit.ly/xs1sMN
[14]: https://play.google.com/store/apps/details?id=com.espn.score_center
[15]: https://play.google.com/store/apps/details?id=com.joelapenna.foursquared
[16]: https://play.google.com/store/apps/details?id=com.mlssoccer
[17]: https://play.google.com/store/apps/details?id=com.ninegag.android.app
[18]: https://play.google.com/store/apps/details?id=com.evernote.food
[19]: https://play.google.com/store/apps/details?id=com.linkedin.android
[20]: https://play.google.com/store/apps/details?id=com.zappos.android -->
