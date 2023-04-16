package com.farsunset.cim.mvc.response;

import org.springframework.http.HttpStatus;

public class ResponseEntity<T> {

    private int code = HttpStatus.OK.value();
    private String message;
    private T data;
    private String token;
    private Long timestamp;

    public static ResponseEntity<Void> make() {
        return new ResponseEntity<>();
    }

    public static ResponseEntity<Void> make(int code) {
        return make(code, null);
    }

    public static <T> ResponseEntity<T> make(int code, String message) {
        ResponseEntity<T> result = new ResponseEntity<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    public static ResponseEntity<Void> make(HttpStatus status) {
        ResponseEntity<Void> result = new ResponseEntity<>();
        result.setCode(status.value());
        result.setMessage(status.getReasonPhrase());
        return result;
    }

    public static <Q> ResponseEntity<Q> make(HttpStatus status, String message) {
        ResponseEntity<Q> result = new ResponseEntity<>();
        result.setCode(status.value());
        result.setMessage(message);
        return result;
    }

    public static <Q> ResponseEntity<Q> ok(Q data) {
        ResponseEntity<Q> result = new ResponseEntity<>();
        result.setData(data);
        return result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

}
