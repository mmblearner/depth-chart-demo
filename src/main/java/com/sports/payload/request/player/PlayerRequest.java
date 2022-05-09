package com.sports.payload.request.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerRequest {

    @Min(value = 1, message = "Player Id should be greater than or equal to 1.")
    private int id;

    @NotBlank(message = "Invalid Player Name. Name should not be blank or Null.")
    private String name;

    @NotBlank(message = "Invalid Sport Name.")
    private String sport;

}
