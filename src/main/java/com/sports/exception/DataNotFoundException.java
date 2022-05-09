package com.sports.exception;

import com.sports.payload.response.Error;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DataNotFoundException extends RuntimeException{
    private Error error;
}
