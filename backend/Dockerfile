
FROM openjdk:17-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制构建后的 JAR 文件到容器
COPY target/*.jar app.jar

# 暴露服务端口
EXPOSE 8080

# 启动 Spring Boot 应用
CMD ["java", "-jar", "app.jar"]