package com.thxpapa.juneberrydiary.repository.fileRepository;

import com.thxpapa.juneberrydiary.domain.file.JuneberryFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JuneberryFileRepository extends JpaRepository<JuneberryFile, Integer> {
}
