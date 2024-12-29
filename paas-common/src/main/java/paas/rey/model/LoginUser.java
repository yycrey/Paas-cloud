package paas.rey.model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @Author yeyc
 * @Description 用户登录信息
 * @Date 2024/12/28
 * @Param
 * @Exception
 **/
@Data
@ToString
@Builder
public class LoginUser {

        @ApiModelProperty(value = "用户id",example = "1")
        private Long id ;
        @ApiModelProperty(value = "用户名",example = "yeyc")
        private String name;
        @ApiModelProperty(value = "用户头像")
        private String headImg;
        @ApiModelProperty(value = "用户邮箱")
        private String mail;
        @ApiModelProperty(value = "用户登录ip",example = "181.232.121.33")
        private String ip;

        public LoginUser(long id, String name, String mail, String headImg, String ip) {
                this.id = id;
                this.name = name;
                this.mail = mail;
                this.headImg = headImg;
                this.ip = ip;
        }
        public LoginUser() {
        }

}
