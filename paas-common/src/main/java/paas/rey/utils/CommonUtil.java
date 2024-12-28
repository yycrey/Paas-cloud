package paas.rey.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import paas.rey.enums.BizCodeEnum;
import paas.rey.exception.BizException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Random;

/**
 * @Author yeyc
 * @Description 全局通用工具类
 * @Date 2024/12/24
 * @Param
 * @Exception
 **/
@Slf4j
public class CommonUtil {
    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals("127.0.0.1")) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    ipAddress = inet.getHostAddress();
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > 15) {
                // "***.***.***.***".length()
                // = 15
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress="";
        }
        return ipAddress;
    }


    /**
     * @Description: MD5加密
     * @Param: [data]
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2024/12/24
     */
    public static String MD5(String data)  {
        try {
            java.security.MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
            }

            return sb.toString().toUpperCase();
        } catch (Exception exception) {
        }
        return null;
    }

    /**
     * 生成一个随机码
     * @param length 随机数长度
     * @return 生成的随机整数
     * @auther yeyc
     */
    public static String generateRandomNumber(int length) {
        String sources = "0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        if(0 > length){
            throw new BizException(BizCodeEnum.CODE_LEGTH_ERROR);
        }else{
            for(int i = 0; i<length; i++){
                sb.append(sources.charAt(random.nextInt(9)));
            }
        }
        return sb.toString();
    }

    /*
    获取当前时间戳
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    //获取一个随机数据并返回
    public static String getUUID(){
        return java.util.UUID.randomUUID().toString().replace("-","").substring(0,32);
    }

    /*
    根据指定长度生成随机的数字跟字母
     */
    private static final String ALL_CHAR_NUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static String getStringNumRandom(int length){
        //生成随机数字和字母,
        Random random = new Random();
        StringBuilder saltString = new StringBuilder(length);
        for (int i = 1; i <= length; ++i) {
            saltString.append(ALL_CHAR_NUM.charAt(random.nextInt(ALL_CHAR_NUM.length())));
        }
        return saltString.toString();
    }

    /**
     * @Description: 响应Json数据给前端
     * @Param: []
     * @Return: java.lang.String
     * @Author: yeyc
     * @Date: 2024/12/28
     */
    public static void sendJsonMessage(HttpServletResponse response, Object object){
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json; charset=utf-8");
        try (PrintWriter printWriter = response.getWriter()){
            printWriter.write(mapper.writeValueAsString(object));
            printWriter.close();
            response.flushBuffer();
        } catch (Exception e) {
           log.info("响应Json数据给前端失败:{}",e.getMessage());
        }
    }
}
