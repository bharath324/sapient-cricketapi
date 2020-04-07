package com.sapient.cricketapi.controllers;

import com.sapient.cricketapi.exceptions.InvalidUniqueIdException;
import com.sapient.cricketapi.exceptions.ServiceIsNotAvailableException;
import com.sapient.cricketapi.model.CricketScore;
import com.sapient.cricketapi.service.CricketScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class CricketScoreController {

    private final RestTemplate restTemplate;
    Logger logger = LoggerFactory.getLogger(CricketScoreController.class);
    @Value("${endpoint}")
    String endpoint;

    @Value("${apikey}")
    String apikey;

    private CricketScoreService cricketScoreService;

    @Autowired
    public CricketScoreController(CricketScoreService cricketScoreService,
                                  RestTemplate restTemplate) {
        this.cricketScoreService = cricketScoreService;
        this.restTemplate = restTemplate;
    }


    @GetMapping("/winner/{uniqueId}")
    public String getWinner(@PathVariable String uniqueId) {
        checkUniqueId(uniqueId);

        String newEndPoint = endpoint + "?apikey=" + apikey + "&unique_id=" + uniqueId;
        logger.info("New Endpoint : " + newEndPoint);

        CricketScore cricketScore = null;
        try {
            cricketScore = restTemplate.getForObject(
                    newEndPoint, CricketScore.class);
        } catch (RuntimeException e) {
            throw new ServiceIsNotAvailableException(String.format("Error:%s<br/>Service is not available for the given uniqueId:%s",
                    e.getMessage(), uniqueId));
        }

        checkCricketSore(cricketScore);
        logger.info("Cricket Score: " + cricketScore);

        String result = cricketScoreService.getWinnerResponse(cricketScore);
        logger.info("Result : " + result);

        return result;
    }

    private void checkUniqueId(@PathVariable String uniqueId) {
        if (uniqueId == null || uniqueId.isEmpty()) {
            throw new InvalidUniqueIdException("Invalid unique id");
        }
    }

    private void checkCricketSore(CricketScore cricketScore) {
        if (cricketScore != null && cricketScore.getError() != null &&
                !cricketScore.getError().isEmpty()) {
            throw new ServiceIsNotAvailableException(String.format("Service is not available with following " +
                    "error :%s\n", cricketScore.getError()));
        }
    }
}
