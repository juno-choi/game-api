package com.juno.gameapi.controller;

import com.juno.gameapi.controller.dto.RequestPlaces;
import com.juno.gameapi.service.GoogleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/google")
@Slf4j
@RequiredArgsConstructor
public class GooglePlacesController {
    private final GoogleService googleService;

    @PostMapping("/places")
    public ResponseEntity<String> places(@RequestBody RequestPlaces requestPlaces){
        log.info("google 검색 요청 : {}");
        ResponseEntity<String> responseEntity = googleService.places(requestPlaces);
        return ResponseEntity.status(responseEntity.getStatusCode()).contentType(MediaType.APPLICATION_JSON).body(responseEntity.getBody());
    }
}
