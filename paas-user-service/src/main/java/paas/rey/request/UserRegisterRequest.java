package paas.rey.request;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.Date;

/**
 * @Author yeyc
 * @Description 请求实体类封装
 * @Date 2024/12/25
 * @Param
 * @Exception
 **/
@Api(tags = "用户模块请求实体类封装")
@Data
public class UserRegisterRequest {

    /**
     * 昵称
     */
    @ApiModelProperty(value = "用户名",example = "yeyc")
    private String name;

    /**
     * 密	码
     */
    @ApiModelProperty(value = "密码",example = "123456")
    private String pwd;

    /**
     * 头像
     */
    @ApiModelProperty(value = "图片地址",example = "https://rey-paas-cloud.oss-cn-hangzhou.aliyuncs.com/test/2024/12/25/3d84327e213d4094914bfecb5581c115.jpg?Expires=1735103372&OSSAccessKeyId=TMP.3KhRwHPvYfzAwmrxytuJE7mbN1V5KD2HqmvxUF2ZBYZBEDSC3VPx2c2j7UVMqzeBMAsbTovUPyrtNh3c9VUKfSP2qisETd&Signature=MA8Mp0o3MT%2BWIv7a0ql%2FrDCXQNs%3D")
    private String headImg;

    /**
     * ⽤户签名
     */
    @ApiModelProperty(value = "签名",example = "https://rey-paas-cloud.oss-cn-hangzhou.aliyuncs.com/test/2024/12/25/3d84327e213d4094914bfecb5581c115.jpg?Expires=1735103372&OSSAccessKeyId=TMP.3KhRwHPvYfzAwmrxytuJE7mbN1V5KD2HqmvxUF2ZBYZBEDSC3VPx2c2j7UVMqzeBMAsbTovUPyrtNh3c9VUKfSP2qisETd&Signature=MA8Mp0o3MT%2BWIv7a0ql%2FrDCXQNs%3D")
    private String slogan;

    /**
     * 0表示	⼥，1表示男
     */
    @ApiModelProperty(value = "性别（0：女，1：男）",example = "1")
    private Integer sex;

    /**
     * 积	分
     */
    @ApiModelProperty(value = "积分",example = "0")
    private Integer points;
    @ApiModelProperty(value = "创建时间",example = "2024-12-25")
    private Date createTime;
    /**
     * 邮	箱
     */
    @ApiModelProperty(value = "邮箱",example = "215598070@qq.com")
    private String mail;

    /**
     * 盐，⽤于个⼈敏感信息处理
     */
    @ApiModelProperty(value = "个⼈敏感信息",example = "fb86d07666874658acd9f36694a5b365")
    private String secret;



    @ApiModelProperty(value = "验证码",example = "")
    private String code;
}
