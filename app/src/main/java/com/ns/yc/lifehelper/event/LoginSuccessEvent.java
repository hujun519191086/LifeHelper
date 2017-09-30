package com.ns.yc.lifehelper.event;

/**
 * Created by PC on 2017/9/27.
 * 作者：PC
 */

public class LoginSuccessEvent {

    private String msg;

    public LoginSuccessEvent(String msg) {//事件传递参数
        this.msg = msg;
    }

    public String getMsg() {//取出事件参数
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
