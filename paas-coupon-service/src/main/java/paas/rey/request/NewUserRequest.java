package paas.rey.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author yeyc
 * @Description 用户服务请求对象
 * @Date 2025/1/3
 * @Param
 * @Exception
 **/
@Data
@ApiModel(value = "用户服务请求对象")
public class NewUserRequest {
        @ApiModelProperty(value = "用户ID",required = true)
        private Long id;
        @ApiModelProperty(value = "用户昵称",required = true)
        private String name;
}
