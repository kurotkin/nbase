import com.kurotkin.processing.PorterStemmer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Created by Андрей on 24.07.2017.
 */
@RunWith(Parameterized.class)
public class PorterTest {
    private String valueIn;
    private String expected;

    public PorterTest(String valueIn, String expected) {
        this.valueIn = valueIn;
        this.expected = expected;
    }

    @Parameterized.Parameters(name = "{index}: {0} - {1}")
    public static Iterable<Object[]> dataForTest() {
        return Arrays.asList(new Object[][]{
                {"абиссинию", "абиссин"},
                {"алексеевичем", "алексеевич"},
                {"проницал", "проница"},
                {"просо", "прос"},
                {"просроченный", "просрочен"}
        });
    }

    @Test
    public void paramTest() {
        assertEquals(expected, PorterStemmer.doStem(valueIn));
    }
}
