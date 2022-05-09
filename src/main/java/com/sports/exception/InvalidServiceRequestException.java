package com.sports.exception;

import com.sports.payload.response.Error;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@ToString
public class InvalidServiceRequestException extends RuntimeException{
    private Error error;
}
