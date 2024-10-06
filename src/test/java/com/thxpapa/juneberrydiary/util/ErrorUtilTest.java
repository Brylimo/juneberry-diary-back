package com.thxpapa.juneberrydiary.util;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ErrorUtilTest {
    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor stringEncryptor;

    @Test
    void testJasypt() {
        String str = stringEncryptor.encrypt("");
        // String str2 = stringEncryptor.decrypt("");
        System.out.println("[[" + str + "]]");
    }
}