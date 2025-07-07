package ru.ntwz.feedify.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ntwz.feedify.dto.response.InfoHealthDto;
import ru.ntwz.feedify.service.InfoService;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/health")
    public InfoHealthDto health() {
        return infoService.health();
    }
}
