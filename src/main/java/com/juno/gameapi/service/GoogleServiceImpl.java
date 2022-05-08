package com.juno.gameapi.service;

import com.juno.gameapi.controller.dto.RequestPlaces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleServiceImpl implements GoogleService{

    //google places api
    //https://developers.google.com/maps/documentation/places/web-service/search-nearby
    @Override
    public ResponseEntity<String> places(RequestPlaces requestPlaces) {
        log.info("places 호출");
        WebClient googleClient = WebClient.builder().baseUrl("https://maps.googleapis.com")
                .build();

        //거리 기본값 500
        if(requestPlaces.getRadius() == 0) requestPlaces.setRadius(500);

        ResponseEntity<String> result = googleClient.get().uri(uriBuilder ->
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

        return result;
    }
}
