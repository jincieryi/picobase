package com.picobase.validator;

import com.picobase.util.StrFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * ErrObject is the default validation error
 * that implements the Err interface.
 */
public class ErrObject implements Err {
    private String code;
    private String message;
    private Map<String, Object> params;


    public ErrObject(String code, String message) {
        this.code = code;
        this.message = message;
        this.params = new HashMap<>();
    }

    /**
     * Err returns the error message.
     */
    @Override
    public String error() {
        if (params == null || params.isEmpty()) {
            return message;
        }
        return StrFormatter.format(this.message, this.params);
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public Err setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public Map<String, Object> params() {
        return this.params;
    }

    @Override
    public Err setParams(Map<String, Object> params) {
        this.params = params;
        return this;
    }

}
