# CITool


## 引入依赖

在app下的`build.gradle`中添加

```groovy
apply from: 'http://gitlab.jfz.net/Android/Android-Dependencies/raw/master/CI/CITool.gradle'
```
citool需要依赖versionName字段命名apk包

项目的版本信息请以如下方式处理：
```groovy
rootProject.ext {
    android = [
            ...
            versionName      : "5.4.0"
    ]
}
```

项目中buildTypes只支持debug和release两种，release产出的包会直接进入release的相关目录，其他debug以及其他包都会放入debug相关目录。分类判断的标准是查看产生的包路径中是否存在release为依据，所以请不要在productFlavors使用release关键词，以免出现apk存错路径。

```groovy
 buildTypes {
        debug {
        	//apk会存入debug相关目录
        }

        release {
           //apk会存入release相关目录 
        }
        
        otherXxx {
           //apk会存入debug相关目录 
        }
    }
```

同步下项目会在Android Studio右侧gradle栏的app目录下生成citool工具组

包括如下功能：

- formatApkName  用于重命名apk
- uploadApk2Ftp  上传到ftp服务器
- uploadApk2AppStore  上传到公司内部App Store供测试以及公司内部人员下载（注：该task只能在命令行下执行）
- uploadApk2AppStoreMacEnv 功能同上，只处理处理系统差异
- printCIConfig  打印配置文件信息

## 配置CI环境参数

两种方式优先获取ciConfig方式中的参数信息，各个参数很简单，不一一介绍。

### ciConfig方式

在项目rootProject对象下创建ciConfig属性：
内容格式如下：
```groovy

rootProject.ext {
    ciConfig = [
            appStoreConfig: [
                    app_store_url: "",
                    token        : "",
                    plat_id      : [
                            debug  : 6,
                            release: 8
                    ]
            ],
            ftpConfig     : [
                    ftp_ip       : "",
                    ftp_username : "",
                    ftp_userpwd  : "",
                    apk_save_path: [
                            debug  : "/Android/CommitBuild",
                            release: "/Android/ReleaseBuild"
                    ]
            ]

    ]
}
```

### 环境变量方式

通过配置环境变量获取上述key所对应的值。

需要注意的是有些值的key需要带上父级key。

```groovy
//app store 配置
    if (ci_config_json.appStoreConfig == null) {
        ci_config_json.appStoreConfig = [
                app_store_url: System.getenv("app_store_url"),
                token        : System.getenv("store_token"),
                plat_id      : [
                        debug  : System.getenv("plat_id_debug"),//android线下
                        release: System.getenv("plat_id_release")//android线上
                ]
        ]
    }

    //ftp 配置
    if (ci_config_json.ftpConfig == null) {
        ci_config_json.ftpConfig = [
                ftp_ip       : System.getenv("ftp_ip"),
                ftp_username : System.getenv("ftp_username"),
                ftp_userpwd  : System.getenv("ftp_userpwd"),
                apk_save_path: [
                        debug  : System.getenv("apk_save_path_debug"), 
                        release: System.getenv("apk_save_path_release") 
                ]
        ]
    }
```




## 使用

在gradle打完包后调用：

./gradlew formatApkName --stacktrace

./gradlew uploadApk2Ftp --stacktrace

./gradlew uploadApk2AppStore --stacktrace