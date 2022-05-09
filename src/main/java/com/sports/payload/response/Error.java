package com.sports.payload.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Error {
    private String errorCode;
    private String summary;
    private String description;
}
