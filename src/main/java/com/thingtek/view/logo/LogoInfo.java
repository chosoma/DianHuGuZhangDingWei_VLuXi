package com.thingtek.view.logo;

import java.util.List;
import java.util.Map;

import com.thingtek.view.shell.BasePanel;
import com.thingtek.view.shell.DataPanel;
import com.thingtek.view.shell.dataCollect.base.BaseCollectPanel;
import com.thingtek.view.shell.systemSetup.systemSetupComptents.base.BaseSystemPanel;

public class LogoInfo {
    private String SoftName, CompanyName, CopyrightName;
    private Map<String, BasePanel> basePanelMap;
    private Map<String, Map<String, BaseSystemPanel>> setPanelMap;
    private Map<String, BaseCollectPanel> collectPanelMap;
    private List<DataPanel> dataPanels;

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

    public Map<String, BaseCollectPanel> getCollectPanelMap() {
        return collectPanelMap;
    }

    public void setCollectPanelMap(Map<String, BaseCollectPanel> collectPanelMap) {
        this.collectPanelMap = collectPanelMap;
    }


}
