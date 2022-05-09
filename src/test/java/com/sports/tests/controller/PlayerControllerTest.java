package com.sports.tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.controller.PlayerController;
import com.sports.model.Player;
import com.sports.payload.request.player.PlayerRequest;
import com.sports.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PlayerController.class)
@Slf4j
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private PlayerController playerController;
    @MockBean
    private PlayerService playerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void test_addPlayer() throws Exception {
        PlayerRequest playerRequest = PlayerRequest.builder().name("Test_Player").sport("Cricket").id(10).build();
        RequestBuilder request = MockMvcRequestBuilders.post("/players/addPlayer")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerRequest));
        when(playerService.addPlayer(any())).thenReturn(Player.builder().name("Test_Player").sport("Cricket").playerId(10).build());
        MvcResult result = mockMvc.perform(request).andReturn();
        String actualResponse = result.getResponse().getContentAsString();
        String expectedResult = "{\"name\":\"Test_Player\",\"sport\":\"Cricket\",\"playerId\":10}";
        JSONAssert.assertEquals(expectedResult, actualResponse, false);
    }

    @Test
    public void test_getPlayer() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/players/10")
                .accept(MediaType.APPLICATION_JSON);
        when(playerService.getPlayer(anyInt())).thenReturn(Player.builder().playerId(10).name("Test_Player").sport("Test_Sport").build());
        MvcResult result = mockMvc.perform(request).andReturn();
        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = "{\"playerId\":10,\"name\":\"Test_Player\",\"sport\":\"Test_Sport\"}";
        JSONAssert.assertEquals(expectedResult, actualResult, false);

    }


    @Test
    public void test_updatePlayer() throws Exception {
        PlayerRequest playerRequest = PlayerRequest.builder().name("Test_Player").sport("Cricket").id(10).build();
        RequestBuilder request = MockMvcRequestBuilders.put("/players/update/10")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(playerRequest));
        when(playerService.checkPlayerExists(anyInt())).thenReturn(true);
        when(playerService.updatePlayer(any())).thenReturn(Player.builder().name("Test_Player").sport("Cricket").playerId(10).build());
        MvcResult result = mockMvc.perform(request).andExpect(status().isOk()).andReturn();
        String actualResult = result.getResponse().getContentAsString();
        String expectedResult = "{\"playerId\":10,\"name\":\"Test_Player\",\"sport\":\"Cricket\"}";
        JSONAssert.assertEquals(expectedResult, actualResult, false);
    }
}
