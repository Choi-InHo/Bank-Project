package com.example.BankProject.loan.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private String code = "";
    private String desc = "";
    private String extraMessage = "";

    public BaseException(ResultType resultType) {
        super(resultType.getDesc());
        this.code = resultType.getCode();
        this.desc = resultType.getDesc();
    }

    public BaseException(ResultType resultType, String extraMessage) {
        super(resultType.getDesc() + " - " + extraMessage);
        this.code = resultType.getCode();
        this.desc = resultType.getDesc();
        this.extraMessage = extraMessage;
    }
}