package skislitsyn;

import java.util.List;

public interface CashBasket {
    public void loadBanknotes(List<Banknote> banknotes);

    public List<Banknote> getBanknotes(int quantity);

    public FaceValue getFaceValue();

    public int getCurrentQuantity();

    public boolean isAvailable(int quantity);

    public boolean isFull();
}
