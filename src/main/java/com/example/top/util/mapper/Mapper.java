package com.example.top.util.mapper;

import org.springframework.beans.BeanUtils;

public class Mapper {

    public static<F, T> T map(F fromObj, T toObj) {
        BeanUtils.copyProperties(fromObj, toObj);
        return toObj;
    }
}
