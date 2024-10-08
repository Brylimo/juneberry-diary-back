package com.thxpapa.juneberrydiary.repository.geoRepository;

import com.thxpapa.juneberrydiary.domain.geo.Album;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumRepository extends JpaRepository<Album, Long> {
}
