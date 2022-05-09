package com.sports.service;

import com.sports.model.Player;
import com.sports.payload.request.player.PlayerRequest;

public interface PlayerService {
    Player addPlayer(PlayerRequest playerRequest) throws RuntimeException;

    Player updatePlayer(PlayerRequest playerRequest)  throws RuntimeException;

    boolean checkPlayerExists(int playerId) throws RuntimeException;

    public void deletePlayer(int playerId) throws RuntimeException;

    Player getPlayer(int playerId);
}
