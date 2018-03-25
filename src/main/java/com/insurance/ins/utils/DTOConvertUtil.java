package com.insurance.ins.utils;

import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by K on 29.7.2017 г..
 */
public final class DTOConvertUtil {

    public DTOConvertUtil() {
    }


    //Generic converter
    public static <S,D> D convert(S source,Class<D> destClass){
        ModelMapper mapper = new ModelMapper();

        return mapper.map(source,destClass);
    }

    //Generic converter multiple
    public static <S,D> List<D> convert(Iterable<S> sources, Class<D> destClass){
        List<D> resultList = new ArrayList<>();
        for (S s : sources) {
           D d = convert(s,destClass);
            resultList.add(d);
        }
        return resultList;
    }

    //Generic converter multiple Set
    public static <S,D> Set<D> convertToSet(Iterable<S> sources, Class<D> destClass){
        Set<D> resultSet = new HashSet<D>();
        for (S s : sources) {
            D d = convert(s,destClass);
            resultSet.add(d);
        }
        return resultSet;
    }




}
