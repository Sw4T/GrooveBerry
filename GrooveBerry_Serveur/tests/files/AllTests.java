package files;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AudioFileTest.class, LibraryTest.class, ReadingQueueTest.class })
public class AllTests {

}
