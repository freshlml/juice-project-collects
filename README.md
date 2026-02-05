# juice 项目组成
juice 项目由多个程序（工程）组成：

| 程序（工程）        | groupId | artifactId | pacakge |
|---------------| --- | ---| - |
| juice-alg     | com.fresh.juice | juice-alg | com.fresh.juice.alg |
| juice-java    | com.fresh.juice | juice-java | com.fresh.juice.jv |
| juice-mq      | com.fresh.juice | juice-mq | com.fresh.juice.mq |
| juice-reactor | com.fresh.juice | juice-reactor | com.fresh.juice.reactor |
| juice-spring  | com.fresh.juice | juice-spring | com.fresh.juice.spring |

juice 项目下多个程序（工程）有相同的 groupId: com.fresh.juice，多个程序（工程）通过 artifactId 区分，artifactId 带有项目名称前缀，
工程名称一般与 artifactId 相同，打包名称默认为 artifactId-version。多个程序（工程）有不同的 package name。  

juice-common-parent, juice-common-dependencies 为 juice 项目的公共 maven 配置依赖，
其 pom.xml 中写入公共的 maven 配置，以上工程 pom.xml 均继承自 juice-common-parent，均导入 juice-common-dependencies。  

为了减少 git 仓库数量，将以上程序（工程）统一纳入同一个 git 仓库，因此创建一个公共层 juice-project-collects。将以上程序（工程）
作为 juice-project-collects 的子 module 维护。（在 juice-project-collects 这一级目录创建 git 仓库）  

juice-project-collects 之中的程序（工程）不相互依赖，因此不会出现循环依赖的情况。  

# juice-project-collects 打包说明
一般对此 collects 打包，会将自身即所有子 module 打包。  

子 module 单独打包。没有 -am 参数，假设仓库里面没有此 collects, 或者仓库里面 collects 不是最新的。则此子 module 打的包是无效的（不能被其他工程依赖）。

子 module 单独打包。没有 -am 参数，子 module 依赖于此 collects 中其他子 module 时，打包失败。
即通过增加 -am 参数，子 module 单独打包时，会将此 module 依赖的其他子 module 同时打包。  




