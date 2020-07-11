package com.semonics.tiktik.WebService;

public interface ResponseListener {
    void onSuccess(String response);

    void onFailure(String error);
}

