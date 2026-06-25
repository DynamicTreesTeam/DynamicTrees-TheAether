import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()

plugins {
    id("java-library")
    id("net.neoforged.moddev") version "2.0.141"
    id("idea")
    id("maven-publish")
}

repositories {
    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
    maven("https://maven.parchmentmc.org")
    mavenCentral()
    mavenLocal()
    flatDir {
        dirs("libs")
    }
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")
val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

base {
    archivesName.set(modName)
}

neoForge {
    version = property("neoForgeVersion")

    parchment {
        minecraftVersion = mcVersion
        mappingsVersion = property("mappingsVersion")
    }

    runs {
        configureEach {
            logLevel = org.slf4j.event.Level.DEBUG
            systemProperty("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
        }

        create("client") {
            client()
            gameDirectory.set(file("run"))
        }

        create("server") {
            server()
            gameDirectory.set(file("run-server"))
        }

        create("data") {
            data()
            gameDirectory.set(file("run"))
            programArguments.addAll(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources").absolutePath,
                "--existing-mod", "dynamictrees",
                "--existing-mod", "dynamictreesplus",
                "--existing-mod", "aether"
            )
        }
    }

    mods {
        create(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    srcDir("src/localization/resources")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    includeEmptyDirs = false
}

dependencies {
    implementation("curse.maven:dynamictrees-252818:${property("dynamicTreesFileId")}")
    implementation("maven.modrinth:dynamictreesplus:${property("dynamicTreesPlusVersion")}")
    implementation("curse.maven:aether-255308:${property("aetherFileId")}")
}

tasks.jar {
    manifest.attributes(
        "Specification-Title" to project.name,
        "Specification-Vendor" to "Max Hyper",
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to project.version,
        "Implementation-Vendor" to "Max Hyper",
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )
}

tasks.withType<Jar>().configureEach {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven("file:///${project.projectDir}/mcmodsrepo")
    }
}
