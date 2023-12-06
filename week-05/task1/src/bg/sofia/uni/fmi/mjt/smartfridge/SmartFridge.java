package bg.sofia.uni.fmi.mjt.smartfridge;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;

import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

public class SmartFridge implements SmartFridgeAPI {
    private int totalCapacity;
    private int capacity;
    private Map<String, List<Storable>> store;


    public SmartFridge(int totalCapacity) {
        this.totalCapacity = totalCapacity;
        this.capacity = 0;
        this.store = new HashMap<String, List<Storable>>();

    }

    private List<Storable> toList(Storable item, int quantity) {
        List<Storable> result = new ArrayList<Storable>();
        for (int i = 0; i < quantity; i++) {
            result.add(item);
        }
        
        Collections.sort(result, (Storable f1, Storable f2) -> f2.getExpiration().compareTo(f1.getExpiration()));
        return result;
    }

    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {
        if (item == null || quantity <= 0) throw new IllegalArgumentException();
        if (capacity + quantity > totalCapacity) throw new FridgeCapacityExceededException();

        if (store.containsKey(item.getName())) {
            store.get(item.getName()).addAll(toList(item, quantity));
        } else {
            store.put(item.getName(), toList(item, quantity));
        }
        capacity += quantity;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName) {
        if (itemName == null || itemName.trim().length() == 0) throw new IllegalArgumentException();

        if (!store.containsKey(itemName)) {
            return new ArrayList<Storable>();
        }

        List<Storable> result = store.get(itemName);
        store.remove(itemName);
        capacity -= result.size();
        return result;
    }

    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {
        if (itemName == null || itemName.trim().length() == 0 || quantity <= 0) throw new IllegalArgumentException();

        if (!store.containsKey(itemName)) throw new InsufficientQuantityException();
    
        List<Storable> items = store.get(itemName);
        if (items.size() < quantity) throw new InsufficientQuantityException();

        List<Storable> result;
        if (items.size() == quantity) {
            result = items;
            store.put(itemName, new ArrayList<Storable>());
        } else {
            result = items.subList(0, quantity);
            store.put(itemName, items.subList(quantity, items.size()));
        }


        capacity -= quantity;
        return result;
    }

    @Override
    public int getQuantityOfItem(String itemName) {
        if (itemName == null || itemName.trim().length() == 0) throw new IllegalArgumentException();
        if (!store.containsKey(itemName)) return 0;

        return store.get(itemName).size();
    }

    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {
        if (recipe == null) throw new IllegalArgumentException();

        List<Ingredient<? extends Storable>> missing = new ArrayList<Ingredient<? extends Storable>>();
        for (Ingredient<? extends Storable> ingredient : recipe.getIngredients()) {
            if (!store.containsKey(ingredient.item().getName())) {
                missing.add(ingredient);
            }
            List<Storable> items = store.get(ingredient.item().getName());
            if (items == null) {
                missing.add(new DefaultIngredient<Storable>(ingredient.item(), ingredient.quantity()));
                continue;
            }

            int usableItems = 0;
            for (Storable item: items) {
                if (!item.isExpired()) usableItems++;
            }

            if (usableItems < ingredient.quantity()) {
                missing.add(new DefaultIngredient<Storable>(ingredient.item(), ingredient.quantity() - usableItems));
            }
        }

        return missing.iterator();
    }

    @Override
    public List<? extends Storable> removeExpired() {
        List<Storable> expired = new ArrayList<Storable>();
        for (Map.Entry<String, List<Storable>> entry : store.entrySet()) {
            List<Storable> nonexpired = new ArrayList<Storable>();
            if (entry.getValue() == null) continue;

            for (Storable item: entry.getValue()) {
                if (!item.isExpired()) nonexpired.add(item);
                else expired.add(item);
            }
            store.put(entry.getKey(), nonexpired);
            capacity -= expired.size();
        }
        return expired;
    }
}