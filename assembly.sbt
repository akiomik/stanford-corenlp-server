import AssemblyPlugin.defaultShellScript

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    // case PathList(ps @ _*) if ps.last endsWith ".class"          => MergeStrategy.first
    // case PathList(ps @ _*) if ps.last == "pom.xml"               => MergeStrategy.first
    // case PathList(ps @ _*) if ps.last endsWith ".properties"     => MergeStrategy.first
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case _ => MergeStrategy.first
    // case x => old(x)
  }
}

// for prepending shell script
assemblyOption in assembly := (assemblyOption in assembly).value.copy(prependShellScript = Some(defaultShellScript))
assemblyJarName in assembly := s"${name.value}-${version.value}"

