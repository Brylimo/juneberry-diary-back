package com.thxpapa.juneberrydiary.service.geo;

import com.thxpapa.juneberrydiary.domain.geo.Spot;
import com.thxpapa.juneberrydiary.dto.SpotRegisterRequestDto;

public interface SpotService {
    Spot storeSpot(SpotRegisterRequestDto spotRegisterRequestDto);
}
