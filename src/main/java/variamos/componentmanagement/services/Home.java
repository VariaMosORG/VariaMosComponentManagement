package variamos.componentmanagement.services;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class Home {
	
	@CrossOrigin
	@RequestMapping("/test")
	String home() {
		return "Ok";
	}
	
}
