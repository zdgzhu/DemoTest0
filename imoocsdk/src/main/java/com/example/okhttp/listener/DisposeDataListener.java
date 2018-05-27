package com.example.okhttp.listener;

/**
 * 自定义的事件监听  为什要自定义事件监听？？
 *  1、 当OkHttp 本身有事件监听 onFailure , onResponse .onHttp对应的监听事件就是这两个
 *  2、如果直接用OkHttp 的回调处理的话，如果有一天okHttp 的开发团队觉得onResponse 名字取得不是很
 *  具有代表性，它想改名字，那么我们就要遭殃了，我们所有涉及到的回调的地方，都需要修改它的名字。这样就比较麻烦了
 *  3、不利于我们扩展。例如下载。做下载功能的时候，肯定是要监听它的下载进度了，但是他的两个回调方法，
 *  没有onProgress 的监听，所以我们需要自己定义
 *
 */
public interface DisposeDataListener {




    /**
     * 请求成功回调事件处理
     */
    public void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     */
    public void onFailure(Object reasonObj);




}
