allprojects {
    group = "com.tomkeuper.bedwars"
    version = rootProject.version
    description = "BedWars minigame by Tom Keuper forked from BedWars1058"

    ext.set("id", "bedwars")
    ext.set("website", "https://github.com/tomkeuper/BedWars2023")
    ext.set("author", "Mr. Ceasar")
}

val versions = setOf(
    projects.versionsupportCommon,
    projects.versionsupport18R3,
    projects.resetadapterSlime,
    projects.resetadapterSlimepaper,
    projects.bedwarsApi,
    projects.resetadapterAswm
).map { it.dependencyProject }

val special = setOf(
    projects.bedwarsPlugin,
).map { it.dependencyProject }

subprojects {
    when (this) {
        in versions -> plugins.apply("bedwars.version-conventions")
        in special -> plugins.apply("bedwars.standard-conventions")
        else -> plugins.apply("bedwars.base-conventions")
    }
}

tasks.register("printTag") {
    doLast {
        println("Generated Tag: ${rootProject.version}")
    }
}
