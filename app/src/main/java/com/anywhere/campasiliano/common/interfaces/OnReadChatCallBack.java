package com.anywhere.campasiliano.common.interfaces;

import com.anywhere.campasiliano.models.chats.Chats;

import java.util.List;

public interface OnReadChatCallBack {

    void onReadSuccess(List<Chats> list);
    void onReadFailed();

}
