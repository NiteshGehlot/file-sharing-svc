package com.alzion.project.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponse {
    private String status;
    private String errorCode;
    private String errorMessage;

    public ErrorResponse(String status, String errorCode, String errorMessage) {
        super();
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

}
