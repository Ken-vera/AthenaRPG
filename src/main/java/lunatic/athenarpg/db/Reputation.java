package lunatic.athenarpg.db;

import java.util.UUID;

public class Reputation {
    private UUID playerUUID;
    private String playerName;
    private int bryzleReputation, articReputation;

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getBryzleReputation() {
        return bryzleReputation;
    }

    public void setBryzleReputation(int bryzleReputation) {
        this.bryzleReputation = bryzleReputation;
    }

    public int getArticReputation() {
        return articReputation;
    }

    public void setArticReputation(int articReputation) {
        this.articReputation = articReputation;
    }
}
