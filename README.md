### How to Build with Production Profile

Based on the Maven configuration in your project, you can build with the production profile using the following command:

```bash
mvn clean package -Pprod
```

### What This Does

When you run this command:

1. **Activates the Production Profile**: The `-Pprod` flag activates the production Maven profile defined in your `pom.xml`
2. **Uses Production Properties**: The build will use `application-prod.properties` instead of the default `application.properties`
3. **Sets Production JVM Arguments**: The production profile includes `jvmArguments` configuration that sets `-Dspring.profiles.active=prod`

### Production Profile Configuration

Your `pom.xml` contains the following production profile configuration:

```xml
<profile>
    <id>prod</id>
    <properties>
        <spring.profiles.active>prod</spring.profiles.active>
    </properties>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <jvmArguments>-Dspring.profiles.active=prod</jvmArguments>
                </configuration>
            </plugin>
        </plugins>
    </build>
</profile>
```

### Key Differences in Production Build

When building with the production profile, your application will use:
- `application-prod.properties` configuration file
- Production-specific database settings
- Hibernate DDL auto set to `none` (safer for production)
- Production logging levels

### Alternative Commands

You can also use these variations:

```bash
# Clean, compile, test, and package
mvn clean package -Pprod

# Just package (if already compiled)
mvn package -Pprod

# Install to local repository
mvn clean install -Pprod
```

The production JAR will be created in the `target/` directory with production configuration embedded.