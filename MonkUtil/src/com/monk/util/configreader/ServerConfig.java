package com.monk.util.configreader;

import org.aeonbits.owner.Config;


@Config.HotReload
@Config.Sources("classpath:/conf.properties")
@Config.LoadPolicy(Config.LoadType.FIRST)
public interface ServerConfig extends Config {

    @Key("url")
    String url();
    
    @Key("user")
    String user();
    
    @Key("password")
    String password();
    
    @Key("port")
    int port();
    
    @Key("tableName")
    String tableName();
    
    @Key("loginPassword")
    String loginPassword();
    
    @Key("loginName")
    String loginName();
}
