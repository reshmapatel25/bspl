package com.example.register.error;

public class RecordAlreadyExistException  extends RuntimeException{
    String message="Record already exist.";
    public RecordAlreadyExistException (){
        super();
    }
    public RecordAlreadyExistException (String message){
        this.message=message;
    }
    public String getMessage(){
        return message;
    }
}
