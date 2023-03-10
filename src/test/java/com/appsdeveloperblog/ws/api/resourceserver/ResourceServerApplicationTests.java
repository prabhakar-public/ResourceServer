package com.appsdeveloperblog.ws.api.resourceserver;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ResourceServerApplicationTests {

	@Test
	void contextLoads() {
		Assertions.assertEquals(5, 3 + 2, "basic math check ");
	}

}
