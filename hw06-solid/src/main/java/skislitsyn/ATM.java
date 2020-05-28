package skislitsyn;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface ATM extends Serializable {
    public void doCashIn(List<Banknote> banknotes) throws IOException;

    public List<Banknote> doCashOut(long amountRequested) throws IOException;

    public long getRest();
}
