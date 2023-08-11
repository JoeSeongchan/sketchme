package com.dutaduta.sketchme.chat.constant;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ChatConstant {
    NUMBER_OF_CHAT(30);
    private final int count;
}
