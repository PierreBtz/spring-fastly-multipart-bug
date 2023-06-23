package eu.pierrebeitz.springfastlymultipartbug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    private static final Logger LOG = LoggerFactory.getLogger(Controller.class);

    @PostMapping(value = "/api/test")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void uploadAdvisorReport(@RequestParam(value = "report", required = false) String report,
                                    @RequestParam("attachmentId") String attachmentId) {
        LOG.info("Received a report for attachment {}", attachmentId);
        LOG.info("Report content is {}", report);
    }
}