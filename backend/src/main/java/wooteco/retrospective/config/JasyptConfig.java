package wooteco.retrospective.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JasyptConfig {

    @Bean
    public StringEncryptor JasyptStringEncryptor() {
        PooledPBEStringEncryptor pooledPBEStringEncryptor = new PooledPBEStringEncryptor();

        pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig());

        return pooledPBEStringEncryptor;
    }

    private SimpleStringPBEConfig simpleStringPBEConfig() {
        SimpleStringPBEConfig simpleStringPBEConfig = new SimpleStringPBEConfig();

        simpleStringPBEConfig.setPassword(System.getProperty("jasypt.encryptor.password"));
        simpleStringPBEConfig.setAlgorithm("PBEWithMD5AndDES");
        simpleStringPBEConfig.setKeyObtentionIterations("1000");
        simpleStringPBEConfig.setPoolSize("1");
        simpleStringPBEConfig.setProviderName("SunJCE");
        simpleStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        simpleStringPBEConfig.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        simpleStringPBEConfig.setStringOutputType("base64");

        return simpleStringPBEConfig;
    }
}
