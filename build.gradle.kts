import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseExtension
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import net.minecraftforge.gradle.common.util.RunConfig
import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()
fun optionalProperty(key: String) = project.findProperty(key)?.toString()

apply(from = "https://gist.githubusercontent.com/Harleyoc1/4d23d4e991e868d98d548ac55832381e/raw/applesiliconfg.gradle")
apply(from = "https://raw.githubusercontent.com/SizableShrimp/ForgeUpdatesRemapper/main/remapper.gradle")

plugins {
    id("java")
    id("net.minecraftforge.gradle")
    id("org.parchmentmc.librarian.forgegradle")
    id("idea")
    id("maven-publish")
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("com.harleyoconnor.autoupdatetool") version "1.0.5"
}

repositories {
    maven("https://ldtteam.jfrog.io/ldtteam/modding/")
    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    //maven("https://harleyoconnor.com/maven")
    maven("https://squiddev.cc/maven/")
    flatDir {
        dir("libs")
    }
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")
val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

minecraft {
    mappings("parchment", "${property("mappingsVersion")}-$mcVersion")
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            applyDefaultConfiguration()

            if (project.hasProperty("mcUuid")) {
                args("--uuid", property("mcUuid"))
            }
            if (project.hasProperty("mcUsername")) {
                args("--username", property("mcUsername"))
            }
            if (project.hasProperty("mcAccessToken")) {
                args("--accessToken", property("mcAccessToken"))
            }
        }

        create("server") {
            applyDefaultConfiguration("run-server")
        }

        create("data") {
            applyDefaultConfiguration()

            args(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources"),
                "--existing-mod", "dynamictrees",
                "--existing-mod", "dynamictreesplus",
                "--existing-mod", "aether",
                "--existing-mod", "deep_aether",
                "--existing-mod", "aether_redux"
            )
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    srcDir("src/localization/resources")
}

dependencies {
    minecraft("net.minecraftforge:forge:${mcVersion}-${property("forgeVersion")}")

    //Dynamic Trees
    implementation(fg.deobf("curse.maven:dynamictrees-252818:5527670"))
    implementation(fg.deobf("curse.maven:dynamictreesplus-478155:5536181"))
    //implementation(fg.deobf("com.ferreusveritas.dynamictrees:DynamicTrees-$mcVersion:${property("dynamicTreesVersion")}"))
    //implementation(fg.deobf("com.ferreusveritas.dynamictreesplus:DynamicTreesPlus-$mcVersion:${property("dynamicTreesPlusVersion")}"))

    //The Aether itself
    implementation(fg.deobf("curse.maven:aether-255308:5302178"))

    //Addon dependencies
    runtimeOnly(fg.deobf("curse.maven:geckolib-388172:5414523"))
    runtimeOnly(fg.deobf("curse.maven:terrablender-563928:5378180"))
    runtimeOnly(fg.deobf("curse.maven:aeroblender-879879:4789008"))
    runtimeOnly(fg.deobf("curse.maven:the-twilight-forest-227639:5468648"))
    runtimeOnly(fg.deobf("curse.maven:dynamic-trees-the-twilight-forest-391986:5536317"))
    runtimeOnly(fg.deobf("curse.maven:structure-gel-api-378802:5278429"))
    //Aether addons
    runtimeOnly(fg.deobf("curse.maven:deep-aether-852465:5441091"))
    implementation(fg.deobf("curse.maven:aether-redux-867237:5415985"))
    runtimeOnly(fg.deobf("curse.maven:aether-lost-content-318602:4927038"))
    runtimeOnly(fg.deobf("curse.maven:aether-gravitation-855631:5086492"))
    runtimeOnly(fg.deobf("curse.maven:ancient-aether-886751:5364191"))
    runtimeOnly(fg.deobf("curse.maven:umbral-skies-855821:4768252"))

    //Other useful mods
    runtimeOnly(fg.deobf("curse.maven:jade-324717:5072729"))
    runtimeOnly(fg.deobf("curse.maven:jei-238222:5101366"))
    runtimeOnly(fg.deobf("curse.maven:cc-tweaked-282001:5118388"))
    runtimeOnly(fg.deobf("curse.maven:suggestion-provider-fix-469647:4591193"))
}

tasks.jar {
    manifest.attributes(
        "Specification-Title" to modName,
        "Specification-Vendor" to "Max Hyper",
        "Specification-Version" to "1",
        "Implementation-Title" to modName,
        "Implementation-Version" to project.version,
        "Implementation-Vendor" to "Max Hyper",
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    archiveBaseName.set(modName)
    finalizedBy("reobfJar")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val changelogFile = file("build/changelog.txt")

curseforge {
    if (!project.hasProperty("curseApiKey")) {
        project.logger.warn("API Key for CurseForge not detected; uploading will be disabled.")
        return@curseforge
    }

    apiKey = property("curseApiKey")

    project {
        id = "387443"

        addGameVersion(mcVersion)

        changelog = changelogFile
        changelogType = "markdown"
        releaseType = optionalProperty("versionType") ?: "release"

        addArtifact(tasks.findByName("sourcesJar"))

        mainArtifact(tasks.findByName("jar")) {
            relations {
                requiredDependency("dynamictrees")
                requiredDependency("aether")
                optionalDependency("dynamictreesplus")
            }
        }
    }
}

val minecraftVersion = mcVersion

autoUpdateTool {
    mcVersion.set(minecraftVersion)
    version.set(modVersion)
    versionRecommended.set(property("versionRecommended") == "true")
    changelogOutputFile.set(changelogFile)
    updateCheckerFile.set(file(property("dynamictrees.version_info_repo.path") + File.separatorChar + property("updateCheckerPath")))
}

tasks.autoUpdate {
    finalizedBy("curseforge")
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

fun RunConfig.applyDefaultConfiguration(runDirectory: String = "run") {
    workingDirectory = file(runDirectory).absolutePath

    property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
    property("forge.logging.console.level", "debug")

    property("mixin.env.remapRefMap", "true")
    property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")

    mods {
        create(modId) {
            source(sourceSets.main.get())
        }
    }
}

fun CurseExtension.project(action: CurseProject.() -> Unit) {
    this.project(closureOf(action))
}

fun CurseProject.mainArtifact(artifact: Task?, action: CurseArtifact.() -> Unit) {
    this.mainArtifact(artifact, closureOf(action))
}

fun CurseArtifact.relations(action: CurseRelation.() -> Unit) {
    this.relations(closureOf(action))
}
