package skislitsyn;

public interface CashBasket {
    public void loadBanknotes(int quantity);

    public int getBanknotes(int quantity);

    public Nominal getNominal();

    public int getCurrentQuantity();

    public boolean isLoadAvailable(int quantity);

    public boolean isAvailable(int quantity);

    public boolean isFull();
}
