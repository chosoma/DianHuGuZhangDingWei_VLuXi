package com.thingtek.view.logo;

import com.thingtek.view.shell.base.BasePanel;
import com.thingtek.view.shell.base.DataPanel;
import com.thingtek.view.shell.base.BaseSystemPanel;

import java.util.List;
import java.util.Map;

public class LogoInfo {
    private String SoftName, CompanyName, CopyrightName;
    private Map<String, BasePanel> basePanelMap;
    private Map<String, Map<String, BaseSystemPanel>> setPanelMap;
    private List<DataPanel> dataPanels;
    private List<BaseSystemPanel> systemPanels;
    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setSystemPanels(List<BaseSystemPanel> systemPanels) {
        this.systemPanels = systemPanels;
    }

    public List<BaseSystemPanel> getSystemPanels() {
        return systemPanels;
    }

    public List<DataPanel> getDataPanels() {
        return dataPanels;
    }

    public void setDataPanels(List<DataPanel> dataPanels) {
        this.dataPanels = dataPanels;
    }

    public String getSoftName() {
        return SoftName;
    }

    public void setSoftName(String softName) {
        SoftName = softName;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCopyrightName() {
        return CopyrightName;
    }

    public void setCopyrightName(String copyrightName) {
        CopyrightName = copyrightName;
    }

    public Map<String, BasePanel> getBasePanelMap() {
        return basePanelMap;
    }

    public void setBasePanelMap(Map<String, BasePanel> basePanelMap) {
        this.basePanelMap = basePanelMap;
    }

    public Map<String, Map<String, BaseSystemPanel>> getSetPanelMap() {
        return setPanelMap;
    }

    public void setSetPanelMap(Map<String, Map<String, BaseSystemPanel>> setPanelMap) {
        this.setPanelMap = setPanelMap;
    }


}
