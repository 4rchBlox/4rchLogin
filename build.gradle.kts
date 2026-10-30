plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "hu.archblox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://oss.sonatype.org/content/repositories/snapshots")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    // BungeeCord API
    compileOnly("net.md-5:bungeecord-api:1.20-R0.2")
    
    // LuckPerms API
    compileOnly("net.luckperms:api:5.4")

    // Jelszó hasheléshez és SQLite/MySQL-hez
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.xerial:sqlite-jdbc:3.45.1.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    targetCompatibility = "21"
    sourceCompatibility = "21"
}

tasks.shadowJar {
    archiveFileName.set("4rchLogin.jar")
}