package com.formation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {com.formation.parking.ParkingApplication.class})
class ParkingApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
		// assertTrue(false);
	}

}
