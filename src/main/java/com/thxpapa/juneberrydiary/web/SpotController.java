package com.thxpapa.juneberrydiary.web;

import com.thxpapa.juneberrydiary.domain.geo.Spot;
import com.thxpapa.juneberrydiary.dto.SpotRegisterRequestDto;
import com.thxpapa.juneberrydiary.service.geo.SpotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/spot")
public class SpotController {

    private final SpotService spotService;

    @GetMapping("/item/{spotId}")
    public String spotItem(@PathVariable Integer spotId) {
        return "geo/spot";
    }

    @PostMapping("/register")
    public String spotRegister(@ModelAttribute SpotRegisterRequestDto spotRegisterRequestDto, RedirectAttributes redirectAttributes) {
        log.debug("spotRegister starts!");

        Spot storedSpot = spotService.storeSpot(spotRegisterRequestDto);
        redirectAttributes.addAttribute("spotId", storedSpot.getSpotUid());

        return "redirect:/spot/item/{spotId}";
    }
}
