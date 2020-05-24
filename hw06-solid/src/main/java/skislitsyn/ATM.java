package skislitsyn;

import java.io.IOException;
import java.util.List;

public interface ATM {
    public void doCashIn(List<Banknote> banknotes) throws IOException;

    public List<Banknote> doCashOut(long amountRequested) throws IOException;

    public long getRest();
}
