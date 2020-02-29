package com.mfz.yourbatis.typehandler;

import com.mfz.yourbatis.typehandler.Impls.IntegerTypeHandler;
import com.mfz.yourbatis.typehandler.Impls.StringTypeHandler;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MyTypeHandlerRegistry {

    private Map<Type,MyTypeHandler> typeHandlerMap = new HashMap<Type, MyTypeHandler>();

    public MyTypeHandlerRegistry() {
        typeHandlerMap.put(Integer.class,new IntegerTypeHandler());
        typeHandlerMap.put(String.class,new StringTypeHandler());
    }

    public MyTypeHandler getTypeHandler(Type type){
        return typeHandlerMap.get(type);
    }

}
