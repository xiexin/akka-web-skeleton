package org.xx.web.skeleton

import akka.actor.Actor

class SimpleStrategyExecutor(fundManagementStrategy: SimpleFundManagementStrategy, actionPublisher: ActionPublisher) extends Actor {

  override def receive: Receive = {
    case actionPoint: ActionPoint =>
      actionPublisher.publish(Buy(actionPoint.price, fundManagementStrategy.))
    case _ =>
  }
}
