package com.juno.gameapi.controller.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestPlaces {
    private String lat;
    private String lng;
    private String keyword;
    private int radius;
}
