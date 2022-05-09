package com.sports.controller;

import com.sports.model.Player;
import com.sports.payload.request.player.PlayerRequest;
import com.sports.payload.response.player.PlayerResponse;
import com.sports.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RequestMapping("/players")
@RestController
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Player Created"),
            @ApiResponse(responseCode = "400", description = "Invalid information Provided"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    @Operation(description = "Adding Player", operationId = "add_player")
    @PostMapping(path = "/addPlayer", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerResponse> addPlayer(@RequestBody @Valid PlayerRequest playerRequest) {
        log.info("Creating player {} ",playerRequest);
        Player player = playerService.addPlayer(playerRequest);
        PlayerResponse playerResponse =
                PlayerResponse.builder().playerId(player.getPlayerId()).name(player.getName()).sport(player.getSport()).build();
        return new ResponseEntity<PlayerResponse>(playerResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "/{playerId}")
    public ResponseEntity<PlayerResponse> getPlayer(@PathVariable @Min(value = 1, message = "Player Id should be greater than or equal to 1") int playerId) {
        Player player = playerService.getPlayer(playerId);
        PlayerResponse playerResponse =
                PlayerResponse.builder().playerId(player.getPlayerId()).name(player.getName()).sport(player.getSport()).build();
        return new ResponseEntity<PlayerResponse>(playerResponse, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Player Updated"),
            @ApiResponse(responseCode = "201", description = "Player Created"),
            @ApiResponse(responseCode = "400", description = "Invalid information Provided"),
            @ApiResponse(responseCode = "404", description = "Not found")})
    @Operation(description = "Updating Player", operationId = "update_player")
    @PutMapping(path = "/update/{playerId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PlayerResponse> updatePlayer(@RequestBody @Valid PlayerRequest playerRequest, @PathVariable int playerId) {
        boolean checkPlayerExists = playerService.checkPlayerExists(playerId);
        Player player = playerService.updatePlayer(playerRequest);
        PlayerResponse playerResponse =
                PlayerResponse.builder().playerId(player.getPlayerId()).name(player.getName()).sport(player.getSport()).build();
        return new ResponseEntity<PlayerResponse>(playerResponse, checkPlayerExists == true ? HttpStatus.OK : HttpStatus.CREATED);
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Player details Successfully.")})
    @DeleteMapping(path = "/delete/{playerId}")
    public ResponseEntity<String> deletePlayer(@PathVariable @Min(value = 1, message = "Player Id should be greater than or equal to 1") int playerId) {
        playerService.deletePlayer(playerId);
        return new ResponseEntity(String.format("Removed Player %s", playerId), HttpStatus.OK);
    }
}
