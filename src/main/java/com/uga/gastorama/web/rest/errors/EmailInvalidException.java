package com.uga.gastorama.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EmailInvalidException extends AbstractThrowableProblem {

    private static final long serialVersionUID = 1L;

    public EmailInvalidException() {
        super(ErrorConstants.INVALID_EMAIL_TYPE, "Invalid email", Status.BAD_REQUEST);
    }
}
