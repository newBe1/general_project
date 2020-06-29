package com.example.uitls;

import cn.hutool.core.codec.Base64;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.exception.CustomException;
import com.example.redis.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * Description:
 * 获取JwtToken，获取JwtToken中封装的信息，判断JwtToken是否存在
 * 1. encode()，参数是=签发人，存在时间，一些其他的信息=。返回值是JwtToken对应的字符串
 * 2. decode()，参数是=JwtToken=。返回值是荷载部分的键值对
 * 3. isVerify()，参数是=JwtToken=。返回值是这个JwtToken是否存在
 * User: Ryan
 * Date: 2020-04-23
 * Time: 13:46
 */
@Slf4j
public class JwtUtil {
    /**
     * 过期时间改为从配置文件获取
     */
    private static String accessTokenExpireTime;

    /**
     * JWT认证加密私钥(Base64加密)
     */
    private static String encryptJWTKey;

    @Value("${accessTokenExpireTime}")
    public void setAccessTokenExpireTime(String accessTokenExpireTime) {
        JwtUtil.accessTokenExpireTime = accessTokenExpireTime;
    }

    @Value("${encryptJWTKey}")
    public void setEncryptJWTKey(String encryptJWTKey) {
        JwtUtil.encryptJWTKey = encryptJWTKey;
    }

    /**
     * 校验token是否正确
     *
     * @param token Token
     * @return boolean 是否正确
     */
    public static boolean verify(String token) {

        // 帐号加JWT私钥解密
        String secret = getClaim(token, RedisConstant.USERNAME) + Base64.decodeStr(encryptJWTKey);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
        return true;

    }

    /**
     * 获得Token中的信息无需secret解密也能获得
     *
     * @param token
     * @param claim
     * @return java.lang.String
     */
    public static String getClaim(String token, String claim) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            // 只能输出String类型，如果是其他类型返回null
            return jwt.getClaim(claim).asString();
        } catch (JWTDecodeException e) {
            log.error("解密Token中的公共信息出现JWTDecodeException异常:{}", e.getMessage());
            throw new CustomException("解密Token中的公共信息出现JWTDecodeException异常:" + e.getMessage());
        }
    }

    /**
     * 生成签名
     *
     * @param userName 帐号
     * @return java.lang.String 返回加密的Token
     * @author Wang926454
     * @date 2018/8/31 9:07
     */
    public static String sign(String userName, String currentTimeMillis) {

        // 帐号加JWT私钥加密
        String secret = userName + Base64.decodeStr(encryptJWTKey);
        // 此处过期时间是以毫秒为单位，所以乘以1000
        Date date = new Date(System.currentTimeMillis() + Long.parseLong(accessTokenExpireTime) * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带account帐号信息
        return JWT.create()
                .withClaim("userName", userName)
                .withClaim("currentTimeMillis", currentTimeMillis)
                .withExpiresAt(date)
                .sign(algorithm);

    }

}
