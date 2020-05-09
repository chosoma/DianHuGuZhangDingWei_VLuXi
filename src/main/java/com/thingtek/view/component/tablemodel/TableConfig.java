package com.thingtek.view.component.tablemodel;

import lombok.Data;

import java.util.Map;

public
@Data
class TableConfig {

    private Map<String, BaseTableModel> dataTableModels;

    private Map<String, BaseTableModel> unitTableModels;

    private Map<String, String> decimalreg;

    private String datereg;


}
