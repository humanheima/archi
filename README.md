# Archi
This repository showcases and compares different architectural patterns that can be used to build Android apps. The exact same sample app is built three times using the following approaches:
* __Standard Android__: traditional approach with layouts, Activities/Fragments and Model.
* __MVP__: Model View Presenter.
* __MVVM__: Model View ViewModel with data binding. 

[美团点评团队-如何构建Android MVVM 应用框架](https://tech.meituan.com/android_mvvm.html?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io?hmsr=toutiao.io&utm_medium=toutiao.io&utm_source=toutiao.io)

## The App

The sample app displays a list of GitHub public repositories for a given username. Tapping on one of them will open a repository details screen, where more information about the repo can be found. This screen also shows information about the owner of the repository. 

![Screenshots](images/archi-screenshots.png)

### Libraries used 
* AppCompat, CardView and RecyclerView
* Data Binding (only MVVM)
* RxJava & RxAndroid
* Retrofit 2
* Picasso
* Mockito
* Robolectric

## Standard Android
The `/app` directoy contains the implementation that follows the traditional standard Android approach.
This is a couple of layout files, two Activities and the model. The model is exactly the same for the three implementations and it 
contains: `Repository`, `User` and a retrofit service (`GithubService`).With this approach, Activities are in charge of 
calling the `GithubService`, processing the data and updating the views. They act kind of like a controller in MVC but with some extra 
responsibilities that should be part of the view. The problem with this standard architecture is that Activities and Fragments can become 
quite large and very difficult to tests. Hence why I didn't write any unit test for this case. 

* View：XML布局文件。
* Model：实体模型（数据的获取、存储、数据状态变化）。
* Controller：对应于Activity，处理数据、业务和UI。

从上面这个结构来看，Android本身的设计还是符合MVC架构的，但是Android中纯粹作为View的XML视图功能太弱，我们大量处理View的逻辑只能写在Activity中，
这样Activity就充当了View和Controller两个角色，直接导致Activity中的代码大爆炸。相信大多数Android开发者都遇到过一个Acitivty数以千行的代码情况吧！
所以，更贴切的说法是，这个MVC结构最终其实只是一个Model-View（Activity:View&Controller）的结构。

## MVP - Model View Presenter
In `/app-mvp` you will find the sample app implemented following this pattern. 
When using mvp, Activities and Fragments become part of the view layer and they delegate most of the work to presenters.
Each Activity has a matching presenter that handles accessing the model via the `GithubService`. 
They also notify the Activities when the data is ready to display. 
Unit testing presenters becomes very easy by mocking the view layer (Activities).

* View: 对应于Activity和XML，负责View的绘制以及与用户的交互。
* Model: 依然是实体模型。
* Presenter: 负责完成View与Model间的交互和业务逻辑。

前面我们说，Activity充当了View和Controller两个角色，MVP就能很好地解决这个问题，其核心理念是通过一个抽象的View接口（不是真正的View层）
将Presenter与真正的View层进行解耦。Persenter持有该View接口，对该接口进行操作，而不是直接操作View层。这样就可以把视图操作和业务逻辑解耦，
从而让Activity成为真正的View层。

但MVP也存在一些弊端：

* Presenter（以下简称P）层与View（以下简称V）层是通过接口进行交互的，接口粒度不好控制。粒度太小，就会存在大量接口的情况，使代码太过碎版化；粒度太大，解耦效果不好。同时对于UI的输入和数据的变化，需要手动调用V层或者P层相关的接口，相对来说缺乏自动性、监听性。如果数据的变化能自动响应到UI、UI的输入能自动更新到数据，那该多好！
* MVP是以UI为驱动的模型，更新UI都需要保证能获取到控件的引用，同时更新UI的时候要考虑当前是否是UI线程，也要考虑Activity的生命周期（是否已经销毁等）。
* MVP是以UI和事件为驱动的传统模型，数据都是被动地通过UI控件做展示，但是由于数据的时变性，我们更希望数据能转被动为主动，希望数据能更有活性，由数据来驱动UI。
* V层与P层还是有一定的耦合度。一旦V层某个UI元素更改，那么对应的接口就必须得改，数据如何映射到UI上、事件监听接口这些都需要转变，牵一发而动全身。如果这一层也能解耦就更好了。
* 复杂的业务同时也可能会导致P层太大，代码臃肿的问题依然不能解决。

## MVVM - Model View ViewModel
This pattern has recently started to gain popularity due to the release of the [data binding library](https://developer.android.com/tools/data-binding/guide.html). 
You will find the implementation in `/app-mvvm`. In this case, ViewModels retrieve data from the model when requested from the view 
via data binding. With this pattern, Activities and Fragments become very lightweight. 
Moreover, writting unit tests becomes easier because the ViewModels are decoupled from the view.

* View : 对应于Activity和XML，负责View的绘制以及与用户交互。
* Model: 实体模型。
* ViewModel: 负责完成View与Model间的交互，负责业务逻辑。

MVVM的目标和思想与MVP类似，利用数据绑定(Data Binding)、依赖属性(Dependency Property)、命令(Command)、路由事件(Routed Event)等新特性，打造了一个更加灵活高效的架构。

**数据驱动**
在常规的开发模式中，数据变化需要更新UI的时候，需要先获取UI控件的引用，然后再更新UI。获取用户的输入和操作也需要通过UI控件的引用。在MVVM中，这些都是通过数据驱动来自动完成的，数据变化后会自动更新UI，UI的改变也能自动反馈到数据层，数据成为主导因素。这样MVVM层在业务逻辑处理中只要关心数据，不需要直接和UI打交道，在业务处理过程中简单方便很多。

**低耦合度**
MVVM模式中，数据是独立于UI的。

数据和业务逻辑处于一个独立的ViewModel中，ViewModel只需要关注数据和业务逻辑，不需要和UI或者控件打交道。UI想怎么处理数据都由UI自己决定，ViewModel不涉及任何和UI相关的事，也不持有UI控件的引用。即便是控件改变了（比如：TextView换成EditText），ViewModel也几乎不需要更改任何代码。它非常完美的解耦了View层和ViewModel，解决了上面我们所说的MVP的痛点。

**更新UI**
在MVVM中，数据发生变化后，我们在工作线程直接修改（在数据是线程安全的情况下）ViewModel的数据即可，不用再考虑要切到主线程更新UI了，这些事情相关框架都帮我们做了。

**团队协作**
MVVM的分工是非常明显的，由于View和ViewModel之间是松散耦合的：一个是处理业务和数据、一个是专门的UI处理。所以，完全由两个人分工来做，一个做UI（XML和Activity）一个写ViewModel，效率更高。

**可复用性**
一个ViewModel可以复用到多个View中。同样的一份数据，可以提供给不同的UI去做展示。对于版本迭代中频繁的UI改动，更新或新增一套View即可。如果想在UI上做A/B Testing，那MVVM是你不二选择。

## License

```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
