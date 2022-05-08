package com.juno.gameapi.service;

import com.juno.gameapi.controller.dto.RequestPlaces;
import org.springframework.http.ResponseEntity;

public interface GoogleService {
    ResponseEntity<String> places(RequestPlaces requestPlaces);
}
