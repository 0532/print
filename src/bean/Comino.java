package bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Create by Lichao.W On 2014/12/8
 * email:wanglichao@163.com
 */
public class Comino  {
    private String comcod;  // 客户号
    private String comnam; //客户名称
    private String biznam; //大经营体
    private String cmsnam; //小经营体
    private String mngnam; //客户经理
    private String compan; //集团内外
    private Date upddat;  //更新日期
    //private String upddat;

    /*public Comino() {
    }

    public Comino(String comcod, String comnam) {
        this.comcod = comcod;
        this.comnam = comnam;
    }*/

    public Comino(String comcod, String comnam, String biznam, String cmsnam, String mngnam, String compan, Date upddat) {
        this.comcod = comcod;
        this.comnam = comnam;
        this.biznam = biznam;
        this.cmsnam = cmsnam;
        this.mngnam = mngnam;
        this.compan = compan;
        this.upddat = upddat;
    }

    public String getComcod() {
        return comcod;
    }

    public void setComcod(String comcod) {
        this.comcod = comcod;
    }

    public String getComnam() {
        return comnam;
    }

    public void setComnam(String comnam) {
        this.comnam = comnam;
    }

    public String getBiznam() {
        return biznam;
    }

    public void setBiznam(String biznam) {
        this.biznam = biznam;
    }

    public String getCmsnam() {
        return cmsnam;
    }

    public void setCmsnam(String cmsnam) {
        this.cmsnam = cmsnam;
    }

    public String getMngnam() {
        return mngnam;
    }

    public void setMngnam(String mngnam) {
        this.mngnam = mngnam;
    }

    public String getCompan() {
        return compan;
    }

    public void setCompan(String compan) {
        this.compan = compan;
    }

    public Date getUpddat() {
        return upddat;
    }

    public void setUpddat(Date upddat) {
        this.upddat = upddat;
    }
}
