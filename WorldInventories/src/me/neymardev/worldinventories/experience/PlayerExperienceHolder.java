package me.neymardev.worldinventories.experience;

import org.bukkit.entity.Player;

public class PlayerExperienceHolder {
    //--------------------------------------------------------------
    // class members
    private Integer level_;
    private float exp_;
    //--------------------------------------------------------------

    public PlayerExperienceHolder(Player player) {
        level_ = player.getLevel();
        exp_ = player.getExp();
    }

    public PlayerExperienceHolder() {
        level_ = 0;
        exp_ = 0;
    }

    public void resetValues() {
        level_ = 0;
        exp_ = 0;
    }

    public Integer getHolderLevel() {
        return level_;
    }

    public Float getHolderExp() {
        return exp_;
    }

}
