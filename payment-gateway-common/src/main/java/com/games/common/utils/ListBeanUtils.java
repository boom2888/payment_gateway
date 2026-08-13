package com.games.common.utils;

import com.games.common.utils.bean.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListBeanUtils<S,T> {

    public List<T> copyList(List<S> list2, Class<T> classObj) {
        List<T> result = new ArrayList<>();
        if ((!Objects.isNull(list2))) {
            list2.forEach(item -> {
                try {
                    T data = classObj.newInstance();
                    BeanUtils.copyProperties(item, data);
                    if(data instanceof FormatAble){
                        ((FormatAble)data).format();
                    }
                    result.add(data);
                } catch (InstantiationException e) {
                } catch (IllegalAccessException e) {
                }
            });
        }
        return result;
    }
}
