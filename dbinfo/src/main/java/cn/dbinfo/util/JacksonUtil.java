package cn.dbinfo.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

public class JacksonUtil<T> {
    private static ObjectMapper mapper = new ObjectMapper();

    public T getArrayList(Object data) {
        return mapper.convertValue(data, new TypeReference<T>() {});
    }
}
