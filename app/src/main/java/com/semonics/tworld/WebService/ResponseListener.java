package com.semonics.tworld.WebService;

public interface ResponseListener {
    void onSuccess(String response);

    void onFailure(String error);
}

