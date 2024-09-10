package io.andy.shorten_url;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/ping")
    public String pingTest() {
        logger.debug("request ping..");
        return "pong!";
    }
}
