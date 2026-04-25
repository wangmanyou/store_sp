# Store SP 商城系统

这是一个前后端分离的商城系统，包含用户端商城、管理后台、图片上传、购物车、订单、地址管理、Banner 管理、商品管理和基于 JWT + Shiro 的登录鉴权。

## 技术栈

- 前端：Vue 3、Vite、Vue Router、Element Plus、Axios
- 后端：Spring Boot 3、MyBatis Plus、Apache Shiro、JJWT、Druid、MySQL
- 运行环境：JDK 17、Node.js、MySQL 8

## 项目结构

```text
store_sp
├── db                         # 数据库 SQL
│   ├── init_store_sp.sql       # 初始化表结构
│   └── store_sp——date.sql      # 带演示数据的完整 SQL
├── frontend                   # Vue 前端项目
├── image                      # Banner 和商品演示图片素材
├── store_sp_backend           # Spring Boot 后端项目
│   └── uploads                # 已上传图片目录，项目已提交，拉取后可直接访问
└── README.md
```

## 主要功能

用户端：

- 首页 Banner 展示
- 商品列表与商品详情
- 商品图集浏览
- 购物车管理
- 提交订单
- 我的地址
- 我的购物 / 订单详情
- 用户登录与注册

管理端：

- 管理员登录
- 经营概览图表
- Banner 管理与图片上传
- 商品管理、封面图上传、商品图集上传
- 分类管理
- 订单管理与发货
- 用户管理

鉴权设计：

- Token 由 JJWT 生成和解析
- 登录认证与接口鉴权交给 Shiro
- 前端请求通过 `Authorization: Bearer <token>` 携带登录凭证
- 管理端路由会校验管理员 Token，未登录会跳转到后台登录页

## 数据库准备

先创建数据库：

```sql
CREATE DATABASE store_sp DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
```

推荐直接导入带数据的 SQL：

```bash
mysql -u root -p store_sp < db/store_sp——date.sql
```

如果只想初始化表结构，可以导入：

```bash
mysql -u root -p store_sp < db/init_store_sp.sql
```

## 后端运行

进入后端目录：

```bash
cd store_sp_backend
```

确认数据库配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/store_sp?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: password
```

如果你的 MySQL 密码不是 `password`，请修改：

```text
store_sp_backend/src/main/resources/application.yaml
```

启动后端：

```bash
./mvnw spring-boot:run
```

Windows 下也可以使用：

```bash
mvnw.cmd spring-boot:run
```

后端默认端口：

```text
http://localhost:8080
```

图片访问路径：

```text
http://localhost:8080/upload/...
```

上传目录默认是：

```text
store_sp_backend/uploads
```

也可以在 `application.yaml` 中配置：

```yaml
app:
  upload:
    dir: uploads
    url-prefix: /upload
```

## 前端运行

进入前端目录：

```bash
cd frontend
```

安装依赖：

```bash
npm install
```

启动前端：

```bash
npm run dev
```

前端默认端口：

```text
http://localhost:5173
```

后台地址：

```text
http://localhost:5173/admin
```

前端已经在 `vite.config.js` 中代理了后端接口：

```js
"/api" -> "http://localhost:8080"
"/upload" -> "http://localhost:8080"
```

## 默认账号

管理员账号：

```text
用户名：admin
密码：123456
```

普通用户账号：

```text
用户名：wangmanyou
密码：123456
```

## 常用命令

后端编译：

```bash
cd store_sp_backend
./mvnw compile
```

前端打包：

```bash
cd frontend
npm run build
```

## 注意事项

- 本项目为了课程演示方便，默认密码是明文保存；如果用于正式环境，应改成 BCrypt 等安全加密方式。
- `uploads` 目录已经提交到仓库，方便在其他电脑拉取后直接看到演示图片。
- 如果图片不显示，请确认后端已启动，并且前端 `/upload` 代理正常。
- 如果管理后台无法进入，请先访问 `/admin/login` 登录管理员账号。
