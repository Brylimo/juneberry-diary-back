package com.thxpapa.juneberrydiary.web.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/v1/geo")
public class GeoController {
    @Value("${vworld.api.akey}")
    private String vKey;

    @GetMapping("/map")
    public String map(Model model) {
        log.debug("map starts!");

        model.addAttribute("vKey", vKey);
        return "geo/map";
    }
}
