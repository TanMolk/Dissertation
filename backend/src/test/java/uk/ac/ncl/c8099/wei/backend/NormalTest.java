package uk.ac.ncl.c8099.wei.backend;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author wei tan
 */
public class NormalTest {

    @Test
    public void test() {
        System.out.println(LocalDateTime.now(ZoneId.of("UTC")));
    }
}
