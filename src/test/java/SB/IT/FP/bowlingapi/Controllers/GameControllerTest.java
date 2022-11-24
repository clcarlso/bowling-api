package SB.IT.FP.bowlingapi.Controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class GameControllerTest {
    
    @org.springframework.boot.test.web.server.LocalServerPort
    private int port;

	@Autowired
	private TestRestTemplate restTemplate;

    @Autowired
    private GameController controller;

    @Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

    @Test
	public void controllerRoll() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/roll/7",
				String.class)).contains("7");
	}

    @Test
	public void controllerScore() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/score",
				String.class)).contains("The current score of the game is: 0");
	}
}
