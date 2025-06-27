# 联路短信 SDK（LianLu SMS SDK）

[![Maven Central](https://img.shields.io/maven-central/v/com.ninghua-itc.nexus/lianlu-sms-sdk.svg)](https://search.maven.org/artifact/com.ninghua-itc.nexus/lianlu-sms-sdk)

一个轻量级 Java SDK，支持通过联路短信平台发送短信。内置参数签名、响应解析等功能，适用于 Java 应用或 Spring 项目。

---

## ✨ 功能特性

- ✅ 通过 HTTP API 发送短信
- 🔐 内置请求签名逻辑，保障接口安全
- 📩 自动解析联路平台响应结果
- 💡 适配 Spring、Spring Boot 等主流框架
- ⚙️ 精简依赖，易于集成与维护

---

## 📦 安装方式

项目已发布至 Maven Central，可通过以下方式引入：

```xml
<dependency>
  <groupId>com.ninghua-itc.nexus</groupId>
  <artifactId>lianlu-sms-sdk</artifactId>
  <version>1.0.0</version>
</dependency>