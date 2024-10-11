package com.thxpapa.juneberrydiary.service.geo;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import com.thxpapa.juneberrydiary.domain.geo.Spot;
import com.thxpapa.juneberrydiary.dto.SpotRegisterRequestDto;
import com.thxpapa.juneberrydiary.repository.fileRepository.JuneberryFileRepository;
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
    private final JuneberryFileRepository juneberryFileRepository;

    @Override
    public Spot storeSpot(SpotRegisterRequestDto spotRegisterRequestDto) {
        try {
            // store juneberryfile item
            JuneberryFile file = fileUtil.storeFile(spotRegisterRequestDto.getSpotImg());
            juneberryFileRepository.save(file);

            // store spot item
            Spot storedSpot = spotRepository.save(Spot.builder()
                    .name(spotRegisterRequestDto.getSpotName())
                    .loc(spotRegisterRequestDto.getSpotLoc())
                    .exp(spotRegisterRequestDto.getSpotExp())
                    .lon(spotRegisterRequestDto.getSpotLon())
                    .lat(spotRegisterRequestDto.getSpotLat())
                    .juneberryFile(file)
                    .build());

            return storedSpot;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
