import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slf4jApiModuleHelloWorld {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Slf4jApiModuleHelloWorld.class);
        logger.info("Hello World");
    }
}
