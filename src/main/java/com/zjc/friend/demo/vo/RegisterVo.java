package com.zjc.friend.demo.vo;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * @ Author     ：zjc
 * @ Date       ：Created in 15:07 2020/2/25
 * @ Description：
 * @ Modified By：
 * @Version: $
 */
@Data
@Valid
public class RegisterVo {

    private String phone;

    private String realName;

    private String userPwd;

    private String username;

    private String sex;

    private String address;

    private String icon;
}
