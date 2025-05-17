plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.capgemini.estimate.poc"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-jooq")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.session:spring-session-core")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation ("org.apache.poi:poi-ooxml:5.2.5")
	runtimeOnly(files("libs/ojdbc11.jar")) // Oracle JDBCドライバを配置
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// jooq { version.set("3.18.6")
// 	configurations { 
// 		create("main") { 
// 			generationTool.apply { 
// 				jdbc.apply { 
// 					driver = "oracle.jdbc.OracleDriver" 
// 					url = "jdbc:oracle:thin:@your-oracle-host:1521/ORCLPDB1" 
// 					user = "your_user" 
// 					password = "your_pass" 
// 				} 
// 				generator.apply { 
// 					name = "org.jooq.codegen.DefaultGenerator" 
// 					database.apply { 
// 						name = "org.jooq.meta.oracle.OracleDatabase" 
// 						inputSchema = "YOUR_SCHEMA" 
// 					} 
// 					generate.apply { 
// 						deprecated = false 
// 						records = true 
// 						immutablePojos = true 
// 					} 
// 					target.apply { 
// 						packageName = "jp.co.yourcompany.quote.jooq.generated" 
// 						directory = "src/main/java" 
// 				} 
// 				} 
// 			} 
// 		} 
// 	}
// }
