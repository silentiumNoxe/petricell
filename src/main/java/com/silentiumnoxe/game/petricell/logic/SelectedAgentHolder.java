package com.silentiumnoxe.game.petricell.logic;

import com.silentiumnoxe.game.petricell.model.Agent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectedAgentHolder {

    private static SelectedAgentHolder instance;

    public static SelectedAgentHolder getInstance() {
        if (instance == null) {
            instance = new SelectedAgentHolder();
        }
        return instance;
    }

    private Agent selected;

    public void setSelected(final Agent selected) {
        if (this.selected != null) {
            this.selected.setSelected(false);
        }
        this.selected = selected;
    }
}
