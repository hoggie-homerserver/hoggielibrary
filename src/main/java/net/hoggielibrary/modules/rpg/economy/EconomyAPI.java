package net.hoggielibrary.modules.rpg.economy;

import net.hoggielibrary.core.logging.HoggieLogger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Economy API for RPG currency management.
 */
public final class EconomyAPI {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    /**
     * Gets or creates an account for a player.
     *
     * @param playerUuid the player UUID
     * @return the account
     */
    public Account getAccount(UUID playerUuid) {
        return accounts.computeIfAbsent(playerUuid, k -> new Account());
    }

    /**
     * Deposits currency into a player's account.
     *
     * @param playerUuid the player UUID
     * @param currency the currency type
     * @param amount the amount to deposit
     */
    public void deposit(UUID playerUuid, String currency, double amount) {
        getAccount(playerUuid).deposit(currency, amount);
    }

    /**
     * Withdraws currency from a player's account.
     *
     * @param playerUuid the player UUID
     * @param currency the currency type
     * @param amount the amount to withdraw
     * @return true if successful
     */
    public boolean withdraw(UUID playerUuid, String currency, double amount) {
        return getAccount(playerUuid).withdraw(currency, amount);
    }

    /**
     * Gets the balance for a specific currency.
     *
     * @param playerUuid the player UUID
     * @param currency the currency type
     * @return the balance
     */
    public double getBalance(UUID playerUuid, String currency) {
        return getAccount(playerUuid).getBalance(currency);
    }

    /**
     * Transfers currency between players.
     *
     * @param from the sender UUID
     * @param to the receiver UUID
     * @param currency the currency type
     * @param amount the amount to transfer
     * @return true if successful
     */
    public boolean transfer(UUID from, UUID to, String currency, double amount) {
        if (withdraw(from, currency, amount)) {
            deposit(to, currency, amount);
            return true;
        }
        return false;
    }

    public static final class Account {
        private final Map<String, Double> balances = new ConcurrentHashMap<>();

        public double getBalance(String currency) {
            return balances.getOrDefault(currency, 0.0);
        }

        public void deposit(String currency, double amount) {
            balances.merge(currency, amount, Double::sum);
        }

        public boolean withdraw(String currency, double amount) {
            double current = getBalance(currency);
            if (current >= amount) {
                balances.put(currency, current - amount);
                return true;
            }
            return false;
        }

        public Map<String, Double> getAllBalances() {
            return Map.copyOf(balances);
        }
    }
}
