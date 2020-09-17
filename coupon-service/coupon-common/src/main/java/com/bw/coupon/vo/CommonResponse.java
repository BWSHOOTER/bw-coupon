package com.bw.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description: 统一响应对象
 * @Author: BaoWei
 * @Date: 9/2 0002 2:03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> implements Serializable {
    private Integer code;
    private String msg;
    private T data;

    public CommonResponse(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    
}
