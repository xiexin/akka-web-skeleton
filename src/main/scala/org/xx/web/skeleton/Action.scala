package org.xx.web.skeleton

trait Action {
  val price: Double
  val volume: Long
}

case class Buy(price: Double, volume: Long) extends Action {

}

case class Sell(price: Double, volume: Long) extends Action {

}