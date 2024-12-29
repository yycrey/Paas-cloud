package paas.rey.model;

import lombok.Data;

/**
 * @Author yeyc
 * @Description 分页初始化
 * @Date 2024/12/29
 * @Param
 * @Exception
 **/
@Data
public class PageDto {
    private int pageSize;
    private int pageNum;
    private int total;
}
