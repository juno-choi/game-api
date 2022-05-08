package com.juno.gameapi.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPlaces {
    private String lat;
    private String lng;
    private String keyword;
    private int radius = 500;
}
