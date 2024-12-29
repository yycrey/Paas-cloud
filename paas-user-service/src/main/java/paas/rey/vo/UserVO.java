package paas.rey.vo;

import lombok.Data;
/**
 * @Author yeyc
 * @Description UserVo
 * @Date 2024/12/29
 * @Param
 * @Exception
 **/
@Data
public class UserVO {
    private Long id;
    /**
     * 昵称
     */
    private String name;

    /**
     * 密	码
     */
    private String pwd;

    /**
     * 头像
     */
    private String headImg;

    /**
     * ⽤户签名
     */
    private String slogan;

    /**
     * 0表示	⼥，1表示男
     */
    private Integer sex;
    /**
     * 邮	箱
     */
    private String mail;

    public UserVO() {}
}
