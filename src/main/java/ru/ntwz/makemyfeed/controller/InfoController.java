package ru.ntwz.makemyfeed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ntwz.makemyfeed.dto.response.InfoStatusDTO;
import ru.ntwz.makemyfeed.service.InfoService;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/status")
    public InfoStatusDTO status() {
        return infoService.status();
    }
}
