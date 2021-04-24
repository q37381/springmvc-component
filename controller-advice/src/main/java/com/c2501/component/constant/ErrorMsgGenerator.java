package com.c2501.component.constant;

import java.util.Map;

import com.common.constant.SomeEnums.DASErrorCode;
import com.common.exce.DASRuntimeException;
import com.common.model.CommonExceptionReturnVO;
import com.google.common.collect.Maps;

public class ErrorMsgGenerator {

    // TODOM 后期错误码默认可以做成配置
    public static Map<String, Object> getDefaultExceptionMap() {
        Map<String, Object> map = Maps.newLinkedHashMap();
        map.put(Constant.RESPONSE_CODE_KEY, DASErrorCode.UNKNOWN_ERROR.getCode());
        map.put(Constant.RESPONSE_MSG_KEY, DASErrorCode.UNKNOWN_ERROR.getDesc());
        map.put(Constant.RESPONSE_DATA_KEY, null);
        
        return map;
    }

    public static CommonExceptionReturnVO getExceptionCommonReturnVO(Throwable e) {
        // code msg 不同项目之间调用不是直接传递枚举
        String code = DASErrorCode.UNKNOWN_ERROR.getCode();
        String msg = DASErrorCode.UNKNOWN_ERROR.getDesc();
        if (e instanceof DASRuntimeException) {
            code = ((DASRuntimeException) e).getCode();
            msg = ((DASRuntimeException) e).getDesc();
        }

        CommonExceptionReturnVO vo = new CommonExceptionReturnVO();
        vo.setCode(code);
        vo.setMsg(msg);

        return vo;
    }
}
