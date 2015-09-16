package com.sdk.info.extrainfo.yahoo_finance.bean;

/**
 * Created by Administrator on 2015/8/8.
 * 汇率信息、黄金价格、土耳其指数
 */
public class FinanceInfo {

    /**
     * 欧元兑里拉比率
     */
    private String euRate;
    /**
     * 欧元/里拉比率上升
     */
    private boolean isEuRateRise;
    /**
     * 美元兑里拉比率
     */
    private String usRate;
    /**
     * 美元/里拉比率上升
     */
    private boolean isUsRateRise;
    /**
     * 黄金价格
     */
    private String altinPrice;
    /**
     * 黄金价格是否上升
     */
    private boolean isAltinRise;
    /**
     * 土耳其指数
     */
    private float bist;
    /**
     * 土耳其指数是否上涨
     */
    private boolean isBistRise;


    public String getEuRate() {
        return euRate;
    }

    public void setEuRate(String euRate) {
        this.euRate = euRate;
    }

    public boolean isEuRateRise() {
        return isEuRateRise;
    }

    public void setIsEuRateRise(boolean isEuRateRise) {
        this.isEuRateRise = isEuRateRise;
    }

    public String getUsRate() {
        return usRate;
    }

    public void setUsRate(String usRate) {
        this.usRate = usRate;
    }

    public boolean isUsRateRise() {
        return isUsRateRise;
    }

    public void setIsUsRateRise(boolean isUsRateRise) {
        this.isUsRateRise = isUsRateRise;
    }

    public String getAltinPrice() {
        return altinPrice;
    }

    public void setAltinPrice(String altinPrice) {
        this.altinPrice = altinPrice;
    }

    public boolean isAltinRise() {
        return isAltinRise;
    }

    public void setIsAltinRise(boolean isAltinRise) {
        this.isAltinRise = isAltinRise;
    }

    public float getBist() {
        return bist;
    }

    public void setBist(float bist) {
        this.bist = bist;
    }

    public boolean isBistRise() {
        return isBistRise;
    }

    public void setIsBistRise(boolean isBistRise) {
        this.isBistRise = isBistRise;
    }
}
