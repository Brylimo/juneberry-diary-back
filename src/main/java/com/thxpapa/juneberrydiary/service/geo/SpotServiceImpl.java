package com.thxpapa.juneberrydiary.service.geo;

import com.thxpapa.juneberrydiary.domain.file.MerciFile;
import com.thxpapa.juneberrydiary.domain.geo.Spot;
import com.thxpapa.juneberrydiary.dto.SpotRegisterRequestDto;
import com.thxpapa.juneberrydiary.repository.fileRepository.MerciFileRepository;
import com.thxpapa.juneberrydiary.repository.geoRepository.SpotRepository;
import com.thxpapa.juneberrydiary.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotServiceImpl implements SpotService {
    private final FileUtil fileUtil;

    private final SpotRepository spotRepository;
    private final MerciFileRepository merciFileRepository;

    @Override
    public Spot storeSpot(SpotRegisterRequestDto spotRegisterRequestDto) {
        try {
            // store mercifile item
            MerciFile file = fileUtil.storeFile(spotRegisterRequestDto.getSpotImg());
            merciFileRepository.save(file);

            // store spot item
            Spot storedSpot = spotRepository.save(Spot.builder()
                    .name(spotRegisterRequestDto.getSpotName())
                    .loc(spotRegisterRequestDto.getSpotLoc())
                    .exp(spotRegisterRequestDto.getSpotExp())
                    .lon(spotRegisterRequestDto.getSpotLon())
                    .lat(spotRegisterRequestDto.getSpotLat())
                    .merciFile(file)
                    .statusCd("01")
                    .build());

            return storedSpot;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
