package cn.vpclub.pinganquan.mobile.common.weixin.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/6/26 0026.
 */
public class WeixinUserInfoDto implements Serializable {

    private String openid;

    private String nickname;

    private int sex;

    private String language;

    private String city;

    private String province;

    private String country;

    private String headimgurl;

    private List<String> privilege ;

    public void setOpenid(String openid){
        this.openid = openid;
    }
    public String getOpenid(){
        return this.openid;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setSex(int sex){
        this.sex = sex;
    }
    public int getSex(){
        return this.sex;
    }
    public void setLanguage(String language){
        this.language = language;
    }
    public String getLanguage(){
        return this.language;
    }
    public void setCity(String city){
        this.city = city;
    }
    public String getCity(){
        return this.city;
    }
    public void setProvince(String province){
        this.province = province;
    }
    public String getProvince(){
        return this.province;
    }
    public void setCountry(String country){
        this.country = country;
    }
    public String getCountry(){
        return this.country;
    }
    public void setHeadimgurl(String headimgurl){
        this.headimgurl = headimgurl;
    }
    public String getHeadimgurl(){
        return this.headimgurl;
    }
    public void setPrivilege(List<String> privilege){
        this.privilege = privilege;
    }
    public List<String> getPrivilege(){
        return this.privilege;
    }

    @Override
    public String toString() {
        return "WeixinUserInfoDto{" +
                "openid='" + openid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", headimgurl='" + headimgurl + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}
