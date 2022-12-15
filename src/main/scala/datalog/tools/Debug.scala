package datalog.tools

import datalog.storage.StorageManager

import scala.collection.immutable

object Debug:
  inline val on = true
  inline def debug(prefix: String, f: () => String): Unit = if (on) println(prefix + f())
