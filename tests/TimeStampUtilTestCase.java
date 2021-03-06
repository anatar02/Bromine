import org.apache.log4j.Logger;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Features;
import util.TimeStampUtil;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Test class testing the TimeStampUtil
 *
 * @author Thibault Helsmoortel
 */
@Features("Utils")
public class TimeStampUtilTestCase {

    private static final Logger LOGGER = Logger.getLogger(TimeStampUtilTestCase.class);

    /**
     * Tests if a time stamp is successfully generated and returned.
     */
    @Test
    public void shouldReturnTimeStampString() {
        LOGGER.debug("Time stamp: " + TimeStampUtil.getTimeStamp());
        assertNotNull(TimeStampUtil.getTimeStamp());
        assertTrue(TimeStampUtil.getTimeStamp().length() > 0);

        String dateString = new Date().toString().substring(0, 10);
        assertEquals(dateString, TimeStampUtil.getTimeStamp().substring(0, 10));
    }

    /**
     * Tests if a short time stamp is successfully generated and returned.
     */
    @Test
    public void shouldReturnShortTimeStampString() {
        LOGGER.debug("Time stamp: " + TimeStampUtil.getShortTimeStamp());
        assertNotNull(TimeStampUtil.getShortTimeStamp());
        assertTrue(TimeStampUtil.getShortTimeStamp().length() > 0);
    }
}
