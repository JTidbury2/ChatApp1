package uk.ac.soton.comp1206.webapp;

import java.time.*;
import java.time.format.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@GetMapping("/")
	public String index() {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
      LocalTime localTime = LocalTime.now();
      return "The time at the tone will be " + formatter.format(localTime);
	}

}
