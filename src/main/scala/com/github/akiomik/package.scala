package com.github.akiomik

import com.typesafe.config.Config
import java.util.Properties

package object stanford_corenlp_server extends JsonRpc with Encoder {

  implicit class ConfigOps(c: Config) {
    def toProps(): Properties = {
      val map = c.root.unwrapped
      val props = new Properties
      props.putAll(map)
      props
    }
  }

}
