package noraui.indus;

import org.junit.Test;

public class CounterDbPostgreUT {

    @Test
    public void testCount() throws Exception {
        Counter.main(new String[] { "DbPostgre" });
    }

}
