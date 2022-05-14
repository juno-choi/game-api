package com.juno.gameapi.service;

import com.juno.gameapi.controller.dto.RequestPlaces;
import com.juno.gameapi.webclient_config.GoogleWebClient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GoogleServiceImplTest {

    @Autowired
    public GoogleWebClient googleWebClient;

    @Autowired
    private GoogleServiceImpl googleService;


    @Test
    void plcaes_api_호출(){
        //given
        RequestPlaces requestPlaces = RequestPlaces.builder()
                .lat("37.4472704")
                .lng("127.156224")
                .keyword("중국집")
                .radius(500)
                .build();
        ResponseEntity<String> result = googleWebClient.getGoogleWebClient().get().uri(uriBuilder ->
                uriBuilder.path("/maps/api/place/nearbysearch/json")
                        .queryParam("location", String.format("%s , %s",requestPlaces.getLat(), requestPlaces.getLng()))
                        .queryParam("keyword", requestPlaces.getKeyword())
                        .queryParam("radius", requestPlaces.getRadius())
                        .queryParam("language", "ko")
                        .queryParam("key", "AIzaSyDd4q1fnJ_2BBXJo8TgMA1-0Csgf_y6Ya8")
                        .build()
        ).retrieve()
        .toEntity(String.class)
        .block();

        //when
        ResponseEntity<String> places = googleService.places(requestPlaces);
        //then
        Assertions.assertThat(result.getStatusCode()).isEqualTo(places.getStatusCode());
    }
}