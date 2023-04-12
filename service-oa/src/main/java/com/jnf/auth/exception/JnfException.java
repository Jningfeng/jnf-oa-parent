package com.jnf.auth.exception;

import com.jnf.auth.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author jnfstart
 */
@Data
public class JnfException extends RuntimeException {

    private Integer code ;

    private String msg ;

    public JnfException(Integer code,String msg){
        super(msg);
        this.code = code ;
        this.msg = msg ;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public JnfException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "JnfException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }


}
