package statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController {
	
	@Autowired
	StatisticsService statsService;
	
	@GetMapping("/statistics")
	public ResponseEntity <Statistics> getStatistics() {
		return statsService.computeStatistics();
	}

}
