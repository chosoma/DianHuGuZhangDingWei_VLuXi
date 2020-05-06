package com.thingtek.config.clazz;


import java.util.Map;
public class ClazzConfig {

    private Map<Integer, String> unitClazzMap;
    private Map<Integer, String> dataClazzMap;

    public String getUnitClazzName(int clt_type) {
        return unitClazzMap.get(clt_type);
    }

    public String getDataClazzName(int clt_type){
        return dataClazzMap.get(clt_type);
    }

    public Map<Integer, String> getUnitClazzMap() {
        return unitClazzMap;
    }

    public void setUnitClazzMap(Map<Integer, String> unitClazzMap) {
        this.unitClazzMap = unitClazzMap;
    }

    public Map<Integer, String> getDataClazzMap() {
        return dataClazzMap;
    }

    public void setDataClazzMap(Map<Integer, String> dataClazzMap) {
        this.dataClazzMap = dataClazzMap;
    }
}
