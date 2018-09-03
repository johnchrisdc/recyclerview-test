package xyz.jcdc.chipz.model;

import java.util.ArrayList;
import java.util.List;

public class RadioGroup {
    private int selected_id;
    private List<Radio> radios = new ArrayList<>();

    public int getSelected_id() {
        return selected_id;
    }

    public void setSelected_id(int selected_id) {
        this.selected_id = selected_id;
    }

    public List<Radio> getRadios() {
        return radios;
    }

    public void setRadios(List<Radio> radios) {
        this.radios = radios;
    }
}
