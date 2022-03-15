package chapter02;

import java.util.Map;

public class Store {

    private final Map<Product, Integer> inventory;

    public Store(Map<Product, Integer> inventory) {
        this.inventory = inventory;
    }

    public boolean hasEnoughInventory(Product product, int quantity) {
        if (quantity < 0) {
            return false;
        }
        return inventory.getOrDefault(product, -1) > quantity;
    }

    public void removeInventory(Product product, int quantity) {
        if (!hasEnoughInventory(product, quantity)) {
            throw new IllegalStateException("해당 물품은 재고가 부족합니다.");
        }
        inventory.computeIfPresent(product, (key, value) -> value -= quantity);
    }

    public void addInventory(Product product, int quantity) {
        if (inventory.containsKey(product)) {
            inventory.computeIfPresent(product, (key, value) -> value + quantity);
        }
        inventory.put(product, quantity);
    }

    public int getInventory(Product product) {
        return inventory.getOrDefault(product, 0);
    }
}
