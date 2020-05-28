package skislitsyn;

import java.io.IOException;
import java.util.List;

public interface ATMDepartment {
    public void addATM(ATM atm);

    public List<ATM> getATMs();

    public void loadBanknotes(List<Banknote> banknotes) throws IOException;

    public List<Banknote> getAmount(long amountRequested) throws IOException;

    public long getRest();

    public void restoreInitialATMsState();
}
