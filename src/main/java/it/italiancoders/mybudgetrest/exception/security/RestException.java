package it.italiancoders.mybudgetrest.exception.security;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class RestException extends RuntimeException{

    private String title;

    private HttpStatus status;

    private String detail;
}
