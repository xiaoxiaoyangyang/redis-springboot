package com.yangyang.redis.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: guozhiyang_vendor
 * @Date: 2019/2/20 13:37
 * @Version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable{
    private static final long serialVersionUID = 743212050645262442L;
    private Long id;
    private String username;
    private String password;
}
