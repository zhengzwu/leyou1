package com.leyou.auth.config;

import com.leyou.auth.utils.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
@ConfigurationProperties(prefix = "ly.jwt")
public class JwtProperties {
    private String secret;
    private String pubKeyPath;
    private String cookieName;
    private String priKeyPath;
    private int expire;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    //构造方法，对象实例化后就应该读取公钥私钥
    @PostConstruct
    public void init() throws Exception {
        File pubPath = new File(pubKeyPath);
        File priPath = new File(priKeyPath);
        //如果不存在则生成公钥私钥
        if(!pubPath.exists()||!priPath.exists()){
            RsaUtils.generateKey(pubKeyPath,priKeyPath,secret);
        }
        this.publicKey= RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey=RsaUtils.getPrivateKey(priKeyPath);
    }
}
