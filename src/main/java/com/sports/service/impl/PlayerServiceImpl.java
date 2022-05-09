package com.sports.service.impl;

import com.sports.exception.DataNotFoundException;
import com.sports.model.Player;
import com.sports.payload.request.player.PlayerRequest;
import com.sports.payload.response.Error;
import com.sports.repository.PlayerRepository;
import com.sports.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public Player addPlayer(PlayerRequest playerRequest) throws RuntimeException {
        return playerRepository.save(
                Player.builder()
                        .name(playerRequest.getName())
                        .sport(playerRequest.getSport())
                        .build()
        );
    }


    @Override
    public Player updatePlayer(PlayerRequest playerRequest) throws RuntimeException {
        return playerRepository.save(
                Player.builder()
                        .name(playerRequest.getName())
                        .sport(playerRequest.getSport())
                        .build()
        );
    }

    @Override
    public boolean checkPlayerExists(int playerId) throws RuntimeException {
        return playerRepository.existsById(playerId);
    }

    @Override
    public void deletePlayer(int playerId) throws RuntimeException {
        playerRepository.deleteById(playerId);
    }

    @Override
    public Player getPlayer(int playerId) {
        Player player = playerRepository.getById(playerId);
        if (null==player){
            Error error = Error.builder()
                    .errorCode("EX-1003")
                    .summary(String.format("Player with Player id %s not found.",playerId))
                    .description(String.format("Player with Player id %s not found.",playerId)).build();
            throw new DataNotFoundException(error);
        }
        return player;
    }
}
