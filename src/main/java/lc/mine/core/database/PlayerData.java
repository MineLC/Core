package lc.mine.core.database;

public class PlayerData {
    private double lcoins = 0, vipPoints = 0;
    private final double joinLcoins, joinVipPoints;
    private final String playerName;

    public PlayerData(String playerName) {
        this.playerName = playerName;
        this.joinLcoins = 0;
        this.joinVipPoints = 0;
    }

    public PlayerData(String playerName, double lcoins, double vipPoints) {
        this.playerName = playerName;
        this.lcoins = lcoins;
        this.joinLcoins = lcoins;
        this.vipPoints = vipPoints;
        this.joinVipPoints = vipPoints;
    }

    public double getLcoins() {
        return lcoins;
    }
    public double getVipPoins() {
        return vipPoints;
    }

    public double getJoinLcoins() {
        return joinLcoins;
    }
    public double getJoinVipPoints() {
        return joinVipPoints;
    }

    public void setLcoins(double lcoins) {
        this.lcoins = lcoins;
    }
    public void setVipPoins(double vipPoins) {
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
