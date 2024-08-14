package lc.mine.core.database;

public class PlayerData {
    private double lcoins = 0, vipPoints = 0;
    private double oldLcoins = 0, oldVipPoints = 0;
    private final String playerName;

    public PlayerData(String playerName) {
        this.playerName = playerName;
    }

    public PlayerData(String playerName, double lcoins, double vipPoints) {
        this.playerName = playerName;
        this.lcoins = lcoins;
        this.oldLcoins = lcoins;
        this.vipPoints = vipPoints;
        this.oldVipPoints = vipPoints;
    }

    public double getLcoins() {
        return lcoins;
    }
    public double getVipPoins() {
        return vipPoints;
    }

    public double getOldLcoins() {
        return oldLcoins;
    }

    public double getOldVipPoints() {
        return oldVipPoints;
    }

    public void setLcoins(double lcoins) {
        this.oldLcoins = this.lcoins;
        this.lcoins = lcoins;
    }
    public void setVipPoins(double vipPoins) {
        this.oldVipPoints = this.vipPoints;
        this.vipPoints = vipPoins;
    }

    public String getPlayerName() {
        return playerName;
    }

    public static class New extends PlayerData {
        public New(String playerName) {
            super(playerName);
        }
    }
}
