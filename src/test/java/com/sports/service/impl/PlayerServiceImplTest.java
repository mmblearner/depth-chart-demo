package com.sports.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.exception.DataNotFoundException;
import com.sports.model.Player;
import com.sports.payload.request.player.PlayerRequest;
import com.sports.repository.PlayerRepository;
import com.sports.service.PlayerService;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void test_addPlayer() throws JsonProcessingException, JSONException {
        PlayerRequest playerRequest = PlayerRequest.builder().name("Test_Player").sport("Cricket").id(10).build();
        when(playerRepository.save(any())).thenReturn(Player.builder().name(playerRequest.getName()).sport(playerRequest.getSport()).playerId(playerRequest.getId()).build());
        Player player = playerService.addPlayer(playerRequest);
        String expectedResult = "{\"name\":\"Test_Player\",\"sport\":\"Cricket\",\"playerId\":10}";
        JSONAssert.assertEquals(expectedResult,objectMapper.writeValueAsString(player),false);
    }

    @Test
    public void test_updatePlayer() throws JsonProcessingException, JSONException {
        PlayerRequest playerRequest = PlayerRequest.builder().name("Test_Player").sport("Cricket").id(10).build();
        when(playerRepository.save(any())).thenReturn(Player.builder().name(playerRequest.getName()).sport(playerRequest.getSport()).playerId(playerRequest.getId()).build());
        Player player = playerService.updatePlayer(playerRequest);
        String expectedResult = "{\"name\":\"Test_Player\",\"sport\":\"Cricket\",\"playerId\":10}";
        JSONAssert.assertEquals(expectedResult,objectMapper.writeValueAsString(player),false);
    }

    @Test
    public void test_checkPlayerExists() {
        when(playerRepository.existsById(anyInt())).thenReturn(true);
        boolean result = playerService.checkPlayerExists(10);
        assertTrue(result);
    }

    @Test
    public void test_deletePlayer() {
        doNothing().when(playerRepository).deleteById(anyInt());
        playerService.deletePlayer(10);
        assertTrue(true);
    }

    @Test
    public void test_getPlayer() {
        Player player = Player.builder().name("Test_Player").sport("Cricket").playerId(10).build();
        when(playerRepository.getById(anyInt())).thenReturn(player);
        Player actualPlayer = playerService.getPlayer(10);
        assertEquals(10,actualPlayer.getPlayerId());
    }

    @Test(expected = DataNotFoundException.class)
    public void test_getPlayerNotAvailable(){
        when(playerRepository.getById(anyInt())).thenReturn(null);
        playerService.getPlayer(10);
    }
}