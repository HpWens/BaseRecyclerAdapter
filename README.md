# BaseRecyclerAdapter

RecyclerView 是Android L版本中新添加的一个用来取代ListView的SDK，它的灵活性与可替代性比listview更好。RecyclerView  同样也用到适配，枯燥重复的适配肯定会让你不胜其烦，下面让我们一起来打造一款通用的适配（BaseRecyclerAdapter）。受益群体几乎是所有Android开发者，希望更你们能够一起来维护这个项目，把这个项目做得更好，帮助更多人。

看到这里你肯定会有疑问，通用适配（BaseRecyclerAdapter），它具体能够做些什么呢，或者说它都有哪些功能？

##一、BaseRecyclerAdapter 简介

-  优化Adapter代码

-  添加 Item的点击事件，长按事件以及子控件的点击事件

- 添加头部、尾部、下拉刷新、上拉加载（上拉加载的5种加载更多动画任你选择，后期会添加更多的加载动画）、没有更多数据

- 可以自定义头部、尾部、加载更多布局

-  添加 Item滑动动画 （9种动画切换，轻松一行代码，当然后期会添加更多的类型）

-  添加新增、删除 Item动画 （目前支持默认的动画方式）

- 网格，列表随意切换

- 添加空布局（列表无数据时，显示更加人性化）

- 拖拽和侧滑删除

- 支持多类型布局

看了 BaseRecyclerAdapter的特性，接着来看看外面如何在项目中导入（依赖）它。

##BaseRecyclerAdapter 导入（依赖）

方式一：build.gradle 的 dependencies 添加如下代码：

```
compile 'com.github.baserecycleradapter:library:1.0.9'

```

