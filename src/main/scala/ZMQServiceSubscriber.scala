package scala
import java.io.IOException

import org.zeromq.ZMQ

import scala.ZMQServiceServer.context

object ZMQServiceSubscriber {
  var pushClient: ZMQ.Socket = _
  var pubClient: ZMQ.Socket = _
  var forwardMessage: ZMQ.Socket = _

  def init (pushPort: String, pubPort: String): Unit = {
    //creating ZMQ context which will be used for PUB and PUSH
    val context = ZMQ.context(1)

    //using context to create PUSH and PUB models and binding them to sockets
    pushClient = context.socket(ZMQ.PULL)
    pushClient.bind("tcp://127.0.0.1:"+ pushPort)

    pubClient = context.socket(ZMQ.SUB)
    pubClient.connect("tcp://127.0.0.1:"+ pubPort)
    pubClient.subscribe("Publisher".getBytes(ZMQ.CHARSET))
  }

  def init (pushPort: String, pubPort: String, forwardPort: String): Unit = {
    //creating ZMQ context which will be used for PUB and PUSH
    val context = ZMQ.context(1)

    //using context to create a PUB and SUB model and binding them to sockets
//    pushClient = context.socket(ZMQ.PULL)
//    pushClient.bind("tcp://127.0.0.1:"+ pushPort)

    pubClient = context.socket(ZMQ.SUB)
    pubClient.connect("tcp://127.0.0.1:"+ pubPort)
    pubClient.subscribe("Publisher".getBytes(ZMQ.CHARSET))

    forwardMessage = context.socket(ZMQ.PUB)
    forwardMessage.bind("tcp://127.0.0.1:"+ forwardPort)
  }

  def pull (): Unit = {
    val message = pushClient.recvStr()
    println("Pulled Message: " + message /*+ " Topic: " + Thread.currentThread().getName*/)
  }

  def sub (): Unit = {
    val topic = pubClient.recvStr()
    val message = pubClient.recvStr()
    println("Subscribed Message: " + message + " Topic: " + topic)
  }

  def forward (): Unit = {
    val topic = pubClient.recvStr()
    val message = pubClient.recvStr()
    println("Subscribed Message: " + message + " Topic: " + topic)
    /** publish the message again on forwardPort
     * Mimics the Kafka broker service in a very bare bones way
     */
    val pubMessage = message //this line mimics any data processing that might take place before the data is passed on
    //setting the topic as Publisher
    forwardMessage.sendMore(topic)
    //sending the message
    val msg = String.format("Update %d" + pubMessage)
    forwardMessage.send(msg.getBytes(), 0)
    println(msg)
    Thread sleep 1000
  }

  def end (): Unit = {
    pubClient.close()
    pubClient.close()
  }

  def endForwarder (): Unit = {
    forwardMessage.close()
  }
}
