package lunatic.athenarpg.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerStatus {
    private Player player;
    private int maxHealth;
    private int currentHealth;
    private int maxMana;
    private int currentMana;
    private int currentVitality;
    private int maxVitality;

    public PlayerStatus(Player player, int maxHealth, int currentHealth, int maxMana, int maxVitality) {
        this.player = player;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.maxMana = maxMana;
        this.maxVitality = maxVitality;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMaxVitality(){
        return maxVitality;
    }
    public void setMaxVitality(int maxVitality){
        this.maxVitality = maxVitality;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setHealth(int health){
        this.currentHealth = health;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = Math.min(currentHealth, maxHealth);
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = Math.min(currentMana, maxMana);
    }

    public int getCurrentVitality() {
        return currentVitality;
    }

    public void setCurrentVitality(int currentVitality) {
        this.currentVitality = currentVitality;
    }

    public int getVitalityPercentage() {
        return (int) ((double) currentVitality / 100.0 * 100);
    }

    public void consumeVitality(int amount) {
        currentVitality -= amount;
        currentVitality = Math.max(currentVitality, 0);
    }
    public void consumeMana(int amount) {
        currentMana -= amount;
        currentMana = Math.max(currentMana, 0);
    }
    public void consumeHealth(int amount) {
        currentHealth -= amount;
        currentHealth = Math.max(currentHealth, 0);
    }
}
