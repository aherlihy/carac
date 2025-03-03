package datalog.tools

import datalog.storage.StorageManager

import scala.collection.immutable
import scala.util.Properties

object Debug:
  private val on = Properties.envOrNone("CARAC_DEBUG").nonEmpty && Properties.envOrNone("CARAC_DEBUG").get == "true"
  inline def debug(prefix: String, f: () => String): Unit = if (on) println(s"$prefix ${f()}")
