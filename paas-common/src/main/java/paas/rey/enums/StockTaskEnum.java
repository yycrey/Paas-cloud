package paas.rey.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @Author yeyc
 * @Description 描述类的作用
 * @Date 2025/1/9
 * @Param
 * @Exception
 **/
public enum StockTaskEnum {
    /*
    锁定
     */
    LOCK,
    /*
    完成
     */
    FINISH,
    /*
    释放
     */
    CANCEL;
}