方式二：下载[源码](https://github.com/HpWens/BaseRecyclerAdapter)，添加 **library**库到你项目当中。


##BaseRecyclerAdapter 使用

```
 mAdapter = new BaseRecyclerAdapter<String>(this, getItemDatas(), R.layout.rv_item) {
     @Override
     protected void convert(BaseViewHolder helper, String item) {
         helper.setText(R.id.tv_item_text, item);
     }
 });
        
```

![recy](http://img.blog.csdn.net/20160728111249536)

----------

###添加 Item点击，长按事件

```
 mAdapter.setOnRecyclerItemClickListener();
 mAdapter.setOnRecyclerItemLongClickListener();
```

![recy](http://img.blog.csdn.net/20160728111739762)


###子控件的点击事件

```
@Override
protected void convert(BaseViewHolder helper, String item) {
    helper.setOnClickListener(R.id.tv_item_text, new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        }
    });
}

```

###添加头部，尾部

```
mAdapter.addHeaderView();
mAdapter.addFooterView();
```

以下是添加头部的代码：

```
View headerView=getLayoutInflater().inflate(R.layout.rv_header, null);

headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

mAdapter.addHeaderView(headerView);
```

![recy](http://img.blog.csdn.net/20160728123126645)

----------

###添加 Item动画

![recy](http://img.blog.csdn.net/20160728114134232)    

```
 //一行代码开启动画 默认CUSTOM动画
 mAdapter.openLoadAnimation();
```

当然你可以更换其他动画：

```
//CUSTOM, ALPHA, SCALE, SLIDE_BOTTOM, SLIDE_TOP, SLIDE_LEFT, //SLIDE_RIGHT, SLIDE_LEFT_RIGHT, SLIDE_BOTTOM_TOP 
mAdapter.openLoadAnimation(AnimationType.ALPHA);
```

最后你可以自定义 Item动画：

```
mAdapter.openLoadAnimation(new BaseAnimation[]{
        new BaseAnimation() {
            @Override
            public Animator[] getAnimators(View view) {
                return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1.0f),
                        ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f)};
            }
        }
});
```

###设置加载更多

```
        mAdapter.openLoadingMore(true);
        mAdapter.setOnLoadMoreListener(this);
```

![recy](http://img.blog.csdn.net/20160728123855076)

你可以设置加载更多类型，只需要添加一行代码，实现不同的加载更多动画：

```
  //默认模式
  mAdapter.setLoadMoreType(LoadType.CUSTOM); 
```

更换其他模式：

`LoadType.CUBES`

![recy](http://img.blog.csdn.net/20160728125211173)

`LoadType.SWAP` 

![recy](http://img.blog.csdn.net/20160728125012142)

 `LoadType.EAT_BEANS`

![recy](http://img.blog.csdn.net/20160728130119303)

----------

`onLoadMoreListener` 接口实现：

```
    @Override
    public void onLoadMoreRequested() {
        mRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                if (mAdapter.getData().size() >= TOTAL_COUNT) {
                    mAdapter.notifyDataChangeAfterLoadMore(false);
                    mAdapter.addNoMoreView();
                } else {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataChangeAfterLoadMore(addDatas(), true);
                        }
                    }, DELAY_MILLIS);
                }
            }
        });
    }
```
                                                                   
```
 //显示没有更多数据
 mAdapter.addNoMoreView();
```

![recy](http://img.blog.csdn.net/20160728130541677)  

----------

###添加空布局

```
View headerView=getLayoutInflater().inflate(R.layout.rv_empty, null);
headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));
mAdapter.addHeaderView(headerView);
```

![recy](http://img.blog.csdn.net/20160728130843978)

----------

###拖拽和侧滑

添加以下代码：

```
ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mAdapter);

ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);

mAdapter.setItemTouchHelper(mItemTouchHelper);

mAdapter.setDragViewId(R.id.iv_drag);

mItemTouchHelper.attachToRecyclerView(mRecyclerView);
```

![recycler](http://img.blog.csdn.net/20160802232514813)

----------

###支持不同类型

```
mRecyclerView.setAdapter(mAdapter = new BaseMultiItemAdapter<MultiItem>(this, getMultiItemDatas()) {
    @Override
    protected void convert(BaseViewHolder helper, MultiItem item) {
        switch (helper.getItemViewType()) {
            case MultiItem.SEND:
                helper.setText(R.id.chat_from_content, item.content);
                //helper.setImageBitmap(R.id.chat_from_icon,getRoundCornerBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.chat_head), 16));
                break;
            case MultiItem.FROM:
                helper.setText(R.id.chat_send_content, item.content);
                //helper.setImageBitmap(R.id.chat_send_icon,getRoundCornerBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.from_head), 16));
                break;
        }
    }
    @Override
    protected void addItemLayout() {
        addItemType(MultiItem.SEND, R.layout.chat_send_msg);
        addItemType(MultiItem.FROM, R.layout.chat_from_msg);
    }
});
mAdapter.openLoadAnimation(true);
btnSend.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        MultiItem multiItem = new MultiItem();
        multiItem.itemType = MultiItem.SEND;
        multiItem.content = etChat.getText().toString();
        mAdapter.add(multiItem);
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
        //mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }
});
```
数据源：

```
public static List<MultiItem> getMultiItemDatas() {
    List<MultiItem> list = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
        MultiItem multiItem = new MultiItem();
        if (i % 2 == 0) {
            multiItem.itemType = MultiItem.SEND;
            multiItem.content = "海，妹子约吗";
        } else {
            multiItem.itemType = MultiItem.FROM;
            multiItem.content = "大哥，你别怕";
        }
        list.add(multiItem);
    }
    return list;
}
```

![recycler](http://img.blog.csdn.net/20160807195219773)

这样就轻松实现了聊天界面。

这样简单的配置就可以实现多类型布局，不需要你写格外的代码。

敬请大家的关注，后期会一直维护本库，有什么好的想法，可以提出来。我们互相学习进度。

[源码传送门](https://github.com/HpWens/BaseRecyclerAdapter)

如果对你有所帮助请给**star**

欢迎加入**478720016**来帮助更多的人。
![recy](http://img.blog.csdn.net/20160728130843978)

----------

如果对你有帮助，还请给star哟。 我的博客地址http://blog.csdn.net/u012551350/article/details/52026740

欢迎加入**478720016**群来帮助更多的人。
