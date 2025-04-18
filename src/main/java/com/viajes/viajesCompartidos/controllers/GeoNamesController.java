package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.geonames.GeoCity;
import com.viajes.viajesCompartidos.DTO.geonames.GeoNamesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/geonames")
public class GeoNamesController {
    private final WebClient webClient;
    @Value("${geonames.api.base-url}")
    private String baseUrl;

    @Value("${geonames.api.user-name}")
    private String userName;
    @Autowired
    public GeoNamesController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/top-popular-cities")
    public Mono<GeoNamesResponse> getTopPopularCities(@RequestParam String maxRows,
                                                                   @RequestParam String adminCode) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/searchJSON")
                .queryParam("country", "AR")
                .queryParam("adminCode1", adminCode)
                .queryParam("featureClass", "P")
                .queryParam("orderBy", "population")
                .queryParam("maxRows", maxRows)
                .queryParam("username", userName)
                .toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeoNamesResponse.class);
    }
    @GetMapping("/all-cities-from-province")
    public Mono<GeoNamesResponse> getAllCitiesFromProvince(@RequestParam String adminCode) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl + "/searchJSON")
                .queryParam("country", "AR")
                .queryParam("featureClass", "P") // Ciudades
                .queryParam("adminCode1", adminCode) // Código de la provincia
                .queryParam("username", userName)
                .toUriString();

        return webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(GeoNamesResponse.class)
                .map(response -> {
                    // Ordenar por nombre de ciudad
                    response.getGeonames().sort(Comparator.comparing(GeoCity::getName));
                    return response;
                });
    }
}