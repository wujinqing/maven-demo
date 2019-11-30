## Settings Reference

### settings.xml位置共2个地方

#### Maven安装目录
> ${maven.home}/conf/settings.xml

#### 用户目录(优先级更高)
> ${user.home}/.m2/settings.xml

### settings.xml结构

```xml
    <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
      <localRepository/>
      <interactiveMode/>
      <offline/>
      <pluginGroups/>
      <servers/>
      <mirrors/>
      <proxies/>
      <profiles/>
      <activeProfiles/>
    </settings>
```

### localRepository指定本地仓库位置
> 默认位置: ${user.home}/.m2/repository

### interactiveMode是否和用户输入进行交互，默认true

### offline 离线模式, 默认false, 在没网、无法连到远程仓库的情况下可以开启次模式

### Servers 配置连接远程仓库的用户名密码信息

```xml
<servers>
    <server>
      <id>server001</id>
      <username>my_login</username>
      <password>my_password</password>
      <privateKey>${user.home}/.ssh/id_dsa</privateKey>
      <passphrase>some_passphrase</passphrase>
      <filePermissions>664</filePermissions>
      <directoryPermissions>775</directoryPermissions>
      <configuration></configuration>
    </server>
  </servers>
```

|参数|说明|示例|
|---|---|---|
|id|这个server的id|matches the id element of the repository/mirror that Maven tries to connect to以及pom中distributionManagement.repository.id distributionManagement.snapshotRepository.id的id也会匹配这个id对应的用户名密码|
|username|用户名||
|password|密码||
||||
||||
||||

### Mirrors 镜像

```xml
<mirrors>
    <mirror>
      <id>planetmirror.com</id>
      <name>PlanetMirror Australia</name>
      <url>http://downloads.planetmirror.com/pub/maven2</url>
      <mirrorOf>central</mirrorOf>
    </mirror>
  </mirrors>
```
|参数|说明|示例|
|---|---|---|
|id, name|给这个镜像起一个唯一的友好的名字，同时也会根据这个id到servers中取匹配账号密码|The unique identifier and user-friendly name of this mirror. The id is used to differentiate between mirror elements and to pick the corresponding credentials from the <servers> section when connecting to the mirror|
|url|远程仓库的URL||
|mirrorOf|镜像拦截规则|central中央仓库, *所有仓库|
||||


### Proxies 代理

```xml
<proxies>
    <proxy>
      <id>myproxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy.somewhere.com</host>
      <port>8080</port>
      <username>proxyuser</username>
      <password>somepassword</password>
      <nonProxyHosts>*.google.com|ibiblio.org</nonProxyHosts>
    </proxy>
  </proxies>
```

|参数|说明|示例|
|---|---|---|
|id| The unique identifier for this proxy. This is used to differentiate between proxy elements||
|active|true if this proxy is active. This is useful for declaring a set of proxies, but only one may be active at a time||
|protocol, host, port|The protocol://host:port of the proxy, separated into discrete elements||
|username, password|These elements appear as a pair denoting the login and password required to authenticate to this proxy server||
|nonProxyHosts|This is a list of hosts which should not be proxied. The delimiter of the list is the expected type of the proxy server; the example above is pipe delimited - comma delimited is also common||

### Properties 属性, 在POM中使用占位符${X}, where X is the property, 可能从5个地方来

|序号|参数|说明|原文|
|---|---|---|
|1|env.X|从环境变量获取|Prefixing a variable with “env.” will return the shell’s environment variable. For example, ${env.PATH} contains the $path environment variable (%PATH% in Windows).|
|2|project.x|从POM中获取|project.x: A dot (.) notated path in the POM will contain the corresponding element’s value. For example: \<project\>\<version\>1.0\</version\>\</project\> is accessible via ${project.version}.|
|3|settings.x|从settings.xml文件中获取|settings.x: A dot (.) notated path in the settings.xml will contain the corresponding element’s value. For example: \<settings>\<offline\>false\</offline\>\</settings\> is accessible via ${settings.offline}.|
|4|Java System Properties|从java系统变量中获取|Java System Properties: All properties accessible via java.lang.System.getProperties() are available as POM properties, such as ${java.home}.|
|5|x|从pom的properties节点定义里面获取|x: Set within a \<properties /\> element or an external files, the value may be used as ${someVar}.|



### Profiles 配置文件

```xml
<profiles>
    <profile>
        <id>env-test</id>
      ...
      <repositories>
        <repository>
          <id>codehausSnapshots</id>
          <name>Codehaus Snapshots</name>
          <releases>
            <enabled>false</enabled>
            <updatePolicy>always</updatePolicy>
            <checksumPolicy>warn</checksumPolicy>
          </releases>
          <snapshots>
            <enabled>true</enabled>
            <updatePolicy>never</updatePolicy>
            <checksumPolicy>fail</checksumPolicy>
          </snapshots>
          <url>http://snapshots.maven.codehaus.org/maven2</url>
          <layout>default</layout>
        </repository>
      </repositories>
      <pluginRepositories>
        ...
      </pluginRepositories>
      ...
    </profile>
  </profiles>
```

|参数|说明|示例|
|---|---|---|
|releases, snapshots|These are the policies for each type of artifact, Release or snapshot. With these two sets, a POM has the power to alter the policies for each type independent of the other within a single repository. For example, one may decide to enable only snapshot downloads, possibly for development purposes.||
|enabled|true or false for whether this repository is enabled for the respective type (releases or snapshots).||
|updatePolicy|This element specifies how often updates should attempt to occur. Maven will compare the local POM’s timestamp (stored in a repository’s maven-metadata file) to the remote. The choices are: always, daily (default), interval:X (where X is an integer in minutes) or never.||
|checksumPolicy|When Maven deploys files to the repository, it also deploys corresponding checksum files. Your options are to ignore, fail, or warn on missing or incorrect checksums.||
|layout|In the above description of repositories, it was mentioned that they all follow a common layout. This is mostly correct. Maven 2 has a default layout for its repositories; however, Maven 1.x had a different layout. Use this element to specify which if it is default or legacy.||
||||


### Active Profiles 指定哪些配置需要被激活

```xml
<activeProfiles>
    <activeProfile>env-test</activeProfile>
    <activeProfile>env-dev</activeProfile>
  </activeProfiles>
```
> activeProfile对应的是profile的id值

































