package com.zjc.friend.demo.entity;

import java.util.Date;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 用户信息表
 * </p>
 *
 * @author zjc
 * @since 2020-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Userinfo implements Serializable {

//    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 用户电话
     */
//    @NotEmpty(message = "电话号码不能为空!")
    private String phone;

    /**
     * 昵称
     */
//    @NotEmpty(message = "昵称不能为空!")
    private String username;

    /**
     * 密码
     */
//    @NotEmpty(message = "密码不能为空!")
    private String password;

    /**
     * 真实姓名
     */
    private String petName;

    /**
     * 地址
     */
    private String address;

    /**
     * 头像
     */
    private String userPic;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 性别
     */
    private String sex;

    private Date birthday;

    private Boolean status;

    @TableField(exist = false)
    private String birthdayTime;

    @TableField(exist = false)
    private List<SysRole> roles;

    @TableField(exist = false)
    private Boolean myFriend = false;

    /**
     * 用户id
     */
//    private String userId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public List<SysRole> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRole> roles) {
        this.roles = roles;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayTime() {
        return birthdayTime;
    }

    public void setBirthdayTime(String birthdayTime) {
        this.birthdayTime = birthdayTime;
    }
}
