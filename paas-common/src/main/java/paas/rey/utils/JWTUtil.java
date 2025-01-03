package paas.rey.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import paas.rey.model.LoginUser;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * @Author yeyc
 * @Description JWT工具类
 * JWT组成：header+payload+signature
 * - - - 头部：主要是描述签名算法
 *     - 负载：主要描述是加密对象的信息，如用户的id等，也可以加些规范里面的东西，如iss签发者，exp 过期时间，sub 面向的用户
 *     - 签名：主要是把前面两部分进行加密，防止别人拿到token进行base解密后篡改token
 * @Date 2024/12/28
 * @Param
 * @Exception
 **/
@Slf4j
public class JWTUtil {
    /*
       token失效时间EXPRICE
         */
      private static final Long EXPRICE = 1000 * 60 * 60 * 24 * 7L;
        /*
        测试，过期1秒
         */
//    private static final Long EXPRICE = 1000L;

    /*
    密钥
     */
    private static final String SECRET = "rey.pass666";

    /*
    Token 前缀
     */
    private static final String TOKEN_REFIX = "paas";
    /*
    Subject 前缀
 */
    private static final String SUBJECT = "yeyc233";


    /**
     * @Description: 根据用户信息，生成令牌
     * @Param: 
     * @Return: 
     * @Author: yeyc
     * @Date: 2024/12/28
     */
    public static String getJsonWebToken(LoginUser loginUser){
        if(null == loginUser){
            throw new NullPointerException("用户信息不能为空");
        }
        String token = Jwts.builder().setSubject(SUBJECT)
                .claim("head_img", loginUser.getHeadImg())
                .claim("name", loginUser.getName())
                .claim("mail", loginUser.getMail())
                .claim("ip", loginUser.getIp())
                .claim("id", loginUser.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPRICE))
                .signWith(SignatureAlgorithm.HS256,SECRET).compact();
        token = TOKEN_REFIX + token;
        return token;
    }
    
    /**
     * @Description: 校验token(解析)
     * @Param: [token]
     * @Return: io.jsonwebtoken.Claims
     * @Author: yeyc
     * @Date: 2024/12/28
     */
    public static Claims checkJWT(String token){
        try{
           final Claims claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_REFIX,"")).getBody();
           return claims;
        }catch (Exception e){
           log.info("token校验失败");
           return null;
        }
    }
}
