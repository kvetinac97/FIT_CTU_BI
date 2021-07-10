package kvetinac97;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class FirstController {

    private static final String template = "Hello, %s!\n";
    private final AtomicLong counter = new AtomicLong();

    // Při zavolání adresy localhost/

    @RequestMapping("/")
    public TestObject restMethod ( @RequestParam(value="test", defaultValue = "Default name") String name ) {
        System.out.println("Method was called");

        return new TestObject(
            counter.incrementAndGet(),
            String.format(template, name)
        );
    }

}
